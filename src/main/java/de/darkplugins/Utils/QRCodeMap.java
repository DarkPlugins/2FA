package de.darkplugins.Utils;

import com.google.zxing.WriterException;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.io.IOException;

public class QRCodeMap {

    private final String QRName = "§9§l2FA QR-Code";

    public QRCodeMap() {

    }

    public ItemStack createQRCodeMap(Player player, String qrCodeBase64) throws IOException, WriterException {
        ItemStack map = new ItemStack(Material.FILLED_MAP, 1);
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        mapMeta.setColor(Color.RED);
        mapMeta.setUnbreakable(true);
        mapMeta.setDisplayName(QRName);
        MapView mapView = Bukkit.createMap(player.getWorld());

        mapView.addRenderer(new QRCodeRenderer(qrCodeBase64));
        mapMeta.setMapView(mapView);
        map.setItemMeta(mapMeta);

        return map;
    }

    public void removeMap(Player player) {
        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            ItemStack item = inventory.getItem(slot);
            if (item != null) {
                if (item.getType() == Material.FILLED_MAP) {
                    ItemMeta itemMeta = item.getItemMeta();
                    if (itemMeta != null && itemMeta.isUnbreakable() && itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals(QRName)) {
                        inventory.setItem(slot, new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

}
