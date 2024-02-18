package me.trae.clans.economy.events.abstracts;

import me.trae.clans.economy.events.abstracts.interfaces.IEconomyEvent;
import me.trae.core.event.CustomEvent;
import me.trae.core.event.types.ISenderEvent;
import me.trae.core.event.types.ITargetEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyEvent extends CustomEvent implements IEconomyEvent, ISenderEvent, ITargetEvent<Player> {

    private final CommandSender sender;
    private final Player target;
    private final int coins;

    public EconomyEvent(final CommandSender sender, final Player target, final int coins) {
        this.sender = sender;
        this.target = target;
        this.coins = coins;
    }

    @Override
    public CommandSender getSender() {
        return this.sender;
    }

    @Override
    public Player getTarget() {
        return this.target;
    }

    @Override
    public int getCoins() {
        return this.coins;
    }
}