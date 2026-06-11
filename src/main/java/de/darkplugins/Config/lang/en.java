package de.darkplugins.Config.lang;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class en {

    private String folderPath = "plugins/2FA/lang";
    private String configName = "en.yml";

    private final Map<String, String> defaultValues = new HashMap<String, String>() {{
        put("PlayerNeeded", "&cOnly players can use this command.");
        put("NoRegisterPerm", "&cYou don't have permission to register!");
        put("InvSlots", "&cOne slot in your hotbar must be free so you can receive the QR code directly!");
        put("NotSetup", "&cYou haven't set up 2FA yet. Use /2fa to start.");
        put("SetupCompleted", "&aYou have successfully set up 2FA.");
        put("VerifyCompleted", "&aYou have been verified!");
        put("VerifyNeeded", "&cYou must enter your 2FA code using /2fa <code>.");
        put("VerifyWrongCode", "&cInvalid code! Please try again.");
        put("VerifyGotCode", "&cYou have already received a QR code. Set it up in your authenticator app.");
        put("VerifyError", "&cAn error has occurred. Please contact an administrator. See the console for more details.");
        put("VerifyLine1", "&aScan this QR code with your authenticator app.");
        put("VerifyLine2", "&aAlternatively, you can use this key: &e");
        put("VerifyLine3", "&aThen use /2fa <code> (provided by your authenticator app) to complete verification.");
        put("Verified", "&cYou are already verified.");
        put("QRCodeGenError", "&cFailed to generate QR code!");
        put("UnlinkComplete", "&aYour 2FA has been removed. You can now unlink it in your authenticator app.");
        put("UnlinkOtherComplete", "&cYour 2FA has been removed &eby an administrator&c. Please unlink it in your authenticator app.");
        put("UnlinkOtherCompleteAdmin", "&aThe player's 2FA was successfully removed!");
        put("UnlinkOtherFail", "&cThe player must be online before you can unlink their 2FA!");
        put("Relogin", "&aI remembered you and signed you in automatically.");
        put("ReloadKickMSG", "&cYou were kicked from the server because the 2FA plugin was disabled, possibly due to a server reload.");
    }};

    public en() {
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

        boolean saveNeeded = false;

        for (Map.Entry<String, String> entry : defaultValues.entrySet()) {
            if (!config.contains(entry.getKey())) {
                config.set(entry.getKey(), entry.getValue());
                saveNeeded = true;
            }
        }

        if (saveNeeded) {
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
