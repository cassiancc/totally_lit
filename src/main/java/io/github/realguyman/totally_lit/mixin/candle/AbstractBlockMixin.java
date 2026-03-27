package io.github.realguyman.totally_lit.mixin.candle;

import io.github.realguyman.totally_lit.TotallyLit;
import io.github.realguyman.totally_lit.registry.TagRegistry;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.ticks.LevelTicks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class AbstractBlockMixin {
    @Shadow protected abstract void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random);

    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    private void canSchedule(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void schedule(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!AbstractCandleBlock.isLit(state)) {
            return;
        }

        boolean raining = world.isRainingAt(pos.above());
        boolean chanceInFavor = random.nextFloat() < TotallyLit.CONFIG.candles.extinguishInRainChance();
        boolean waterlogged = false;

        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            waterlogged = state.getValue(BlockStateProperties.WATERLOGGED);
        }

        if ((raining && chanceInFavor) || waterlogged) {
            this.tick(state, world, pos, random);
        } else if (TotallyLit.CONFIG.candles.extinguishOverTime()) {
            LevelTicks<Block> scheduler = world.getBlockTicks();
            Block block = state.getBlock();

            if (!scheduler.hasScheduledTick(pos, block) && !scheduler.willTickThisTick(pos, block)) {
                world.scheduleTick(pos, block, TotallyLit.CONFIG.candles.burnDuration());
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void extinguish(BlockState state, ServerLevel world, BlockPos pos, RandomSource random, CallbackInfo ci) {
        var caretakers = world.getEntitiesOfClass(
                Entity.class,
                new AABB(pos).inflate(TotallyLit.CONFIG.caretakerCheckRadius()),
                EntitySelector.LIVING_ENTITY_STILL_ALIVE
        ).stream().filter(entity -> entity.getType().isIn(TagRegistry.CARETAKERS)).toList();

        if (AbstractCandleBlock.isLit(state) && caretakers.isEmpty()) {
            AbstractCandleBlock.extinguish(null, state, world, pos);
        }
    }
}
