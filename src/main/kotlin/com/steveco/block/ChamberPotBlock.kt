package com.steveco.block

import com.steveco.registry.ModSounds
import com.steveco.urgency.UrgencyManager
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ChamberPotBlock(settings: Settings) : Block(settings) {
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
