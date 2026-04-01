package com.steveco.block

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.FluidBlock
import net.minecraft.entity.Entity
import net.minecraft.fluid.FlowableFluid
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class PeeFluidBlock(fluid: FlowableFluid, settings: Settings) : FluidBlock(fluid, settings) {
    companion object {
        private const val DESPAWN_TICKS = 1200 // 60秒
        private const val PUSH_STRENGTH = 0.014 // 水と同じ押し出し強度
    }

    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        val fluidState = world.getFluidState(pos)
        val velocity = fluidState.getVelocity(world, pos)
        if (velocity != Vec3d.ZERO) {
            entity.addVelocity(
                velocity.x * PUSH_STRENGTH,
                velocity.y * PUSH_STRENGTH,
                velocity.z * PUSH_STRENGTH
            )
        }
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        super.onBlockAdded(state, world, pos, oldState, notify)
        // source ブロック (level=0) のみ消滅タイマーを設定
        if (state.isOf(this) && state.get(LEVEL) == 0) {
            world.scheduleBlockTick(pos, this, DESPAWN_TICKS)
        }
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        // source ブロックを除去 → 流れブロックは自然消失
        if (state.isOf(this) && state.get(LEVEL) == 0) {
            world.setBlockState(pos, Blocks.AIR.defaultState, NOTIFY_ALL)
        }
    }
}
