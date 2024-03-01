package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.Member;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.data.enums.RequestType;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanInviteEvent;
import me.trae.core.client.Client;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import me.trae.core.utility.interfaces.EventContainer;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class InviteCommand extends ClanSubCommand implements EventContainer<ClanInviteEvent> {

    public InviteCommand(final ClanCommand manager) {
        super(manager, "invite");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <player>";
    }

    @Override
    public String getDescription() {
        return "Invite a Player";
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
            UtilMessage.message(player, "Clans", "You did not input a Player to Invite.");
            return;
        }

        final Player target = UtilPlayer.searchPlayer(player, args[0], true);
        if (target == null) {
            return;
        }

        if (!(this.canInvitePlayer(player, client, clan, target))) {
            return;
        }

        this.callEvent(new ClanInviteEvent(clan, player, client, target));
    }

    private boolean canInvitePlayer(final Player player, final Client client, final Clan clan, final Player target) {
        if (player == target) {
            UtilMessage.message(player, "Clans", "You cannot invite yourself!");
            return false;
        }

        final Clan targetClan = this.getModule().getManager().getClanByPlayer(target);

        if (targetClan == clan) {
            UtilMessage.simpleMessage(player, "Clans", "<var> is already apart of the Clan!", Collections.singletonList(ClanRelation.SELF.getSuffix() + target.getName()));
            return false;
        }

        if (targetClan != null) {
            final ClanRelation clanRelation = this.getModule().getManager().getClanRelationByClan(clan, targetClan);

            UtilMessage.simpleMessage(player, "Clans", "<var> is apart of <var>!", Arrays.asList(clanRelation.getSuffix() + target.getName(), this.getModule().getManager().getClanFullName(targetClan, clanRelation)));
            return false;
        }

        if (!(client.isAdministrating())) {
            if (clan.isRequestByPlayer(target, RequestType.INVITATION)) {
                UtilMessage.simpleMessage(player, "Clans", "<var> has already been invited to the clan!", Collections.singletonList(ClanRelation.NEUTRAL.getSuffix() + target.getName()));
                return false;
            }

            if (clan.isFull(this.getModule().getManager())) {
                UtilMessage.message(player, "Clans", "Your Clan has too many members/allies!");
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanInviteEvent> getClassOfEvent() {
        return ClanInviteEvent.class;
    }

    @Override
    public void onEvent(final ClanInviteEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Clan clan = event.getClan();
        final Player player = event.getPlayer();
        final Player target = event.getTarget();

        if (event.getClient().isAdministrating()) {
            this.forceInvitation(player, clan, target);
        } else {
            this.requestInvitation(player, clan, target);
        }
    }

    private void forceInvitation(final Player player, final Clan clan, final Player target) {
        clan.addMember(new Member(target, MemberRole.RECRUIT));
        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.MEMBERS);

        UtilMessage.simpleMessage(player, "Clans", "You forcefully invited <var> to the join Clan.", Collections.singletonList(ClanRelation.SELF.getSuffix() + target.getName()));
        UtilMessage.simpleMessage(target, "Clans", "<var> has forcefully invited you to join <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(clan, ClanRelation.SELF)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has forcefully invited <var> to join the Clan.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), ClanRelation.SELF.getSuffix() + target.getName()), Arrays.asList(player.getUniqueId(), target.getUniqueId()));
    }

    private void requestInvitation(final Player player, final Clan clan, final Player target) {
        clan.addRequestByPlayer(player, RequestType.INVITATION);

        UtilMessage.simpleMessage(player, "Clans", "You invited <var> to the join Clan.", Collections.singletonList(ClanRelation.NEUTRAL.getSuffix() + target.getName()));
        UtilMessage.simpleMessage(target, "Clans", "<var> has invited you to join <var>.", Arrays.asList(ClanRelation.NEUTRAL.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(clan, ClanRelation.NEUTRAL)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has invited <var> to join the Clan.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), ClanRelation.NEUTRAL.getSuffix() + target.getName()), Collections.singletonList(player.getUniqueId()));
    }
}