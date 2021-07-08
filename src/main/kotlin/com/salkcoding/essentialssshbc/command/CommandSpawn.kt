package com.salkcoding.essentialssshbc.command

import com.google.gson.JsonObject
import com.salkcoding.essentialssshbc.essentials
import com.salkcoding.essentialssshbc.metamorphosis
import com.salkcoding.essentialssshbc.util.TeleportCooltime
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandSpawn : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            essentials.logger.warning("Player only command")
            return true
        }

        TeleportCooltime.addPlayer(sender, null, 100, {
            val json = JsonObject().apply {
                addProperty("uuid", sender.uniqueId.toString())
                addProperty("name", sender.name)
            }
            metamorphosis.send("com.salkcoding.essentialsssh.spawn", json.toString())
        }, true)
        return true
    }
}