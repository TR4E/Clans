package me.trae.clans.recipe;

import me.trae.clans.recipe.modules.*;
import me.trae.core.framework.SpigotPlugin;

public class RecipeManager extends me.trae.core.recipe.RecipeManager {

    public RecipeManager(final SpigotPlugin instance) {
        super(instance);
    }

    @Override
    public void registerModules() {
        addModule(new DisableAnvilRecipe(this));
        addModule(new DisableBrewingStandRecipe(this));
        addModule(new DisableCompassRecipe(this));
        addModule(new DisableDispenserRecipe(this));
        addModule(new DisableEnchantmentTableRecipe(this));
        addModule(new DisableEnderChestRecipe(this));
        addModule(new DisableEnderPearlRecipe(this));
        addModule(new DisableEyeOfEnderRecipe(this));
        addModule(new DisableGoldenAppleRecipe(this));
        addModule(new DisableGoldenCarrotRecipe(this));
        addModule(new DisablePistonRecipe(this));
        addModule(new DisableStickyPistonRecipe(this));
        addModule(new DisableTntRecipe(this));
    }
}