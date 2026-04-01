package com.steveco.registry

import com.steveco.SteveCOMod
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModItems {
    val CHAMBER_POT: Item = register(
        "chamber_pot",
        BlockItem(ModBlocks.CHAMBER_POT, Item.Settings())
    )

    private fun register(name: String, item: Item): Item {
        return Registry.register(Registries.ITEM, Identifier.of(SteveCOMod.MOD_ID, name), item)
    }

    fun init() {
        SteveCOMod.logger.info("Registering items...")

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register { entries ->
            entries.add(CHAMBER_POT)
        }
    }
}
