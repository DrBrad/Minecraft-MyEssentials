package rs.v9.myessentials;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static rs.v9.myessentials.Config.*;
import static rs.v9.myessentials.handlers.BlockHandler.*;
import static rs.v9.myessentials.handlers.GeneralHandler.*;
import static rs.v9.myessentials.handlers.PlayerCooldown.*;
import static rs.v9.myessentials.handlers.PlayerResolver.*;

public class MyEventHandler implements Listener {

    private HashMap<UUID, ArrayList<Location>> xray = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        setPlayer(event.getPlayer().getName(), event.getPlayer().getUniqueId());

        event.getPlayer().setPlayerListName("§c"+event.getPlayer().getName());
        event.setJoinMessage("§c"+event.getPlayer().getName()+"§7 Has joined the server!");

        setPlayerCooldown(event.getPlayer().getUniqueId());

        if(isXRay()){
            antiXRay(event.getPlayer(), event.getPlayer().getLocation().getChunk());
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        event.setQuitMessage("§c"+event.getPlayer().getName()+"§7 Has left the server!");

        setPlayerCooldown(event.getPlayer().getUniqueId());

        if(xray.containsKey(event.getPlayer().getUniqueId())){
            xray.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        lastTeleport.put(event.getEntity().getPlayer(), Objects.requireNonNull(event.getEntity().getPlayer()).getLocation());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event){
        spawnCircle(event.getPlayer(), event.getTo(), Color.RED);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void onPortalTravel(PlayerPortalEvent event){
        if(event.getCause() == PlayerPortalEvent.TeleportCause.END_PORTAL){
            Location endSpawn = getEndSpawn();
            if(endSpawn != null){
                event.setTo(endSpawn);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        event.setFormat("§6[§c"+event.getPlayer().getName()+"§6]§7: "+event.getMessage());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Block block = event.getBlockPlaced();
        Material material = block.getType();

        if(material == Material.SPAWNER){
            CreatureSpawner type = (CreatureSpawner) block.getState();
            type.setSpawnedType(EntityType.AREA_EFFECT_CLOUD);
            type.update(true, false);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Material material = player.getItemInHand().getType();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.SPAWNER){
            if(material == Material.BLAZE_SPAWN_EGG){
                if(event.getClickedBlock().getWorld().getEnvironment() != World.Environment.NETHER){
                    player.sendMessage("§7Sorry, you can make blaze spawners in the §cNether§7.");
                    event.setCancelled(true);
                }
            }else if(material != Material.SKELETON_SPAWN_EGG && material != Material.ZOMBIE_SPAWN_EGG && material != Material.BLAZE_SPAWN_EGG &&
                    material != Material.SPIDER_SPAWN_EGG && material != Material.SILVERFISH_SPAWN_EGG && material != Material.MAGMA_CUBE_SPAWN_EGG &&
                    material != Material.CAVE_SPIDER_SPAWN_EGG && material != Material.PIG_SPAWN_EGG){
                player.sendMessage("§cSorry, you cant make a spawner with this creature.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event){
        if(isXRay()){
            if(!event.isCancelled()){
                if(xray.containsKey(event.getPlayer())){
                    isNextToOre(event.getPlayer(), event.getBlock());
                }
            }
        }

        Block block = event.getBlock();
        Material material = block.getType();

        if(material == Material.SPAWNER &&
                event.getPlayer().getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)){
            CreatureSpawner type = (CreatureSpawner) block.getState();
            EntityType mobtype = type.getSpawnedType();

            event.getBlock().getDrops().clear();

            switch(mobtype.name()){
                case "SKELETON":
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.SKELETON_SPAWN_EGG));
                    break;

                case "ZOMBIE":
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.ZOMBIE_SPAWN_EGG));
                    break;

                case "BLAZE":
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.BLAZE_SPAWN_EGG));
                    break;

                case "SPIDER":
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.SPIDER_SPAWN_EGG));
                    break;

                case "SILVERFISH":
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.SILVERFISH_SPAWN_EGG));
                    break;

                case "MAGMA_CUBE":
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.MAGMA_CUBE_SPAWN_EGG));
                    break;

                case "CAVE_SPIDER":
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.CAVE_SPIDER_SPAWN_EGG));
                    break;

                case "PIG":
                    block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.PIG_SPAWN_EGG));
                    break;
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        event.setDeathMessage("§7"+event.getDeathMessage());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(isXRay()){
            antiXRay(event.getPlayer(), event.getPlayer().getLocation().getChunk());
        }
    }

    public void antiXRay(Player player, Chunk centerChunk){
        if(xray.containsKey(player.getUniqueId())){
            int size = xray.get(player.getUniqueId()).size();
            if(size > 0){
                for(int i = size; i > 0; i--){
                    if(xray.get(player.getUniqueId()).size() > i){
                        Location location = xray.get(player.getUniqueId()).get(i);
                        if(location.getWorld() == centerChunk.getWorld()){
                            if(player.getLocation().distance(location) > 100){
                                xray.get(player.getUniqueId()).remove(i);
                            }
                        }else{
                            xray.get(player.getUniqueId()).remove(i);
                        }
                    }
                }
            }
        }else{
            xray.put(player.getUniqueId(), new ArrayList<>());
        }

        ArrayList<Chunk> chunks = new ArrayList<>();

        for(int x = -getXRayRadius(); x < getXRayRadius()+1; x++){
            for(int z = -getXRayRadius(); z < getXRayRadius()+1; z++){
                chunks.add(centerChunk.getBlock(0, 0, 0).getLocation().add(x*16, 0, z*16).getChunk());
            }
        }

        List<Material> xrayBlocks = getXRayBlocks();

        for(Chunk chunk : chunks){
            if(!xray.get(player.getUniqueId()).contains(chunk.getBlock(0, 0, 0).getLocation())){
                if(!xray.get(player.getUniqueId()).contains(chunk.getBlock(0, 0, 0).getLocation())){
                    xray.get(player.getUniqueId()).add(chunk.getBlock(0, 0, 0).getLocation());
                }

                for(int x = 0; x < 16; x++){
                    for(int z = 0; z < 16; z++){
                        for(int y = chunk.getWorld().getLogicalHeight(); y < chunk.getWorld().getHighestBlockAt(
                                (int)chunk.getBlock(x, 0, z).getLocation().getX(),
                                (int)chunk.getBlock(x, 0, z).getLocation().getZ()).getY(); y++){
                            Block block = chunk.getBlock(x, y, z);
                            if(xrayBlocks.contains(block.getType())){
                                if(!isOreExposed(block)){
                                    player.sendBlockChange(block.getLocation(), Material.STONE.createBlockData());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isOreExposed(Block block){
        List<Material> air = getAir();

        if(air.contains(block.getLocation().getWorld().getBlockAt(block.getLocation().add(1, 0, 0)).getType())){
            return true;
        }
        if(air.contains(block.getLocation().getWorld().getBlockAt(block.getLocation().subtract(1, 0, 0)).getType())){
            return true;
        }

        if(air.contains(block.getLocation().getWorld().getBlockAt(block.getLocation().add(0, 1, 0)).getType())){
            return true;
        }
        if(air.contains(block.getLocation().getWorld().getBlockAt(block.getLocation().subtract(0, 1, 0)).getType())){
            return true;
        }

        if(air.contains(block.getLocation().getWorld().getBlockAt(block.getLocation().add(0, 0, 1)).getType())){
            return true;
        }
        if(air.contains(block.getLocation().getWorld().getBlockAt(block.getLocation().subtract(0, 0, 1)).getType())){
            return true;
        }

        return false;
    }

    private void isNextToOre(Player player, Block block){
        List<Material> ores = getXRayBlocks();

        Block sideBlock = block.getLocation().getWorld().getBlockAt(block.getLocation().add(1, 0, 0));
        if(ores.contains(sideBlock.getType())){
            player.sendBlockChange(sideBlock.getLocation(), sideBlock.getBlockData());
        }
        sideBlock = block.getLocation().getWorld().getBlockAt(block.getLocation().subtract(1, 0, 0));
        if(ores.contains(sideBlock.getType())){
            player.sendBlockChange(sideBlock.getLocation(), sideBlock.getBlockData());
        }

        sideBlock = block.getLocation().getWorld().getBlockAt(block.getLocation().add(0, 1, 0));
        if(ores.contains(sideBlock.getType())){
            player.sendBlockChange(sideBlock.getLocation(), sideBlock.getBlockData());
        }
        sideBlock = block.getLocation().getWorld().getBlockAt(block.getLocation().subtract(0, 1, 0));
        if(ores.contains(sideBlock.getType())){
            player.sendBlockChange(sideBlock.getLocation(), sideBlock.getBlockData());
        }

        sideBlock = block.getLocation().getWorld().getBlockAt(block.getLocation().add(0, 0, 1));
        if(ores.contains(sideBlock.getType())){
            player.sendBlockChange(sideBlock.getLocation(), sideBlock.getBlockData());
        }
        sideBlock = block.getLocation().getWorld().getBlockAt(block.getLocation().subtract(0, 0, 1));
        if(ores.contains(sideBlock.getType())){
            player.sendBlockChange(sideBlock.getLocation(), sideBlock.getBlockData());
        }
    }
}
