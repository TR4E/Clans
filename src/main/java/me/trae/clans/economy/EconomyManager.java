package me.trae.clans.economy;

import me.trae.clans.economy.commands.EconomyCommand;
import me.trae.clans.economy.interfaces.IEconomyManager;
import me.trae.clans.economy.modules.HandleScoreboardUpdate;
import me.trae.core.framework.SpigotManager;
import me.trae.core.framework.SpigotPlugin;
import me.trae.core.utility.UtilMessage;
import me.trae.framework.shared.utility.UtilInput;
import org.bukkit.command.CommandSender;

public class EconomyManager extends SpigotManager implements IEconomyManager {

    public EconomyManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        // Commands
        addModule(new EconomyCommand(this));

        // Modules
        addModule(new HandleScoreboardUpdate(this));
    }

    @Override
    public int getAmount(final CommandSender sender, final String string, final int minimumAmount) {
        final Integer amount = UtilInput.getInput(Integer.class, string);
        if (amount == null) {
            UtilMessage.message(sender, "Economy", "You did not input a valid Amount.");
            return -1;
        }

        if (amount < minimumAmount) {
            UtilMessage.message(sender, "Economy", String.format("The Amount cannot be less than %s.", minimumAmount));
            return -1;
        }

        return amount;
    }
}