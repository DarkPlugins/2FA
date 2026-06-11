package de.darkplugins.Config.lang;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class de {

    private String folderPath = "plugins/2FA/lang";
    private String configName = "de.yml";

    private final Map<String, String> defaultValues = new HashMap<String, String>() {{
        put("PlayerNeeded", "&cNur Spieler können diesen Befehl verwenden");
        put("NoRegisterPerm", "&cDu hast keine Berechtigung zur Registrierung!");
        put("InvSlots", "&cEiner deiner Slots in der Hot bar muss frei sein, damit du den QR-Code direkt erhalten kannst!");
        put("NotSetup", "&cDu hast 2FA noch nicht eingerichtet. Verwende /2fa, um zu beginnen.");
        put("SetupCompleted", "&aDu hast 2FA erfolgreich eingerichtet.");
        put("VerifyCompleted", "&aDu wurdest verifiziert!");
        put("VerifyNeeded", "&cDu musst deinen 2fa Code mit /2fa <Code> eingeben.");
        put("VerifyWrongCode", "&cUngültiger Code! Bitte versuche es erneut.");
        put("VerifyGotCode", "&cDu hast bereits einen 2FA-Code erhalten. Richte diesen in deiner Authenticator-App ein.");
        put("VerifyError", "&cEs ist ein Fehler aufgetreten. Bitte kontaktiere einen Administrator. Weitere Details in der Konsole");
        put("VerifyLine1", "&aScanne diesen QR-Code mit deiner Authentifizierungs-App.");
        put("VerifyLine2", "&aAlternativ kannst du auch diesen Schlüssel verwenden: &e");
        put("VerifyLine3", "&aNutze danach /2fa <Code> (den du durch die Authentifikation-App erhältst) um die Verifizierung abzuschließen.");
        put("Verified", "&cDu bist bereits verifiziert");
        put("QRCodeGenError", "&cDer QR-Code konnte nicht generiert werden!");
        put("UnlinkComplete", "&aDeine 2FA wurde entfernt. Du kannst nun die Verknüpfung in deiner 2FA App auflösen.");
        put("UnlinkOtherComplete", "&cDeine 2FA wurde &evon einem Admin &centfernt. Bitte löse nun die Verknüpfung in deiner 2FA App auf.");
        put("UnlinkOtherCompleteAdmin", "&aDie Verknüpfung wurde erfolgreich aufgehoben!");
        put("UnlinkOtherFail", "&cDer Spieler muss online sein, damit du dessen Verknüpfung aufheben kannst!");
        put("Relogin", "&aIch habe mich an dich erinnert und dich automatisch angemeldet.");
        put("ReloadKickMSG", "&cDu wurdest vom Server geworfen, weil das 2FA-Plugin deaktiviert wurde. Möglicherweise aufgrund eines Neuladens des Servers");
    }};

    public de() {
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
