package com.steveco.client

import com.steveco.SteveCOMod
import com.steveco.network.S2CUrgencySyncPayload
import com.steveco.registry.ModFluids
import com.steveco.urgency.ClientUrgencyData
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler
import net.minecraft.client.render.RenderLayer

object SteveCOClient : ClientModInitializer {
    override fun onInitializeClient() {
        SteveCOMod.logger.info("Steve Can Oshikko client initializing...")

        ClientPlayNetworking.registerGlobalReceiver(S2CUrgencySyncPayload.ID) { payload, _ ->
            ClientUrgencyData.update(payload.urgency)
        }

        // 流体レンダリング登録 (バニラ水テクスチャ + 黄色 tint)
        FluidRenderHandlerRegistry.INSTANCE.register(
            ModFluids.STILL_PEE,
            ModFluids.FLOWING_PEE,
            SimpleFluidRenderHandler.coloredWater(0xCCCC00)
        )

        BlockRenderLayerMap.INSTANCE.putFluids(
            RenderLayer.getTranslucent(),
            ModFluids.STILL_PEE,
            ModFluids.FLOWING_PEE
        )

        UrgencyHudOverlay.register()
    }
}
