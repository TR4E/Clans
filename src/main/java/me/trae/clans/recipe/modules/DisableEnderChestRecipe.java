package me.trae.clans.recipe.modules;

import me.trae.core.recipe.RecipeManager;
import me.trae.core.recipe.modules.types.DisableRecipeModule;
import org.bukkit.Material;

public class DisableEnderChestRecipe extends DisableRecipeModule {

    public DisableEnderChestRecipe(final RecipeManager manager) {
        super(manager, Material.ENDER_CHEST);
    }
}