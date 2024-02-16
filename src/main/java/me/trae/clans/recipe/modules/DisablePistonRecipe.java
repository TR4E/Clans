package me.trae.clans.recipe.modules;

import me.trae.core.recipe.RecipeManager;
import me.trae.core.recipe.modules.types.DisableRecipeModule;
import org.bukkit.Material;

public class DisablePistonRecipe extends DisableRecipeModule {

    public DisablePistonRecipe(final RecipeManager manager) {
        super(manager, Material.PISTON_BASE);
    }
}