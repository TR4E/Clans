package me.trae.clans.recipe.modules;

import me.trae.core.recipe.RecipeManager;
import me.trae.core.recipe.modules.types.DisableRecipeModule;
import org.bukkit.Material;

public class DisableStickyPistonRecipe extends DisableRecipeModule {

    public DisableStickyPistonRecipe(final RecipeManager manager) {
        super(manager, Material.PISTON_STICKY_BASE);
    }
}