package me.trae.clans.economy.interfaces;

import org.bukkit.command.CommandSender;

public interface IEconomyManager {

    int getAmount(final CommandSender sender, final String string, final int minimumAmount);
}