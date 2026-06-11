package de.darkplugins.Listeners;

import de.darkplugins.Config.Data;
import de.darkplugins.Utils.QRCodeMap;
import de.darkplugins.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.net.InetSocketAddress;
import java.util.UUID;

import static de.darkplugins.Config.lang.util.LangReader.*;

public class JoinListener implements Listener {

    private String Prefix, UpdatePermInfo;
    private boolean Autologin, UpdateAvailable;
    private int Range;
    private final Main plugin;
    private final Data data;
    private final QRCodeMap qrcm;

    public JoinListener(Main Plugin, Data Data, QRCodeMap QRCM, String prefix, boolean autologin, int range, String updatePermInfo, boolean updateAvailable) {
        this.plugin = Plugin;
        this.data = Data;
        this.qrcm = QRCM;

        this.Prefix = prefix;
        this.Autologin = autologin;
        this.Range = range;

        this.UpdatePermInfo = updatePermInfo;
        this.UpdateAvailable = updateAvailable;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // UPDATE-INFO
        if(player.hasPermission(UpdatePermInfo)) {
            if (UpdateAvailable) {
                player.sendMessage(Prefix + SystemVersionOld1);
                player.sendMessage(Prefix + SystemVersionOld3);
            }
        }

        // RELOGIN
        if(plugin.getVerifiedPlayers().getOrDefault(playerUUID, null) != null) {
            plugin.getVerifiedPlayers().put(playerUUID, false);
        }
        if(Autologin) {
            if (data.getKey(playerUUID) != null) {
                if (data.getIP(playerUUID) != null) {
                    InetSocketAddress address = event.getPlayer().getAddress();
                    if (address != null) {
                        String playerIP = address.getAddress().getHostAddress();
                        if (data.getIP(playerUUID).equals(playerIP) && checkLogin(playerUUID)) {
                            player.sendMessage(Prefix + UserRelogin);
                            data.setIP(playerUUID, playerIP, String.valueOf(System.currentTimeMillis()));
                            plugin.getVerifiedPlayers().put(playerUUID, true);
                        }
                    }
                }
            }
        }

        // QR-Code-MAP CHECK
        qrcm.removeMap(player);
    }

    public boolean checkLogin(UUID playerUUID) {
        try {
            long differenceMillis = System.currentTimeMillis() - Long.parseLong(data.getLogin(playerUUID));
            long rangeInMillis = Range * 60 * 1000;

            if (differenceMillis <= rangeInMillis) {
                return true;
            }
        } catch (NumberFormatException e) {
            Bukkit.getConsoleSender().sendMessage(Prefix + "§cAn error occurred while checking the last login:");
            e.printStackTrace();
        }

        return false;
    }
}
