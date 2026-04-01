package com.steveco

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object SteveCOMod : ModInitializer {
    const val MOD_ID = "steveco"
    val logger = LoggerFactory.getLogger(MOD_ID)!!

    override fun onInitialize() {
        logger.info("Steve Can Oshikko initializing...")
    }
}
