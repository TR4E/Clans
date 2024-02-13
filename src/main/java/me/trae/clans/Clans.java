package me.trae.clans;

import me.trae.champions.blood.BloodManager;
import me.trae.champions.role.RoleManager;
import me.trae.champions.skill.SkillManager;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.world.WorldManager;
import me.trae.core.chat.ChatManager;
import me.trae.core.client.ClientManager;
import me.trae.core.command.CommandManager;
import me.trae.core.config.ConfigManager;
import me.trae.core.database.DatabaseManager;
import me.trae.core.framework.SpigotPlugin;
import me.trae.core.item.ItemManager;
import me.trae.core.network.NetworkManager;
import me.trae.core.player.PlayerManager;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.redis.RedisManager;
import me.trae.core.server.ServerManager;
import me.trae.core.updater.UpdaterManager;
import me.trae.framework.shared.utility.enums.ChatColor;

public class Clans extends SpigotPlugin {

    @Override
    public void registerManagers() {
        // Core
        addManager(new ChatManager(this));
        addManager(new ClientManager(this));
        addManager(new CommandManager(this));
        addManager(new ConfigManager(this));
        addManager(new DatabaseManager(this));
        addManager(new ItemManager(this, ChatColor.YELLOW));
        addManager(new NetworkManager(this));
        addManager(new PlayerManager(this));
        addManager(new RechargeManager(this));
        addManager(new RedisManager(this));
        addManager(new ServerManager(this));
        addManager(new UpdaterManager(this));
        addManager(new WorldManager(this));

        // Champions
        addManager(new BloodManager(this));
        addManager(new RoleManager(this));
        addManager(new SkillManager(this));

        // Clans
        addManager(new ClanManager(this));
    }
}