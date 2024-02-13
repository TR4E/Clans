package me.trae.clans.clan.commands;

import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.commands.subcommands.CreateCommand;
import me.trae.clans.clan.commands.subcommands.HelpCommand;
import me.trae.core.client.Client;
import me.trae.core.command.types.PlayerCommand;
import me.trae.core.utility.UtilMessage;
import me.trae.framework.shared.client.enums.Rank;
import org.bukkit.entity.Player;

public class ClanCommand extends PlayerCommand<ClanManager> {

    public ClanCommand(final ClanManager manager) {
        super(manager, "clan", new String[]{"faction", "gang", "fac", "c", "f", "g"}, Rank.DEFAULT);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new CreateCommand(this));
        addSubModule(new HelpCommand(this));
    }

    @Override
    public String getDescription() {
        return "Clan Management";
    }

    @Override
    public void execute(final Player player, final Client client, final String[] args) {
        if (args.length > 0 && this.isSubCommandByLabel(args[0])) {
            this.executeSubCommand(player, args);
            return;
        }

        if (args.length == 0) {
            UtilMessage.message(player, "Clans", "You are not in a Clan.");
        }
    }

    @Override
    public String getHelpPrefix() {
        return "Clans";
    }
}