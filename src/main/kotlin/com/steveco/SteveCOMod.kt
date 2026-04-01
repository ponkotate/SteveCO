package com.steveco

import com.steveco.command.UrgencyCommand
import com.steveco.network.ModNetworking
import com.steveco.registry.ModBlocks
import com.steveco.registry.ModItems
import com.steveco.registry.ModSounds
import com.steveco.urgency.UrgencyManager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import org.slf4j.LoggerFactory

object SteveCOMod : ModInitializer {
    const val MOD_ID = "steveco"
    val logger = LoggerFactory.getLogger(MOD_ID)!!

    override fun onInitialize() {
        logger.info("Steve Can Oshikko initializing...")
        ModBlocks.init()
        ModItems.init()
        ModSounds.init()
        ModNetworking.registerS2CPayloads()
        UrgencyManager.register()
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            UrgencyCommand.register(dispatcher)
        }
    }
}
