package com.salkcoding.essentialssshbc.bungee.receiver


import com.google.common.io.ByteStreams
import com.salkcoding.essentialssshbc.bungee.channelapi.BungeeChannelApi
import com.salkcoding.essentialssshbc.bungeeApi
import com.salkcoding.essentialssshbc.essentials
import com.salkcoding.essentialssshbc.util.TeleportCooltime
import com.salkcoding.essentialssshbc.util.errorFormat
import com.salkcoding.essentialssshbc.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.util.*

class CommandReceiver : BungeeChannelApi.ForwardConsumer {

    override fun accept(channel: String, receiver: Player, data: ByteArray) {
        when (channel) {
            "essentials-home-receive" -> {
                val inMessage = ByteStreams.newDataInput(data)
                val playerName = inMessage.readUTF()
                val uuid = UUID.fromString(inMessage.readUTF())
                val serverName = inMessage.readUTF()
                val player = Bukkit.getPlayer(playerName)
                if (player == null) {
                    essentials.logger.warning("$playerName home receive failed.(Player is not existed)")
                    return
                }

                //hasHome
                if (inMessage.readBoolean()) {
                    val runnable = Runnable {
                        val messageBytes = ByteArrayOutputStream()
                        val messageOut = DataOutputStream(messageBytes)
                        try {
                            messageOut.writeUTF(playerName)
                            messageOut.writeUTF(uuid.toString())
                        } catch (exception: IOException) {
                            exception.printStackTrace()
                        }
                        //For other server teleport(Teleports are managed by EssentialsSSH)
                        bungeeApi.forward(serverName, "essentials-home-teleport", messageBytes.toByteArray())
                    }
                    if (player.isOp) Bukkit.getScheduler().runTaskAsynchronously(essentials, runnable)
                    else TeleportCooltime.addPlayer(player, null, 100, runnable, true)
                } else player.sendMessage("설정된 홈이 없습니다.".errorFormat())
            }
            //For this bungee server
            "essentials-home-teleport" -> {
                Bukkit.getScheduler().runTaskLater(essentials, Runnable {
                    val inMessage = ByteStreams.newDataInput(data)
                    val playerName = inMessage.readUTF()
                    val player = Bukkit.getPlayer(playerName)
                    if (player == null) {
                        essentials.logger.warning("$playerName teleport to home failed.(Player is not existed)")
                        return@Runnable
                    }

                    val world = Bukkit.getWorld(inMessage.readUTF())
                    if (world == null) {
                        essentials.logger.warning("$playerName teleport to home failed.(World is not existed)")
                        return@Runnable
                    }

                    val location = Location(
                        world,
                        inMessage.readDouble(),
                        inMessage.readDouble(),
                        inMessage.readDouble(),
                        inMessage.readFloat(),
                        inMessage.readFloat()
                    )
                    player.teleportAsync(location)
                    player.sendMessage("이동되었습니다.".infoFormat())
                }, 15)
            }
            "essentials-spawn-receive" -> {
                val inMessage = ByteStreams.newDataInput(data)
                val playerName = inMessage.readUTF()
                val serverName = inMessage.readUTF()
                val player = Bukkit.getPlayer(playerName)
                if (player == null) {
                    essentials.logger.warning("$playerName spawn receive failed.(Player is not existed)")
                    return
                }

                val runnable = Runnable {
                    bungeeApi.connect(player, serverName)

                    val messageBytes = ByteArrayOutputStream()
                    val messageOut = DataOutputStream(messageBytes)
                    try {
                        messageOut.writeUTF(playerName)
                    } catch (exception: IOException) {
                        exception.printStackTrace()
                    }
                    bungeeApi.forward(serverName, "essentials-spawn-teleport", messageBytes.toByteArray())
                }
                if (player.isOp) Bukkit.getScheduler().runTaskAsynchronously(essentials, runnable)
                else TeleportCooltime.addPlayer(player, null, 100, runnable, true)
            }
        }
    }
}