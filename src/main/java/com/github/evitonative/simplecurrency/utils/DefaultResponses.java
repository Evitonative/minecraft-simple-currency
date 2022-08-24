package com.github.evitonative.simplecurrency.utils;

import org.bukkit.command.CommandSender;

@SuppressWarnings("SameReturnValue")
public class DefaultResponses {
    public class Errors{
        public static boolean console(CommandSender sender){
            sender.sendMessage(ColourFormatting.parseFromConfig(Messages.Errors.CONSOLE.getPath()));
            return true;
        }

        public static boolean noNegatives(CommandSender sender){
            sender.sendMessage(ColourFormatting.parseFromConfig(Messages.Errors.NO_NEGATIVES.getPath()));
            return true;
        }

        public static boolean noSelf(CommandSender sender){
            sender.sendMessage(ColourFormatting.parseFromConfig(Messages.Errors.NO_SELF.getPath()));
            return true;
        }

        public static boolean notEnoughMoney(CommandSender sender, long needs, long has){
            sender.sendMessage(ColourFormatting.parseFromConfigWithPlaceholders(Messages.Errors.NOT_ENOUGH_MONEY.getPath(), has, needs));
            return true;
        }

        public static boolean notEnoughSpace(CommandSender sender){
            sender.sendMessage(ColourFormatting.parseFromConfig(Messages.Errors.NOT_ENOUGH_SPACE.getPath()));
            return true;
        }

        public static boolean invalidPlayer(CommandSender sender){
            sender.sendMessage(ColourFormatting.parseFromConfig(Messages.Errors.INVALID_PLAYER.getPath()));
            return true;
        }

        public static boolean invalidPlayerOffline(CommandSender sender){
            sender.sendMessage(ColourFormatting.parseFromConfig(Messages.Errors.INVALID_PLAYER_OFFLINE.getPath()));
            return true;
        }

        public static boolean intRequired(CommandSender sender, int pos){
            sender.sendMessage(ColourFormatting.parseFromConfigWithPlaceholders(Messages.Errors.INT_REQUIRED.getPath(), pos));
            return true;
        }

        public static boolean missingArguments(CommandSender sender){
            sender.sendMessage(ColourFormatting.parseFromConfig(Messages.Errors.MISSING_ARGUMENTS.getPath()));
            return true;
        }

        public static boolean missingPermissions(CommandSender sender){
            sender.sendMessage(ColourFormatting.parseFromConfig(Messages.Errors.MISSING_PERMISSIONS.getPath()));
            return true;
        }

    }
}
