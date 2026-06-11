package de.darkplugins.Utils;

import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

public class QRCodeRenderer extends MapRenderer {

    private final String qrCodeImageBase64;
    private boolean hasRendered = false;

    public QRCodeRenderer(String qrCodeImageBase64) {
        this.qrCodeImageBase64 = qrCodeImageBase64;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, org.bukkit.entity.Player player) {
        if (hasRendered) return;
        hasRendered = true;

        try {
            byte[] qrCodeBytes = Base64.getDecoder().decode(qrCodeImageBase64);
            ByteArrayInputStream bis = new ByteArrayInputStream(qrCodeBytes);
            BufferedImage qrCodeImage = ImageIO.read(bis);

            // Resize the QR code image to fit the map size
            int mapSize = 128;  // Map size in pixels
            int qrSize = 145;   // QR code size to fit within the map

            Image scaledImage = qrCodeImage.getScaledInstance(qrSize, qrSize, Image.SCALE_SMOOTH);
            BufferedImage resizedQrCodeImage = new BufferedImage(qrSize, qrSize, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = resizedQrCodeImage.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            int x = (mapSize - qrSize) / 2;
            int y = (mapSize - qrSize) / 2;

            mapCanvas.drawImage(x, y, resizedQrCodeImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
