package com.github.evitonative.simplecurrency.commands;

import com.github.evitonative.simplecurrency.SimpleCurrency;
import com.github.evitonative.simplecurrency.utils.*;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("simple-currency.deposit")) return DefaultResponses.Errors.missingPermissions(sender); //Check Permissions
        if(!(sender instanceof Player player)) return DefaultResponses.Errors.console(sender); //Check console
        if(args.length == 0) return DefaultResponses.Errors.missingArguments(sender); //Check argument count

        ItemStack item = player.getInventory().getItemInMainHand();
        if(item == null || item.getType() == Material.AIR){
            player.sendMessage(ColourFormatting.parseFromConfig(Messages.Deposit.INVALID.getPath()));
            return true;
        }

        PhysicalCurrencyItem currencyItem = null;

        for (PhysicalCurrencyItem itemType : SimpleCurrency.physicalCurrencyItems) {
            if(itemType.material() == item.getType() && item.getItemMeta() == null)
                 if(itemType.customModelData() == null) currencyItem = itemType;
            else if (itemType.material() == item.getType() && itemType.customModelData() == item.getItemMeta().getCustomModelData())
                currencyItem = itemType;
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
        /*final String depositString = args[0];
        AtomicLong depositAmount = new AtomicLong(-1L);

        if (!args[0].equals("all")){ //Keep the deposit amout -1 that -1 is used for all
            try{
                depositAmount.set(Long.parseLong(depositString));
            }catch (NumberFormatException e){
                return DefaultResponses.Errors.intRequired(sender, 1);
            }

            if(depositAmount.get() == 0){ //Don't do work if it is not necessary
                sender.sendMessage(ColourFormatting.parseFromConfigWithPlaceholders(Messages.Deposit.SUCCESS.getPath(), depositString));
                return true;
            }
        }

        List<PlayerInventoryCurrency> currencyItemsInInventory = new ArrayList<>();
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            if(contents == null) continue;
            ItemStack inventoryItem = contents[i];
            if(inventoryItem == null) continue;

            List<PhysicalCurrencyItem> physicalCurrencyItems = SimpleCurrency.physicalCurrencyItems;
            for (int j = 0; j < physicalCurrencyItems.size(); j++) {
                PhysicalCurrencyItem playerInventoryCurrency = physicalCurrencyItems.get(j);

                //If item type is correct
                if (inventoryItem.getType() == playerInventoryCurrency.material()) {
                    //If no item meta
                    if (!inventoryItem.hasItemMeta()) if (playerInventoryCurrency.customModelData() == null)
                        currencyItemsInInventory.add(new PlayerInventoryCurrency(inventoryItem, playerInventoryCurrency, i, j));

                    //If model data matching
                    if(inventoryItem.hasItemMeta() && Objects.requireNonNull(inventoryItem.getItemMeta()).getCustomModelData() == playerInventoryCurrency.customModelData())
                        currencyItemsInInventory.add(new PlayerInventoryCurrency(inventoryItem, playerInventoryCurrency, i, j));

                    break;
                }
            }
        }

        //If they don't have anything send them a message and stop
        if(currencyItemsInInventory.size() == 0) {
            player.sendMessage(ColourFormatting.parseFromConfigWithPlaceholders(Messages.Deposit.NONE.getPath()));
            return true;
        }

        //Put all currency Items in a map with a count variable
        LinkedHashMap<PhysicalCurrencyItem, Long> currencyItems = new LinkedHashMap<>();
        for(PhysicalCurrencyItem playerInventoryCurrency : SimpleCurrency.physicalCurrencyItems)
            currencyItems.put(playerInventoryCurrency, 0L);

        //Count the different Items
        for(PlayerInventoryCurrency playerInventoryCurrency : currencyItemsInInventory)
            currencyItems.put(playerInventoryCurrency.currencyType(), currencyItems.get(playerInventoryCurrency.currencyType()) + playerInventoryCurrency.itemStack().getAmount());

        //Set the depositAmout if it is all
        if(depositAmount.get() == -1) currencyItems.forEach((k, v) -> depositAmount.addAndGet(k.value() * v));

        //Do the actual item removal
        long removedAmount = 0;

        List<PhysicalCurrencyItem> currencyItemsKeyList = new ArrayList<>(currencyItems.keySet()); //TODO REMOVE ITEMS AND CONTINUE THIS MESS
        Collections.reverse(currencyItemsKeyList);
        for (PhysicalCurrencyItem k : currencyItemsKeyList) {
            Long count = currencyItems.get(k);

            //k = Item Type
            //count = Menge des Item Typs im Inventar
            //depositAmount = Einzahl menge
            //removedAmount = Bereits entfernte Menge
            //currencyItemsInInventory = List of item in inventory

            sender.sendMessage(count.toString() );
        }

        return true;*/
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
        return null;
    }
}
