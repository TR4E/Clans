package me.trae.clans.clan.commands.subcommands;

import me.trae.clans.clan.AdminClan;
import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.ClanSubCommand;
import me.trae.clans.clan.events.ClanHomeEvent;
import me.trae.core.client.Client;
import me.trae.core.countdown.CountdownManager;
import me.trae.core.recharge.RechargeManager;
import me.trae.core.teleport.Teleport;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.interfaces.EventContainer;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import me.trae.framework.shared.utility.UtilJava;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeCommand extends ClanSubCommand implements EventContainer<ClanHomeEvent> {

    public HomeCommand(final ClanCommand manager) {
        super(manager, "home");

        this.addPrimitive("Recharge", 30_000L);
        this.addPrimitive("Only-From-Spawn", true);
    }

    @Override
    public String getDescription() {
        return "Teleport to Clan Home";
    }

    @Override
    public void execute(final Player player, final Client client, final GlobalGamer globalGamer, final Clan clan, final String[] args) {
        if (clan == null) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
            return;
        }

        if (!(this.canTeleportHome(player, client, clan))) {
            return;
        }

        this.callEvent(new ClanHomeEvent(clan, player, client));
    }

    private boolean canTeleportHome(final Player player, final Client client, final Clan clan) {
        if (!(clan.hasHome())) {
            UtilMessage.message(player, "Clans", "Your Clan does not have a home set!");
            return false;
        }

        if (!(client.isAdministrating())) {
            if (this.getPrimitiveCasted(Boolean.class, "Only-From-Spawn")) {
                final Clan territoryClan = this.getModule().getManager().getClanByLocation(player.getLocation());
                if (territoryClan == null || !(territoryClan.isAdmin() && UtilJava.cast(AdminClan.class, clan).isSafe() && territoryClan.getName().toLowerCase().contains("spawn"))) {
                    UtilMessage.simpleMessage(player, "Clans", "You can only teleport to Clan Home from <white>Spawn</white>!");
                    return false;
                }
            }

            return !(this.getInstance().getManagerByClass(RechargeManager.class).isCooling(player, "Clan Home", true));
        }

        return true;
    }

    @Override
    public Class<ClanHomeEvent> getClassOfEvent() {
        return ClanHomeEvent.class;
    }

    @Override
    public void onEvent(final ClanHomeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        final Teleport teleport = new Teleport(player) {
            @Override
            public Location getLocation() {
                return event.getClan().getHome();
            }

            @Override
            public void onTeleport(final Player player) {
                HomeCommand.this.getInstance().getManagerByClass(RechargeManager.class).add(player, "Clan Home", HomeCommand.this.getPrimitiveCasted(Long.class, "Recharge"), true);

                UtilMessage.message(player, "Clans", "You teleported to Clan Home.");
            }
        };

        this.getInstance().getManagerByClass(CountdownManager.class).addCountdown(teleport);
    }
}