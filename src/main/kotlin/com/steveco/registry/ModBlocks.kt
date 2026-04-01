package com.steveco.registry

import com.steveco.SteveCOMod
import com.steveco.block.ChamberPotBlock
import com.steveco.block.PeeBlock
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier

object ModBlocks {
    val PEE_BLOCK: Block = register(
        "pee_block",
        PeeBlock(
            AbstractBlock.Settings.create()
                .noCollision()
                .nonOpaque()
                .strength(0.0f)
                .sounds(BlockSoundGroup.SLIME)
        )
    )

    val CHAMBER_POT: Block = register(
        "chamber_pot",
        ChamberPotBlock(
            AbstractBlock.Settings.create()
                .strength(3.5f)
                .sounds(BlockSoundGroup.ANVIL)
        )
    )

    private fun register(name: String, block: Block): Block {
        return Registry.register(Registries.BLOCK, Identifier.of(SteveCOMod.MOD_ID, name), block)
    }

    fun init() {
        SteveCOMod.logger.info("Registering blocks...")
    }
}
