package com.steveco.fluid

import com.steveco.registry.ModBlocks
import com.steveco.registry.ModFluids
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState

abstract class PeeFluid : FlowingFluid() {

    override fun getFlowing(): Fluid = ModFluids.FLOWING_PEE

    override fun getSource(): Fluid = ModFluids.STILL_PEE

    override fun getBucket(): Item = Items.AIR

    override fun canBeReplacedWith(
        state: FluidState,
        world: BlockGetter,
        pos: BlockPos,
        fluid: Fluid,
        direction: Direction
    ): Boolean {
        return direction == Direction.DOWN && !isSame(fluid)
    }

    override fun getTickDelay(world: LevelReader): Int = 5

    override fun getExplosionResistance(): Float = 100.0f

    override fun createLegacyBlock(state: FluidState): BlockState {
        return ModBlocks.PEE_BLOCK.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state))
    }

    override fun isSame(fluid: Fluid): Boolean {
        return fluid === ModFluids.STILL_PEE || fluid === ModFluids.FLOWING_PEE
    }

    override fun canConvertToSource(world: ServerLevel): Boolean = false

    override fun beforeDestroyingBlock(world: LevelAccessor, pos: BlockPos, state: BlockState) {
        val blockEntity = if (state.hasBlockEntity()) world.getBlockEntity(pos) else null
        Block.dropResources(state, world, pos, blockEntity)
    }

    override fun getSlopeFindDistance(world: LevelReader): Int = 3

    override fun getDropOff(world: LevelReader): Int = 1

    override fun getDripParticle(): ParticleOptions? = null

    class Still : PeeFluid() {
        override fun getAmount(state: FluidState): Int = 8

        override fun isSource(state: FluidState): Boolean = true
    }

    class Flowing : PeeFluid() {
        override fun createFluidStateDefinition(builder: StateDefinition.Builder<Fluid, FluidState>) {
            super.createFluidStateDefinition(builder)
            builder.add(LEVEL)
        }

        override fun getAmount(state: FluidState): Int = state.getValue(LEVEL)

        override fun isSource(state: FluidState): Boolean = false
    }
}
