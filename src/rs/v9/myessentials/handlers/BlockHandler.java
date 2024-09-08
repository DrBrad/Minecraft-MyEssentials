package rs.v9.myessentials.handlers;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class BlockHandler {

    public static List<Material> getXRayBlocks(){
        Material[] tmp = {
                Material.DIAMOND_BLOCK,
                Material.IRON_BLOCK,
                Material.GOLD_BLOCK,
                Material.EMERALD_BLOCK,
                Material.NETHERITE_BLOCK,
                Material.LAPIS_BLOCK,
                Material.REDSTONE_BLOCK,
                Material.COAL_BLOCK,

                Material.DIAMOND_ORE,
                Material.IRON_ORE,
                Material.GOLD_ORE,
                Material.NETHER_GOLD_ORE,
                Material.EMERALD_ORE,
                Material.ANCIENT_DEBRIS,
                Material.LAPIS_ORE,
                Material.REDSTONE_ORE,
                Material.COAL_ORE,

                Material.DEEPSLATE_DIAMOND_ORE,
                Material.DEEPSLATE_IRON_ORE,
                Material.DEEPSLATE_GOLD_ORE,
                Material.DEEPSLATE_EMERALD_ORE,
                Material.DEEPSLATE_LAPIS_ORE,
                Material.DEEPSLATE_REDSTONE_ORE,
                Material.DEEPSLATE_COAL_ORE,

                Material.NETHER_GOLD_ORE,

                Material.SPAWNER,
                Material.CHEST,
                Material.TRAPPED_CHEST,
                Material.BLACK_SHULKER_BOX,
                Material.BLUE_SHULKER_BOX,
                Material.BROWN_SHULKER_BOX,
                Material.CYAN_SHULKER_BOX,
                Material.GRAY_SHULKER_BOX,
                Material.GREEN_SHULKER_BOX,
                Material.LIGHT_BLUE_SHULKER_BOX,
                Material.LIGHT_GRAY_SHULKER_BOX,
                Material.LIME_SHULKER_BOX,
                Material.MAGENTA_SHULKER_BOX,
                Material.ORANGE_SHULKER_BOX,
                Material.PINK_SHULKER_BOX,
                Material.PURPLE_SHULKER_BOX,
                Material.RED_SHULKER_BOX,
                Material.WHITE_SHULKER_BOX,
                Material.YELLOW_SHULKER_BOX,
                Material.SHULKER_BOX,
                Material.FURNACE,
                Material.HOPPER,
                Material.CRAFTING_TABLE
        };

        return Arrays.asList(tmp);
    }

    public static List<Material> getAir(){
        Material[] tmp = {
                Material.AIR,
                Material.CAVE_AIR,
                Material.VOID_AIR,
                Material.CHEST,
                Material.TRAPPED_CHEST,
                Material.HOPPER,
                Material.BLACK_SHULKER_BOX,
                Material.BLUE_SHULKER_BOX,
                Material.BROWN_SHULKER_BOX,
                Material.CYAN_SHULKER_BOX,
                Material.GRAY_SHULKER_BOX,
                Material.GREEN_SHULKER_BOX,
                Material.LIGHT_BLUE_SHULKER_BOX,
                Material.LIGHT_GRAY_SHULKER_BOX,
                Material.LIME_SHULKER_BOX,
                Material.MAGENTA_SHULKER_BOX,
                Material.ORANGE_SHULKER_BOX,
                Material.PINK_SHULKER_BOX,
                Material.PURPLE_SHULKER_BOX,
                Material.RED_SHULKER_BOX,
                Material.WHITE_SHULKER_BOX,
                Material.YELLOW_SHULKER_BOX,
                Material.SHULKER_BOX,
                Material.WATER,
                Material.LAVA,
                Material.BLACK_STAINED_GLASS,
                Material.BLUE_STAINED_GLASS,
                Material.BROWN_STAINED_GLASS,
                Material.CYAN_STAINED_GLASS,
                Material.GRAY_STAINED_GLASS,
                Material.GREEN_STAINED_GLASS,
                Material.LIGHT_BLUE_STAINED_GLASS,
                Material.LIGHT_GRAY_STAINED_GLASS,
                Material.LIME_STAINED_GLASS,
                Material.MAGENTA_STAINED_GLASS,
                Material.ORANGE_STAINED_GLASS,
                Material.PINK_STAINED_GLASS,
                Material.PURPLE_STAINED_GLASS,
                Material.RED_STAINED_GLASS,
                Material.WHITE_STAINED_GLASS,
                Material.YELLOW_STAINED_GLASS,
                Material.GLASS,
                Material.BLACK_STAINED_GLASS_PANE,
                Material.BLUE_STAINED_GLASS_PANE,
                Material.BROWN_STAINED_GLASS_PANE,
                Material.CYAN_STAINED_GLASS_PANE,
                Material.GRAY_STAINED_GLASS_PANE,
                Material.GREEN_STAINED_GLASS_PANE,
                Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                Material.LIGHT_GRAY_STAINED_GLASS_PANE,
                Material.LIME_STAINED_GLASS_PANE,
                Material.MAGENTA_STAINED_GLASS_PANE,
                Material.ORANGE_STAINED_GLASS_PANE,
                Material.PINK_STAINED_GLASS_PANE,
                Material.PURPLE_STAINED_GLASS_PANE,
                Material.RED_STAINED_GLASS_PANE,
                Material.WHITE_STAINED_GLASS_PANE,
                Material.YELLOW_STAINED_GLASS_PANE,
                Material.GLASS_PANE,
                Material.BEACON,
                Material.ENCHANTING_TABLE
        };

        return Arrays.asList(tmp);
    }
}
