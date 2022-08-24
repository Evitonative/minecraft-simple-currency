package com.github.evitonative.simplecurrency.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public record PhysicalCurrencyItem(int value, Material material, String name, String[] lore, Integer customModelData) {

    public ItemStack getAsItemStack(int count) {
        ItemStack itemStack = new ItemStack(this.material, count);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList(lore));
        itemMeta.setCustomModelData(customModelData);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
