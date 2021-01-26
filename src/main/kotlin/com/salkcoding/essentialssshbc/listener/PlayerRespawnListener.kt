package com.salkcoding.essentialssshbc.listener

import com.salkcoding.essentialssshbc.bungeeApi
import com.salkcoding.essentialssshbc.currentServerName
import com.salkcoding.essentialssshbc.essentials
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class PlayerRespawnListener : Listener {

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        Bukkit.getScheduler().runTaskLaterAsynchronously(essentials, Runnable {
            val messageBytes = ByteArrayOutputStream()
            val messageOut = DataOutputStream(messageBytes)
            try {
                messageOut.writeUTF(player.name)
                messageOut.writeUTF(currentServerName)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
            bungeeApi.forward("ALL", "essentials-spawn", messageBytes.toByteArray())
        }, 5)
    }
}