package com.github.evitonative.simplecurrency.commands;

import com.github.evitonative.simplecurrency.SimpleCurrency;
import com.github.evitonative.simplecurrency.utils.*;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DepositCommand implements CommandExecutor, TabCompleter {
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
    @Override //TODO THIS SOMEHOW STOPPED WORKING
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("simple-currency.deposit")) return DefaultResponses.Errors.missingPermissions(sender); //Check Permissions
        if(!(sender instanceof Player player)) return DefaultResponses.Errors.console(sender); //Check console

        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType() == Material.AIR){
            player.sendMessage(ColourFormatting.parseFromConfig(Messages.Deposit.INVALID.getPath()));
            return true;
        }

        PhysicalCurrencyItem currencyItem = null;

        for (PhysicalCurrencyItem itemType : SimpleCurrency.physicalCurrencyItems) {
            if(itemType.material() == item.getType()){

                if(!item.hasItemMeta()) {
                    if(itemType.customModelData() == null) {
                        currencyItem = itemType;
                        break;
                    }
                }
                else
                    if(itemType.customModelData() == null) {
                        currencyItem = itemType;
                        break;
                    } else //noinspection ConstantConditions - Cant be null
                        if (itemType.customModelData() == item.getItemMeta().getCustomModelData()) {
                            currencyItem = itemType;
                            break;
                        }
            }
        }

        if(currencyItem == null){
            player.sendMessage(ColourFormatting.parseFromConfig(Messages.Deposit.INVALID.getPath()));
            return true;
        }

        int amount = item.getAmount() * currencyItem.value();

        DataManager.addToPlayerBalance(player.getUniqueId(), amount);
        item.setAmount(0);

        player.sendMessage(ColourFormatting.parseFromConfigWithPlaceholders(Messages.Deposit.SUCCESS.getPath(), amount));

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
        return new ArrayList<>();
    }
}
