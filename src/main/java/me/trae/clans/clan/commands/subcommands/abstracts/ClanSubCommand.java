package me.trae.clans.clan.commands.subcommands.abstracts;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.commands.ClanCommand;
import me.trae.clans.clan.commands.subcommands.abstracts.interfaces.IClanSubCommand;
import me.trae.clans.clan.data.enums.MemberRole;
import me.trae.core.client.Client;
import me.trae.core.command.subcommand.types.PlayerSubCommand;
import me.trae.core.utility.UtilMessage;
import me.trae.framework.shared.client.enums.Rank;
import me.trae.framework.shared.utility.enums.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

public abstract class ClanSubCommand extends PlayerSubCommand<ClanCommand> implements IClanSubCommand {

    public ClanSubCommand(final ClanCommand manager, final String label, final Rank requiredRank) {
        super(manager, label, requiredRank);
    }

    public ClanSubCommand(final ClanCommand manager, final String label) {
        super(manager, label);
    }

    @Override
    public ChatColor getUsageChatColor() {
        if (this.getRequiredMemberRole() != null) {
            return this.getRequiredMemberRole().getChatColor();
        }

        return ChatColor.YELLOW;
    }

    @Override
    public MemberRole getRequiredMemberRole() {
        return null;
    }

    @Override
    public boolean hasRequiredMemberRole(final Player player, final Client client, final Clan clan, final MemberRole requiredMemberRole, final boolean inform) {
        if (requiredMemberRole == null) {
            return true;
        }

        if (clan.isMemberByPlayer(player) && clan.getMemberByPlayer(player).hasRole(requiredMemberRole)) {
            return true;
        }

        if (client.isAdministrating()) {
            return true;
        }

        if (inform) {
            UtilMessage.simpleMessage(player, "Clans", "You must be <var> to <var>!", Arrays.asList(requiredMemberRole.getChatColor() + requiredMemberRole.getName(), ChatColor.WHITE + this.getDescription()));
        }

        return false;
    }

    @Override
    public void execute(final Player player, final Client client, final String[] args) {
        this.execute(player, client, this.getModule().getManager().getClanByPlayer(player), args);
    }
}