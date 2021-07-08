package com.salkcoding.essentialssshbc.util

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

fun String.announceFormat(): String {
    return "\ue4db ${ChatColor.RESET}$this"
}

fun String.infoFormat(): String {
    return "\ue4dc ${ChatColor.RESET}$this"
}

fun String.warnFormat(): String {
    return "\ue4dd ${ChatColor.RESET}$this"
}

fun String.errorFormat(): String {
    return "\ue4de ${ChatColor.RESET}$this"
}

fun Player.sendErrorTipMessage(message: String) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sendwarn ${this.name} $message")
}