package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.AdminClan;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanCreateEvent;
import me.trae.core.client.Client;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.core.utility.interfaces.EventContainer;
import me.trae.framework.shared.client.enums.Rank;
import me.trae.framework.shared.utility.UtilFormat;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class CreateCommand extends ClanSubCommand implements EventContainer<ClanCreateEvent> {

    public CreateCommand(final ClanCommand manager) {
        super(manager, "create");

        this.addPrimitive("Min-Name-Length", 3);
        this.addPrimitive("Max-Name-Length", 14);
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

        String name = args[0];

        if (!(this.canCreateClan(player, client, name))) {
            return;
        }

        if (client.isAdministrating() && client.hasRank(Rank.OWNER)) {
            if (name.contains("_") && !(name.startsWith("_") && name.endsWith("_"))) {
                name = name.replaceAll("_", " ");
            }

            clan = new AdminClan(name, player);
        } else {
            clan = new Clan(name, player);
        }

        this.callEvent(new ClanCreateEvent(clan, player, client));
    }

    private boolean canCreateClan(final Player player, final Client client, final String name) {
        if (this.getModule().isSubCommandByLabel(name)) {
            UtilMessage.message(player, "Clans", "You cannot use that as your Clan name!");
            return false;
        }

        if (UtilFormat.hasSymbols(name)) {
            UtilMessage.message(player, "Clans", "You cannot have special characters in your Clan name!");
            return false;
        }

        if (this.getModule().getManager().isClanByName(name)) {
            UtilMessage.message(player, "Clans", "Clan name is already used by another Clan!");
            return false;
        }

        final int maxNameLength = this.getPrimitiveCasted(Integer.class, "Max-Name-Length");
        if (name.length() > maxNameLength) {
            UtilMessage.simpleMessage(player, "Clans", "Clan name is too long! Maximum Length is <yellow><var></yellow>.", Collections.singletonList(String.valueOf(maxNameLength)));
            return false;
        }

        if (!(client.isAdministrating())) {
            final int minNameLength = this.getPrimitiveCasted(Integer.class, "Min-Name-Length");
            if (name.length() < minNameLength) {
                UtilMessage.simpleMessage(player, "Clans", "Clan name is too short! Minimum Length is <yellow><var></yellow>.", Collections.singletonList(String.valueOf(minNameLength)));
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanCreateEvent> getClassOfEvent() {
        return ClanCreateEvent.class;
    }

    @Override
    public void onEvent(final ClanCreateEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();

        this.getModule().getManager().addClan(clan);
        this.getModule().getManager().getRepository().saveData(clan);

        for (final Player target : UtilServer.getOnlinePlayers()) {
            final ClanRelation clanRelation = this.getModule().getManager().getClanRelationByClan(this.getModule().getManager().getClanByPlayer(target), clan);

            UtilMessage.simpleMessage(target, "Clans", "<var> formed <var>.", Arrays.asList(clanRelation.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(clan, clanRelation)));
        }
    }
}