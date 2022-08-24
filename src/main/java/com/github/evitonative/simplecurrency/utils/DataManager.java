package com.github.evitonative.simplecurrency.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import static com.github.evitonative.simplecurrency.SimpleCurrency.plugin;

public class DataManager implements Listener {

    private static final Map<UUID, CurrencyPlayer> players = new HashMap<>();
    private static Connection conn;
    private static final String table = plugin.getConfig().getString("mysql.prefix") + "players";
    private static final boolean mySql = plugin.getConfig().getBoolean("mysql.enabled");

    public static void initializeDatabaseConnection() throws SQLException, NullPointerException {
        boolean mySql = plugin.getConfig().getBoolean("mysql.enabled");
        String url;

        if (mySql) {
            url = "jdbc:mysql://" + plugin.getConfig().getString("mysql.url") + ":" + plugin.getConfig().getInt("mysql.port") + "/" + plugin.getConfig().getString("mysql.database");
            conn = DriverManager.getConnection(url, plugin.getConfig().getString("mysql.user"), plugin.getConfig().getString("mysql.password"));
        } else {
            url = "jdbc:sqlite:" + plugin.getDataFolder() + "/sc.db";
            conn = DriverManager.getConnection(url);
        }

        Statement stm = conn.createStatement();
        stm.execute("CREATE TABLE IF NOT EXISTS " + table + " (uuid varchar(36) primary key not null, name varchar(16) not null, balance bigint not null)");
    }

    public static void closeDatabaseConnection() {
        saveAndCleanup();
        try {
            conn.close();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to close database connection");
        }
        conn = null;
    }

    /**
     * Gets the UUID of a player with a bank account.
     *
     * @param username Username of the Player
     * @return The uuid or null if the player has no account
     */
    public static @Nullable UUID getUUID(String username) {
        ResultSet rs;
        try {
            Statement stm = conn.createStatement();
            String statement = "SELECT uuid FROM " + table + " WHERE name = '" + username + "'";
            plugin.getLogger().log(Level.FINEST, "Statement for getting a uuid: " + statement);
            rs = stm.executeQuery(statement);

            if(!rs.next()) return null;
            else return UUID.fromString(rs.getString(1));
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get a players balance
     *
     * @param uuid UUID of the player
     * @return Balance of player
     */
    public static Long getPlayerBalance(UUID uuid) {
        fixPlayer(uuid);

        return players.get(uuid).getAmount();
    }

    /**
     * Set a players balance
     *
     * @param uuid   UUID of the player
     * @param amount Amout to set the balance to
     */
    public static void setPlayerBalance(UUID uuid, long amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount less than 0");

        fixPlayer(uuid);
        players.get(uuid).setAmount(amount);
    }

    /**
     * Add to a players balance
     *
     * @param uuid   UUID of the player
     * @param amount Amout to add to the balance
     */
    public static void addToPlayerBalance(UUID uuid, long amount) {
        fixPlayer(uuid);
        long newAmount = getPlayerBalance(uuid) + amount;
        if (newAmount < 0) throw new IllegalArgumentException("New amount less than 0");
        setPlayerBalance(uuid, newAmount);
    }

    /**
     * Checks if a player has not moved after the last time this method ran
     *
     * @param player The player to perform the check on
     * @return If a player is afk
     */
    public static boolean isPlayerAFK(@NotNull Player player) {
        CurrencyPlayer currencyPlayer = players.get(player.getUniqueId());
        if (currencyPlayer == null) {
            getPlayerBalance(player.getUniqueId());
            currencyPlayer = players.get(player.getUniqueId());
        }
        double[] lastXYZ = currencyPlayer.getLastXYZ();
        if (player.getLocation().getX() == lastXYZ[0] &&
                player.getLocation().getY() == lastXYZ[1] &&
                player.getLocation().getZ() == lastXYZ[2])
            return true;

        currencyPlayer.setLastXYZ(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        players.replace(player.getUniqueId(), currencyPlayer);
        return false;
    }

    private static void fixPlayer(UUID uuid){
        if (players.get(uuid) == null){
            boolean foundPlayer = false;

            try {
                Statement stm = conn.createStatement();
                String statement = "SELECT balance FROM " + table + " WHERE uuid = '" + uuid + "'";
                plugin.getLogger().log(Level.FINEST, "Statement for getting balance: " + statement);
                ResultSet rs = stm.executeQuery(statement);
                if(rs.next()){
                    players.put(uuid, new CurrencyPlayer(rs.getLong(1), uuid));
                    foundPlayer = true;
                }
            } catch (SQLException e) {
                throw new RuntimeException("An error occurred while trying to get a players balance.", e);
            }

            if(!foundPlayer) players.put(uuid, new CurrencyPlayer(plugin.getConfig().getLong("default-amount"), uuid));
        }
    }

    @EventHandler
    public void onWorldSave(WorldSaveEvent event){
        if(event.getWorld() == plugin.getServer().getWorlds().get(0)) return;
        saveAndCleanup();
    }

    private static void saveAndCleanup(){
        players.forEach((k, v) -> {
            try {
                Statement stm = conn.createStatement();
                String statement;
                if(mySql) statement = "INSERT INTO " + table + " (uuid, name, balance) VALUES ('" + v.getUuid() + "', '" + v.getName() + "', " + v.getAmount() + ") ON DUPLICATE KEY UPDATE name='" + v.getName() + "', balance=" + v.getAmount();
                else statement = "INSERT OR REPLACE INTO " + table + " (uuid, name, balance) VALUES ('" + v.getUuid() + "', '" + v.getName() + "', " + v.getAmount() + ")";
                stm.execute(statement);

                if(plugin.getServer().getPlayer(k) == null) players.remove(k);
                stm.close();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.WARNING, "Unable to save player (UUID=" + k + ")", e);
            }

        });
    }
}
