package com.steveco.registry

import com.steveco.SteveCOMod
import com.steveco.block.ChamberPotBlock
import com.steveco.block.PeeFluidBlock
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.PushReaction

object ModBlocks {
    val PEE_BLOCK: Block = register(
        "pee_block",
        { settings ->
            PeeFluidBlock(
                ModFluids.STILL_PEE,
                settings
                    .noCollision()
                    .replaceable()
                    .strength(100.0f)
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
            )
        }
    )

    val CHAMBER_POT: Block = register(
        "chamber_pot",
        { settings ->
            ChamberPotBlock(
                settings
                    .strength(3.5f)
                    .noOcclusion()
                    .sound(SoundType.ANVIL)
            )
        }
    )

    private fun register(name: String, factory: (BlockBehaviour.Properties) -> Block): Block {
        val id = Identifier.fromNamespaceAndPath(SteveCOMod.MOD_ID, name)
        val key = ResourceKey.create(Registries.BLOCK, id)
        val settings = BlockBehaviour.Properties.of().setId(key)
        val block = factory(settings)
        return Registry.register(BuiltInRegistries.BLOCK, key, block)
    }

    fun init() {
        SteveCOMod.logger.info("Registering blocks...")
    }
}
