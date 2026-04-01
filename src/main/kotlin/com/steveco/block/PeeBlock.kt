package com.steveco.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.random.Random
import net.minecraft.world.World

class PeeBlock(settings: Settings) : Block(settings) {
    companion object {
        private const val DESPAWN_TICKS = 1200 // 60秒
    }

    override fun onBlockAdded(state: BlockState, world: World, pos: BlockPos, oldState: BlockState, notify: Boolean) {
        super.onBlockAdded(state, world, pos, oldState, notify)
        world.scheduleBlockTick(pos, this, DESPAWN_TICKS)
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos, random: Random) {
        if (state.isOf(this)) {
            world.removeBlock(pos, false)
        }
    }
}
