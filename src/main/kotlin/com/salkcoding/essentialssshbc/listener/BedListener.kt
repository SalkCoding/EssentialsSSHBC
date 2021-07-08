package com.salkcoding.essentialssshbc.listener

import com.google.gson.JsonObject
import com.salkcoding.essentialssshbc.currentServerName
import com.salkcoding.essentialssshbc.enabledWorld
import com.salkcoding.essentialssshbc.essentials
import com.salkcoding.essentialssshbc.metamorphosis
import com.salkcoding.essentialssshbc.util.infoFormat
import com.salkcoding.essentialssshbc.util.sendErrorTipMessage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent

class BedListener : Listener {

    companion object {
        val bedSet = setOf(
            Material.BLACK_BED,
            Material.BLUE_BED,
            Material.BROWN_BED,
            Material.CYAN_BED,
            Material.GRAY_BED,
            Material.GREEN_BED,
            Material.LIGHT_BLUE_BED,
            Material.LIGHT_GRAY_BED,
            Material.LIME_BED,
            Material.MAGENTA_BED,
            Material.ORANGE_BED,
            Material.PINK_BED,
            Material.RED_BED,
            Material.WHITE_BED,
            Material.YELLOW_BED,
        )
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val world = player.world

        if ("nether" in world.name || "ender" in world.name) return

        if (event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock != null) {
            if (event.clickedBlock!!.type !in bedSet) return

            if (world.name !in enabledWorld) {
                player.sendErrorTipMessage("${ChatColor.RED}현재 월드에서는 사용할 수 없습니다.")
                return
            }

            val location = event.clickedBlock!!.location
            player.setBedSpawnLocation(location, true)
            player.sendMessage("홈으로 설정되었습니다.".infoFormat())

            Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
                val json = JsonObject().apply {
                    addProperty("uuid", player.uniqueId.toString())
                    addProperty("serverName", currentServerName)
                    addProperty("worldName", location.world.name)
                    addProperty("x", location.x)
                    addProperty("y", location.y)
                    addProperty("z", location.z)
                    addProperty("yaw", location.yaw)
                    addProperty("pitch", location.pitch)
                }
                metamorphosis.send("com.salkcoding.essentialsssh.sethome", json.toString())
            })

            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        if (event.isCancelled) return
        if (event.block.type !in bedSet) return

        val player = event.player
        val location = event.block.location

        Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
            val json = JsonObject().apply {
                addProperty("uuid", player.uniqueId.toString())
                addProperty("serverName", currentServerName)
                addProperty("worldName", location.world.name)
                addProperty("x", location.x)
                addProperty("y", location.y)
                addProperty("z", location.z)
                addProperty("yaw", location.yaw)
                addProperty("pitch", location.pitch)
            }
            metamorphosis.send("com.salkcoding.essentialsssh.delhome", json.toString())
        })
    }
}