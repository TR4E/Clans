package me.trae.clans.economy;

import me.trae.clans.economy.commands.EconomyCommand;
import me.trae.clans.economy.interfaces.IEconomyManager;
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
        addModule(new EconomyCommand(this));
    }

    @Override
    public int getAmount(final CommandSender sender, final String string) {
        final Integer amount = UtilInput.getInput(Integer.class, string);
        if (amount == null) {
            UtilMessage.message(sender, "Economy", "You did not input a valid Amount.");
            return 0;
        }

        if (amount <= 0) {
            UtilMessage.message(sender, "Economy", "The Amount must be greater than Zero.");
            return 0;
        }

        return amount;
    }
}