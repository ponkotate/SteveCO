package com.steveco.client

import com.steveco.SteveCOMod
import com.steveco.network.S2CUrgencySyncPayload
import com.steveco.urgency.ClientUrgencyData
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object SteveCOClient : ClientModInitializer {
    override fun onInitializeClient() {
        SteveCOMod.logger.info("Steve Can Oshikko client initializing...")

        ClientPlayNetworking.registerGlobalReceiver(S2CUrgencySyncPayload.ID) { payload, _ ->
            ClientUrgencyData.update(payload.urgency)
        }
    }
}
