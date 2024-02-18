package me.trae.clans.economy.events;

import me.trae.clans.economy.events.abstracts.EconomyEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyGiveEvent extends EconomyEvent {

    public EconomyGiveEvent(final CommandSender sender, final Player target, final int coins) {
        super(sender, target, coins);
    }
}