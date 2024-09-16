package rs.v9.myessentials;

import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkUnloadEvent;

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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event){
        if(isXRay()){
            if(!event.isCancelled()){
                if(xray.containsKey(event.getPlayer())){
                    isNextToOre(event.getPlayer(), event.getBlock());
                }
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
