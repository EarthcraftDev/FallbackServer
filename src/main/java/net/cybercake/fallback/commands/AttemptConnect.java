package net.cybercake.fallback.commands;

import net.cybercake.cyberapi.spigot.chat.TabCompleteType;
import net.cybercake.cyberapi.spigot.chat.UChat;
import net.cybercake.cyberapi.spigot.server.commands.CommandInformation;
import net.cybercake.fallback.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AttemptConnect extends net.cybercake.cyberapi.spigot.server.commands.Command {

    public AttemptConnect() {
        super(
                newCommand("attemptconnect")
                        .setDescription("Attempts to connect you to a specified bungee server.")
                        .setUsage("/attemptconnect <server>")
                        .setCommodore(true)
                        .setTabCompleteType(TabCompleteType.SEARCH)
        );
    }

    @Override
    public boolean perform(@net.cybercake.cyberapi.dependencies.annotations.jetbrains.NotNull CommandSender sender, @net.cybercake.cyberapi.dependencies.annotations.jetbrains.NotNull String command, CommandInformation information, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(UChat.component("&cOnly in-game players can use this command!")); return true;
        }

        if(args.length < 1) {
            player.sendMessage(UChat.component("&7Attempting to connect you to default server &8(&b" + Main.getInstance().getConfiguration().getConnectTo() + "&8)&7..."));
            Main.getInstance().send(player, "$$configuration");
        }else{
            player.sendMessage(UChat.component("&7Attempting to connect you to &b" + args[0] + "&7..."));
            Main.getInstance().send(player, args[0]);
        }

        return true;
    }

    @Override
    public List<String> tab(@net.cybercake.cyberapi.dependencies.annotations.jetbrains.NotNull CommandSender sender, @net.cybercake.cyberapi.dependencies.annotations.jetbrains.NotNull String command, CommandInformation information, String[] args) {
        if(args.length == 1) return List.of(Main.getInstance().getConfiguration().getConnectTo());
        return null;
    }
}
