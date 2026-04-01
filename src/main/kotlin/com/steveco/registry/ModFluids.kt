package com.steveco.registry

import com.steveco.SteveCOMod
import com.steveco.fluid.PeeFluid
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid

object ModFluids {
    val STILL_PEE: FlowingFluid = register("pee", PeeFluid.Still())
    val FLOWING_PEE: FlowingFluid = register("flowing_pee", PeeFluid.Flowing())

    private fun <T : Fluid> register(name: String, fluid: T): T {
        val id = Identifier.fromNamespaceAndPath(SteveCOMod.MOD_ID, name)
        return Registry.register(BuiltInRegistries.FLUID, id, fluid)
    }

    fun init() {
        SteveCOMod.logger.info("Registering fluids...")
    }
}
