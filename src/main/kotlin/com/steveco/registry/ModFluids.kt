package com.steveco.registry

import com.steveco.SteveCOMod
import com.steveco.fluid.PeeFluid
import net.minecraft.fluid.FlowableFluid
import net.minecraft.fluid.Fluid
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModFluids {
    val STILL_PEE: FlowableFluid = register("pee", PeeFluid.Still())
    val FLOWING_PEE: FlowableFluid = register("flowing_pee", PeeFluid.Flowing())

    private fun <T : Fluid> register(name: String, fluid: T): T {
        val id = Identifier.of(SteveCOMod.MOD_ID, name)
        return Registry.register(Registries.FLUID, id, fluid)
    }

    fun init() {
        SteveCOMod.logger.info("Registering fluids...")
    }
}
