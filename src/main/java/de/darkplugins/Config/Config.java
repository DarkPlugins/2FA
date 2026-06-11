package de.darkplugins.Config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Config implements Listener {

    private String folderPath = "plugins/2FA";
    private String folderPathBackup = "plugins/2FA/backups";
    private String configName = "config.yml";
    private boolean Backup;

    public Config() {
        createFolder();
        createYML();
    }

    public void createFolder() {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
        }
    }

    public void createYML() {
        File file = new File(folderPath, configName);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(!file.exists()) {
            try (FileWriter fw = new FileWriter(file)) {
                fw.write("########################################\n");
                fw.write("#                                      #\n");
                fw.write("#    Thanks for using this Plugin :)   #\n");
                fw.write("#                                      #\n");
                fw.write("########################################\n");
                fw.write("\n");
                fw.write("# This is the prefix added before every message #\n");
                fw.write("Prefix: '&5&l2FA &8≫ '\n");
                fw.write("# Current plugin language. (You can edit it in the ./lang/ folder) #\n");
                fw.write("Language: 'en'\n");
                fw.write("# bStats #\n");
                fw.write("Metrics: 'true'\n\n");
                fw.write("# This is the name that will appear in the 2FA-Authenticator App before the username #\n");
                fw.write("Servername: 'MinecraftServer'\n\n");
                fw.write("# The permission a player needs to setup 2fa #\n");
                fw.write("# Note: This only includes the setup. /2fa <Code> can still be used once setup finished #\n");
                fw.write("RegisterPerm: 'system.2fa.register'\n\n");
                fw.write("# Enable/Disable auto login on join if the IP address matches #\n");
                fw.write("Autologin: 'true'\n");
                fw.write("# Select the period (in minutes) in which the autologin can occur after the player has last left the server #\n");
                fw.write("# 900 minutes = 15 hours #\n");
                fw.write("Range: '900'\n\n");
                fw.write("# Select the 2fa type (usefull if you want certain players to enable it) #\n");
                fw.write("# choice = everyone can enable 2fa if they want to #\n");
                fw.write("# permission = players with a certain permission (TypePerm) will need to enable 2fa #\n");
                fw.write("# global = every player will need to enable 2fa #\n");
                fw.write("Type: 'choice'\n");
                fw.write("# The permission to force a player to enable 2fa #\n");
                fw.write("# Note: This only be used if Type is set to permission #\n");
                fw.write("TypePerm: 'system.2fa.needed'\n\n");
                fw.write("# Enable to kick players on plugin / server reload (recommended) #\n");
                fw.write("ReloadKick: 'true'\n\n");
                fw.write("# Decides whether you want to receive information about newer versions of the plugin #\n");
                fw.write("# Use: true = yes | false = no #\n");
                fw.write("Updates: 'true'\n");
                fw.write("# The permission a player needs to be notified of new plugin updates #\n");
                fw.write("UpdatePerms: 'system.ads.notify'\n\n");
                fw.write("# MySQL #\n");
                fw.write("Use: 'false'\n");
                fw.write("Host: '127.0.0.1'\n");
                fw.write("Port: '3306'\n");
                fw.write("Database: '2fadb'\n");
                fw.write("Username: 'User'\n");
                fw.write("Password: '123456789'\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());

                boolean hasPrefix = false;
                boolean hasLanguage = false;
                boolean hasbStats = false;
                boolean hasServername = false;
                boolean hasRegisterPerm = false;
                boolean hasAutologin = false;
                boolean hasRange = false;
                boolean hasType = false;
                boolean hasTypePerm = false;
                boolean hasReloadKick = false;
                boolean hasUpdates = false;
                boolean hasUpdatePerms = false;
                boolean hasDBUse = false;
                boolean hasDBHost = false;
                boolean hasDBPort = false;
                boolean hasDBName = false;
                boolean hasDBUser = false;
                boolean hasDBPw = false;

                for (String line : lines) {
                    if (!line.trim().startsWith("#")) {
                        if (line.startsWith("Prefix:")) {
                            hasPrefix = true;
                        } else if (line.startsWith("Language:")) {
                            hasLanguage = true;
                        } else if (line.startsWith("Metrics:")) {
                            hasbStats = true;
                        } else if (line.startsWith("Servername:")) {
                            hasServername = true;
                        } else if (line.startsWith("RegisterPerm:")) {
                            hasRegisterPerm = true;
                        } else if (line.startsWith("Autologin:")) {
                            hasAutologin = true;
                        } else if (line.startsWith("Range:")) {
                            hasRange = true;
                        } else if (line.startsWith("Type:")) {
                            hasType = true;
                        } else if (line.startsWith("TypePerm:")) {
                            hasTypePerm = true;
                        } else if (line.startsWith("ReloadKick")) {
                            hasReloadKick = true;
                        } else if (line.startsWith("Updates:")) {
                            hasUpdates = true;
                        } else if (line.startsWith("UpdatePerms:")) {
                            hasUpdatePerms = true;
                        } else if (line.startsWith("Use:")) {
                            hasDBUse = true;
                        } else if (line.startsWith("Host:")) {
                            hasDBHost = true;
                        } else if (line.startsWith("Port:")) {
                            hasDBPort = true;
                        } else if (line.startsWith("Database:")) {
                            hasDBName = true;
                        } else if (line.startsWith("Username:")) {
                            hasDBUser = true;
                        } else if (line.startsWith("Password:")) {
                            hasDBPw = true;
                        }
                    }
                }

                if (!hasPrefix) {
                    String wert = Backup("Prefix");
                    String value = "Prefix: '" + wert + "'\n";
                    write(file, value);
                }
                if (!hasLanguage) {
                    String wert = Backup("Language");
                    String value1 = "# Current plugin language. (You can edit it in the ./lang/ folder) #\n";
                    String value2 = "Language: '" + wert + "'\n";
                    write(file, value1);
                    write(file, value2);
                }
                if (!hasbStats) {
                    String wert = Backup("Metrics");
                    String value1 = "# bStats #\n";
                    String value2 = "Metrics: '" + wert + "'\n\n";
                    write(file, value1);
                    write(file, value2);
                }
                if (!hasServername) {
                    String wert = Backup("Servername");
                    String value1 = "# This is the name that will appear in the 2FA-Authenticator App before the username #\n";
                    String value2 = "Servername: '" + wert + "'\n\n";
                    write(file, value1);
                    write(file, value2);
                }
                if (!hasRegisterPerm) {
                    String wert = Backup("RegisterPerm");
                    String value1 = "# The permission a player needs to setup 2fa #\n";
                    String value2 = "# Note: This only includes the setup. /2fa <Code> can still be used once setup finished #\\n";
                    String value3 = "RegisterPerm: '" + wert + "'\n\n";
                    write(file, value1);
                    write(file, value2);
                    write(file, value3);
                }
                if (!hasAutologin) {
                    String wert = Backup("Autologin");
                    String value1 = "# Enable/Disable auto login on join if the IP address matches #\n";
                    String value2 = "Autologin: '" + wert + "'\n";
                    write(file, value1);
                    write(file, value2);
                }
                if (!hasRange) {
                    String wert = Backup("Range");
                    String value1 = "# Select the period (in minutes) in which the autologin can occur after the player has last left the server #\n";
                    String value2 = "# 900 minutes = 15 hours #\n";
                    String value3 = "Range: '" + wert + "'\n\n";
                    write(file, value1);
                    write(file, value2);
                    write(file, value3);
                }
                if (!hasType) {
                    String wert = Backup("Type");
                    String value1 = "# Select the 2fa type (usefull if you want certain players to enable it) #\n";
                    String value2 = "# choice = everyone can enable 2fa if they want to #\n";
                    String value3 = "# permission = players with a certain permission (TypePerm) will need to enable 2fa #\n";
                    String value4 = "# global = every player will need to enable 2fa #\n";
                    String value5 = "Type: '" + wert + "'\n";
                    write(file, value1);
                    write(file, value2);
                    write(file, value3);
                    write(file, value4);
                    write(file, value5);
                }
                if (!hasTypePerm) {
                    String wert = Backup("TypePerm");
                    String value1 = "# The permission to force a player to enable 2fa #\n";
                    String value2 = "# Note: This only be used if Type is set to permission #\n";
                    String value3 = "TypePerm: '" + wert + "'\n\n";
                    write(file, value1);
                    write(file, value2);
                    write(file, value3);
                }
                if (!hasReloadKick) {
                    String wert = Backup("ReloadKick");
                    String value1 = "# Enable to kick players on plugin / server reload (recommended) #\n";
                    String value2 = "ReloadKick: '" + wert + "'\n";
                    write(file, value1);
                    write(file, value2);
                }
                if (!hasUpdates) {
                    String wert = Backup("Updates");
                    String value1 = "# Decides whether you want to receive information about newer versions of the plugin #\n";
                    String value2 = "# Use: true = yes | false = no #";
                    String value3 = "Updates: '" + wert + "'\n";
                    write(file, value1);
                    write(file, value2);
                    write(file, value3);
                }
                if (!hasUpdatePerms) {
                    String wert = Backup("UpdatePerms");
                    String value1 = "# The permission a player needs to be notified of new plugin updates #\n";
                    String value2 = "UpdatePerms: '" + wert + "'\n\n";
                    write(file, value1);
                    write(file, value2);
                }
                if (!hasDBUse) {
                    String wert = Backup("Use");
                    String value1 = "\n# MySQL #\n";
                    String value2 = "Use: '" + wert + "'\n";
                    write(file, value1);
                    write(file, value2);
                }
                if (!hasDBHost) {
                    String wert = Backup("Host");
                    String value1 = "Host: '" + wert + "'\n";
                    write(file, value1);
                }
                if (!hasDBPort) {
                    String wert = Backup("Port");
                    String value1 = "Port: '" + wert + "'\n";
                    write(file, value1);
                }
                if (!hasDBName) {
                    String wert = Backup("Database");
                    String value1 = "Database: '" + wert + "'\n";
                    write(file, value1);
                }
                if (!hasDBUser) {
                    String wert = Backup("Username");
                    String value1 = "Username: '" + wert + "'\n";
                    write(file, value1);
                }
                if (!hasDBPw) {
                    String wert = Backup("Password");
                    String value1 = "Password: '" + wert + "'\n";
                    write(file, value1);
                }
                EndBackup();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String ReadYML(String wert) {
        File configPath = new File(folderPath, configName);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configPath);

        if (config.isString(wert)) {
            String rawValue = config.getString(wert);
            if (rawValue != null) {
                return ChatColor.translateAlternateColorCodes('&', rawValue);
            }
        }
        return null;
    }

    private String Backup(String wert) {
        if (!Backup) {
            Backup = true;
            File folderBackup = new File(folderPathBackup);
            if (!folderBackup.exists()) {
                boolean created = folderBackup.mkdirs();
            }
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            String backupTime = currentTime.format(formatter);
            String backupName = "Backup_" + configName + "_" + backupTime + ".yml";
            File backupFile = new File(folderPathBackup, backupName);
            File originalFile = new File(folderPath, configName);

            if (originalFile.exists()) {
                try (InputStream in = Files.newInputStream(originalFile.toPath());
                     OutputStream out = Files.newOutputStream(backupFile.toPath())) {

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }

                    try (FileWriter writer = new FileWriter(backupFile, true)) {
                        String value1 = "\n\n\n#################################################################\n";
                        String value2 = "#####    This backup was created due to a missing value.    #####\n";
                        String value3 = "##### It only serves as a backup in case settings are lost. #####\n";
                        String value4 = "#################################################################\n";
                        writer.write(value1);
                        writer.write(value2);
                        writer.write(value3);
                        writer.write(value4);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to write additional information to the backup file.");
                    }

                } catch (IOException e) {
                    throw new RuntimeException("Failed to create a backup.");
                }
            } else {
                throw new RuntimeException("Original file does not exist.");
            }
        }
        String wertContent = ReadYML(wert);
        if(wertContent == null) {
            switch (wert) {
                case "Prefix":
                    wertContent = "&5&l2FA &8≫ ";
                    break;
                case "Language":
                    wertContent = "en";
                    break;
                case "Metrics":
                    wertContent = "true";
                    break;
                case "Servername":
                    wertContent = "MinecraftServer";
                    break;
                case "RegisterPerm":
                    wertContent = "system.2fa.register";
                    break;
                case "Autologin":
                    wertContent = "true";
                    break;
                case "Range":
                    wertContent = "900";
                    break;
                case "Type":
                    wertContent = "choice";
                    break;
                case "TypePerm":
                    wertContent = "system.2fa.needed";
                    break;
                case "ReloadKick":
                    wertContent = "true";
                    break;
                case "Updates":
                    wertContent = "true";
                    break;
                case "UpdatePerms":
                    wertContent = "system.ads.notify";
                    break;
                case "Use":
                    wertContent = "false";
                    break;
                case "Host":
                    wertContent = "127.0.0.1";
                    break;
                case "Port":
                    wertContent = "3306";
                    break;
                case "Database":
                    wertContent = "adsdb";
                    break;
                case "Username":
                    wertContent = "User";
                    break;
                case "Password":
                    wertContent = "123456789";
                    break;
            }
        }
        return wertContent;
    }

    private void EndBackup() {
        if(Backup) {
            Backup = false;
        }
    }

    private void write(File file, String value) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
