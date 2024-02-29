package me.trae.clans.clan.data.interfaces;

import me.trae.clans.clan.data.enums.MemberRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface IMember {

    default Player getPlayer() {
        return Bukkit.getPlayer(this.getUUID());
    }

    UUID getUUID();

    MemberRole getRole();

    void setRole(final MemberRole memberRole);

    boolean hasRole(final MemberRole memberRole);
}