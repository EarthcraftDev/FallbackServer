package net.cybercake.fallback;

import net.cybercake.cyberapi.common.builders.settings.Settings;
import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.spigot.chat.Log;
import net.cybercake.cyberapi.spigot.chat.UChat;
import net.cybercake.fallback.tasks.AttemptToSend;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public final class Main extends CyberAPI {

    private static Main instance;

    private Configuration configuration;

    @Override
    public void onEnable() {
        instance = this;
        startCyberAPI(Settings.builder()
                .name("Fallback")
                .prefix("Fallback")
                .checkForUpdates(false)
                .muteStartMessage(true)
                .showPrefixInLogs(true)
                .mainPackage("net.cybercake.fallback")
                .build()
        );
        long mss = System.currentTimeMillis();

        configuration = new Configuration();
        Log.info("Loaded the configuration!");

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Log.info("Registered outgoing plugin channel for BungeeCord!");

        registerRunnable(new AttemptToSend(), getConfiguration().getConnectionInterval());
        Log.info("Started all tasks!");

        Log.info("&e&l-".repeat(Math.max(0, 60)));
        Log.info("&bSuccessfully enabled!");
        Log.info(" &c\u250D &fPlugin &7" + this.getName());
        Log.info(" &c\u251C &fVersion &7" + this.getPluginMeta().getVersion());
        Log.info(" &c\u251C &fCreated By &7" + String.join(", ", this.getDescription().getAuthors()));
        Log.info(" &c\u251C &fDescription &7" + (this.getPluginMeta().getDescription().length() > 60 ? this.getPluginMeta().getDescription().substring(0, 60) + "&8..." : this.getPluginMeta().getDescription()));
        Log.info(" &c\u2515 &fEnabled In &7" + (System.currentTimeMillis()-mss) + "ms");
        Log.info(" ");
        Log.info("&7Occassionally check for updates at &b" + this.getPluginMeta().getWebsite() + "&r&7!");
        Log.info("&e&l-".repeat(Math.max(0, 60)));
    }

    @Override
    public void onDisable() {
        long mss = System.currentTimeMillis();

        Log.info(ChatColor.RED + "Successfully disabled " + this.getName() + " [v" + this.getPluginMeta().getVersion() + "] in " + (System.currentTimeMillis()-mss) + "ms!");
    }

    public static Main getInstance() { return instance; }

    public @NotNull Configuration getConfiguration() { return configuration; }

    public void send(Player player, String server) {
        player.sendMessage(UChat.getClearedChat());
        if(server.equalsIgnoreCase("$$configuration")) {
            server = getConfiguration().getConnectTo();
            String customServer = String.valueOf(Main.getInstance().getConfiguration().getPlayersToServers().get(player.getName()));
            if(customServer != null)
                server = customServer;
        }
        player.sendMessage(UChat.component("&7Sending you to &b" + server + "&7!"));
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(byteArrayOutputStream);

            out.writeUTF("Connect");
            out.writeUTF(server);

            player.sendPluginMessage(Main.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());

            byteArrayOutputStream.close();
            out.close();
        } catch (Exception exception) {
            player.sendMessage(UChat.component("&e" + server + " &c(exception) -> &8" + exception));
            Log.error("&cAn error occurred whilst sending " + player.getName() + " to " + server + ": " + ChatColor.DARK_GRAY + exception);
        }
    }
}
