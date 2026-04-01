package com.steveco.network

import com.steveco.SteveCOMod
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

data class S2CUrgencySyncPayload(val urgency: Int) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<S2CUrgencySyncPayload> = ID

    companion object {
        val ID = CustomPacketPayload.Type<S2CUrgencySyncPayload>(
            Identifier.fromNamespaceAndPath(SteveCOMod.MOD_ID, "urgency_sync")
        )
        val CODEC: StreamCodec<RegistryFriendlyByteBuf, S2CUrgencySyncPayload> = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, S2CUrgencySyncPayload::urgency,
            ::S2CUrgencySyncPayload
        )
    }
}
