package com.steveco.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.steveco.urgency.UrgencyManager
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object UrgencyCommand {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            literal("urgency")
                .requires { it.hasPermissionLevel(2) }
                .then(
                    literal("get").executes { context ->
                        val player = context.source.playerOrThrow
                        val value = UrgencyManager.getUrgency(player)
                        context.source.sendFeedback(
                            { Text.literal("Urgency: $value / ${UrgencyManager.MAX_URGENCY}") },
                            false
                        )
                        value
                    }
                )
                .then(
                    literal("set").then(
                        argument("value", IntegerArgumentType.integer(0, 100))
                            .executes { context ->
                                val player = context.source.playerOrThrow
                                val value = IntegerArgumentType.getInteger(context, "value")
                                UrgencyManager.setUrgency(player, value)
                                UrgencyManager.syncToClient(player)
                                context.source.sendFeedback(
                                    { Text.literal("Urgency set to $value") },
                                    true
                                )
                                value
                            }
                    )
                )
        )
    }
}
