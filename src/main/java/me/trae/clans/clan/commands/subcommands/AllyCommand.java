package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.Alliance;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.data.enums.RequestType;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanAllyEvent;
import me.trae.core.client.Client;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.interfaces.EventContainer;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class AllyCommand extends ClanSubCommand implements EventContainer<ClanAllyEvent> {

    public AllyCommand(final ClanCommand manager) {
        super(manager, "ally");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <clan>";
    }

    @Override
    public String getDescription() {
        return "Ally a Clan";
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

        if (!(this.hasRequiredMemberRole(player, client, clan, true))) {
            return;
        }

        if (args.length == 0) {
            UtilMessage.message(player, "Clans", "You did not input a Clan to Ally.");
            return;
        }

        final Clan target = this.getModule().getManager().searchClan(player, args[0], true);
        if (target == null) {
            return;
        }

        if (!(this.canAllyClan(player, client, clan, target))) {
            return;
        }

        if (client.isAdministrating()) {
            this.callEvent(new ClanAllyEvent(clan, player, client, target) {
                @Override
                public Result getResult() {
                    return Result.FORCE;
                }
            });
            return;
        }

        if (!(target.isRequestByClan(clan, RequestType.ALLIANCE))) {
            this.requestAlliance(player, clan, target);
            return;
        }

        this.callEvent(new ClanAllyEvent(clan, player, client, target) {
            @Override
            public Result getResult() {
                return Result.ACCEPT;
            }
        });
    }

    private boolean canAllyClan(final Player player, final Client client, final Clan clan, final Clan target) {
        if (target == clan) {
            UtilMessage.message(player, "Clans", "You cannot request an alliance with yourself!");
            return false;
        }

        if (target.isAllianceByClan(clan)) {
            UtilMessage.simpleMessage(player, "Clans", "You are already allies with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, this.getModule().getManager().getClanRelationByClan(clan, target))));
            return false;
        }

        if (!(client.isAdministrating())) {
            if (target.isAdmin()) {
                UtilMessage.message(player, "Clans", "You cannot request an alliance with Admin Clans!");
                return false;
            }

            if (!(clan.isNeutralByClan(target))) {
                UtilMessage.simpleMessage(player, "Clans", "You must be neutral with <var> to request an alliance!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, this.getModule().getManager().getClanRelationByClan(clan, target))));
                return false;
            }

            if (clan.isFull(this.getModule().getManager())) {
                UtilMessage.message(player, "Clans", "Your Clan has too many members/allies!");
                return false;
            }

            if (target.isFull(this.getModule().getManager())) {
                UtilMessage.simpleMessage(player, "Clans", "<var> has too many members/allies!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, this.getModule().getManager().getClanRelationByClan(clan, target))));
                return false;
            }

            if (clan.isRequestByClan(target, RequestType.ALLIANCE)) {
                UtilMessage.simpleMessage(player, "Clans", "You have already requested an alliance with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.NEUTRAL)));
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanAllyEvent> getClassOfEvent() {
        return ClanAllyEvent.class;
    }

    @Override
    public void onEvent(final ClanAllyEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Clan target = event.getTarget();

        switch (event.getResult()) {
            case ACCEPT:
                this.acceptAlliance(event.getPlayer(), clan, target);
                break;
            case FORCE:
                this.forceAlliance(clan, target);
                break;
        }
    }

    private void requestAlliance(final Player player, final Clan clan, final Clan target) {
        clan.addRequestByClan(target, RequestType.ALLIANCE);

        UtilMessage.simpleMessage(player, "Clans", "You requested an alliance with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.NEUTRAL)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has requested an alliance with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(target, ClanRelation.NEUTRAL)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(target, "Clans", "<var> has requested an alliance with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.NEUTRAL)), null);
    }

    private void acceptAlliance(final Player player, final Clan clan, final Clan target) {
        this.handleAlliance(clan, target);

        UtilMessage.simpleMessage(player, "Clans", "You accepted alliance with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.ALLIANCE)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has accepted alliance with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(target, ClanRelation.ALLIANCE)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(target, "Clans", "<var> has accepted alliance with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.ALLIANCE)), null);
    }

    private void forceAlliance(final Clan clan, final Clan target) {
        this.handleAlliance(clan, target);

        this.getModule().getManager().messageClan(clan, "Clans", "You are now allies with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.ALLIANCE)), null);
        this.getModule().getManager().messageClan(target, "Clans", "You are now allies with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.ALLIANCE)), null);
    }

    private void handleAlliance(final Clan clan, final Clan target) {
        clan.removeRequestByClan(target, RequestType.ALLIANCE);
        target.removeRequestByClan(clan, RequestType.ALLIANCE);

        if (clan.isEnemyByClan(target)) {
            clan.removeEnemy(clan.getEnemyByClan(target));
            target.removeEnemy(target.getEnemyByClan(clan));

            this.getModule().getManager().getRepository().updateData(clan, ClanProperty.ENEMIES);
            this.getModule().getManager().getRepository().updateData(target, ClanProperty.ENEMIES);
        }

        if (clan.isPillageByClan(target)) {
            clan.removePillage(clan.getPillageByClan(target));

            this.getModule().getManager().getRepository().updateData(clan, ClanProperty.PILLAGES);
        }

        if (target.isPillageByClan(clan)) {
            target.removePillage(target.getPillageByClan(clan));

            this.getModule().getManager().getRepository().updateData(target, ClanProperty.PILLAGES);
        }

        clan.addAlliance(new Alliance(target));
        target.addAlliance(new Alliance(clan));

        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.ALLIANCES);
        this.getModule().getManager().getRepository().updateData(target, ClanProperty.ALLIANCES);
    }
}