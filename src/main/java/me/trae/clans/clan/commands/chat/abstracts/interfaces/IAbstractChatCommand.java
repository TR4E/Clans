package me.trae.clans.clan.commands.chat.abstracts.interfaces;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.enums.ChatType;
import org.bukkit.entity.Player;

public interface IAbstractChatCommand {

    ChatType getType();

    String getFormat(final Player player, final Clan clan, final String message);
}