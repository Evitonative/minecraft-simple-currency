package com.github.evitonative.simplecurrency.utils;

import com.github.evitonative.simplecurrency.SimpleCurrency;
import org.bukkit.ChatColor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class ColourFormatting {
    public static String parse(String message){
        return message
                .replace("&0", ChatColor.BLACK.toString())
                .replace("&1", ChatColor.DARK_BLUE.toString())
                .replace("&2", ChatColor.DARK_GREEN.toString())
                .replace("&3", ChatColor.DARK_AQUA.toString())
                .replace("&4", ChatColor.DARK_RED.toString())
                .replace("&5", ChatColor.DARK_PURPLE.toString())
                .replace("&6", ChatColor.GOLD.toString())
                .replace("&7", ChatColor.GRAY.toString())
                .replace("&8", ChatColor.DARK_GRAY.toString())
                .replace("&9", ChatColor.BLUE.toString())
                .replace("&a", ChatColor.GREEN.toString())
                .replace("&b", ChatColor.AQUA.toString())
                .replace("&c", ChatColor.RED.toString())
                .replace("&d", ChatColor.LIGHT_PURPLE.toString())
                .replace("&e", ChatColor.YELLOW.toString())
                .replace("&f", ChatColor.WHITE.toString())
                .replace("&k", ChatColor.MAGIC.toString())
                .replace("&l", ChatColor.BOLD.toString())
                .replace("&m", ChatColor.STRIKETHROUGH.toString())
                .replace("&n", ChatColor.UNDERLINE.toString())
                .replace("&o", ChatColor.ITALIC.toString())
                .replace("&r", ChatColor.RESET.toString());
    }

    public static List<String> parse(String[] message){
        List<String> list = new ArrayList<>();

        for (String str:message) {
            list.add(parse(str));
        }

        return list;
    }

    public static String parseWithPlaceholders(String message, Object... values){
        return parse(MessageFormat.format(message, values));
    }

    public static List<String> parseWithPlaceholders(String[] message, Object... values){
        List<String> list = new ArrayList<>();

        for (String str:message) {
            list.add(parse(MessageFormat.format(str, values)));
        }

        return list;
    }

    public static String parseFromConfig(String configPath){
        return parse(Objects.requireNonNull(SimpleCurrency.plugin.getConfig().getString(configPath)));
    }

    public static List<String> parseListFromConfig(String configPath){
        List<String> list = new ArrayList<>();

        for (String str:SimpleCurrency.plugin.getConfig().getStringList(configPath)) {
            list.add(parse(str));
        }

        return list;
    }

    public static String parseFromConfigWithPlaceholders(String configPath, Object... values){
        return parse(MessageFormat.format(Objects.requireNonNull(SimpleCurrency.plugin.getConfig().getString(configPath)), values));
    }

    public static List<String> parseListFromConfigWithPlaceholders(String configPath, Object... values){
        List<String> list = new ArrayList<>();

        for (String str:SimpleCurrency.plugin.getConfig().getStringList(configPath)) {
            list.add(parse(MessageFormat.format(str, values)));
        }

        return list;
    }
}