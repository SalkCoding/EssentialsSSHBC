package com.salkcoding.essentialssshbc

import com.salkcoding.essentialssshbc.bungee.channelapi.BungeeChannelApi
import com.salkcoding.essentialssshbc.bungee.receiver.CommandReceiver
import com.salkcoding.essentialssshbc.command.CommandHome
import com.salkcoding.essentialssshbc.command.CommandSetHome
import com.salkcoding.essentialssshbc.command.CommandSpawn
import com.salkcoding.essentialssshbc.listener.BedListener
import com.salkcoding.essentialssshbc.listener.PlayerRespawnListener
import org.bukkit.plugin.java.JavaPlugin

lateinit var essentials: EssentialsSSHBC
lateinit var bungeeApi: BungeeChannelApi
lateinit var currentServerName: String

class EssentialsSSHBC : JavaPlugin() {

    override fun onEnable() {
        essentials = this

        saveDefaultConfig()
        currentServerName = config.getString("serverName")!!

        bungeeApi = BungeeChannelApi.of(this)

        val receiver = CommandReceiver()
        bungeeApi.registerForwardListener("essentials-home-receive", receiver)
        bungeeApi.registerForwardListener("essentials-home-teleport", receiver)
        bungeeApi.registerForwardListener("essentials-spawn-receive", receiver)

        getCommand("home")!!.setExecutor(CommandHome())
        getCommand("sethome")!!.setExecutor(CommandSetHome())
        getCommand("spawn")!!.setExecutor(CommandSpawn())

        server.pluginManager.registerEvents(BedListener(), this)
        server.pluginManager.registerEvents(PlayerRespawnListener(), this)

        logger.info("Plugin enabled")
    }

    override fun onDisable() {
        logger.warning("Plugin disabled")
    }
}