package com.github.evitonative.simplecurrency.utils;

import org.bukkit.inventory.ItemStack;

public record PlayerInventoryCurrency(ItemStack itemStack, PhysicalCurrencyItem currencyType, int inventoryPosition, int payoutItemPosition) {

    public int getTotalValue() {
        return currencyType.value() * itemStack.getAmount();
    }
}
