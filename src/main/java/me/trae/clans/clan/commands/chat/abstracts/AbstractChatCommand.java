package me.trae.clans.clan.commands.chat.abstracts;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.commands.chat.abstracts.interfaces.IAbstractChatCommand;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.core.chat.events.ChatSendEvent;
import me.trae.core.client.Client;
import me.trae.core.command.types.PlayerCommand;
import me.trae.core.gamer.local.events.ChatTypeUpdateEvent;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilServer;
import me.trae.framework.shared.client.enums.Rank;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import me.trae.framework.shared.utility.UtilFormat;
import me.trae.framework.shared.utility.enums.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public abstract class AbstractChatCommand extends PlayerCommand<ClanManager> implements IAbstractChatCommand, Listener {

    public AbstractChatCommand(final ClanManager manager, final String label, final String[] aliases) {
        super(manager, label, aliases, Rank.DEFAULT);
    }

    @Override
    public ChatColor getUsageChatColor() {
        return ChatColor.AQUA;
    }

    @Override
    public void execute(final Player player, final Client client, final GlobalGamer globalGamer, final String[] args) {
        if (args.length == 0) {
            final Gamer gamer = this.getInstance().getManagerByClass(GamerManager.class).getGamerByPlayer(player);

            if (gamer.isChatType(this.getType())) {
                gamer.resetChatType();

                UtilMessage.simpleMessage(player, "Clans", UtilFormat.pairString(UtilFormat.cleanString(this.getType().name()), "<red>Disabled"));
            } else {
                gamer.setChatType(this.getType());

                UtilMessage.simpleMessage(player, "Clans", UtilFormat.pairString(UtilFormat.cleanString(this.getType().name()), "<green>Enabled"));
            }

            UtilServer.callEvent(new ChatTypeUpdateEvent(gamer, gamer.getChatType()));
            return;
        }

        UtilServer.callEvent(new ChatSendEvent(player, client, this.getType(), UtilFormat.getFinalArgs(args, 0)));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatSend(final ChatSendEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();

        if (event.isType(this.getType())) {
            return;
        }

        final Gamer gamer = this.getInstance().getManagerByClass(GamerManager.class).getGamerByPlayer(player);

        if (!(gamer.isChatType(this.getType()))) {
            return;
        }

        gamer.setChatType(this.getType());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatSend2(final ChatSendEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.isType(this.getType()))) {
            return;
        }

        final Player player = event.getPlayer();

        final Clan clan = this.getManager().getClanByPlayer(player);
        if (clan == null) {
            return;
        }

        event.setFormat(this.getFormat(player, clan, event.getMessage()));
    }
}