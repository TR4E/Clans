package me.trae.clans.economy.commands;

import me.trae.clans.economy.EconomyManager;
import me.trae.clans.economy.events.EconomyGiveEvent;
import me.trae.clans.economy.events.EconomySendEvent;
import me.trae.clans.economy.events.EconomySetEvent;
import me.trae.clans.economy.events.EconomyTakeEvent;
import me.trae.clans.gamer.Gamer;
import me.trae.clans.gamer.GamerManager;
import me.trae.clans.gamer.enums.GamerProperty;
import me.trae.core.client.Client;
import me.trae.core.command.subcommand.types.PlayerSubCommand;
import me.trae.core.command.subcommand.types.SubCommand;
import me.trae.core.command.types.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.core.utility.UtilPlayer;
import me.trae.core.utility.UtilServer;
import me.trae.framework.shared.client.enums.Rank;
import me.trae.framework.shared.gamer.global.types.GlobalGamer;
import me.trae.framework.shared.utility.UtilFormat;
import me.trae.framework.shared.utility.UtilJava;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class EconomyCommand extends Command<EconomyManager> {

    public EconomyCommand(final EconomyManager manager) {
        super(manager, "economy", new String[]{"eco", "money", "coins", "gold", "balance", "bal"}, Rank.DEFAULT);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new SetCommand(this));
        addSubModule(new GiveCommand(this));
        addSubModule(new TakeCommand(this));
        addSubModule(new CheckCommand(this));
        addSubModule(new SendCommand(this));
    }

    @Override
    public String getDescription() {
        return "Economy Management";
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            if (!(this.isValidSender(sender, Player.class, false))) {
                this.help(sender);
                return;
            }

            final Player player = UtilJava.cast(Player.class, sender);

            final Gamer gamer = this.getInstance().getManagerByClass(GamerManager.class).getGamerByPlayer(player);
            if (gamer == null) {
                return;
            }

            UtilMessage.simpleMessage(player, "Economy", "You have <gold><var> Coins</gold>.", Collections.singletonList(gamer.getCoinsString()));
            return;
        }

        this.executeSubCommand(sender, args);
    }

    private static class CheckCommand extends SubCommand<EconomyCommand> {

        public CheckCommand(final EconomyCommand economyCommand) {
            super(economyCommand, "check", Rank.ADMIN);
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player>";
        }

        @Override
        public String getDescription() {
            return "Check Coins of Player";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Economy", "You did not input a Player to Check.");
                return;
            }

            final Player target = UtilPlayer.searchPlayer(sender, args[0], true);
            if (target == null) {
                return;
            }

            final Gamer targetGamer = this.getInstance().getManagerByClass(GamerManager.class).getGamerByPlayer(target);
            if (targetGamer == null) {
                return;
            }

            UtilMessage.simpleMessage(sender, "Economy", "<yellow><var></yellow> has <gold><var> Coins</gold>.", Arrays.asList(target.getName(), targetGamer.getCoinsString()));
        }
    }

    private static class SendCommand extends PlayerSubCommand<EconomyCommand> {

        public SendCommand(final EconomyCommand economyCommand) {
            super(economyCommand, "send");
        }

        @Override
        public String getShortcut() {
            return "pay";
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player> <amount>";
        }

        @Override
        public String getDescription() {
            return "Send Coins to a Player";
        }

        @Override
        public void execute(final Player player, final Client client, final GlobalGamer globalGamer, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(player, "Economy", "You did not input a Player to Send.");
                return;
            }

            final Player target = UtilPlayer.searchPlayer(player, args[0], true);
            if (target == null) {
                return;
            }

            if (target == player) {
                UtilMessage.message(player, "Economy", "You cannot send yourself coins.");
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(player, "Economy", "You did not input an Amount to Send.");
                return;
            }

            final int amount = this.getModule().getManager().getAmount(player, args[1], 1);
            if (amount == -1) {
                return;
            }

            final GamerManager gamerManager = this.getInstance().getManagerByClass(GamerManager.class);

            final Gamer playerGamer = gamerManager.getGamerByPlayer(player);
            if (playerGamer == null) {
                return;
            }

            if (playerGamer.getCoins() < amount) {
                UtilMessage.simpleMessage(player, "Economy", "You do not have sufficient funds to send coins to <yellow><var></yellow>.", Collections.singletonList(target.getName()));
                return;
            }

            final Gamer targetGamer = this.getInstance().getManagerByClass(GamerManager.class).getGamerByPlayer(target);
            if (targetGamer == null) {
                return;
            }

            playerGamer.setCoins(playerGamer.getCoins() - amount);
            targetGamer.setCoins(targetGamer.getCoins() + amount);

            gamerManager.getRepository().updateData(playerGamer, GamerProperty.COINS);
            gamerManager.getRepository().updateData(targetGamer, GamerProperty.COINS);

            UtilServer.callEvent(new EconomySendEvent(player, target, amount));

            UtilMessage.simpleMessage(player, "Economy", "You have sent <gold><var> Coins</gold> to <yellow><var></yellow>.", Arrays.asList(UtilFormat.toDollar(amount), target.getName()));
            UtilMessage.simpleMessage(target, "Economy", "<yellow><var></yellow> has sent you <gold><var> Coins</gold>.", Arrays.asList(player.getName(), UtilFormat.toDollar(amount)));
        }
    }

    private static class SetCommand extends SubCommand<EconomyCommand> {

        public SetCommand(final EconomyCommand economyCommand) {
            super(economyCommand, "set", Rank.ADMIN);
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player> <amount>";
        }

        @Override
        public String getDescription() {
            return "Set Coins for a Player";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Economy", "You did not input a Player to Set.");
                return;
            }

            final Player target = UtilPlayer.searchPlayer(sender, args[0], true);
            if (target == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Economy", "You did not input an Amount to Set.");
                return;
            }

            final int amount = this.getModule().getManager().getAmount(sender, args[1], 0);
            if (amount == -1) {
                return;
            }

            final GamerManager gamerManager = this.getInstance().getManagerByClass(GamerManager.class);

            final Gamer targetGamer = gamerManager.getGamerByPlayer(target);
            if (targetGamer == null) {
                return;
            }

            targetGamer.setCoins(amount);

            gamerManager.getRepository().updateData(targetGamer, GamerProperty.COINS);

            UtilServer.callEvent(new EconomySetEvent(sender, target, amount));

            UtilMessage.simpleMessage(sender, "Economy", "You have updated <gold><var> Coins</gold> for <yellow><var></yellow>.", Arrays.asList(UtilFormat.toDollar(amount), target.getName()));
            UtilMessage.simpleMessage(target, "Economy", "<yellow><var></yellow> has updated you with <gold><var> Coins</gold>.", Arrays.asList(sender.getName(), UtilFormat.toDollar(amount)));
        }
    }

    private static class GiveCommand extends SubCommand<EconomyCommand> {

        public GiveCommand(final EconomyCommand economyCommand) {
            super(economyCommand, "give", Rank.ADMIN);
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player> <amount>";
        }

        @Override
        public String getDescription() {
            return "Give Coins to a Player";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Economy", "You did not input a Player to Give.");
                return;
            }

            final Player target = UtilPlayer.searchPlayer(sender, args[0], true);
            if (target == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Economy", "You did not input an Amount to Give.");
                return;
            }

            final int amount = this.getModule().getManager().getAmount(sender, args[1], 1);
            if (amount == -1) {
                return;
            }

            final GamerManager gamerManager = this.getInstance().getManagerByClass(GamerManager.class);

            final Gamer targetGamer = gamerManager.getGamerByPlayer(target);
            if (targetGamer == null) {
                return;
            }

            targetGamer.setCoins(targetGamer.getCoins() + amount);

            gamerManager.getRepository().updateData(targetGamer, GamerProperty.COINS);

            UtilServer.callEvent(new EconomyGiveEvent(sender, target, amount));

            UtilMessage.simpleMessage(sender, "Economy", "You have given <gold><var> Coins</gold> to <yellow><var></yellow>.", Arrays.asList(UtilFormat.toDollar(amount), target.getName()));
            UtilMessage.simpleMessage(target, "Economy", "<yellow><var></yellow> has given you <gold><var> Coins</gold>.", Arrays.asList(sender.getName(), UtilFormat.toDollar(amount)));
        }
    }

    private static class TakeCommand extends SubCommand<EconomyCommand> {

        public TakeCommand(final EconomyCommand economyCommand) {
            super(economyCommand, "take", Rank.ADMIN);
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <player> <amount>";
        }

        @Override
        public String getDescription() {
            return "Take Coins from a Player";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (args.length == 0) {
                UtilMessage.message(sender, "Economy", "You did not input a Player to Take.");
                return;
            }

            final Player target = UtilPlayer.searchPlayer(sender, args[0], true);
            if (target == null) {
                return;
            }

            if (args.length == 1) {
                UtilMessage.message(sender, "Economy", "You did not input an Amount to Take.");
                return;
            }

            final int amount = this.getModule().getManager().getAmount(sender, args[1], 1);
            if (amount == -1) {
                return;
            }

            final GamerManager gamerManager = this.getInstance().getManagerByClass(GamerManager.class);

            final Gamer targetGamer = gamerManager.getGamerByPlayer(target);
            if (targetGamer == null) {
                return;
            }

            targetGamer.setCoins(targetGamer.getCoins() - amount);

            gamerManager.getRepository().updateData(targetGamer, GamerProperty.COINS);

            UtilServer.callEvent(new EconomyTakeEvent(sender, target, amount));

            UtilMessage.simpleMessage(sender, "Economy", "You have taken <gold><var> Coins</gold> from <yellow><var></yellow>.", Arrays.asList(UtilFormat.toDollar(amount), target.getName()));
            UtilMessage.simpleMessage(target, "Economy", "<yellow><var></yellow> has taken <gold><var> Coins</gold> from you.", Arrays.asList(sender.getName(), UtilFormat.toDollar(amount)));
        }
    }
}