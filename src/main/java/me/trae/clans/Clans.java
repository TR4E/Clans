package me.trae.clans;

import me.trae.clans.clan.ClanManager;
import me.trae.core.client.ClientManager;
import me.trae.core.command.CommandManager;
import me.trae.core.config.ConfigManager;
import me.trae.core.database.DatabaseManager;
import me.trae.core.framework.SpigotPlugin;
import me.trae.core.network.NetworkManager;
import me.trae.core.redis.RedisManager;
import me.trae.core.updater.UpdaterManager;

public class Clans extends SpigotPlugin {

    @Override
    public void registerManagers() {
        // Core
        addManager(new ClientManager(this));
        addManager(new CommandManager(this));
        addManager(new ConfigManager(this));
        addManager(new DatabaseManager(this));
        addManager(new NetworkManager(this));
        addManager(new RedisManager(this));
        addManager(new UpdaterManager(this));

        // Clans
        addManager(new ClanManager(this));
    }
}