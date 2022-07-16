package net.cybercake.fallback.commands;

import net.cybercake.cyberapi.spigot.basic.BetterStackTraces;
import net.cybercake.cyberapi.spigot.chat.TabCompleteType;
import net.cybercake.cyberapi.spigot.chat.UChat;
import net.cybercake.cyberapi.spigot.server.commands.CommandInformation;
import net.cybercake.fallback.Main;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import java.util.List;

public class FallbackReload extends net.cybercake.cyberapi.spigot.server.commands.Command {

    public FallbackReload() {
        super(
                newCommand("fallbackreload")
                        .setDescription("Reloads the configuration file for the fallback server.")
                        .setUsage("/fallbackreload")
                        .setCommodore(true)
                        .setTabCompleteType(TabCompleteType.SEARCH)
        );
    }

    @Override
    public boolean perform(@net.cybercake.cyberapi.dependencies.annotations.jetbrains.NotNull CommandSender sender, @net.cybercake.cyberapi.dependencies.annotations.jetbrains.NotNull String command, CommandInformation information, String[] args) {
        try {
            long mss = System.currentTimeMillis();

            Main.getInstance().getConfiguration().reload();

            sender.sendMessage(UChat.component("&aSuccessfully &7reloaded the fallback configuration in &b" + (System.currentTimeMillis()-mss) + "&bms&7!"));
            Main.getInstance().playSound(sender, Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
        } catch (Exception exception) {
            sender.sendMessage(UChat.component("&cAn error occurred whilst reloading the fallback configuration! &4" + exception));
            BetterStackTraces.print(exception);
        }

        return true;
    }

    @Override
    public List<String> tab(@net.cybercake.cyberapi.dependencies.annotations.jetbrains.NotNull CommandSender sender, @net.cybercake.cyberapi.dependencies.annotations.jetbrains.NotNull String command, CommandInformation information, String[] args) {
        return null;
    }
}
