package com.github.evitonative.simplecurrency.commands;

import com.github.evitonative.simplecurrency.utils.*;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.evitonative.simplecurrency.SimpleCurrency.physicalCurrencyItems;
import static com.github.evitonative.simplecurrency.utils.ColourFormatting.parseFromConfig;
import static com.github.evitonative.simplecurrency.utils.ColourFormatting.parseFromConfigWithPlaceholders;
import static com.github.evitonative.simplecurrency.utils.DataManager.getPlayerBalance;

public class WithdrawCommand implements CommandExecutor, TabCompleter {
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
        if(!sender.hasPermission("simple-currency.bank")) return DefaultResponses.Errors.missingPermissions(sender);
        Player player = ((Player) sender);

        long amount = 0;

        if(args.length == 0)
            return DefaultResponses.Errors.missingArguments(sender);

        if(args[0].equals("all")){
            amount = getPlayerBalance(player.getUniqueId());
        }

        //Check for arguments and get amount to withdraw
        if(!args[0].equals("all")) try{
            amount = Long.parseLong(args[0]);
        }catch (NumberFormatException e){
            return DefaultResponses.Errors.intRequired(sender, 1);
        }

        //Don't pay anyone a negative amount
        if(amount <= 0)
            return DefaultResponses.Errors.noNegatives(sender);

        //Check if they have enough
        if(getPlayerBalance(player.getUniqueId()) < amount)
            return DefaultResponses.Errors.notEnoughMoney(sender, getPlayerBalance(player.getUniqueId()), amount);

        //Generate a list of all needed coin items
        long workingAmount = amount;
        List<Integer> counts = new ArrayList<>();
        for (int i = physicalCurrencyItems.size() -1; i >= 0; i--) {
            PhysicalCurrencyItem item = physicalCurrencyItems.get(i);
            int c = 0;

            while (workingAmount >= item.value()){
                c += 1;
                workingAmount -= item.value();
            }

            counts.add(c);
        }

        //Calculate the needed inventory space
        int requiredSlots = 0;
        for (Integer i : counts) requiredSlots += (int) Math.ceil((double) i / 64);
        if(Arrays.stream(player.getInventory().getContents()).filter(itemStack -> itemStack == null || itemStack.getType() == Material.AIR).count() < requiredSlots){
            return DefaultResponses.Errors.notEnoughSpace(player);
        }

        //Reverse the list that it is the same as payoutItems
        Collections.reverse(counts);

        ComponentBuilder msgBuilder = new ComponentBuilder(parseFromConfig(Messages.Withdrawal.TITLE.getPath()));
        for (int i = counts.size() -1; i >= 0; i--) {
            player.getInventory().addItem(physicalCurrencyItems.get(i).getAsItemStack(counts.get(i)));
            msgBuilder.append("\n").append(parseFromConfigWithPlaceholders(Messages.Withdrawal.ITEMS.getPath(), counts.get(i), physicalCurrencyItems.get(i).name()));
        }
        player.spigot().sendMessage(msgBuilder.create());

        DataManager.addToPlayerBalance(player.getUniqueId(), -amount);

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
        @SuppressWarnings("DuplicatedCode") List<String> list = new ArrayList<>();
        if(args.length==1){
            list.add("1");
            list.add("5");
            list.add("10");
            list.add("50");
            list.add("100");
        }
        return list;
    }
}
