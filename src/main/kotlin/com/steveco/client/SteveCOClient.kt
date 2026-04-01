package com.steveco.client

import com.steveco.SteveCOMod
import com.steveco.network.S2CUrgencySyncPayload
import com.steveco.registry.ModFluids
import com.steveco.urgency.ClientUrgencyData
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry
import net.minecraft.client.renderer.block.FluidModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier

object SteveCOClient : ClientModInitializer {
    override fun onInitializeClient() {
        SteveCOMod.logger.info("Steve Can Oshikko client initializing...")

        ClientPlayNetworking.registerGlobalReceiver(S2CUrgencySyncPayload.ID) { payload, _ ->
            ClientUrgencyData.update(payload.urgency)
        }

        // 流体レンダリング登録 (バニラ水テクスチャ + 黄色 tint)
        val peeModel = FluidModel.Unbaked(
            Material(Identifier.withDefaultNamespace("block/water_still"), true),
            Material(Identifier.withDefaultNamespace("block/water_flow"), true),
            null,
            { _ -> 0xCCCC00 }
        )
        FluidRenderingRegistry.register(
            ModFluids.STILL_PEE,
            ModFluids.FLOWING_PEE,
            peeModel
        )

        UrgencyHudOverlay.register()
    }
}
