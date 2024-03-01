package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.MemberKickEvent;
import me.trae.core.client.Client;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.interfaces.EventContainer;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class KickCommand extends ClanSubCommand implements EventContainer<MemberKickEvent> {

    public KickCommand(final ClanCommand manager) {
        super(manager, "kick");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <member>";
    }

    @Override
    public String getDescription() {
        return "Kick a Member";
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
            UtilMessage.message(player, "Clans", "You did not input a Member to Kick.");
            return;
        }

        final Client target = this.getModule().getManager().searchMember(clan, player, args[0], true);
        if (target == null) {
            return;
        }

        if (!(this.canKickMember(player, client, clan, target))) {
            return;
        }

        this.callEvent(new MemberKickEvent(clan, player, client, target));
    }

    private boolean canKickMember(final Player player, final Client client, final Clan clan, final Client target) {
        if (target == client) {
            UtilMessage.message(player, "Clans", "You cannot kick yourself!");
            return false;
        }

        if (!(client.isAdministrating())) {
            if (target.isAdministrating() || clan.compareMemberRoleByUUID(target.getUUID(), client.getUUID())) {
                UtilMessage.simpleMessage(player, "Clans", "You do not outrank <var>!", Collections.singletonList(ClanRelation.SELF.getSuffix() + target.getName()));
                return false;
            }

            if (clan.isBeingPillaged(this.getModule().getManager())) {
                UtilMessage.message(player, "Clans", "You cannot kick a member while being conquered by another clan!");
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<MemberKickEvent> getClassOfEvent() {
        return MemberKickEvent.class;
    }

    @Override
    public void onEvent(final MemberKickEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        final Client target = event.getTarget();

        clan.removeMember(clan.getMemberByUUID(target.getUUID()));
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.MEMBERS);

        UtilMessage.simpleMessage(player, "Clans", "You kicked <var> from the Clan.", Collections.singletonList(ClanRelation.NEUTRAL.getSuffix() + target.getName()));
        UtilMessage.simpleMessage(target.getPlayer(), "Clans", "<var> has kicked you from <var>.", Arrays.asList(ClanRelation.NEUTRAL.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(clan, ClanRelation.NEUTRAL)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has kicked <var> from the Clan.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), ClanRelation.NEUTRAL.getSuffix() + target.getName()), Collections.singletonList(player.getUniqueId()));
    }
}