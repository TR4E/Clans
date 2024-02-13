package me.trae.clans.scoreboard;

import me.trae.core.client.Client;
import me.trae.core.framework.SpigotPlugin;
import me.trae.core.scoreboard.MainScoreboard;
import me.trae.framework.shared.utility.enums.ChatColor;
import org.bukkit.entity.Player;

public class ClanScoreboard extends MainScoreboard {

    public ClanScoreboard(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerLines(final Player player, final Client client) {
        this.addCustomLine(ChatColor.YELLOW, "Clan", "No Clan");
        this.addBlankLine();

        this.addCustomLine(ChatColor.YELLOW, "Territory", ChatColor.GRAY + "Wilderness");
        this.addBlankLine();

        this.addCustomLine(ChatColor.YELLOW, "Coins", ChatColor.GOLD + "$1,000,000");
        this.addBlankLine();

        this.addCustomLine(ChatColor.AQUA, "World Event", "Fishing Frenzy");
    }
}