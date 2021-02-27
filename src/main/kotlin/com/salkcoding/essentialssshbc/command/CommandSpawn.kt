package com.salkcoding.essentialssshbc.command

import com.salkcoding.essentialssshbc.bungeeApi
import com.salkcoding.essentialssshbc.currentServerName
import com.salkcoding.essentialssshbc.essentials
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class CommandSpawn : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            essentials.logger.warning("Player only command")
            return true
        }

        Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
            val messageBytes = ByteArrayOutputStream()
            val messageOut = DataOutputStream(messageBytes)
            try {
                messageOut.writeUTF(sender.name)
                messageOut.writeUTF(currentServerName)
            } catch (exception: IOException) {
                exception.printStackTrace()
            } finally {
                messageOut.close()
            }
            bungeeApi.forward("ALL", "essentials-spawn", messageBytes.toByteArray())
        })
        return true
    }
}