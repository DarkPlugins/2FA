package de.darkplugins.Listeners;

import de.darkplugins.Config.Data;
import de.darkplugins.main.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.vehicle.*;

import java.util.UUID;

public class EventListener implements Listener {

    private String Type, TypePerm;
    private final Main plugin;
    private final Data data;

    public EventListener(Main Plugin, Data Data, String type, String typePerm) {
        this.plugin = Plugin;
        this.data = Data;

        this.Type = type;
        this.TypePerm = typePerm;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (event.getFrom().getX() != event.getTo().getX() ||
                event.getFrom().getY() != event.getTo().getY() ||
                event.getFrom().getZ() != event.getTo().getZ()) {

            String checkType = getCheckType();
            Boolean isVerified = plugin.getVerifiedPlayers().getOrDefault(player.getUniqueId(), null);

            switch (checkType) {
                case "global":
                    if (data.getKey(player.getUniqueId()) == null) {
                        event.setCancelled(true);
                        if(checkTimeOut(playerUUID)) {
                            getPlayerMSGRegister(player);
                        }
                    } else if (isVerified == null || !isVerified) {
                        event.setCancelled(true);
                        if(checkTimeOut(playerUUID)) {
                            getPlayerMSGVerify(player);
                        }
                    }
                    break;
                case "permission":
                    if (player.hasPermission(TypePerm)) {
                        if (data.getKey(player.getUniqueId()) == null) {
                            event.setCancelled(true);
                            if(checkTimeOut(playerUUID)) {
                                getPlayerMSGRegister(player);
                            }
                        } else if (isVerified == null || !isVerified) {
                            event.setCancelled(true);
                            if(checkTimeOut(playerUUID)) {
                                getPlayerMSGVerify(player);
                            }
                        }
                    }
                    break;
                case "choice":
                    if (data.getKey(player.getUniqueId()) != null) {
                        if (isVerified == null || !isVerified) {
                            event.setCancelled(true);
                            if(checkTimeOut(playerUUID)) {
                                getPlayerMSGVerify(player);
                            }
                        }
                    }
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        String checkType = getCheckType();
        Boolean isVerified = plugin.getVerifiedPlayers().getOrDefault(player.getUniqueId(), null);

        switch (checkType) {
            case "global":
                if (data.getKey(player.getUniqueId()) == null) {
                    event.setCancelled(true);
                    if(checkTimeOut(playerUUID)) {
                        getPlayerMSGRegister(player);
                    }
                } else if (isVerified == null || !isVerified) {
                    event.setCancelled(true);
                    if(checkTimeOut(playerUUID)) {
                        getPlayerMSGVerify(player);
                    }
                }
                break;
            case "permission":
                if (player.hasPermission(TypePerm)) {
                    if (data.getKey(player.getUniqueId()) == null) {
                        event.setCancelled(true);
                        if(checkTimeOut(playerUUID)) {
                            getPlayerMSGRegister(player);
                        }
                    } else if (isVerified == null || !isVerified) {
                        event.setCancelled(true);
                        if(checkTimeOut(playerUUID)) {
                            getPlayerMSGVerify(player);
                        }
                    }
                }
                break;
            case "choice":
                if (data.getKey(player.getUniqueId()) != null) {
                    if (isVerified == null || !isVerified) {
                        event.setCancelled(true);
                        if(checkTimeOut(playerUUID)) {
                            getPlayerMSGVerify(player);
                        }
                    }
                }
                break;
            default:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        String checkType = getCheckType();
        Boolean isVerified = plugin.getVerifiedPlayers().getOrDefault(player.getUniqueId(), null);

        switch (checkType) {
            case "global":
                if (data.getKey(player.getUniqueId()) == null) {
                    event.setCancelled(true);
                    if(checkTimeOut(playerUUID)) {
                        getPlayerMSGRegister(player);
                    }
                } else if (isVerified == null || !isVerified) {
                    event.setCancelled(true);
                    if(checkTimeOut(playerUUID)) {
                        getPlayerMSGVerify(player);
                    }
                }
                break;
            case "permission":
                if (player.hasPermission(TypePerm)) {
                    if (data.getKey(player.getUniqueId()) == null) {
                        event.setCancelled(true);
                        if(checkTimeOut(playerUUID)) {
                            getPlayerMSGRegister(player);
                        }
                    } else if (isVerified == null || !isVerified) {
                        event.setCancelled(true);
                        if(checkTimeOut(playerUUID)) {
                            getPlayerMSGVerify(player);
                        }
                    }
                }
                break;
            case "choice":
                if (data.getKey(player.getUniqueId()) != null) {
                    if (isVerified == null || !isVerified) {
                        event.setCancelled(true);
                        if(checkTimeOut(playerUUID)) {
                            getPlayerMSGVerify(player);
                        }
                    }
                }
                break;
            default:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity()).getPlayer();
            if(player != null) {
                String checkType = getCheckType();
                Boolean isVerified = plugin.getVerifiedPlayers().getOrDefault(player.getUniqueId(), null);

                switch (checkType) {
                    case "global":
                        if (data.getKey(player.getUniqueId()) == null) {
                            event.setCancelled(true);
                        } else if (isVerified == null || !isVerified) {
                            event.setCancelled(true);
                        }
                        break;
                    case "permission":
                        if (player.hasPermission(TypePerm)) {
                            if (data.getKey(player.getUniqueId()) == null) {
                                event.setCancelled(true);
                            } else if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                            }
                        }
                        break;
                    case "choice":
                        if (data.getKey(player.getUniqueId()) != null) {
                            if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                            }
                        }
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = ((Player) event.getEntity()).getPlayer();
            if(player != null) {
                UUID playerUUID = player.getUniqueId();

                String checkType = getCheckType();
                Boolean isVerified = plugin.getVerifiedPlayers().getOrDefault(player.getUniqueId(), null);

                switch (checkType) {
                    case "global":
                        if (data.getKey(player.getUniqueId()) == null) {
                            event.setCancelled(true);
                            if (checkTimeOut(playerUUID)) {
                                getPlayerMSGRegister(player);
                            }
                        } else if (isVerified == null || !isVerified) {
                            event.setCancelled(true);
                            if (checkTimeOut(playerUUID)) {
                                getPlayerMSGVerify(player);
                            }
                        }
                        break;
                    case "permission":
                        if (player.hasPermission(TypePerm)) {
                            if (data.getKey(player.getUniqueId()) == null) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGRegister(player);
                                }
                            } else if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGVerify(player);
                                }
                            }
                        }
                        break;
                    case "choice":
                        if (data.getKey(player.getUniqueId()) != null) {
                            if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGVerify(player);
                                }
                            }
                        }
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = ((Player) event.getWhoClicked()).getPlayer();
            if(player != null) {
                UUID playerUUID = player.getUniqueId();

                String checkType = getCheckType();
                Boolean isVerified = plugin.getVerifiedPlayers().getOrDefault(player.getUniqueId(), null);

                switch (checkType) {
                    case "global":
                        if (data.getKey(player.getUniqueId()) == null) {
                            event.setCancelled(true);
                            if (checkTimeOut(playerUUID)) {
                                getPlayerMSGRegister(player);
                            }
                        } else if (isVerified == null || !isVerified) {
                            event.setCancelled(true);
                            if (checkTimeOut(playerUUID)) {
                                getPlayerMSGVerify(player);
                            }
                        }
                        break;
                    case "permission":
                        if (player.hasPermission(TypePerm)) {
                            if (data.getKey(player.getUniqueId()) == null) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGRegister(player);
                                }
                            } else if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGVerify(player);
                                }
                            }
                        }
                        break;
                    case "choice":
                        if (data.getKey(player.getUniqueId()) != null) {
                            if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGVerify(player);
                                }
                            }
                        }
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if(!event.getMessage().startsWith("/2fa") || event.getMessage().startsWith("/2fa unlink")) {
            Player player = event.getPlayer();
            if (player != null) {
                UUID playerUUID = player.getUniqueId();

                String checkType = getCheckType();
                Boolean isVerified = plugin.getVerifiedPlayers().getOrDefault(player.getUniqueId(), null);

                switch (checkType) {
                    case "global":
                        if (data.getKey(player.getUniqueId()) == null) {
                            event.setCancelled(true);
                            if (checkTimeOut(playerUUID)) {
                                getPlayerMSGRegister(player);
                            }
                        } else if (isVerified == null || !isVerified) {
                            event.setCancelled(true);
                            if (checkTimeOut(playerUUID)) {
                                getPlayerMSGVerify(player);
                            }
                        }
                        break;
                    case "permission":
                        if (player.hasPermission(TypePerm)) {
                            if (data.getKey(player.getUniqueId()) == null) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGRegister(player);
                                }
                            } else if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGVerify(player);
                                }
                            }
                        }
                        break;
                    case "choice":
                        if (data.getKey(player.getUniqueId()) != null) {
                            if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGVerify(player);
                                }
                            }
                        }
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player) {
            Player player = ((Player) event.getEntered()).getPlayer();
            if(player != null) {
                UUID playerUUID = player.getUniqueId();

                String checkType = getCheckType();
                Boolean isVerified = plugin.getVerifiedPlayers().getOrDefault(player.getUniqueId(), null);

                switch (checkType) {
                    case "global":
                        if (data.getKey(player.getUniqueId()) == null) {
                            event.setCancelled(true);
                            if (checkTimeOut(playerUUID)) {
                                getPlayerMSGRegister(player);
                            }
                        } else if (isVerified == null || !isVerified) {
                            event.setCancelled(true);
                            if (checkTimeOut(playerUUID)) {
                                getPlayerMSGVerify(player);
                            }
                        }
                        break;
                    case "permission":
                        if (player.hasPermission(TypePerm)) {
                            if (data.getKey(player.getUniqueId()) == null) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGRegister(player);
                                }
                            } else if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGVerify(player);
                                }
                            }
                        }
                        break;
                    case "choice":
                        if (data.getKey(player.getUniqueId()) != null) {
                            if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGVerify(player);
                                }
                            }
                        }
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) {
            Player player = ((Player) event.getExited()).getPlayer();
            if(player != null) {
                UUID playerUUID = player.getUniqueId();

                String checkType = getCheckType();
                Boolean isVerified = plugin.getVerifiedPlayers().getOrDefault(player.getUniqueId(), null);

                switch (checkType) {
                    case "global":
                        if (data.getKey(player.getUniqueId()) == null) {
                            event.setCancelled(true);
                            if (checkTimeOut(playerUUID)) {
                                getPlayerMSGRegister(player);
                            }
                        } else if (isVerified == null || !isVerified) {
                            event.setCancelled(true);
                            if (checkTimeOut(playerUUID)) {
                                getPlayerMSGVerify(player);
                            }
                        }
                        break;
                    case "permission":
                        if (player.hasPermission(TypePerm)) {
                            if (data.getKey(player.getUniqueId()) == null) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGRegister(player);
                                }
                            } else if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGVerify(player);
                                }
                            }
                        }
                        break;
                    case "choice":
                        if (data.getKey(player.getUniqueId()) != null) {
                            if (isVerified == null || !isVerified) {
                                event.setCancelled(true);
                                if (checkTimeOut(playerUUID)) {
                                    getPlayerMSGVerify(player);
                                }
                            }
                        }
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }

    private String getCheckType() {
        if (Type.equals("global") || Type.equals("Global") || Type.equals("GLOBAL")) {
            return "global";
        }
        if (Type.equals("Permission") || Type.equals("permission") || Type.equals("PERMISSION") || Type.equals("perm") || Type.equals("Perm") || Type.equals("PERM")) {
            return "permission";
        }
        if (Type.equals("choice") || Type.equals("Choice") || Type.equals("CHOICE")) {
            return "choice";
        }
        return "global";
    }

    private boolean checkTimeOut(UUID playerUUID) {
        if (System.currentTimeMillis() > (plugin.getPlayerTimeouts().getOrDefault(playerUUID, 0L)+2000)) {
            setTimeOut(playerUUID);
            return true;
        }
        return false;
    }

    private void setTimeOut(UUID playerUUID) {
        plugin.getPlayerTimeouts().put(playerUUID, System.currentTimeMillis());
    }

    private void getPlayerMSGRegister(Player player) {
        String Prefix = "§9§l2FA §8> §f";
        player.sendMessage(Prefix + "§cZwei-Faktor-Authentifizierung (2FA) muss von Ihnen aktiviert werden. Dies wurde von einem Administrator vorgesehen. Bitte benutze §e/2fa");
    }

    private void getPlayerMSGVerify(Player player) {
        String Prefix = "§9§l2FA §8> §f";
        player.sendMessage(Prefix + "§cDu musst deinen 2fa Code mit /2fa <Code> eingeben.");
    }
}
