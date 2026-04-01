package com.steveco.block

import com.steveco.registry.ModSounds
import com.steveco.urgency.UrgencyManager
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World

class ChamberPotBlock(settings: Settings) : Block(settings) {
    private val SHAPE: VoxelShape = VoxelShapes.union(
        createCuboidShape(2.0, 0.0, 2.0, 14.0, 1.0, 14.0),   // ベース
        createCuboidShape(3.0, 1.0, 3.0, 13.0, 7.0, 13.0),    // ボウル
        createCuboidShape(5.0, 7.0, 11.0, 11.0, 10.0, 14.0),  // 首
        createCuboidShape(5.0, 10.0, 10.0, 11.0, 15.0, 15.0), // 頭
        createCuboidShape(6.0, 10.0, 15.0, 10.0, 13.0, 16.0), // くちばし
        createCuboidShape(6.0, 6.0, 1.0, 10.0, 9.0, 3.0)      // しっぽ
    )

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = SHAPE
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
