package com.salkcoding.essentialssshbc

import com.salkcoding.essentialssshbc.command.CommandHome
import com.salkcoding.essentialssshbc.command.CommandSetHome
import com.salkcoding.essentialssshbc.command.CommandSpawn
import com.salkcoding.essentialssshbc.listener.BedListener
import com.salkcoding.essentialssshbc.listener.PlayerRespawnListener
import fish.evatuna.metamorphosis.Metamorphosis
import me.baiks.bukkitlinked.BukkitLinked
import me.baiks.bukkitlinked.api.BukkitLinkedAPI
import org.bukkit.plugin.java.JavaPlugin

lateinit var essentials: EssentialsSSHBC
lateinit var currentServerName: String
lateinit var enabledWorld: Set<String>
lateinit var metamorphosis: Metamorphosis
lateinit var bukkitLinkedAPI: BukkitLinkedAPI

class EssentialsSSHBC : JavaPlugin() {

    override fun onEnable() {
        essentials = this

        val tempMetamorphosis = server.pluginManager.getPlugin("Metamorphosis") as? Metamorphosis
        if (tempMetamorphosis == null) {
            server.pluginManager.disablePlugin(this)
            logger.warning("Metamorphosis is not running on this server!")
            return
        }
        metamorphosis = tempMetamorphosis

        val tempBukkitLinked = server.pluginManager.getPlugin("BukkitLinked") as? BukkitLinked
        if (tempBukkitLinked == null) {
            server.pluginManager.disablePlugin(this)
            logger.warning("BukkitLinked is not running on this server!")
            return
        }
        bukkitLinkedAPI = tempBukkitLinked.api

        saveDefaultConfig()
        currentServerName = config.getString("serverName")!!
        enabledWorld = config.getList("enabledWorld")!!.toSet() as Set<String>

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