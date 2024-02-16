package me.trae.clans.clan.modules;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.chat.events.ChatReceiveEvent;
import me.trae.core.framework.types.SpigotListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class HandleChatReceiver extends SpigotListener<ClanManager> {

    public HandleChatReceiver(final ClanManager manager) {
        super(manager);
    }

    @EventHandler
    public void onChatReceive(final ChatReceiveEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        final Clan playerClan = this.getManager().getClanByPlayer(player);
        if (playerClan == null) {
            return;
        }

        final Player target = event.getTarget();

        final ClanRelation clanRelation = this.getManager().getClanRelationByClan(this.getManager().getClanByPlayer(target), playerClan);

        event.setPlayerName(clanRelation.getPrefix() + playerClan.getName() + " " + clanRelation.getSuffix() + player.getName());
    }
}