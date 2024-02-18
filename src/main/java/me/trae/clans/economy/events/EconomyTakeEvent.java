package me.trae.clans.economy.events;

import me.trae.clans.economy.events.abstracts.EconomyEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyTakeEvent extends EconomyEvent {

    public EconomyTakeEvent(final CommandSender sender, final Player target, final int coins) {
        super(sender, target, coins);
    }
}