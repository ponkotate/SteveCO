package com.steveco.network

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry

object ModNetworking {
    fun registerS2CPayloads() {
        PayloadTypeRegistry.playS2C().register(S2CUrgencySyncPayload.ID, S2CUrgencySyncPayload.CODEC)
    }
}
