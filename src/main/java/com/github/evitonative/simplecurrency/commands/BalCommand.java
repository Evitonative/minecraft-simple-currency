package com.github.evitonative.simplecurrency.commands;

import com.github.evitonative.simplecurrency.SimpleCurrency;
import com.github.evitonative.simplecurrency.utils.ColourFormatting;
import com.github.evitonative.simplecurrency.utils.DataManager;
import com.github.evitonative.simplecurrency.utils.DefaultResponses;
import com.github.evitonative.simplecurrency.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BalCommand implements CommandExecutor, TabCompleter {
    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(!sender.hasPermission("simple-currency.bal")) return DefaultResponses.Errors.missingPermissions(sender);
        Player player = null;
        if(sender instanceof Player) player = (Player) sender;

        if(args.length == 0){
            if(player == null) return DefaultResponses.Errors.console(sender);
            return sendBalMsg(player);
        }

        //Set players balance
        if(args[0].equals("set")){
            //noinspection DuplicatedCode
            if(args.length < 3) return DefaultResponses.Errors.missingArguments(sender);

            if(player != null && !player.hasPermission("simple-currency.set.others") ||
                    (player != null && player.getName().equals(args[1]) && !player.hasPermission("simple-currency.set.own"))) //TODO HUH?
                return DefaultResponses.Errors.missingPermissions(sender);

            try{
                UUID setPlayer;
                if(Bukkit.getServer().getPlayer(args[1]) != null){
                    //noinspection ConstantConditions
                    setPlayer = Bukkit.getServer().getPlayer(args[1]).getUniqueId();
                }
                else
                    setPlayer = DataManager.getUUID(args[1]);

                if(setPlayer == null)
                    return DefaultResponses.Errors.invalidPlayerOffline(sender);

                if(Integer.parseInt(args[2]) < 0) return DefaultResponses.Errors.noNegatives(sender);

                DataManager.setPlayerBalance(setPlayer, Integer.parseInt(args[2]));
            }catch (NumberFormatException e){
                return DefaultResponses.Errors.intRequired(sender, 2);
            }catch (NullPointerException e){
                return DefaultResponses.Errors.invalidPlayerOffline(sender);
            }

            sender.sendMessage(ColourFormatting.parseFromConfigWithPlaceholders("messages.set", args[1]));
            return true;
        }

        //Add to players balance
        if(args[0].equals("add")){
            //noinspection DuplicatedCode
            if(args.length < 3) return DefaultResponses.Errors.missingArguments(sender);

            if(player != null && !player.hasPermission("simple-currency.set.others") ||
                    (player != null && player.getName().equals(args[1]) && !player.hasPermission("simple-currency.set.own")))
                return DefaultResponses.Errors.missingPermissions(sender);

            try{
                UUID setPlayer;
                if(Bukkit.getServer().getPlayer(args[1]) != null){
                    //noinspection ConstantConditions
                    setPlayer = Bukkit.getServer().getPlayer(args[1]).getUniqueId();
                }
                else
                    setPlayer = DataManager.getUUID(args[1]);
                if(setPlayer == null)
                    return DefaultResponses.Errors.invalidPlayerOffline(sender);

                DataManager.addToPlayerBalance(setPlayer, Integer.parseInt(args[2]));
            }catch (NumberFormatException e){
                return DefaultResponses.Errors.intRequired(sender, 2);
            }catch (NullPointerException e){
                return DefaultResponses.Errors.invalidPlayerOffline(sender);
            }

            sender.sendMessage(ColourFormatting.parseFromConfigWithPlaceholders("messages.set", args[1]));
            return true;
        }

        if(player != null && !player.hasPermission("simple-currency.bal.others")) return DefaultResponses.Errors.missingPermissions(sender);

        UUID setPlayer;
        if(Bukkit.getServer().getPlayer(args[0]) != null){
            //noinspection ConstantConditions
            setPlayer = Bukkit.getServer().getPlayer(args[0]).getUniqueId();
        }
        else
            setPlayer = DataManager.getUUID(args[0]);
        if(setPlayer == null)
            return DefaultResponses.Errors.invalidPlayerOffline(sender);

        sender.sendMessage(ColourFormatting.parseFromConfigWithPlaceholders(Messages.BALANCE_OTHER.getPath(), args[0], DataManager.getPlayerBalance(setPlayer)));

        return true;
    }

    @SuppressWarnings("SameReturnValue")
    public boolean sendBalMsg(Player player){
        player.sendMessage(ColourFormatting.parseFromConfigWithPlaceholders(Messages.BALANCE.getPath(), DataManager.getPlayerBalance(player.getUniqueId())));
        return true;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside a command block, this will be the player, not
     *                the command block.
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    The arguments passed to the command, including final
     *                partial argument to be completed
     * @return A List of possible completions for the final argument, or null
     * to default to the command executor
     */
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> list = new ArrayList<>();

        boolean hasSetPerms = sender.hasPermission("simple-currency.bal.set.own") || sender.hasPermission("simple-currency.bal.set.others");

        switch (args.length) {
            case 1 -> {
                if (hasSetPerms) {
                    if("add".startsWith(args[0])) list.add("add");
                    if("set".startsWith(args[0])) list.add("set");
                }
                if (sender.hasPermission("simple.currency.bal.others")) {
                    for (Player player : SimpleCurrency.plugin.getServer().getOnlinePlayers()) {
                        if(player.getName().startsWith(args[0]))
                            list.add(player.getName());
                    }
                }
            }
            case 2 -> {
                if (hasSetPerms && (args[0].equals("add") || args[0].equals("set"))) {
                    if (sender.hasPermission("simple.currency.bal.set.others")) {
                        for (Player player : SimpleCurrency.plugin.getServer().getOnlinePlayers()) {
                            if(player.getName().startsWith(args[1]))
                                list.add(player.getName());
                        }
                        list.remove(sender.getName());
                    }
                    if (sender.hasPermission("simple-currency.bal.set.own")) list.add(sender.getName());
                }
            }
            case 3 -> {
                if (hasSetPerms && (args[0].equals("add") || args[0].equals("set"))) {
                    list.add("1");
                    list.add("5");
                    list.add("10");
                    list.add("50");
                    list.add("100");
                }
            }
        }

        return list;
    }
}
