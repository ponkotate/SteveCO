package com.steveco.network

import com.steveco.SteveCOMod
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

data class S2CUrgencySyncPayload(val urgency: Int) : CustomPayload {
    override fun getId(): CustomPayload.Id<S2CUrgencySyncPayload> = ID

    companion object {
        val ID = CustomPayload.Id<S2CUrgencySyncPayload>(
            Identifier.of(SteveCOMod.MOD_ID, "urgency_sync")
        )
        val CODEC: PacketCodec<RegistryByteBuf, S2CUrgencySyncPayload> = PacketCodec.tuple(
            PacketCodecs.VAR_INT, S2CUrgencySyncPayload::urgency,
            ::S2CUrgencySyncPayload
        )
    }
}
