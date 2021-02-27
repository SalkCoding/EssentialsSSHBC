package com.salkcoding.essentialssshbc.command

import com.salkcoding.essentialssshbc.bungeeApi
import com.salkcoding.essentialssshbc.currentServerName
import com.salkcoding.essentialssshbc.essentials
import com.salkcoding.essentialssshbc.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class CommandSetHome : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            essentials.logger.warning("Player only command")
            return true
        }

        val location = sender.location
        sender.sendMessage("해당 위치를 홈으로 설정했습니다".infoFormat())
        Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
            val messageBytes = ByteArrayOutputStream()
            val messageOut = DataOutputStream(messageBytes)
            try {
                messageOut.writeUTF(sender.uniqueId.toString())
                messageOut.writeUTF(currentServerName)
                messageOut.writeUTF(location.world.name)
                messageOut.writeDouble(location.x)
                messageOut.writeDouble(location.y)
                messageOut.writeDouble(location.z)
                messageOut.writeFloat(location.yaw)
                messageOut.writeFloat(location.pitch)
            } catch (exception: IOException) {
                exception.printStackTrace()
            } finally {
                messageOut.close()
            }
            bungeeApi.forward("ALL", "essentials-sethome", messageBytes.toByteArray())
        })
        return true
    }
}