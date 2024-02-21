package me.trae.clans.worldevent;

import me.trae.clans.worldevent.commands.WorldEventCommand;
import me.trae.clans.worldevent.interfaces.IWorldEventManager;
import me.trae.clans.worldevent.modules.HandleWorldEventUpdater;
import me.trae.clans.worldevent.types.FishingFrenzy;
import me.trae.clans.worldevent.types.MiningFrenzy;
import me.trae.core.framework.SpigotManager;
import me.trae.core.framework.SpigotPlugin;
import me.trae.core.utility.UtilSearch;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class WorldEventManager extends SpigotManager implements IWorldEventManager {

    public WorldEventManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new WorldEventCommand(this));

        // Modules
        addModule(new HandleWorldEventUpdater(this));

        // World Events
        addModule(new FishingFrenzy(this));
        addModule(new MiningFrenzy(this));
    }

    @Override
    public WorldEvent searchWorldEvent(final CommandSender sender, final String name, final boolean inform) {
        final List<Predicate<WorldEvent>> predicates = Arrays.asList(
                (worldEvent -> worldEvent.getClass().getSimpleName().equalsIgnoreCase(name)),
                (worldEvent -> worldEvent.getClass().getSimpleName().toLowerCase().contains(name.toLowerCase()))
        );

        final Function<WorldEvent, String> function = (WorldEvent::getName);

        return UtilSearch.search(WorldEvent.class, this.getModulesByClass(WorldEvent.class), predicates, null, function, "World Event Search", sender, name, inform);
    }

    @Override
    public WorldEvent getActiveWorldEvent() {
        for (final WorldEvent worldEvent : this.getModulesByClass(WorldEvent.class)) {
            if (!(worldEvent.isActive())) {
                continue;
            }

            return worldEvent;
        }

        return null;
    }

    @Override
    public boolean isActiveWorldEvent(final Class<? extends WorldEvent> clazz) {
        return this.getModuleByClass(clazz).isActive();
    }
}