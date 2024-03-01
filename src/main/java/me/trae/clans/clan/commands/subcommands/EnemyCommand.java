package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.data.Enemy;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.clans.clan.data.enums.RequestType;
import me.trae.clans.clan.enums.ClanProperty;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.clans.clan.events.ClanEnemyEvent;
import me.trae.core.client.Client;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.interfaces.EventContainer;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class EnemyCommand extends ClanSubCommand implements EventContainer<ClanEnemyEvent> {

    public EnemyCommand(final ClanCommand manager) {
        super(manager, "enemy");
    }

    @Override
    public String getUsage() {
        return super.getUsage() + " <clan>";
    }

    @Override
    public String getDescription() {
        return "Enemy a Clan";
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
            UtilMessage.message(player, "Clans", "You did not input a Clan to Enemy.");
            return;
        }

        final Clan target = this.getModule().getManager().searchClan(player, args[0], true);
        if (target == null) {
            return;
        }

        if (!(this.canEnemyClan(player, client, clan, target))) {
            return;
        }

        this.callEvent(new ClanEnemyEvent(clan, player, client, target));
    }

    private boolean canEnemyClan(final Player player, final Client client, final Clan clan, final Clan target) {
        if (clan == target) {
            UtilMessage.message(player, "Clans", "You cannot enemy yourself!");
            return false;
        }

        if (clan.isEnemyByClan(target)) {
            UtilMessage.simpleMessage(player, "Clans", "You are already enemies with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.ENEMY)));
            return false;
        }

        if (!(client.isAdministrating())) {
            if (target.isAdmin()) {
                UtilMessage.message(player, "Clans", "You cannot enemy Admin Clans!");
                return false;
            }

            if (!(clan.isNeutralByClan(target))) {
                UtilMessage.simpleMessage(player, "Clans", "You must be neutral with <var>!", Collections.singletonList(this.getModule().getManager().getClanFullName(target, this.getModule().getManager().getClanRelationByClan(clan, target))));
                return false;
            }
        }

        return true;
    }

    @Override
    public Class<ClanEnemyEvent> getClassOfEvent() {
        return ClanEnemyEvent.class;
    }

    @Override
    public void onEvent(final ClanEnemyEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();
        final Clan clan = event.getClan();
        final Clan target = event.getTarget();

        this.handleEnemy(clan, target);

        UtilMessage.simpleMessage(player, "Clans", "You waged war with <var>.", Collections.singletonList(this.getModule().getManager().getClanFullName(target, ClanRelation.ENEMY)));

        this.getModule().getManager().messageClan(clan, "Clans", "<var> has waged war with <var>.", Arrays.asList(ClanRelation.SELF.getSuffix() + player.getName(), this.getModule().getManager().getClanFullName(target, ClanRelation.ENEMY)), Collections.singletonList(player.getUniqueId()));
        this.getModule().getManager().messageClan(target, "Clans", "<var> has waged war with your Clan.", Collections.singletonList(this.getModule().getManager().getClanFullName(clan, ClanRelation.ENEMY)), null);
    }

    private void handleEnemy(final Clan clan, final Clan target) {
        for (final RequestType requestType : Arrays.asList(RequestType.NEUTRALITY, RequestType.ALLIANCE, RequestType.TRUST)) {
            clan.removeRequestByClan(target, requestType);
            target.removeRequestByClan(clan, requestType);
        }

        if (clan.isAllianceByClan(target)) {
            clan.removeAlliance(clan.getAllianceByClan(target));
            target.removeAlliance(target.getAllianceByClan(clan));

            this.getModule().getManager().getRepository().updateData(clan, ClanProperty.ALLIANCES);
            this.getModule().getManager().getRepository().updateData(target, ClanProperty.ALLIANCES);
        }

        if (clan.isPillageByClan(target)) {
            clan.removePillage(clan.getPillageByClan(target));

            this.getModule().getManager().getRepository().updateData(clan, ClanProperty.PILLAGES);
        }

        if (target.isPillageByClan(clan)) {
            target.removePillage(target.getPillageByClan(clan));

            this.getModule().getManager().getRepository().updateData(target, ClanProperty.PILLAGES);
        }

        clan.addEnemy(new Enemy(target));
        target.addEnemy(new Enemy(clan));

        this.getModule().getManager().getRepository().updateData(clan, ClanProperty.ENEMIES);
        this.getModule().getManager().getRepository().updateData(target, ClanProperty.ENEMIES);
    }
}