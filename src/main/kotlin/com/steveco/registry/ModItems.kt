package com.steveco.registry

import com.steveco.SteveCOMod
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item

object ModItems {
    private val FUNCTIONAL_BLOCKS_KEY: ResourceKey<CreativeModeTab> =
        ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("functional_blocks"))

    val CHAMBER_POT: Item = register(
        "chamber_pot",
        { settings -> BlockItem(ModBlocks.CHAMBER_POT, settings) }
    )

    private fun register(name: String, factory: (Item.Properties) -> Item): Item {
        val id = Identifier.fromNamespaceAndPath(SteveCOMod.MOD_ID, name)
        val key = ResourceKey.create(Registries.ITEM, id)
        val settings = Item.Properties().setId(key)
        val item = factory(settings)
        return Registry.register(BuiltInRegistries.ITEM, key, item)
    }

    fun init() {
        SteveCOMod.logger.info("Registering items...")

        CreativeModeTabEvents.modifyOutputEvent(FUNCTIONAL_BLOCKS_KEY).register { entries ->
            entries.accept(CHAMBER_POT)
        }
    }
}
