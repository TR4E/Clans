package me.trae.clans.clan.data.enums;

import me.trae.clans.clan.data.enums.interfaces.IMemberRole;
import me.trae.framework.shared.utility.UtilFormat;
import me.trae.framework.shared.utility.enums.ChatColor;

public enum MemberRole implements IMemberRole {

    RECRUIT, MEMBER, ADMIN, LEADER;

    private final String name;

    MemberRole() {
        this.name = UtilFormat.cleanString(this.name());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ChatColor getChatColor() {
        switch (this) {
            case LEADER:
                return ChatColor.DARK_RED;
            case ADMIN:
                return ChatColor.RED;
            case MEMBER:
            case RECRUIT:
                return ChatColor.AQUA;
        }

        return null;
    }
}