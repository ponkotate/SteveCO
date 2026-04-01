package com.steveco.client

import com.steveco.SteveCOMod
import net.fabricmc.api.ClientModInitializer

object SteveCOClient : ClientModInitializer {
    override fun onInitializeClient() {
        SteveCOMod.logger.info("Steve Can Oshikko client initializing...")
    }
}
