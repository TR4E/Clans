package me.trae.clans.world.modules;

import me.trae.clans.world.WorldManager;
import me.trae.core.framework.types.SpigotUpdater;
import me.trae.core.world.enums.TimeType;
import me.trae.framework.shared.updater.annotations.Update;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class FasterNightTimeCycle extends SpigotUpdater<WorldManager> {

    public FasterNightTimeCycle(final WorldManager manager) {
        super(manager);

        this.addPrimitive("Multiplier", 4);
    }

    @Update(delay = 250L)
    public void onUpdate() {
        final World world = Bukkit.getWorld("world");
        if (world == null) {
            return;
        }

        if (world.getTime() < TimeType.NIGHT.getDuration()) {
            return;
        }

        world.setTime(world.getTime() + this.getTime());
    }

    private long getTime() {
        return this.getPrimitiveCasted(Integer.class, "Multiplier") * 20L / 4L;
    }
}