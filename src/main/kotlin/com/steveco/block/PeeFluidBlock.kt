package com.steveco.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.InsideBlockEffectApplier
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.phys.Vec3

class PeeFluidBlock(fluid: FlowingFluid, settings: Properties) : LiquidBlock(fluid, settings) {
    companion object {
        private const val DESPAWN_TICKS = 1200 // 60秒
        private const val PUSH_STRENGTH = 0.014 // 水と同じ押し出し強度
    }

    override fun entityInside(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        entity: Entity,
        effectApplier: InsideBlockEffectApplier,
        isPrecise: Boolean
    ) {
        val fluidState = world.getFluidState(pos)
        val velocity = fluidState.getFlow(world, pos)
        if (velocity != Vec3.ZERO) {
            entity.push(
                velocity.x * PUSH_STRENGTH,
                velocity.y * PUSH_STRENGTH,
                velocity.z * PUSH_STRENGTH
            )
        }
    }

    override fun onPlace(state: BlockState, world: Level, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        super.onPlace(state, world, pos, oldState, notify)
        // source ブロック (level=0) のみ消滅タイマーを設定
        if (state.`is`(this) && state.getValue(LEVEL) == 0) {
            world.scheduleTick(pos, this, DESPAWN_TICKS)
        }
    }

    override fun tick(state: BlockState, world: ServerLevel, pos: BlockPos, random: RandomSource) {
        // source ブロックを除去 → 流れブロックは自然消失
        if (state.`is`(this) && state.getValue(LEVEL) == 0) {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), UPDATE_ALL)
        }
    }
}
