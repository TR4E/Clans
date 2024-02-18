package me.trae.clans.clan.modules.death;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.death.events.CustomDeathEvent;
import me.trae.core.death.events.CustomDeathMessageEvent;
import me.trae.core.framework.types.SpigotListener;
import me.trae.framework.shared.utility.UtilJava;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class HandleCustomDeathMessageReceiver extends SpigotListener<ClanManager> {

    public HandleCustomDeathMessageReceiver(final ClanManager manager) {
        super(manager);
    }

    @EventHandler
    public void onCustomDeathMessage(final CustomDeathMessageEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final CustomDeathEvent deathEvent = event.getDeathEvent();

        final Player target = event.getTarget();

        if (deathEvent.getEntity() instanceof Player) {
            UtilJava.call(this.getName(deathEvent.getEntityCasted(Player.class), target), entityName -> {
                if (entityName != null) {
                    event.setEntityName(entityName);
                }
            });
        }

        if (deathEvent.getKiller() instanceof Player) {
            UtilJava.call(this.getName(deathEvent.getKillerCasted(Player.class), target), killerName -> {
                if (killerName != null) {
                    event.setKillerName(killerName);
                }
            });
        }
    }

    private String getName(final Player player, final Player target) {
        final Clan playerClan = this.getManager().getClanByPlayer(player);
        if (playerClan == null) {
            return null;
        }

        final ClanRelation clanRelation = this.getManager().getClanRelationByClan(this.getManager().getClanByPlayer(target), playerClan);

        return clanRelation.getSuffix() + player.getName();
    }
}