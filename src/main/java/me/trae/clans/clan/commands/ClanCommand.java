package me.trae.clans.clan.commands;

import me.trae.clans.clan.ClanManager;
import me.trae.core.client.Client;
import me.trae.core.command.types.PlayerCommand;
import me.trae.framework.shared.client.enums.Rank;
import org.bukkit.entity.Player;

public class ClanCommand extends PlayerCommand<ClanManager> {

    public ClanCommand(final ClanManager manager) {
        super(manager, "clan", new String[]{"faction", "gang", "fac", "c", "f", "g"}, Rank.DEFAULT);
    }

    @Override
    public String getDescription() {
        return "Clan Management";
    }

    @Override
    public void execute(final Player player, final Client client, final String[] args) {
    }
}