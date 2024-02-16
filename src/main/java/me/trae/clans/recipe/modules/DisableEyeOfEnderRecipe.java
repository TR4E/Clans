package me.trae.clans.recipe.modules;

import me.trae.core.recipe.RecipeManager;
import me.trae.core.recipe.modules.types.DisableRecipeModule;
import org.bukkit.Material;

public class DisableEyeOfEnderRecipe extends DisableRecipeModule {

    public DisableEyeOfEnderRecipe(final RecipeManager manager) {
        super(manager, Material.EYE_OF_ENDER);
    }
}