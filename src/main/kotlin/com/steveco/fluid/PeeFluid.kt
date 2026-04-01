package com.steveco.fluid

import com.steveco.registry.ModBlocks
import com.steveco.registry.ModFluids
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FluidBlock
import net.minecraft.fluid.FlowableFluid
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.FluidState
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.particle.ParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.BlockView
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView

abstract class PeeFluid : FlowableFluid() {

    override fun getFlowing(): Fluid = ModFluids.FLOWING_PEE

    override fun getStill(): Fluid = ModFluids.STILL_PEE

    override fun getBucketItem(): Item = Items.AIR

    override fun canBeReplacedWith(
        state: FluidState,
        world: BlockView,
        pos: BlockPos,
        fluid: Fluid,
        direction: Direction
    ): Boolean {
        return direction == Direction.DOWN && !matchesType(fluid)
    }

    override fun getTickRate(world: WorldView): Int = 5

    override fun getBlastResistance(): Float = 100.0f

    override fun toBlockState(state: FluidState): BlockState {
        return ModBlocks.PEE_BLOCK.defaultState.with(FluidBlock.LEVEL, getBlockStateLevel(state))
    }

    override fun matchesType(fluid: Fluid): Boolean {
        return fluid === ModFluids.STILL_PEE || fluid === ModFluids.FLOWING_PEE
    }

    override fun isInfinite(world: ServerWorld): Boolean = false

    override fun beforeBreakingBlock(world: WorldAccess, pos: BlockPos, state: BlockState) {
        val blockEntity = if (state.hasBlockEntity()) world.getBlockEntity(pos) else null
        Block.dropStacks(state, world, pos, blockEntity)
    }

    override fun getMaxFlowDistance(world: WorldView): Int = 3

    override fun getLevelDecreasePerBlock(world: WorldView): Int = 1

    override fun getParticle(): ParticleEffect? = null

    class Still : PeeFluid() {
        override fun getLevel(state: FluidState): Int = 8

        override fun isStill(state: FluidState): Boolean = true
    }

    class Flowing : PeeFluid() {
        override fun appendProperties(builder: StateManager.Builder<Fluid, FluidState>) {
            super.appendProperties(builder)
            builder.add(LEVEL)
        }

        override fun getLevel(state: FluidState): Int = state.get(LEVEL)

        override fun isStill(state: FluidState): Boolean = false
    }
}
