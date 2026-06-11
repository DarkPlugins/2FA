package de.darkplugins.Utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class TwoFAUtils {

    public static GoogleAuthenticatorKey generateSecretKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.createCredentials();
    }

    public static String getGoogleAuthenticatorBarCode(GoogleAuthenticatorKey secretKey, String account, String issuer) {
        return GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(issuer, account, secretKey);
    }

    public static boolean verifyCode(String secretKey, String code) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secretKey, Integer.parseInt(code));
    }

    public static String generateQRCodeImage(String barCodeData) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(barCodeData, BarcodeFormat.QR_CODE, 400, 400);

        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 400; x++) {
            for (int y = 0; y < 400; y++) {
                bufferedImage.setRGB(x, y, (bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF));
            }
        }

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(pngData);
    }
}
