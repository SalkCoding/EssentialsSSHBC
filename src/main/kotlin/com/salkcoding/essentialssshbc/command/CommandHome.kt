package com.salkcoding.essentialssshbc.command

import com.salkcoding.essentialssshbc.bungeeApi
import com.salkcoding.essentialssshbc.currentServerName
import com.salkcoding.essentialssshbc.essentials
import com.salkcoding.essentialssshbc.util.TeleportCooltime
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class CommandHome : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            essentials.logger.warning("Player only command")
            return true
        }

        val runnable = Runnable {
            val messageBytes = ByteArrayOutputStream()
            val messageOut = DataOutputStream(messageBytes)
            try {
                messageOut.writeUTF(sender.name)
                messageOut.writeUTF(sender.uniqueId.toString())
                messageOut.writeUTF(currentServerName)
            } catch (exception: IOException) {
                exception.printStackTrace()
            } finally {
                messageOut.close()
            }
            bungeeApi.forward("ALL", "essentials-home", messageBytes.toByteArray())
        }
        if (sender.isOp) Bukkit.getScheduler().runTaskAsynchronously(essentials, runnable)
        else TeleportCooltime.addPlayer(sender, null, 100, runnable, true)
        return true
    }
}