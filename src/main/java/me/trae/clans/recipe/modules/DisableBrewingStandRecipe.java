package me.trae.clans.recipe.modules;

import me.trae.core.recipe.RecipeManager;
import me.trae.core.recipe.modules.types.DisableRecipeModule;
import org.bukkit.Material;

public class DisableBrewingStandRecipe extends DisableRecipeModule {

    public DisableBrewingStandRecipe(final RecipeManager manager) {
        super(manager, Material.BREWING_STAND);
    }
}