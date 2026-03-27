package io.github.realguyman.totally_lit.mixin;

import io.github.realguyman.totally_lit.TotallyLit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, EquipmentSlot slot, CallbackInfo ci) {
        if (!TotallyLit.CONFIG.itemsCanExtinguishInPlayerInventory() || !entity.isAlwaysTicking()) {
            return;
        }

        Player player = (Player) entity;

        if (
                !world.isClientSide()
                && !player.isCreative()  // Do not extinguish items if in creative mode
                && !player.isSpectator() // Do not extinguish items if in spectator mode
                && player.tickCount % 20 == 0  // Only check once a second
                && player.isInWaterOrRain()
        ) {
            TotallyLit.JACK_O_LANTERN_MAP.forEach((lit, unlit) -> {
                        extinguish(
                                TotallyLit.CONFIG.jackOLanterns.extinguishInRainChance(),
                                lit, unlit, stack, player, slot, world
                        );
                    }
            );

            TotallyLit.LANTERN_MAP.forEach((lit, unlit) -> {
                        extinguish(
                                TotallyLit.CONFIG.lanterns.extinguishInRainChance(),
                                lit, unlit, stack, player, slot, world
                        );
                    }
            );

            TotallyLit.TORCH_MAP.forEach((lit, unlit) -> {
                extinguish(
                        TotallyLit.CONFIG.torches.extinguishInRainChance(),
                        lit, unlit, stack, player, slot, world
                    );
                }
            );
        }
    }

    @Unique
    private boolean shouldExtinguish(float chance, Block lit, Player player, Level world) {
        if (player.isUnderWater() || player.isSwimming()) {
            return true;
        }

        if (player.isInWater() && new Random().nextInt(100) == 0) {
            return true;
        }

        return player.tickCount % 940 == 0
                && world.isRainingAt(player.blockPosition())
                && world.getRandom().nextFloat() < chance
                && TotallyLit.TORCH_MAP.containsKey(lit);
    }

    @Unique
    private void extinguish(Float chance, Block lit, Block unlit, ItemStack stack, Player player, EquipmentSlot slot, Level world) {
        if (!shouldExtinguish(chance, lit, player, world)) {
            return;
        }

        if (player.getInventory().getItem(40).is(lit.asItem())) {
            player.getInventory().setItem(40, new ItemStack(unlit.asItem(), player.getInventory().getItem(40).getCount()));
        }

        if (player.getInventory().getItem(slot.getIndex()).is(lit.asItem())) {
            player.getInventory().setItem(slot.getIndex(), new ItemStack(unlit.asItem(), player.getInventory().getItem(slot.getIndex()).getCount()));
        }
    }
}
