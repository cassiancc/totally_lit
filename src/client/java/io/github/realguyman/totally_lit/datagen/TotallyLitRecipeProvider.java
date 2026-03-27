package io.github.realguyman.totally_lit.datagen;

import io.github.realguyman.totally_lit.registry.ItemRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import java.util.concurrent.CompletableFuture;

public class TotallyLitRecipeProvider extends FabricRecipeProvider {
    public TotallyLitRecipeProvider(
            FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> future
    ) {
        super(output, future);
    }

    @Override
    protected RecipeProvider createRecipeProvider(
            HolderLookup.Provider lookup,
            RecipeOutput exporter
    ) {
        return new RecipeProvider(lookup, exporter) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.DECORATIONS, ItemRegistry.UNLIT_TORCH, 4)
                        .pattern("#")
                        .pattern("|")
                        .input('#', ItemTags.COALS)
                        .input('|', Items.STICK)
                        .group("multi_bench")
                        .criterion(getHasName(Items.COAL), has(Items.COAL))
                        .offerTo(output);

                shaped(RecipeCategory.DECORATIONS, ItemRegistry.UNLIT_SOUL_TORCH, 4)
                        .pattern("#")
                        .pattern("|")
                        .pattern("s")
                        .input('#', ItemTags.COALS)
                        .input('|', Items.STICK)
                        .input('s', ItemTags.SOUL_FIRE_BASE_BLOCKS)
                        .group("multi_bench")
                        .criterion(getHasName(Items.SOUL_SAND), has(Items.SOUL_SAND))
                        .offerTo(output);

                shaped(RecipeCategory.DECORATIONS, ItemRegistry.GLOWSTONE_TORCH, 4)
                        .pattern("#")
                        .pattern("|")
                        .input('#', ConventionalItemTags.GLOWSTONE_DUSTS)
                        .input('|', Items.STICK)
                        .group("multi_bench")
                        .criterion(getHasName(Items.GLOWSTONE_DUST), has(Items.GLOWSTONE_DUST))
                        .offerTo(output);

                shaped(RecipeCategory.DECORATIONS, ItemRegistry.UNLIT_LANTERN)
                        .pattern("nnn")
                        .pattern("ntn")
                        .pattern("nnn")
                        .input('n', ConventionalItemTags.IRON_NUGGETS)
                        .input('t', ItemRegistry.UNLIT_TORCH)
                        .group("multi_bench")
                        .criterion(getHasName(ItemRegistry.UNLIT_TORCH), has(ItemRegistry.UNLIT_TORCH))
                        .offerTo(output);

                shaped(RecipeCategory.DECORATIONS, ItemRegistry.UNLIT_SOUL_LANTERN)
                        .pattern("nnn")
                        .pattern("ntn")
                        .pattern("nnn")
                        .input('n', ConventionalItemTags.IRON_NUGGETS)
                        .input('t', ItemRegistry.UNLIT_SOUL_TORCH)
                        .group("multi_bench")
                        .criterion(getHasName(ItemRegistry.UNLIT_SOUL_TORCH), has(ItemRegistry.UNLIT_SOUL_TORCH))
                        .offerTo(output);

                shaped(RecipeCategory.DECORATIONS, ItemRegistry.GLOWSTONE_LANTERN)
                        .pattern("nnn")
                        .pattern("ntn")
                        .pattern("nnn")
                        .input('n', ConventionalItemTags.IRON_NUGGETS)
                        .input('t', ItemRegistry.GLOWSTONE_TORCH)
                        .group("multi_bench")
                        .criterion(getHasName(ItemRegistry.GLOWSTONE_TORCH), has(ItemRegistry.GLOWSTONE_TORCH))
                        .offerTo(output);

                shaped(RecipeCategory.DECORATIONS, ItemRegistry.UNLIT_JACK_O_LANTERN)
                        .pattern("p")
                        .pattern("t")
                        .input('p', Items.CARVED_PUMPKIN)
                        .input('t', ItemRegistry.UNLIT_TORCH)
                        .group("multi_bench")
                        .criterion(getHasName(ItemRegistry.UNLIT_TORCH), has(ItemRegistry.UNLIT_TORCH))
                        .offerTo(output);

                simpleCookingRecipe(
                        "campfire_cooking",
                        RecipeSerializer.CAMPFIRE_COOKING_RECIPE,
                        CampfireCookingRecipe::new,
                        20,
                        ItemRegistry.UNLIT_TORCH,
                        Items.TORCH,
                        0
                );

                simpleCookingRecipe(
                        "campfire_cooking",
                        RecipeSerializer.CAMPFIRE_COOKING_RECIPE,
                        CampfireCookingRecipe::new,
                        20,
                        ItemRegistry.UNLIT_SOUL_TORCH,
                        Items.SOUL_TORCH,
                        0
                );
            }
        };
    }

    @Override
    public String getName() {
        return "TotallyLitRecipeProvider";
    }
}
