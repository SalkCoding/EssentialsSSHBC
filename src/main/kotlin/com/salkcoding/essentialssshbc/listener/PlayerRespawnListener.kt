package com.salkcoding.essentialssshbc.listener

import com.google.gson.JsonObject
import com.salkcoding.essentialssshbc.essentials
import com.salkcoding.essentialssshbc.metamorphosis
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawnListener : Listener {

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        event.respawnLocation = player.location
        player.sendTitle("\ue405", "", 0, 50, 30)
        Bukkit.getScheduler().runTaskLaterAsynchronously(essentials, Runnable {
            val json = JsonObject().apply {
                addProperty("uuid", player.uniqueId.toString())
                addProperty("name", player.name)
            }
            metamorphosis.send("com.salkcoding.essentialsssh.respawn", json.toString())
        }, 10)
    }
}