package me.trae.clans.recipe.modules;

import me.trae.core.recipe.RecipeManager;
import me.trae.core.recipe.modules.types.DisableRecipeModule;
import org.bukkit.Material;

public class DisableGoldenCarrotRecipe extends DisableRecipeModule {

    public DisableGoldenCarrotRecipe(final RecipeManager manager) {
        super(manager, Material.GOLDEN_CARROT);
    }
}