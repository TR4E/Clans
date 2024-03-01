package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanRevokeTrustEvent;
import me.trae.core.client.Client;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.interfaces.EventContainer;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class UnTrustCommand extends ClanSubCommand implements EventContainer<ClanRevokeTrustEvent> {

    public UnTrustCommand(final ClanCommand manager) {
        super(manager, "untrust");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <clan>";
    }

    @Override
    public String getDescription() {
        return "Trust Revoke a Clan";
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Revoke Trust.");
            return;
        }

        final Clan target = this.getModule().getManager().searchClan(player, args[0], true);
        if (target == null) {
            return;
        }

        if (!(this.canRevokeTrustClan(player, client, clan, target))) {
            return;
        }

        this.callEvent(new ClanRevokeTrustEvent(clan, player, client, target));
    }

    private boolean canRevokeTrustClan(final Player player, final Client client, final Clan clan, final Clan target) {
        if (target == clan) {
            UtilMessage.message(player, "Clans", "You cannot request to trust with yourself!");
            return false;
        }

        if (!(clan.isAllianceByClan(target))) {
            UtilMessage.simpleMessage(player, "Clans", "You are not allies with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, this.getModule().getManager().getClanRelationByClan(clan, target))));
            return false;
        }

        if (!(clan.isTrustedByClan(target))) {
            UtilMessage.simpleMessage(player, "Clans", "You are not trusted with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.ALLIANCE)));
            return false;
        }

        return true;
    }

    @Override
    public Class<ClanRevokeTrustEvent> getClassOfEvent() {
        return ClanRevokeTrustEvent.class;
    }

    @Override
    public void onEvent(final ClanRevokeTrustEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();
        final Clan clan = event.getClan();
        final Clan target = event.getTarget();

        clan.getAllianceByClan(target).setTrusted(false);
        target.getAllianceByClan(clan).setTrusted(false);

        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.ALLIANCES);
        this.getModule().getManager().getRepository().updateData(target, ClanProperty.ALLIANCES);

        UtilMessage.simpleMessage(player, "Clans", "You revoked trust with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.ALLIANCE)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has revoked trust with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(target, ClanRelation.ALLIANCE)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(target, "Clans", "<var> has revoked trust with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.ALLIANCE)), null);
    }
}