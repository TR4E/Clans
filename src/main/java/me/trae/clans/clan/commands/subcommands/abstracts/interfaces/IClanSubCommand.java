package me.trae.clans.clan.commands.subcommands.abstracts.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.core.client.Client;
import org.bukkit.entity.Player;

public interface IClanSubCommand {

    void execute(final Player player, final Client client, final Clan clan, final String[] args);
}