package io.github.realguyman.totally_lit.datagen;

import io.github.realguyman.totally_lit.registry.TagRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import java.util.concurrent.CompletableFuture;

public class TotallyLitItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public TotallyLitItemTagProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> completableFuture
    ) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        valueLookupBuilder(TagRegistry.CAMPFIRE_IGNITER_ITEMS).add(
                Items.TORCH,
                Items.SOUL_TORCH,
                Items.LAVA_BUCKET,
                Items.MAGMA_BLOCK
        );

        valueLookupBuilder(TagRegistry.JACK_O_LANTERN_IGNITER_ITEMS).add(
                Items.TORCH,
                Items.SOUL_TORCH,
                Items.FLINT_AND_STEEL
        );

        valueLookupBuilder(TagRegistry.LANTERN_IGNITER_ITEMS).add(
                Items.TORCH,
                Items.SOUL_TORCH,
                Items.FLINT_AND_STEEL
        );

        valueLookupBuilder(TagRegistry.TORCH_IGNITER_ITEMS).add(
                Items.TORCH,
                Items.SOUL_TORCH,
                Items.LANTERN,
                Items.SOUL_LANTERN,
                Items.LAVA_BUCKET,
                Items.MAGMA_BLOCK,
                Items.FLINT_AND_STEEL
        );

        valueLookupBuilder(TagRegistry.SOUL_FIRE_VARIANT_ITEMS).add(
                Items.SOUL_TORCH,
                Items.SOUL_LANTERN,
                Items.SOUL_CAMPFIRE
        );
    }
}
