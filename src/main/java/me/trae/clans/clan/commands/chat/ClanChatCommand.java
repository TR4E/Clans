package me.trae.clans.clan.commands.chat;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.commands.chat.abstracts.AbstractChatCommand;
import me.trae.clans.clan.enums.ChatType;
import org.bukkit.entity.Player;

public class ClanChatCommand extends AbstractChatCommand {

    public ClanChatCommand(final ClanManager manager) {
        super(manager, "clanchat", new String[]{"cc"});
    }

    @Override
    public String getDescription() {
        return "Toggle Clan Chat";
    }

    @Override
    public ChatType getType() {
        return ChatType.CLAN_CHAT;
    }

    @Override
    public String getFormat(final Player player, final Clan clan, final String message) {
        return String.format("<aqua>%s <dark_aqua>%s", player.getName(), message);
    }
}