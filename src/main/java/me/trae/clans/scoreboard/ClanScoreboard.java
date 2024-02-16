package me.trae.clans.scoreboard;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.enums.ClanRelation;
import me.trae.core.client.Client;
import me.trae.core.framework.SpigotPlugin;
import me.trae.core.scoreboard.MainScoreboard;
import me.trae.framework.shared.utility.UtilFormat;
import me.trae.framework.shared.utility.enums.ChatColor;
import org.bukkit.entity.Player;

public class ClanScoreboard extends MainScoreboard {

    public ClanScoreboard(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerLines(final Player player, final Client client) {
        final ClanManager clanManager = this.getInstance().getManagerByClass(ClanManager.class);

        final Clan playerClan = clanManager.getClanByPlayer(player);

        this.addCustomLine(ChatColor.YELLOW, "Clan", playerClan != null ? ClanRelation.SELF.getSuffix() + playerClan.getName() : "No Clan");
        this.addBlankLine();

        this.addCustomLine(ChatColor.YELLOW, "Territory", ChatColor.GRAY + "Wilderness");
        this.addBlankLine();

        this.addCustomLine(ChatColor.YELLOW, "Coins", ChatColor.GOLD + "$1,000,000");
        this.addBlankLine();

        this.addCustomLine(ChatColor.AQUA, "World Event", "Fishing Frenzy");
    }

    @Override
    public String getPlayerTeamPrefix(final Player player, final Player target) {
        final ClanManager clanManager = this.getInstance().getManagerByClass(ClanManager.class);

        final Clan playerClan = clanManager.getClanByPlayer(player);

        if (playerClan == null) {
            return super.getPlayerTeamPrefix(player, target);
        }

        final String clanName = UtilFormat.trimString(playerClan.getName(), 11);

        final ClanRelation clanRelation = clanManager.getClanRelationByClan(clanManager.getClanByPlayer(target), playerClan);

        return clanRelation.getPrefix() + clanName + " " + clanRelation.getSuffix();
    }
}