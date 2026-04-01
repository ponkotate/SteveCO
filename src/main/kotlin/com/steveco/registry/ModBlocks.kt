package com.steveco.registry

import com.steveco.SteveCOMod
import com.steveco.block.ChamberPotBlock
import com.steveco.block.PeeBlock
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

object ModBlocks {
    val PEE_BLOCK: Block = register(
        "pee_block",
        { settings ->
            PeeBlock(
                settings
                    .noCollision()
                    .nonOpaque()
                    .strength(0.0f)
                    .sounds(BlockSoundGroup.SLIME)
            )
        }
    )

    val CHAMBER_POT: Block = register(
        "chamber_pot",
        { settings ->
            ChamberPotBlock(
                settings
                    .strength(3.5f)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.ANVIL)
            )
        }
    )

    private fun register(name: String, factory: (AbstractBlock.Settings) -> Block): Block {
        val id = Identifier.of(SteveCOMod.MOD_ID, name)
        val key = RegistryKey.of(RegistryKeys.BLOCK, id)
        val settings = AbstractBlock.Settings.create().registryKey(key)
        val block = factory(settings)
        return Registry.register(Registries.BLOCK, key, block)
    }

    fun init() {
        SteveCOMod.logger.info("Registering blocks...")
    }
}
