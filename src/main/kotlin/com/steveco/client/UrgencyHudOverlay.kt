package com.steveco.client

import com.steveco.SteveCOMod
import com.steveco.urgency.ClientUrgencyData
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier

object UrgencyHudOverlay : HudElement {
    private const val ICON_SIZE = 16

    private val ID = Identifier.fromNamespaceAndPath(SteveCOMod.MOD_ID, "urgency_hud")

    private val FACE_TEXTURES = arrayOf(
        Identifier.fromNamespaceAndPath(SteveCOMod.MOD_ID, "textures/gui/face_normal.png"),
        Identifier.fromNamespaceAndPath(SteveCOMod.MOD_ID, "textures/gui/face_mild.png"),
        Identifier.fromNamespaceAndPath(SteveCOMod.MOD_ID, "textures/gui/face_moderate.png"),
        Identifier.fromNamespaceAndPath(SteveCOMod.MOD_ID, "textures/gui/face_severe.png"),
        Identifier.fromNamespaceAndPath(SteveCOMod.MOD_ID, "textures/gui/face_critical.png"),
    )

    fun register() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.FOOD_BAR, ID, this)
    }

    override fun extractRenderState(graphics: GuiGraphicsExtractor, deltaTracker: DeltaTracker) {
        val client = Minecraft.getInstance()
        val player = client.player ?: return
        if (client.options.hideGui || player.isCreative || player.isSpectator) return

        val urgency = ClientUrgencyData.urgency
        val stage = when {
            urgency >= 100 -> 4
            urgency >= 75 -> 3
            urgency >= 50 -> 2
            urgency >= 25 -> 1
            else -> 0
        }

        val screenWidth = graphics.guiWidth()
        val x = screenWidth / 2 + 95
        val y = graphics.guiHeight() - 39

        graphics.blit(
            RenderPipelines.GUI_TEXTURED,
            FACE_TEXTURES[stage],
            x, y,
            0f, 0f,
            ICON_SIZE, ICON_SIZE,
            ICON_SIZE, ICON_SIZE
        )
    }
}
