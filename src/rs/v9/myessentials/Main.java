package rs.v9.myessentials;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import rs.v9.myessentials.handlers.PlayerCooldown;
import rs.v9.myessentials.handlers.PlayerResolver;

public class Main extends JavaPlugin {

    public static Plugin plugin;

    //BETTER FAILURES - STATE EXACT REASON - SOME ARE OFF

    @Override
    public void onEnable(){
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new MyEventHandler(), this);

        getCommand("warp").setExecutor(new MyCommands());
        getCommand("warps").setExecutor(new MyCommands());
        getCommand("setwarp").setExecutor(new MyCommands());
        getCommand("delwarp").setExecutor(new MyCommands());
        getCommand("home").setExecutor(new MyCommands());
        getCommand("sethome").setExecutor(new MyCommands());
        getCommand("spawn").setExecutor(new MyCommands());
        getCommand("setspawn").setExecutor(new MyCommands());
        getCommand("tpa").setExecutor(new MyCommands());
        getCommand("tpaa").setExecutor(new MyCommands());
        getCommand("tpad").setExecutor(new MyCommands());
        getCommand("msg").setExecutor(new MyCommands());
        getCommand("gamemode").setExecutor(new MyCommands());
        getCommand("back").setExecutor(new MyCommands());

        new Config();
        new PlayerResolver();
        new PlayerCooldown();
    }

    @Override
    public void onDisable(){
    }
}
