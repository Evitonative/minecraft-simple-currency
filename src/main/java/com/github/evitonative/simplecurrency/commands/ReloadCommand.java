package com.github.evitonative.simplecurrency.commands;

import com.github.evitonative.simplecurrency.SimpleCurrency;
import com.github.evitonative.simplecurrency.utils.DefaultResponses;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
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
        if(!sender.hasPermission("simple-currency.reload")) return DefaultResponses.Errors.missingPermissions(sender);
        SimpleCurrency.plugin.reloadConfig();
        SimpleCurrency.reload();
        sender.sendMessage("Reloading...");
        return true;
    }
}
