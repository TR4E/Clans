package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.TerritoryUnClaimEvent;
import me.trae.core.client.Client;
import me.trae.core.utility.UtilChunk;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.interfaces.EventContainer;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class UnClaimCommand extends ClanSubCommand implements EventContainer<TerritoryUnClaimEvent> {

    public UnClaimCommand(final ClanCommand manager) {
        super(manager, "unclaim");
    }

    @Override
    public String getDescription() {
        return "Unclaim a Chunk of Territory";
    }

    @Override
    public MemberRole getRequiredMemberRole() {
        return MemberRole.ADMIN;
    }

    @Override
    public void execute(final Player player, final Client client, final GlobalGamer globalGamer, final Clan clan, final String[] args) {
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        final Chunk chunk = player.getLocation().getChunk();

        final Clan territoryClan = this.getModule().getManager().getClanByChunk(chunk);

        if (!(this.canUnClaimTerritory(player, client, clan, chunk, territoryClan))) {
            return;
        }

        this.callEvent(new TerritoryUnClaimEvent(clan, player, client, chunk, territoryClan));
    }

    private boolean canUnClaimTerritory(final Player player, final Client client, final Clan clan, final Chunk chunk, final Clan territoryClan) {
        if (territoryClan == null) {
            UtilMessage.message(player, "Clans", "This Territory is not owned by anyone!");
            return false;
        }

        if (!(client.isAdministrating())) {
            if (territoryClan == clan) {
                return this.hasRequiredMemberRole(player, client, clan, true);
            } else if (territoryClan.getTerritory().size() <= territoryClan.getMaxClaims()) {
                UtilMessage.message(player, "Clans", "This Territory is not owned by your Clan!");
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<TerritoryUnClaimEvent> getClassOfEvent() {
        return TerritoryUnClaimEvent.class;
    }

    @Override
    public void onEvent(final TerritoryUnClaimEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        final Chunk chunk = event.getChunk();
        final Clan territoryClan = event.getTerritoryClan();

        territoryClan.removeTerritory(chunk);
        this.getModule().getManager().getRepository().updateData(territoryClan, ClanProperty.TERRITORY);

        this.getModule().getManager().unOutlineChunk(territoryClan, chunk);

        if (territoryClan.hasHome()) {
            final Location home = territoryClan.getHome();

            if (home.getChunk() == chunk) {
                territoryClan.setHome(null);
                this.getModule().getManager().getRepository().updateData(territoryClan, ClanProperty.HOME);
            }
        }

        if (territoryClan == clan) {
            this.messageSelf(player, clan, chunk);
        } else {
            this.messageOther(player, clan, territoryClan, chunk);
        }
    }

    private void messageSelf(final Player player, final Clan clan, final Chunk chunk) {
        UtilMessage.simpleMessage(player, "Clans", "You un-claimed territory at <var>.", Collections.singletonList(UtilChunk.chunkToString(chunk)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has un-claimed territory at <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), UtilChunk.chunkToString(chunk)), Collections.singletonList(player.getUniqueId()));
    }

    private void messageOther(final Player player, final Clan clan, final Clan territoryClan, final Chunk chunk) {
        final ClanRelation clanRelation = this.getModule().getManager().getClanRelationByClan(clan, territoryClan);

        UtilMessage.simpleMessage(player, "Clans", "You un-claimed territory at <var> from <var>.", Arrays.asList(UtilChunk.chunkToString(chunk), this.getModule().getManager().getClanFullName(territoryClan, clanRelation)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has un-claimed territory at <var> from <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), UtilChunk.chunkToString(chunk), this.getModule().getManager().getClanFullName(territoryClan, clanRelation)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(territoryClan, "Clans", "<var> has un-claimed and stole your territory at <var> from your Clan.", Arrays.asList(clanRelation.getSuffix() + player.getName(), UtilChunk.chunkToString(chunk)), Collections.singletonList(player.getUniqueId()));
    }
}