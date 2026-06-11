package de.darkplugins.Config;

import org.bukkit.event.Listener;
import java.io.File;
import java.sql.*;
import java.util.UUID;

public class Data implements Listener {

    private String DBHost, DBPort, DBName, DBUsername, DBPw;
    private boolean DBUse;
    private final String folderPath = "plugins/2FA/data";
    private final String databasePath = "plugins/2FA/data/data.db";

    public Data(boolean dbuse, String dbhost, String dbport, String dbname, String dbusername, String dbpw) {
        this.DBUse = dbuse;
        this.DBHost = dbhost;
        this.DBPort = dbport;
        this.DBName = dbname;
        this.DBUsername = dbusername;
        this.DBPw = dbpw;
        createFolder();
        createTable();
    }

    private void createFolder() {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public Connection connect() throws SQLException {
        if (DBUse) {
            String url = "jdbc:mysql://" + DBHost + ":" + DBPort + "/" + DBName;
            return DriverManager.getConnection(url, DBUsername, DBPw);
        } else {
            return connectLocal();
        }
    }

    private Connection connectLocal() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + databasePath);
    }

    private void createTable() {
        String sqlKey, sqlIP;
        if (DBUse) {
            sqlKey = "CREATE TABLE IF NOT EXISTS `keys` ("
                    + "`uuid` VARCHAR(36) PRIMARY KEY,"
                    + "`key` VARCHAR(255)"
                    + ");";
            sqlIP = "CREATE TABLE IF NOT EXISTS `ips` ("
                    + "`uuid` VARCHAR(36) PRIMARY KEY,"
                    + "`ip` VARCHAR(255),"
                    + "`login` VARCHAR(255)"
                    + ");";
        } else {
            sqlKey = "CREATE TABLE IF NOT EXISTS keys ("
                    + "uuid TEXT PRIMARY KEY,"
                    + "key TEXT"
                    + ");";
            sqlIP = "CREATE TABLE IF NOT EXISTS ips ("
                    + "uuid TEXT PRIMARY KEY,"
                    + "ip TEXT,"
                    + "login TEXT"
                    + ");";
        }

        try (Connection connection = connect();
             PreparedStatement stmtKeys = connection.prepareStatement(sqlKey);
             PreparedStatement stmtIPs = connection.prepareStatement(sqlIP)) {

            stmtKeys.execute();
            stmtIPs.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getKey(UUID uuid) {
        String sql = DBUse ? "SELECT `key` FROM `keys` WHERE `uuid` = ?" : "SELECT key FROM keys WHERE uuid = ?";
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("key");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setKey(UUID uuid, String key) {
        String sql = "INSERT INTO `keys` (`uuid`, `key`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `key` = VALUES(`key`);";
        if (!DBUse) {
            sql = "INSERT INTO keys (uuid, key) VALUES (?, ?) ON CONFLICT(uuid) DO UPDATE SET key = EXCLUDED.key;";
        }
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, key);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setIP(UUID uuid, String ip, String login) {
        String sql = "INSERT INTO `ips` (`uuid`, `ip`, `login`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE `ip` = VALUES(`ip`), `login` = VALUES(`login`);";
        if (!DBUse) {
            sql = "INSERT INTO ips (uuid, ip, login) VALUES (?, ?, ?) ON CONFLICT(uuid) DO UPDATE SET ip = EXCLUDED.ip, login = EXCLUDED.login;";
        }
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, ip);
            stmt.setString(3, login);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getIP(UUID uuid) {
        String sql = DBUse ? "SELECT `ip` FROM `ips` WHERE `uuid` = ?" : "SELECT ip FROM ips WHERE uuid = ?";
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("ip");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLogin(UUID uuid) {
        String sql = DBUse ? "SELECT `login` FROM `ips` WHERE `uuid` = ?" : "SELECT login FROM ips WHERE uuid = ?";
        try (Connection connection = connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("login");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void remove(UUID uuid) {
        String sqlDeleteKey = "DELETE FROM `keys` WHERE `uuid` = ?";
        String sqlDeleteIP = "DELETE FROM `ips` WHERE `uuid` = ?";
        if (!DBUse) {
            sqlDeleteKey = "DELETE FROM keys WHERE uuid = ?";
            sqlDeleteIP = "DELETE FROM ips WHERE uuid = ?";
        }
        try (Connection connection = connect();
             PreparedStatement stmtDeleteKey = connection.prepareStatement(sqlDeleteKey);
             PreparedStatement stmtDeleteIP = connection.prepareStatement(sqlDeleteIP)) {
            stmtDeleteKey.setString(1, uuid.toString());
            stmtDeleteIP.setString(1, uuid.toString());
            stmtDeleteKey.executeUpdate();
            stmtDeleteIP.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}