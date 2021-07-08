package com.salkcoding.essentialssshbc.command

import com.google.gson.JsonObject
import com.salkcoding.essentialssshbc.currentServerName
import com.salkcoding.essentialssshbc.essentials
import com.salkcoding.essentialssshbc.metamorphosis
import com.salkcoding.essentialssshbc.util.TeleportCooltime
import com.salkcoding.essentialssshbc.util.errorFormat
import com.salkcoding.essentialssshbc.util.infoFormat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandSetHome : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            essentials.logger.warning("Player only command")
            return true
        }

        if (!sender.isOp) {
            sender.sendMessage("해당 명령어를 사용할 권한이 없습니다!".errorFormat())
            return true
        }

        val location = sender.location
        sender.sendMessage("현재 위치를 홈으로 설정했습니다".infoFormat())
        val json = JsonObject().apply {
            addProperty("uuid", sender.uniqueId.toString())
            addProperty("serverName", currentServerName)
            addProperty("worldName", location.world.name)
            addProperty("x", location.x)
            addProperty("y", location.y)
            addProperty("z", location.z)
            addProperty("yaw", location.yaw)
            addProperty("pitch", location.pitch)
        }
        metamorphosis.send("com.salkcoding.essentialsssh.sethome", json.toString())
        return true
    }
}