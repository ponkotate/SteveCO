package com.steveco.block

import com.steveco.registry.ModSounds
import com.steveco.urgency.UrgencyManager
import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class ChamberPotBlock(settings: Properties) : HorizontalDirectionalBlock(settings) {
    override fun codec(): MapCodec<out HorizontalDirectionalBlock> = CODEC

    companion object {
        val CODEC: MapCodec<ChamberPotBlock> = simpleCodec(::ChamberPotBlock)

        // デフォルト: アヒルが南を向く (くちばしが +Z 方向)
        private fun buildShape(facing: Direction): VoxelShape {
            data class Box(
                val x1: Double, val y1: Double, val z1: Double,
                val x2: Double, val y2: Double, val z2: Double
            )

            val parts = listOf(
                Box(2.0, 0.0, 2.0, 14.0, 1.0, 14.0),
                Box(3.0, 1.0, 3.0, 13.0, 7.0, 13.0),
                Box(5.0, 7.0, 11.0, 11.0, 10.0, 14.0),
                Box(5.0, 10.0, 10.0, 11.0, 15.0, 15.0),
                Box(6.0, 10.0, 15.0, 10.0, 13.0, 16.0),
                Box(6.0, 6.0, 1.0, 10.0, 9.0, 3.0)
            )

            fun rotateBox(box: Box, dir: Direction): Box {
                return when (dir) {
                    Direction.SOUTH -> box
                    Direction.NORTH -> Box(16.0 - box.x2, box.y1, 16.0 - box.z2, 16.0 - box.x1, box.y2, 16.0 - box.z1)
                    Direction.WEST -> Box(16.0 - box.z2, box.y1, box.x1, 16.0 - box.z1, box.y2, box.x2)
                    Direction.EAST -> Box(box.z1, box.y1, 16.0 - box.x2, box.z2, box.y2, 16.0 - box.x1)
                    else -> box
                }
            }

            return parts
                .map { rotateBox(it, facing) }
                .map { box(
                    minOf(it.x1, it.x2), minOf(it.y1, it.y2), minOf(it.z1, it.z2),
                    maxOf(it.x1, it.x2), maxOf(it.y1, it.y2), maxOf(it.z1, it.z2)
                ) }
                .reduce { acc, shape -> Shapes.or(acc, shape) }
        }

        private val SHAPES: Map<Direction, VoxelShape> = mapOf(
            Direction.SOUTH to buildShape(Direction.SOUTH),
            Direction.NORTH to buildShape(Direction.NORTH),
            Direction.WEST to buildShape(Direction.WEST),
            Direction.EAST to buildShape(Direction.EAST)
        )
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.SOUTH))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(FACING, ctx.horizontalDirection.opposite)
    }

    override fun getShape(
        state: BlockState,
        world: BlockGetter,
        pos: BlockPos,
        context: CollisionContext
    ): VoxelShape = SHAPES[state.getValue(FACING)] ?: SHAPES[Direction.SOUTH]!!

    override fun useWithoutItem(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        player: Player,
        hit: BlockHitResult
    ): InteractionResult {
        if (world.isClientSide) return InteractionResult.SUCCESS

        val serverPlayer = player as? ServerPlayer ?: return InteractionResult.PASS
        val currentUrgency = UrgencyManager.getUrgency(serverPlayer)
        if (currentUrgency <= 0) return InteractionResult.PASS

        UrgencyManager.setUrgency(serverPlayer, 0)
        UrgencyManager.syncToClient(serverPlayer)

        world.playSound(
            null, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
            ModSounds.CHAMBER_POT_USE, SoundSource.BLOCKS,
            1.0f, 1.0f
        )

        return InteractionResult.SUCCESS
    }
}
