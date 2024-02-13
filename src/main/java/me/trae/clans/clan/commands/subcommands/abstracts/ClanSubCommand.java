package me.trae.clans.clan.commands.subcommands.abstracts;

import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.interfaces.IClanSubCommand;
import me.trae.core.client.Client;
import me.trae.core.command.subcommand.types.PlayerSubCommand;
import me.trae.framework.shared.client.enums.Rank;
import me.trae.framework.shared.utility.enums.ChatColor;
import org.bukkit.entity.Player;

public abstract class ClanSubCommand extends PlayerSubCommand<ClanCommand> implements IClanSubCommand {

    public ClanSubCommand(final ClanCommand manager, final String label, final Rank requiredRank) {
        super(manager, label, requiredRank);
    }

    public ClanSubCommand(final ClanCommand manager, final String label) {
        super(manager, label);
    }

    @Override
    public ChatColor getUsageChatColor() {
        return super.getUsageChatColor();
    }

    @Override
    public void execute(final Player player, final Client client, final String[] args) {
        this.execute(player, client, null, args);
    }
}