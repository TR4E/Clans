package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.data.enums.RequestType;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanTrustEvent;
import me.trae.core.client.Client;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.interfaces.EventContainer;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class TrustCommand extends ClanSubCommand implements EventContainer<ClanTrustEvent> {

    public TrustCommand(final ClanCommand manager) {
        super(manager, "trust");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <clan>";
    }

    @Override
    public String getDescription() {
        return "Trust a Clan";
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Trust.");
            return;
        }

        final Clan target = this.getModule().getManager().searchClan(player, args[0], true);
        if (target == null) {
            return;
        }

        if (!(this.canTrustClan(player, client, clan, target))) {
            return;
        }

        if (client.isAdministrating()) {
            this.callEvent(new ClanTrustEvent(clan, player, client, target) {
                @Override
                public Result getResult() {
                    return Result.FORCE;
                }
            });
            return;
        }

        if (!(target.isRequestByClan(clan, RequestType.TRUST))) {
            this.requestTrust(player, clan, target);
            return;
        }

        this.callEvent(new ClanTrustEvent(clan, player, client, target) {
            @Override
            public Result getResult() {
                return Result.ACCEPT;
            }
        });
    }

    private boolean canTrustClan(final Player player, final Client client, final Clan clan, final Clan target) {
        if (target == clan) {
            UtilMessage.message(player, "Clans", "You cannot request to trust with yourself!");
            return false;
        }

        if (!(clan.isAllianceByClan(target))) {
            UtilMessage.simpleMessage(player, "Clans", "You are not allies with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, this.getModule().getManager().getClanRelationByClan(clan, target))));
            return false;
        }

        if (clan.isTrustedByClan(target)) {
            UtilMessage.simpleMessage(player, "Clans", "You are already trusted with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, this.getModule().getManager().getClanRelationByClan(clan, target))));
        }

        if (!(client.isAdministrating())) {
            if (target.isAdmin()) {
                UtilMessage.message(player, "Clans", "You cannot trust Admin Clans!");
                return false;
            }

            if (clan.isRequestByClan(target, RequestType.TRUST)) {
                UtilMessage.simpleMessage(player, "Clans", "You have already requested to trust with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.ALLIANCE)));
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanTrustEvent> getClassOfEvent() {
        return ClanTrustEvent.class;
    }

    @Override
    public void onEvent(final ClanTrustEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Clan target = event.getTarget();

        switch (event.getResult()) {
            case ACCEPT:
                this.acceptTrust(event.getPlayer(), clan, target);
                break;
            case FORCE:
                this.forceTrust(clan, target);
                break;
        }
    }

    private void requestTrust(final Player player, final Clan clan, final Clan target) {
        clan.addRequestByClan(target, RequestType.TRUST);

        UtilMessage.simpleMessage(player, "Clans", "You requested to trust with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.ALLIANCE)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has requested to trust with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(target, ClanRelation.ALLIANCE)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(target, "Clans", "<var> has requested to trust with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.ALLIANCE)), null);
    }

    private void acceptTrust(final Player player, final Clan clan, final Clan target) {
        this.handleTrust(clan, target);

        UtilMessage.simpleMessage(player, "Clans", "You accepted to trust with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.TRUSTED_ALLIANCE)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has accepted to trust with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(target, ClanRelation.TRUSTED_ALLIANCE)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(target, "Clans", "<var> has accepted to trust with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.TRUSTED_ALLIANCE)), null);
    }

    private void forceTrust(final Clan clan, final Clan target) {
        this.handleTrust(clan, target);

        this.getModule().getManager().messageClan(clan, "Clans", "You are now trusted with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.TRUSTED_ALLIANCE)), null);
        this.getModule().getManager().messageClan(target, "Clans", "You are now trusted with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.TRUSTED_ALLIANCE)), null);
    }

    private void handleTrust(final Clan clan, final Clan target) {
        clan.removeRequestByClan(target, RequestType.TRUST);
        target.removeRequestByClan(clan, RequestType.TRUST);

        clan.getAllianceByClan(target).setTrusted(true);
        target.getAllianceByClan(clan).setTrusted(true);

        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.ALLIANCES);
        this.getModule().getManager().getRepository().updateData(target, ClanProperty.ALLIANCES);
    }
}