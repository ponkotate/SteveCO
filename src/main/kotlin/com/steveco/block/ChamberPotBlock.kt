package com.steveco.block

import com.steveco.registry.ModSounds
import com.steveco.urgency.UrgencyManager
import com.mojang.serialization.MapCodec
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.ShapeContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.state.StateManager
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class ChamberPotBlock(settings: Settings) : HorizontalFacingBlock(settings) {
    override fun getCodec(): MapCodec<out HorizontalFacingBlock> = CODEC

    companion object {
        val CODEC: MapCodec<ChamberPotBlock> = createCodec(::ChamberPotBlock)

        // デフォルト: アヒルが南を向く (くちばしが +Z 方向)
        private fun buildShape(facing: Direction): VoxelShape {
            // 南向き基準の各パーツ座標を回転
            data class Box(
                val x1: Double, val y1: Double, val z1: Double,
                val x2: Double, val y2: Double, val z2: Double
            )

            val parts = listOf(
                Box(2.0, 0.0, 2.0, 14.0, 1.0, 14.0),   // ベース
                Box(3.0, 1.0, 3.0, 13.0, 7.0, 13.0),    // ボウル
                Box(5.0, 7.0, 11.0, 11.0, 10.0, 14.0),  // 首
                Box(5.0, 10.0, 10.0, 11.0, 15.0, 15.0), // 頭
                Box(6.0, 10.0, 15.0, 10.0, 13.0, 16.0), // くちばし
                Box(6.0, 6.0, 1.0, 10.0, 9.0, 3.0)      // しっぽ
            )

            fun rotateBox(box: Box, dir: Direction): Box {
                return when (dir) {
                    Direction.SOUTH -> box // デフォルト
                    Direction.NORTH -> Box(16.0 - box.x2, box.y1, 16.0 - box.z2, 16.0 - box.x1, box.y2, 16.0 - box.z1)
                    Direction.WEST -> Box(16.0 - box.z2, box.y1, box.x1, 16.0 - box.z1, box.y2, box.x2)
                    Direction.EAST -> Box(box.z1, box.y1, 16.0 - box.x2, box.z2, box.y2, 16.0 - box.x1)
                    else -> box
                }
            }

            return parts
                .map { rotateBox(it, facing) }
                .map { createCuboidShape(
                    minOf(it.x1, it.x2), minOf(it.y1, it.y2), minOf(it.z1, it.z2),
                    maxOf(it.x1, it.x2), maxOf(it.y1, it.y2), maxOf(it.z1, it.z2)
                ) }
                .reduce { acc, shape -> VoxelShapes.union(acc, shape) }
        }

        private val SHAPES: Map<Direction, VoxelShape> = mapOf(
            Direction.SOUTH to buildShape(Direction.SOUTH),
            Direction.NORTH to buildShape(Direction.NORTH),
            Direction.WEST to buildShape(Direction.WEST),
            Direction.EAST to buildShape(Direction.EAST)
        )
    }

    init {
        defaultState = stateManager.defaultState.with(FACING, Direction.SOUTH)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(FACING, ctx.horizontalPlayerFacing.opposite)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = SHAPES[state.get(FACING)] ?: SHAPES[Direction.SOUTH]!!

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult
    ): ActionResult {
        if (world.isClient) return ActionResult.SUCCESS

        val serverPlayer = player as? ServerPlayerEntity ?: return ActionResult.PASS
        val currentUrgency = UrgencyManager.getUrgency(serverPlayer)
        if (currentUrgency <= 0) return ActionResult.PASS

        UrgencyManager.setUrgency(serverPlayer, 0)
        UrgencyManager.syncToClient(serverPlayer)

        world.playSound(
            null, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
            ModSounds.CHAMBER_POT_USE, SoundCategory.BLOCKS,
            1.0f, 1.0f
        )

        return ActionResult.SUCCESS
    }
}
