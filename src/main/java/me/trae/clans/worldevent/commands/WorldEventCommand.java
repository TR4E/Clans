package me.trae.clans.worldevent.commands;

import me.trae.clans.worldevent.WorldEvent;
import me.trae.clans.worldevent.WorldEventManager;
import me.trae.core.command.subcommand.types.SubCommand;
import me.trae.core.command.types.Command;
import me.trae.core.utility.UtilMessage;
import me.trae.framework.shared.client.enums.Rank;
import org.bukkit.command.CommandSender;

public class WorldEventCommand extends Command<WorldEventManager> {

    public WorldEventCommand(final WorldEventManager manager) {
        super(manager, "worldevent", new String[]{"event"}, Rank.ADMIN);
    }

    @Override
    public void registerSubModules() {
        addSubModule(new StartCommand(this));
        addSubModule(new StopCommand(this));
    }

    @Override
    public String getDescription() {
        return "World Event Management";
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        this.executeSubCommand(sender, args);
    }

    private static class StartCommand extends SubCommand<WorldEventCommand> {

        public StartCommand(final WorldEventCommand worldEventCommand) {
            super(worldEventCommand, "start");
        }

        @Override
        public String getUsage() {
            return super.getUsage() + " <type>";
        }

        @Override
        public String getDescription() {
            return "Start a World Event";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            if (this.getModule().getManager().getActiveWorldEvent() != null) {
                UtilMessage.message(sender, "World Event", "There is already an event started.");
                return;
            }

            if (args.length == 0) {
                UtilMessage.message(sender, "World Event", "You did not input a Event to Start.");
                return;
            }

            final WorldEvent worldEvent = this.getModule().getManager().searchWorldEvent(sender, args[0], true);
            if (worldEvent == null) {
                return;
            }

            worldEvent.start();
        }
    }

    private static class StopCommand extends SubCommand<WorldEventCommand> {

        public StopCommand(final WorldEventCommand worldEventCommand) {
            super(worldEventCommand, "stop");
        }

        @Override
        public String getDescription() {
            return "Stop the World Event";
        }

        @Override
        public void execute(final CommandSender sender, final String[] args) {
            final WorldEvent worldEvent = this.getModule().getManager().getActiveWorldEvent();
            if (worldEvent == null) {
                UtilMessage.message(sender, "World Event", "There is currently no event to stop.");
                return;
            }

            worldEvent.stop();
        }
    }
}