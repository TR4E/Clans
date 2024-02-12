package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.core.client.Client;
import me.trae.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class CreateCommand extends ClanSubCommand {

    public CreateCommand(final ClanCommand manager) {
        super(manager, "create");
    }

    @Override
    public void execute(final Player player, final Client client, final Clan clan, final String[] args) {
        if (clan != null) {
            UtilMessage.message(player, "Clans", "You are already in a Clan!");
            return;
        }

        if (args.length == 0) {
            UtilMessage.message(player, "Clans", "You did not input a Name to Create.");
            return;
        }

        final String name = args[0];
    }
}