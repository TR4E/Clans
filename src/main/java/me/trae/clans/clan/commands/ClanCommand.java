package me.trae.clans.clan.commands;

import me.trae.clans.clan.Clan;
import me.trae.clans.clan.ClanManager;
import me.trae.clans.clan.commands.subcommands.*;
import me.trae.core.client.Client;
import me.trae.core.command.types.PlayerCommand;
import me.trae.core.utility.UtilMessage;
import me.trae.framework.shared.client.enums.Rank;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import me.trae.framework.shared.utility.UtilFormat;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

public class ClanCommand extends PlayerCommand<ClanManager> {

    public ClanCommand(final ClanManager manager) {
        super(manager, "clan", new String[]{"faction", "gang", "fac", "c", "f", "g"}, Rank.DEFAULT);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new AllyCommand(this));
        addSubModule(new CreateCommand(this));
        addSubModule(new DemoteCommand(this));
        addSubModule(new DisbandCommand(this));
        addSubModule(new EnemyCommand(this));
        addSubModule(new HelpCommand(this));
        addSubModule(new HomeCommand(this));
        addSubModule(new InviteCommand(this));
        addSubModule(new JoinCommand(this));
        addSubModule(new KickCommand(this));
        addSubModule(new LeaveCommand(this));
        addSubModule(new NeutralCommand(this));
        addSubModule(new PromoteCommand(this));
        addSubModule(new SetHomeCommand(this));
        addSubModule(new TrustCommand(this));
        addSubModule(new UnTrustCommand(this));

        // TODO: ClaimCommand, UnClaimCommand, UnClaimAllCommand
    }

    @Override
    public String getDescription() {
        return "Clan Management";
    }

    @Override
    public void execute(final Player player, final Client client, final GlobalGamer globalGamer, final String[] args) {
        if (args.length > 0 && this.isSubCommandByLabel(args[0])) {
            this.executeSubCommand(player, args);
            return;
        }

        final Clan playerClan = this.getManager().getClanByPlayer(player);

        Clan targetClan = null;

        if (args.length == 0) {
            if (playerClan == null) {
                UtilMessage.message(player, "Clans", "You are not in a Clan.");
                return;
            }

            targetClan = playerClan;
        }

        if (args.length == 1) {
            targetClan = this.getManager().searchClan(player, args[0], true);
            if (targetClan == null) {
                return;
            }
        }

        if (targetClan == null) {
            return;
        }

        UtilMessage.simpleMessage(player, "Clans", "<var> Information:", Collections.singletonList(this.getManager().getClanRelationByClan(playerClan, targetClan).getSuffix() + targetClan.getName()));

        for (final Map.Entry<String, String> entry : this.getManager().getClanInformation(player, client, playerClan, targetClan).entrySet()) {
            UtilMessage.simpleMessage(player, null, UtilFormat.pairString(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public String getHelpPrefix() {
        return "Clans";
    }
}