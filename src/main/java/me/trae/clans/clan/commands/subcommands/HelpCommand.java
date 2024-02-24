package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.core.client.Client;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import org.bukkit.entity.Player;

public class HelpCommand extends ClanSubCommand {

    public HelpCommand(final ClanCommand manager) {
        super(manager, "help");
    }

    @Override
    public void execute(final Player player, final Client client, final GlobalGamer globalGamer, final Clan clan, final String[] args) {
        this.getModule().help(player);
    }
}