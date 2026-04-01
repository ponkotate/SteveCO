package com.steveco.registry

import com.steveco.SteveCOMod
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier

object ModSounds {
    val PEE_RELEASE: SoundEvent = register("pee_release")
    val CHAMBER_POT_USE: SoundEvent = register("chamber_pot_use")

    private fun register(name: String): SoundEvent {
        val id = Identifier.of(SteveCOMod.MOD_ID, name)
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id))
    }

    fun init() {
        SteveCOMod.logger.info("Registering sounds...")
    }
}
