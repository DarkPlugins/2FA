package de.darkplugins.main;

import de.darkplugins.Commands.TwoFACommand;
import de.darkplugins.Commands.TwoFACommandCompleter;
import de.darkplugins.Config.Config;
import de.darkplugins.Config.Data;
import de.darkplugins.Config.lang.de;
import de.darkplugins.Config.lang.en;
import de.darkplugins.Listeners.EventListener;
import de.darkplugins.Listeners.JoinListener;
import de.darkplugins.Updater.UpdateChecker;
import de.darkplugins.Utils.Metrics;
import de.darkplugins.Utils.QRCodeMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import static de.darkplugins.Config.lang.util.LangReader.*;

public class Main extends JavaPlugin {

    private String Prefix, Servername, RegisterPerm, Type, TypePerm, UpdatePermInfo, DBHost, DBPort, DBName, DBUsername, DBPw;
    private boolean MetricS = true, Autologin = true, ReloadKick = true, UpdateCheck = true, UpdateAvailable = false, DBUse = false;
    private int Range;
    private final HashMap<UUID, Boolean> verifiedPlayers = new HashMap<>();
    private final HashMap<UUID, Long> PlayerTimeouts = new HashMap<>();

    @Override
    public void onEnable() {
        // Reading config values
        Config cc = new Config();
        Prefix = cc.ReadYML("Prefix");
        MetricS = Boolean.parseBoolean(cc.ReadYML("Metrics"));
        Servername = cc.ReadYML("Servername");
        RegisterPerm = cc.ReadYML("RegisterPerm");
        Autologin = Boolean.parseBoolean(cc.ReadYML("Autologin"));
        Range = Integer.parseInt(cc.ReadYML("Range"));
        Type = cc.ReadYML("Type");
        TypePerm = cc.ReadYML("TypePerm");
        ReloadKick = Boolean.parseBoolean(cc.ReadYML("ReloadKick"));
        UpdateCheck = Boolean.parseBoolean(cc.ReadYML("Updates"));
        UpdatePermInfo = cc.ReadYML("UpdatePerms");
        DBUse = Boolean.parseBoolean(cc.ReadYML("Use"));
        DBHost = cc.ReadYML("Host");
        DBPort = cc.ReadYML("Port");
        DBName = cc.ReadYML("Database");
        DBUsername = cc.ReadYML("Username");
        DBPw = cc.ReadYML("Password");
        // bStats
        if(MetricS) {
            int pluginId = 22556;
            Metrics metrics = new Metrics(this, pluginId);
            metrics.addCustomChart(new Metrics.SimplePie("mysqluse", () -> String.valueOf(DBUse)));
        }

        // Sprachvorlagen generieren
        de DE = new de();
        en EN = new en();

        // Initializing data-db
        Data data = new Data(DBUse, DBHost, DBPort, DBName, DBUsername, DBPw);
        if(DBUse) {
            Bukkit.getConsoleSender().sendMessage(Prefix + SystemSavingMethod + SystemSavingMethodMySQL);
        } else {
            Bukkit.getConsoleSender().sendMessage(Prefix + SystemSavingMethod + SystemSavingMethodLocalDB);
        }
        try (Connection connection = data.connect()) {

            new UpdateChecker(this, 117855).getVersion(VersionNew -> {
                Bukkit.getScheduler().runTask(this, () -> {
                    if (UpdateCheck) {
                        String VersionCurrent = this.getDescription().getVersion();
                        if (VersionCurrent.compareTo(VersionNew) >= 0) {
                            Bukkit.getConsoleSender().sendMessage(Prefix + SystemVersionNewest);
                            UpdateAvailable = false;
                        } else {
                            Bukkit.getConsoleSender().sendMessage(Prefix + SystemVersionOld1);
                            String versionmsg = SystemVersionOld2;
                            versionmsg = versionmsg.replaceAll("%_VERSIONOLD_%", VersionCurrent);
                            versionmsg = versionmsg.replaceAll("%_VERSIONNEW_%", VersionNew);
                            Bukkit.getConsoleSender().sendMessage(Prefix + versionmsg);
                            Bukkit.getConsoleSender().sendMessage(Prefix + SystemVersionOld3);
                            UpdateAvailable = true;
                        }
                    } else {
                        Bukkit.getConsoleSender().sendMessage(Prefix + SystemVersionSearchDeactivated);
                    }
                    // Initializing QRCodeMap Class
                    QRCodeMap qrcm = new QRCodeMap();

                    // Initializing Events and Commands
                    getServer().getPluginManager().registerEvents(new EventListener(this, data, Type, TypePerm), this);
                    getServer().getPluginManager().registerEvents(new JoinListener(this, data, qrcm, Prefix, Autologin, Range, UpdatePermInfo, UpdateAvailable), this);
                    this.getCommand("2fa").setExecutor(new TwoFACommand(this, data, qrcm, Prefix, Servername, RegisterPerm));
                    this.getCommand("2fa").setTabCompleter(new TwoFACommandCompleter());

                    Bukkit.getConsoleSender().sendMessage(Prefix + SystemPluginActivated + " §2(v" + this.getDescription().getVersion() + ")");
                });
            });
        } catch (SQLException e) {
            // Catch SQL-Connection error
            Bukkit.getConsoleSender().sendMessage( Prefix + SystemMySQLConnError);
            throw new RuntimeException(e);
        }
    }

    public void onDisable() {
        // Kick all players (for verify reasons)
        if(ReloadKick) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.kickPlayer(Prefix + UserReloadKickMSG);
            }
        }
        Bukkit.getConsoleSender().sendMessage(Prefix + SystemPluginDeactivated + " §4(v" + this.getDescription().getVersion() + ")");
    }

    public HashMap<UUID, Boolean> getVerifiedPlayers() {
        return verifiedPlayers;
    }

    public HashMap<UUID, Long> getPlayerTimeouts() {
        return PlayerTimeouts;
    }
}
