package com.github.evitonative.simplecurrency;

import com.github.evitonative.simplecurrency.utils.ColourFormatting;
import com.github.evitonative.simplecurrency.utils.DataManager;
import com.github.evitonative.simplecurrency.utils.PhysicalCurrencyItem;
import com.github.evitonative.simplecurrency.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

public final class SimpleCurrency extends JavaPlugin {
    public static SimpleCurrency plugin;
    public static List<PhysicalCurrencyItem> physicalCurrencyItems;

    private static Integer taskId = null;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        plugin = this;
        this.getCommand("pay").setExecutor(new PayCommand());
        this.getCommand("bal").setExecutor(new BalCommand());
        this.getCommand("withdraw").setExecutor(new WithdrawCommand());
        this.getCommand("deposit").setExecutor(new DepositCommand());
        this.getCommand("bank").setExecutor(new BankCommand());
        this.getCommand("screload").setExecutor(new ReloadCommand());

        this.saveDefaultConfig();
        try {
            DataManager.initializeDatabaseConnection();
        } catch (SQLException e) {
            this.getLogger().log(Level.SEVERE, "Failed to initialize Database connection. Disabling...", e);
            getPluginLoader().disablePlugin(this);
        }

        this.getServer().getPluginManager().registerEvents(new DataManager(), this);

        reload();
    }

    @Override
    public void onDisable() {
        DataManager.closeDatabaseConnection();
        plugin = null;
        physicalCurrencyItems = null;
        if(taskId != null) Bukkit.getScheduler().cancelTask(taskId);
    }

    public static void reload(){
        physicalCurrencyItems = new ArrayList<>();
        //noinspection unchecked
        List<LinkedHashMap<String, Object>> payoutItemsList = plugin.getConfig().getObject("items", List.class);
        payoutItemsList.forEach(o -> {
            //noinspection unchecked
            PhysicalCurrencyItem item = new PhysicalCurrencyItem((int) o.get("value"),
                    Material.getMaterial((String) o.get("item")),
                    ColourFormatting.parse((String) o.get("name")),
                    ColourFormatting.parseWithPlaceholders(((List<String>) o.get("lore")).toArray(String[]::new), o.get("value")).toArray(String[]::new),
                    (Integer) o.get("customModelData"));
            physicalCurrencyItems.add(item);
        });

        if(taskId != null) Bukkit.getScheduler().cancelTask(taskId);
        taskId = null;
        if(plugin.getConfig().getBoolean("autobalance.enabled"))
            //noinspection Convert2Lambda
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                        if(!DataManager.isPlayerAFK(player) || plugin.getConfig().getBoolean("autobalance.ignore-afk"))
                            DataManager.addToPlayerBalance(player.getUniqueId(), plugin.getConfig().getLong("autobalance.amount"));
                    });
                }
            }, 0, 20L * plugin.getConfig().getInt("autobalance.delay"));
    }
}
