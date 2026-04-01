package com.steveco

import com.steveco.network.ModNetworking
import com.steveco.urgency.UrgencyManager
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object SteveCOMod : ModInitializer {
    const val MOD_ID = "steveco"
    val logger = LoggerFactory.getLogger(MOD_ID)!!

    override fun onInitialize() {
        logger.info("Steve Can Oshikko initializing...")
        ModNetworking.registerS2CPayloads()
        UrgencyManager.register()
    }
}
