package me.trae.clans.clan.commands.chat;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.commands.chat.abstracts.AbstractChatCommand;
import me.trae.clans.clan.enums.ChatType;
import org.bukkit.entity.Player;

public class AllyChatCommand extends AbstractChatCommand {

    public AllyChatCommand(final ClanManager manager) {
        super(manager, "allychat", new String[]{"ac"});
    }

    @Override
    public String getDescription() {
        return "Toggle Ally Chat";
    }

    @Override
    public ChatType getType() {
        return ChatType.ALLY_CHAT;
    }

    @Override
    public String getFormat(final Player player, final Clan clan, final String message) {
        return String.format("<dark_green>%s %s <green>%s", clan.getName(), player.getName(), message);
    }
}