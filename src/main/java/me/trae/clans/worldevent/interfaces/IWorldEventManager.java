package me.trae.clans.worldevent.interfaces;

import me.trae.clans.worldevent.WorldEvent;
import org.bukkit.command.CommandSender;

public interface IWorldEventManager {

    WorldEvent searchWorldEvent(final CommandSender sender, final String name, final boolean inform);

    WorldEvent getActiveWorldEvent();

    boolean isActiveWorldEvent(final Class<? extends WorldEvent> clazz);
}