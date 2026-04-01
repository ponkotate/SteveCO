package com.steveco.registry

import com.steveco.SteveCOMod
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

object ModItems {
    val CHAMBER_POT: Item = register(
        "chamber_pot",
        { settings -> BlockItem(ModBlocks.CHAMBER_POT, settings) }
    )

    private fun register(name: String, factory: (Item.Settings) -> Item): Item {
        val id = Identifier.of(SteveCOMod.MOD_ID, name)
        val key = RegistryKey.of(RegistryKeys.ITEM, id)
        val settings = Item.Settings().registryKey(key)
        val item = factory(settings)
        return Registry.register(Registries.ITEM, key, item)
    }

    fun init() {
        SteveCOMod.logger.info("Registering items...")

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register { entries ->
            entries.add(CHAMBER_POT)
        }
    }
}
