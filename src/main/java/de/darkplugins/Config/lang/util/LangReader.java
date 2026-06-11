package de.darkplugins.Config.lang.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class LangReader {

    private static final String langError = "§4Critical language error. Check configuration or report this!";
    private static final String lang = getLang();
    private static final String langfolderPath = "plugins/2FA/lang/";
    private static final String langconfigName = lang + ".yml";

    private static String getLang() {
        String folderPath = "plugins/2FA";
        String configName = "config.yml";

        File configPath = new File(folderPath, configName);
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configPath);

        if (config.isString("Language")) {
            String Value = config.getString("Language");
            if (Value != null) {
                return Value;
            }
        }
        return langError;
    }

    // SET SYSTEM MESSAGES
    public static final String SystemLangConfError = SystemReader("SystemLangConfError");
    public static final String SystemMySQLConnError = SystemReader("SystemMySQLConnError");
    public static final String SystemSavingMethod = SystemReader("SavingMethod");
    public static final String SystemSavingMethodMySQL = "§eMySQL";
    public static final String SystemSavingMethodLocalDB = SystemReader("SystemSavingMethodLocalDB");
    public static final String SystemVersionNewest = SystemReader("SystemVersionNewest");
    public static final String SystemVersionOld1 = SystemReader("SystemVersionOld1");
    public static final String SystemVersionOld2 = SystemReader("SystemVersionOld2");
    public static final String SystemVersionOld3 = "§eDownload: §chttps://www.spigotmc.org/resources/simple-2fa.117855/history";
    public static final String SystemVersionSearchDeactivated = SystemReader("SystemVersionSearchDeactivated");
    public static final String SystemPluginActivated = SystemReader("SystemPluginActivated");
    public static final String SystemPluginDeactivated = SystemReader("SystemPluginDeactivated");
    public static final String SystemAddressUserError = SystemReader("SystemAddressUserError");

    // SET USER MESSAGES
    public static final String UserPlayerNeeded = UserReader("PlayerNeeded");
    public static final String UserNoRegisterPerm = UserReader("NoRegisterPerm");
    public static final String UserInvSlots = UserReader("InvSlots");
    public static final String UserNotSetup = UserReader("NotSetup");
    public static final String UserSetupCompleted = UserReader("SetupCompleted");
    public static final String UserVerifyCompleted = UserReader("VerifyCompleted");
    public static final String UserVerifyNeeded = UserReader("VerifyNeeded");
    public static final String UserVerifyWrongCode = UserReader("VerifyWrongCode");
    public static final String UserVerifyGotCode = UserReader("VerifyGotCode");
    public static final String UserVerifyError = UserReader("VerifyError");
    public static final String UserVerifyLine1 = UserReader("VerifyLine1");
    public static final String UserVerifyLine2 = UserReader("VerifyLine2");
    public static final String UserVerifyLine3 = UserReader("VerifyLine3");
    public static final String UserVerified = UserReader ("Verified");
    public static final String UserQRCodeGenError = UserReader("QRCodeGenError");
    public static final String UserUnlinkComplete = UserReader("UnlinkComplete");
    public static final String UserUnlinkOtherComplete = UserReader("UnlinkOtherComplete");
    public static final String UserUnlinkOtherCompleteAdmin = UserReader("UnlinkOtherCompleteAdmin");
    public static final String UserUnlinkOtherFail = UserReader("UnlinkOtherFail");
    public static final String UserRelogin = UserReader("Relogin");
    public static final String UserReloadKickMSG = UserReader("ReloadKickMSG");

    // GET USER MESSAGES
    private static String UserReader(String reading) {
        File configPath = new File(langfolderPath, langconfigName);
        if(configPath.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(configPath);

            if (config.isString(reading)) {
                String Value = config.getString(reading);
                if (Value != null) {
                    return ChatColor.translateAlternateColorCodes('&', Value);
                }
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(SystemLangConfError);
        }
        return langError;
    }

    // GET SYSTEM MESSAGES
    public static String SystemReader(String reading) {
        if(lang.equals("de") || lang.equals("DE") || lang.equals("de-de") || lang.equals("DE-DE")) {
            switch (reading) {
                case "SystemLangConfError":
                    return "§4Der angegebene Dateipfad für die Sprachauswahl konnte nicht gefunden werden. Bitte überprüfe die Konfiguration! §c(" + langfolderPath + langconfigName + ")";
                case "SystemMySQLConnError":
                    return "§cFehler bei dem Verbindungsaufbau mit der MySQL Datenbank. Plugin deaktiviert:";
                case "SavingMethod":
                    return "§7Speichermethode: ";
                case "SystemSavingMethodLocalDB":
                    return "§eLokale Datenbank";
                case "SystemVersionNewest":
                    return "§eDas Plugin ist auf der §aaktuellsten §eVersion.";
                case "SystemVersionOld1":
                    return "§eNeuere Plugin-Version gefunden:";
                case "SystemVersionOld2":
                    return "§eAktuell: §cv%_VERSIONOLD_% §e| Neu: §av%_VERSIONNEW_%";
                case "SystemVersionSearchDeactivated":
                    return "§cUpdate-Suche ist §4deaktiviert§c.";
                case "SystemPluginActivated":
                    return "§a§l2FA-Plugin aktiviert!";
                case "SystemPluginDeactivated":
                    return "§c§l2FA-Plugin deaktiviert!";
                case "SystemAddressUserError":
                    return "§4Ein Fehler ist aufgetreten: §cDie angeforderte Adresse ist null!";
                default:
                    return langError;
            }
        } else {
            switch (reading) {
                case "SystemLangConfError":
                    return "§4The specified file path for the language selection could not be found. Please check the configuration! §c(" + langfolderPath + langconfigName + ".yml)";
                case "SystemMySQLConnError":
                    return "§cError establishing connection to the MySQL database. Plugin disabled:";
                case "SavingMethod":
                    return "§7Storage method: ";
                case "SystemSavingMethodLocalDB":
                    return "§eLocal database";
                case "SystemVersionNewest":
                    return "§eThe plugin is on the §alatest §eversion.";
                case "SystemVersionOld1":
                    return "§eNewer plugin version found:";
                case "SystemVersionOld2":
                    return "§eCurrent: §cv%_VERSIONOLD_% §e| New: §av%_VERSIONNEW_%";
                case "SystemVersionSearchDeactivated":
                    return "§cUpdate search is §4disabled§c.";
                case "SystemPluginActivated":
                    return "§a§l2FA-Plugin activated!";
                case "SystemPluginDeactivated":
                    return "§c§l2FA-Plugin deactivated!";
                case "SystemImportFolderError":
                    return "§cAn error occurred while importing!";
                case "SystemAddressUserError":
                    return "§4An error occurred: §cThe requested address is null!";
                default:
                    return langError;
            }
        }
    }
}
