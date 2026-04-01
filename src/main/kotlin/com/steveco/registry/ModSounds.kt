package com.steveco.registry

import com.steveco.SteveCOMod
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundEvent

object ModSounds {
    val PEE_RELEASE: SoundEvent = register("pee_release")
    val CHAMBER_POT_USE: SoundEvent = register("chamber_pot_use")

    private fun register(name: String): SoundEvent {
        val id = Identifier.fromNamespaceAndPath(SteveCOMod.MOD_ID, name)
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id))
    }

    fun init() {
        SteveCOMod.logger.info("Registering sounds...")
    }
}
