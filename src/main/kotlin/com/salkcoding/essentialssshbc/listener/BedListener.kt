package com.salkcoding.essentialssshbc.listener

import com.salkcoding.essentialssshbc.bungeeApi
import com.salkcoding.essentialssshbc.currentServerName
import com.salkcoding.essentialssshbc.essentials
import com.salkcoding.essentialssshbc.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

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
        if (world.environment != World.Environment.NORMAL) return

        if (event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock != null) {
            if (event.clickedBlock!!.type !in bedSet) return

            val location = event.clickedBlock!!.location
            player.setBedSpawnLocation(location, true)
            player.sendMessage("홈으로 설정되었습니다.".infoFormat())

            Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
                val messageBytes = ByteArrayOutputStream()
                val messageOut = DataOutputStream(messageBytes)
                try {
                    messageOut.writeUTF(player.uniqueId.toString())
                    messageOut.writeUTF(currentServerName)
                    messageOut.writeUTF(location.world.name)
                    messageOut.writeDouble(location.x)
                    messageOut.writeDouble(location.y)
                    messageOut.writeDouble(location.z)
                    messageOut.writeFloat(location.yaw)
                    messageOut.writeFloat(location.pitch)
                } catch (exception: IOException) {
                    exception.printStackTrace()
                }
                bungeeApi.forward("ALL", "essentials-sethome", messageBytes.toByteArray())
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
            val messageBytes = ByteArrayOutputStream()
            val messageOut = DataOutputStream(messageBytes)
            try {
                messageOut.writeUTF(player.name)
                messageOut.writeUTF(player.uniqueId.toString())
                messageOut.writeUTF(currentServerName)
                messageOut.writeUTF(location.world.name)
                messageOut.writeDouble(location.x)
                messageOut.writeDouble(location.y)
                messageOut.writeDouble(location.z)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
            bungeeApi.forward("ALL", "essentials-sethome-delete", messageBytes.toByteArray())
        })
    }
}