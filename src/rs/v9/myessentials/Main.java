package rs.v9.myessentials;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
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

        createRecipes();
    }

    @Override
    public void onDisable(){
    }

    private void createRecipes(){
        ShapedRecipe spawner = new ShapedRecipe(new ItemStack(Material.SPAWNER, 1));

        spawner.shape("NNN","NTN","NNN");

        spawner.setIngredient('N', Material.NETHERITE_INGOT);
        spawner.setIngredient('T', Material.TOTEM_OF_UNDYING);

        getServer().addRecipe(spawner);


        getServer().addRecipe(new FurnaceRecipe(new ItemStack(Material.LEATHER), Material.ROTTEN_FLESH));
    }
}
