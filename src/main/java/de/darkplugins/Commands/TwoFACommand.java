package de.darkplugins.Commands;

import com.google.zxing.WriterException;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import de.darkplugins.Config.Data;
import de.darkplugins.Utils.QRCodeMap;
import de.darkplugins.main.Main;
import de.darkplugins.Utils.TwoFAUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.UUID;

import static de.darkplugins.Config.lang.util.LangReader.*;

public class TwoFACommand implements CommandExecutor {

    private final String Prefix, RegisterPerm, Servername;
    private final Main plugin;
    private final Data data;
    private final QRCodeMap qrcm;

    private final HashMap<UUID, String> PlayerTempKeys = new HashMap<>();

    public TwoFACommand(Main Plugin, Data Data, QRCodeMap QRCM, String prefix, String servername, String registerPerm) {
        this.plugin = Plugin;
        this.data = Data;
        this.qrcm = QRCM;

        this.Prefix = prefix;
        this.Servername = servername;
        this.RegisterPerm = registerPerm;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            if (args.length >= 1) {
                if(args[0].equals("unlink")) {
                    // UNLINK OTHER PLAYER
                    if(args.length > 1 && player.isOp()) {
                        Player playerOther = Bukkit.getPlayerExact(args[1]);
                        if(playerOther != null) {
                            UUID playerOtherUUID = playerOther.getUniqueId();
                            if (data.getKey(playerOtherUUID) != null) {
                                data.remove(playerOtherUUID);
                                plugin.getVerifiedPlayers().remove(playerOtherUUID);

                                playerOther.sendMessage(Prefix + UserUnlinkOtherComplete);
                                player.sendMessage(Prefix + UserUnlinkOtherCompleteAdmin);

                                return true;
                            } else {
                                player.sendMessage(Prefix + UserNotSetup);
                                return false;
                            }
                        } else {
                            player.sendMessage(Prefix + UserUnlinkOtherFail + " §8(§e" + args[1] + "§8)");
                            return false;
                        }
                    }
                    // UNLINK YOURSELF
                    if (data.getKey(playerUUID) != null) {
                        if (plugin.getVerifiedPlayers().getOrDefault(playerUUID, false)) {
                            data.remove(playerUUID);
                            plugin.getVerifiedPlayers().remove(playerUUID);
                            player.sendMessage(Prefix + UserUnlinkComplete);

                            return true;
                        } else {
                            player.sendMessage(Prefix + UserVerifyNeeded);
                            return false;
                        }
                    } else {
                        player.sendMessage(Prefix + UserNotSetup);
                        return false;
                    }
                } else if (data.getKey(playerUUID) == null && PlayerTempKeys.getOrDefault(playerUUID, null) != null) {
                    // Getting the Key
                    String inputCode = args[0];
                    String secretKey = PlayerTempKeys.getOrDefault(playerUUID, "KEYDOESNOTEXISTTF");

                    // Checking the Key with the Code
                    boolean isVerified = TwoFAUtils.verifyCode(secretKey, inputCode);

                    if (isVerified) {
                        InetSocketAddress address = player.getAddress();
                        if(address != null) {
                            // SAVING THE KEY TO DB AND REMOVING FROM TEMP
                            data.setKey(playerUUID, PlayerTempKeys.get(playerUUID));
                            PlayerTempKeys.remove(playerUUID);
                            qrcm.removeMap(player);
                            // SAVE CURRENT USER IP & TIME
                            data.setIP(playerUUID, address.getAddress().getHostAddress(), String.valueOf(System.currentTimeMillis()));
                            // VERIFY PLAYER
                            plugin.getVerifiedPlayers().put(playerUUID, true);
                            player.sendMessage(Prefix + UserSetupCompleted);
                            player.sendMessage(Prefix + UserVerifyCompleted);

                            return true;
                        } else {
                            player.sendMessage(Prefix + UserVerifyError);
                            Bukkit.getConsoleSender().sendMessage(Prefix + SystemAddressUserError + " §e(" + player.getName() + ")");
                            return false;
                        }
                    } else {
                        player.sendMessage(Prefix + UserVerifyWrongCode);
                        return false;
                    }
                } else if (data.getKey(playerUUID) != null) {
                    // Getting the Key
                    String inputCode = args[0];
                    String secretKey = data.getKey(playerUUID);

                    // Checking the Key with the Code
                    boolean isVerified = TwoFAUtils.verifyCode(secretKey, inputCode);

                    if (isVerified) {
                        InetSocketAddress address = player.getAddress();
                        if(address != null) {
                            // SAVE CURRENT USER IP & TIME
                            data.setIP(playerUUID, address.getAddress().getHostAddress(), String.valueOf(System.currentTimeMillis()));
                            // VERIFY PLAYER
                            plugin.getVerifiedPlayers().put(playerUUID, true);
                            player.sendMessage(Prefix + UserVerifyCompleted);

                            return true;
                        } else {
                            player.sendMessage(Prefix + UserVerifyError);
                            Bukkit.getConsoleSender().sendMessage(Prefix + SystemAddressUserError + " §e(" + player.getName() + ")");
                            return false;
                        }
                    } else {
                        player.sendMessage(Prefix + UserVerifyWrongCode);
                        return false;
                    }
                } else {
                    player.sendMessage(Prefix + UserNotSetup);
                    return false;
                }
            } else {
                if(player.hasPermission(RegisterPerm)) {
                    if (data.getKey(playerUUID) == null && PlayerTempKeys.getOrDefault(playerUUID, null) == null) {
                        if (checkPlayerSlots(player)) {
                            // Generate secret key and QR code
                            GoogleAuthenticatorKey key = TwoFAUtils.generateSecretKey();
                            String secretKey = key.getKey();
                            PlayerTempKeys.put(playerUUID, secretKey);

                            try {
                                String qrCodeUrl = TwoFAUtils.getGoogleAuthenticatorBarCode(key, player.getName(), Servername);
                                String qrCodeBase64 = TwoFAUtils.generateQRCodeImage(qrCodeUrl);
                                ItemStack map = qrcm.createQRCodeMap(player, qrCodeBase64);
                                player.getInventory().addItem(map);
                                player.sendMessage(Prefix + UserVerifyLine1);
                                player.sendMessage(Prefix + UserVerifyLine2 + secretKey);
                                player.sendMessage(Prefix + UserVerifyLine3);

                                return true;
                            } catch (WriterException | IOException e) {
                                player.sendMessage(Prefix + UserQRCodeGenError);
                                throw new RuntimeException(e);
                            }
                        } else {
                            player.sendMessage(Prefix + UserInvSlots);
                            return false;
                        }
                    } else {
                        if (PlayerTempKeys.getOrDefault(playerUUID, null) == null) {
                            player.sendMessage(Prefix + UserVerified);

                            return true;
                        } else {
                            player.sendMessage(Prefix + UserVerifyGotCode);
                            return false;
                        }
                    }
                } else {
                    player.sendMessage(Prefix + UserNoRegisterPerm);
                    return false;
                }
            }
        } else {
            sender.sendMessage(Prefix + UserPlayerNeeded);
            return false;
        }
    }

    private boolean checkPlayerSlots(Player player) {
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot <= 8; slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item == null) {
                return true;
            }
        }
        return false;
    }
}
