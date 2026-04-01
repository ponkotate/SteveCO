package com.steveco.client

import com.steveco.SteveCOMod
import com.steveco.urgency.ClientUrgencyData
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.util.Identifier

object UrgencyHudOverlay : HudRenderCallback {
    private const val ICON_SIZE = 16

    private val FACE_TEXTURES = arrayOf(
        Identifier.of(SteveCOMod.MOD_ID, "textures/gui/face_normal.png"),
        Identifier.of(SteveCOMod.MOD_ID, "textures/gui/face_mild.png"),
        Identifier.of(SteveCOMod.MOD_ID, "textures/gui/face_moderate.png"),
        Identifier.of(SteveCOMod.MOD_ID, "textures/gui/face_severe.png"),
        Identifier.of(SteveCOMod.MOD_ID, "textures/gui/face_critical.png"),
    )

    fun register() {
        HudRenderCallback.EVENT.register(this)
    }

    override fun onHudRender(drawContext: DrawContext, tickCounter: RenderTickCounter) {
        val client = net.minecraft.client.MinecraftClient.getInstance()
        val player = client.player ?: return
        if (client.options.hudHidden || player.isCreative || player.isSpectator) return

        val urgency = ClientUrgencyData.urgency
        val stage = when {
            urgency >= 100 -> 4
            urgency >= 75 -> 3
            urgency >= 50 -> 2
            urgency >= 25 -> 1
            else -> 0
        }

        val screenWidth = drawContext.scaledWindowWidth
        val x = screenWidth / 2 + 95
        val y = drawContext.scaledWindowHeight - 39

        drawContext.drawTexture(
            RenderLayer::getGuiTextured,
            FACE_TEXTURES[stage],
            x, y,
            0f, 0f,
            ICON_SIZE, ICON_SIZE,
            ICON_SIZE, ICON_SIZE
        )
    }
}
