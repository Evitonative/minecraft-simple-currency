package com.github.evitonative.simplecurrency.commands;

import com.github.evitonative.simplecurrency.utils.ColourFormatting;
import com.github.evitonative.simplecurrency.utils.DataManager;
import com.github.evitonative.simplecurrency.utils.DefaultResponses;
import com.github.evitonative.simplecurrency.utils.Messages;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BankCommand implements CommandExecutor, TabCompleter {
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
        Player player = (Player) sender;

        TextComponent line = new TextComponent("----------------------------------------");
        TextComponent title = new TextComponent(ColourFormatting.parseFromConfigWithPlaceholders(Messages.Bank.TITLE.getPath(), player.getDisplayName()));
        TextComponent balance = new TextComponent(ColourFormatting.parseFromConfigWithPlaceholders(Messages.Bank.CURRENT_BALANCE.getPath(), DataManager.getPlayerBalance(player.getUniqueId())));

        TextComponent deposit = new TextComponent("");
        if(sender.hasPermission("simple-currency.deposit")){
            deposit = new TextComponent(ColourFormatting.parseFromConfig(Messages.Bank.DEPOSIT.getPath()) + "\n");
            deposit.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/deposit "));
        }

        TextComponent withdraw = new TextComponent("");
        if(sender.hasPermission("simple-currency.withdraw")){
            withdraw = new TextComponent(ColourFormatting.parseFromConfig(Messages.Bank.WITHDRAW.getPath()) + "\n");
            withdraw.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/withdraw "));
        }

        TextComponent pay = new TextComponent("");
        if(sender.hasPermission("simple-currency.pay")) {
            pay = new TextComponent(ColourFormatting.parseFromConfig(Messages.Bank.PAY.getPath()) + "\n");
            pay.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pay "));
        }

        BaseComponent[] msg =
                new ComponentBuilder(line).append("\n").append("\n")
                        .append(title).append("\n")
                        .append(balance).append("\n")
                        .append("\n")
                        .append(deposit)
                        .append(withdraw)
                        .append(pay)
                        .append("\n").append(line).color(ChatColor.RESET).create();

        player.spigot().sendMessage(msg);

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
