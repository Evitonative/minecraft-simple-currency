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

public class PayCommand implements CommandExecutor, TabCompleter {
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
        if(!sender.hasPermission("simple-currency.pay")) return DefaultResponses.Errors.missingPermissions(sender);

        if(args.length < 2)  return DefaultResponses.Errors.missingArguments(sender);

        Player receivingPlayer = Bukkit.getServer().getPlayer(args[0]);
        if(receivingPlayer == null) return DefaultResponses.Errors.invalidPlayer(sender);
        UUID receivingUuid = receivingPlayer.getUniqueId();

        Player givingPlayer = (Player) sender;
        UUID givingUuid = givingPlayer.getUniqueId();

        //Check for arguments and get amount to pay
        int amount;
        try{
            amount = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            return DefaultResponses.Errors.intRequired(sender, 2);
        }

        //Don't pay anyone a negative amount
        if(amount <= 0)
            return DefaultResponses.Errors.noNegatives(sender);

        //Prevent paying yourself
        if(givingUuid.equals(receivingUuid))
            return DefaultResponses.Errors.noSelf(sender);

        //Check if they have enough
        if(DataManager.getPlayerBalance(givingUuid) < amount)
            return DefaultResponses.Errors.notEnoughMoney(sender, DataManager.getPlayerBalance(givingUuid), amount);

        DataManager.addToPlayerBalance(givingUuid, -amount);
        DataManager.addToPlayerBalance(receivingUuid, amount);

        givingPlayer.sendMessage(ColourFormatting.parseFromConfigWithPlaceholders(Messages.PAYED.getPath(), receivingPlayer.getDisplayName(), amount));
        receivingPlayer.sendMessage(ColourFormatting.parseFromConfigWithPlaceholders(Messages.RECEIVED.getPath(), givingPlayer.getDisplayName(), amount));

        return true;
    }

    /**
     * Requests a list of possible completions for a command argument.
     *
     * @param sender  Source of the command.  For players tab-completing a
     *                command inside of a command block, this will be the player, not
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
        switch (args.length){
            case 1: {
                for (Player player : SimpleCurrency.plugin.getServer().getOnlinePlayers()){
                    list.add(player.getName());
                }
                break;
            }
            case 2: {
                list.add("1");
                list.add("5");
                list.add("10");
                list.add("50");
                list.add("100");
                break;
            }
        }
        return list;
    }
}
