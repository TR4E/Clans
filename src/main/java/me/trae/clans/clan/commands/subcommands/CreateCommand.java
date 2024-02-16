package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.client.Client;
import me.trae.core.scoreboard.events.ScoreboardUpdateEvent;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CreateCommand extends ClanSubCommand {

    public CreateCommand(final ClanCommand manager) {
        super(manager, "create");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <name>";
    }

    @Override
    public String getDescription() {
        return "Create a Clan";
    }

    @Override
    public void execute(final Player player, final Client client, Clan clan, final String[] args) {
        if (clan != null) {
            UtilMessage.message(player, "Clans", "You are already in a Clan!");
            return;
        }

        if (args.length == 0) {
            UtilMessage.message(player, "Clans", "You did not input a Name to Create.");
            return;
        }

        final String name = args[0];

        clan = new Clan(name, player);

        this.getModule().getManager().addClan(clan);

        for (final Player target : UtilServer.getOnlinePlayers()) {
            final ClanRelation clanRelation = this.getModule().getManager().getClanRelationByPlayer(target, player);

            UtilMessage.simpleMessage(target, "Clans", "<var> formed <var>", Arrays.asList(clanRelation.getSuffix() + player.getName(), clanRelation.getSuffix() + "Clan " + clan.getName()));
        }

        UtilServer.callEvent(new ScoreboardUpdateEvent(player));
    }
}