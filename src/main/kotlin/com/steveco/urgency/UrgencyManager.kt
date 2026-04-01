package com.steveco.urgency

import com.steveco.SteveCOMod
import com.steveco.network.S2CUrgencySyncPayload
import com.steveco.registry.ModBlocks
import com.steveco.registry.ModSounds
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import java.util.UUID

object UrgencyManager {
    const val MAX_URGENCY = 100
    private const val TICK_INTERVAL = 200 // 10秒 = 200 tick
    private const val SLOWNESS_THRESHOLD = 75

    val URGENCY: AttachmentType<Int> = AttachmentRegistry.createDefaulted(
        Identifier.of(SteveCOMod.MOD_ID, "urgency"),
        { 0 }
    )

    private val tickCounters = mutableMapOf<UUID, Int>()

    fun register() {
        ServerTickEvents.END_SERVER_TICK.register { server ->
            for (player in server.playerManager.playerList) {
                tick(player)
            }
        }

        ServerPlayerEvents.AFTER_RESPAWN.register { _, newPlayer, _ ->
            setUrgency(newPlayer, 0)
            syncToClient(newPlayer)
        }

        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            onPlayerDisconnect(handler.player.uuid)
        }
    }

    private fun tick(player: ServerPlayerEntity) {
        val uuid = player.uuid
        val counter = (tickCounters[uuid] ?: 0) + 1

        if (counter >= TICK_INTERVAL) {
            tickCounters[uuid] = 0
            val current = getUrgency(player)
            if (current < MAX_URGENCY) {
                val newValue = (current + 1).coerceAtMost(MAX_URGENCY)
                setUrgency(player, newValue)

                if (newValue >= MAX_URGENCY) {
                    onUrgencyMax(player)
                } else {
                    applyEffects(player, newValue)
                }
                syncToClient(player)
            }
        } else {
            tickCounters[uuid] = counter
        }
    }

    fun getUrgency(player: ServerPlayerEntity): Int {
        return player.getAttachedOrElse(URGENCY, 0).coerceIn(0, MAX_URGENCY)
    }

    fun setUrgency(player: ServerPlayerEntity, value: Int) {
        player.setAttached(URGENCY, value.coerceIn(0, MAX_URGENCY))
    }

    private fun onUrgencyMax(player: ServerPlayerEntity) {
        val world = player.serverWorld
        val feetPos = BlockPos.ofFloored(player.x, player.y, player.z)

        // 足元にPeeBlock設置
        if (world.getBlockState(feetPos).isAir) {
            world.setBlockState(feetPos, ModBlocks.PEE_BLOCK.defaultState)
        }

        // Slowness II を 5秒間付与
        player.addStatusEffect(
            StatusEffectInstance(
                StatusEffects.SLOWNESS,
                100, // 5秒 = 100 tick
                1,   // amplifier 1 = Slowness II
                false,
                true,
                true
            )
        )

        // パーティクル表示
        world.spawnParticles(
            ParticleTypes.SPLASH,
            player.x, player.y + 0.5, player.z,
            20, 0.3, 0.2, 0.3, 0.05
        )

        // サウンド再生
        world.playSound(
            null, player.x, player.y, player.z,
            ModSounds.PEE_RELEASE, SoundCategory.PLAYERS,
            1.0f, 1.0f
        )

        // 尿意リセット
        setUrgency(player, 0)
    }

    private fun applyEffects(player: ServerPlayerEntity, urgency: Int) {
        if (urgency in SLOWNESS_THRESHOLD until MAX_URGENCY) {
            player.addStatusEffect(
                StatusEffectInstance(
                    StatusEffects.SLOWNESS,
                    TICK_INTERVAL + 20, // 次の更新まで + 1秒の余裕
                    0, // amplifier 0 = Slowness I
                    false,
                    false,
                    true
                )
            )
        }
    }

    fun syncToClient(player: ServerPlayerEntity) {
        ServerPlayNetworking.send(player, S2CUrgencySyncPayload(getUrgency(player)))
    }

    fun onPlayerDisconnect(uuid: UUID) {
        tickCounters.remove(uuid)
    }
}
