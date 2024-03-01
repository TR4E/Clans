package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.data.enums.RequestType;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanNeutralEvent;
import me.trae.core.client.Client;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.interfaces.EventContainer;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class NeutralCommand extends ClanSubCommand implements EventContainer<ClanNeutralEvent> {

    public NeutralCommand(final ClanCommand manager) {
        super(manager, "neutral");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <clan>";
    }

    @Override
    public String getDescription() {
        return "Neutral a Clan";
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Neutral.");
            return;
        }

        final Clan target = this.getModule().getManager().searchClan(player, args[0], true);
        if (target == null) {
            return;
        }

        if (!(this.canNeutralClan(player, client, clan, target))) {
            return;
        }

        if (clan.isAllianceByClan(target) || client.isAdministrating()) {
            this.callEvent(new ClanNeutralEvent(clan, player, client, target) {
                @Override
                public Result getResult() {
                    return Result.FORCE;
                }
            });
            return;
        }

        if (!(target.isRequestByClan(clan, RequestType.NEUTRALITY))) {
            this.requestNeutrality(player, clan, target);
            return;
        }

        this.callEvent(new ClanNeutralEvent(clan, player, client, target) {
            @Override
            public Result getResult() {
                return Result.ACCEPT;
            }
        });
    }

    private boolean canNeutralClan(final Player player, final Client client, final Clan clan, final Clan target) {
        if (target == clan) {
            UtilMessage.message(player, "Clans", "You cannot request neutrality with yourself!");
            return false;
        }

        if (target.isNeutralByClan(clan)) {
            UtilMessage.simpleMessage(player, "Clans", "You are already neutral with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.NEUTRAL)));
            return false;
        }

        if (!(client.isAdministrating())) {
            if (target.isAdmin()) {
                UtilMessage.message(player, "Clans", "You cannot reuqest neutrality with Admin Clans!");
                return false;
            }

            if (clan.isPillageByClan(target) || target.isPillageByClan(clan)) {
                UtilMessage.simpleMessage(player, "Clans", "You are not neutral with <var> until the pillage is over!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.PILLAGE)));
                return false;
            }

            if (clan.isRequestByClan(target, RequestType.NEUTRALITY)) {
                UtilMessage.simpleMessage(player, "Clans", "Your clan has already requested neutrality with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, this.getModule().getManager().getClanRelationByClan(clan, target))));
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanNeutralEvent> getClassOfEvent() {
        return ClanNeutralEvent.class;
    }

    @Override
    public void onEvent(final ClanNeutralEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Clan target = event.getTarget();

        switch (event.getResult()) {
            case ACCEPT:
                this.acceptNeutrality(event.getPlayer(), clan, target);
                break;
            case FORCE:
                this.forceNeutrality(clan, target);
                break;
        }
    }

    private void requestNeutrality(final Player player, final Clan clan, final Clan target) {
        clan.addRequestByClan(target, RequestType.NEUTRALITY);

        UtilMessage.simpleMessage(player, "Clans", "You requested neutrality with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(target, this.getModule().getManager().getClanRelationByClan(clan, target))));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has requested neutrality with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(target, this.getModule().getManager().getClanRelationByClan(clan, target))), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(target, "Clans", "<var> has requested neutrality with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, this.getModule().getManager().getClanRelationByClan(target, clan))), null);
    }

    private void acceptNeutrality(final Player player, final Clan clan, final Clan target) {
        this.handleNeutral(clan, target);

        UtilMessage.simpleMessage(player, "Clans", "You accepted neutrality with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.NEUTRAL)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has accepted neutrality with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(target, ClanRelation.NEUTRAL)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(target, "Clans", "<var> has accepted neutrality with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.NEUTRAL)), null);
    }

    private void forceNeutrality(final Clan clan, final Clan target) {
        this.handleNeutral(clan, target);

        this.getModule().getManager().messageClan(clan, "Clans", "You are now neutral with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.NEUTRAL)), null);
        this.getModule().getManager().messageClan(target, "Clans", "You are now neutral with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.NEUTRAL)), null);
    }

    private void handleNeutral(final Clan clan, final Clan target) {
        clan.removeRequestByClan(target, RequestType.NEUTRALITY);
        target.removeRequestByClan(clan, RequestType.NEUTRALITY);

        if (clan.isAllianceByClan(target)) {
            clan.removeAlliance(clan.getAllianceByClan(target));
            target.removeAlliance(target.getAllianceByClan(clan));

            this.getModule().getManager().getRepository().updateData(clan, ClanProperty.ALLIANCES);
            this.getModule().getManager().getRepository().updateData(target, ClanProperty.ALLIANCES);
        }

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
    }
}