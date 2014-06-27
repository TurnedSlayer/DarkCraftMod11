package com.turnedslayer.darkcraft.help;

import com.turnedslayer.darkcraft.blocks.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class BlockHelper
{
    public static Block blockDark;
    public static Block blockDarkDiamond;
    public static Block blockDarkDiamondBricks;
    //public static Block blockDarkGlass;
   // public static Block blockDarkBasicFurnace;
    public static Block blockDarkFlux;
    public static Block blockDarkFluxCrystal;
    public static Block blockWorldDarkOre;


public static void init()
{
    blockDarkDiamond = new BlockDarkDiamond().setBlockName("blockDarkDiamond");
    blockDark = new BlockDark().setBlockName("blockDark");
    blockDarkDiamondBricks = new BlockDarkDiamondBricks().setBlockName("blockDarkDiamondBricks");
    blockWorldDarkOre = new BlockWorldDarkOre().setBlockName("blockWorldDarkOre");


}

 /**
 * Registeres the blocks.
 * Any block calling this NEEDS to extend blockDarkCraft.
 * For registering a block, you need to call registerBlock and use
 * Format: [block], [unlocalizedname]
 * @param block
 */
    public static void register(BlockDarkCraft block)
    {
        GameRegistry.registerBlock(block, block.getUnwrappedUnlocalizedName(block.getUnlocalizedName()));

    }

    public static void registerBlocksWithMetadata(BlockDarkCraft block, Class<? extends ItemBlock> itemBlockClass, String name)
    {
        GameRegistry.registerBlock(block, itemBlockClass, name);
    }


}