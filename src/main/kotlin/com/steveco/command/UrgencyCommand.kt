package com.steveco.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.steveco.urgency.UrgencyManager
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands.argument
import net.minecraft.commands.Commands.literal
import net.minecraft.network.chat.Component
import net.minecraft.server.permissions.Permissions

object UrgencyCommand {
    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            literal("urgency")
                .requires { it.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER) }
                .then(
                    literal("get").executes { context ->
                        val player = context.source.playerOrException
                        val value = UrgencyManager.getUrgency(player)
                        context.source.sendSuccess(
                            { Component.literal("Urgency: $value / ${UrgencyManager.MAX_URGENCY}") },
                            false
                        )
                        value
                    }
                )
                .then(
                    literal("set").then(
                        argument("value", IntegerArgumentType.integer(0, 100))
                            .executes { context ->
                                val player = context.source.playerOrException
                                val value = IntegerArgumentType.getInteger(context, "value")
                                UrgencyManager.setUrgency(player, value)
                                UrgencyManager.syncToClient(player)
                                context.source.sendSuccess(
                                    { Component.literal("Urgency set to $value") },
                                    true
                                )
                                value
                            }
                    )
                )
        )
    }
}
