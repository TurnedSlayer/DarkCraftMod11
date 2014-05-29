package com.turnedslayer.darkcraft;

import com.turnedslayer.darkcraft.blocks.itemblocks.ItemblockFancy;
import com.turnedslayer.darkcraft.blocks.itemblocks.ItemblockDarkFlux;
import com.turnedslayer.darkcraft.blocks.blockDarkFlux;
import com.turnedslayer.darkcraft.blocks.blockDarkGlass;
import com.turnedslayer.darkcraft.blocks.tiles.TileDarkBasicFurnace;
import com.turnedslayer.darkcraft.help.Gui.GuiHandler;
import com.turnedslayer.darkcraft.libs.References;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import com.turnedslayer.darkcraft.help.BlockHelper;
import com.turnedslayer.darkcraft.help.ItemHelper;
import net.minecraft.block.Block;
import com.turnedslayer.darkcraft.blocks.blockDarkBasicFurnace;
import com.turnedslayer.darkcraft.blocks.blockFancy;
//import com.turnedslayer.darkcraft.blocks.blockDarkBasicFurnaceActive;

@Mod(modid="turnedslayer_darkcraft", name="darkcraft Mod", version="0.0.1")
public class DarkCraft{


    public static final String modid = "turnedslayer_darkcraft";

    public static CreativeTabs DarkCraftTab = new DarkCraftTab(CreativeTabs.getNextID(), References.MODID);

    public static CreativeTabs getCreativeTab()
    {
        return DarkCraftTab;
    }

    //blocks
    public static Block blockDarkGlass;
    public static Block blockDarkBasicFurnace;
    public static Block blockDarkFlux;
    public static Block blockFancy;
    public static Block blockDarkBasicFurnaceActive;

    @Mod.Metadata
    public static ModMetadata metadata;

    @Mod.Instance
    public static DarkCraft instance;


    @Mod.EventHandler()
    public void preInit(FMLPreInitializationEvent event)
    {
        //Blocks
        blockDarkGlass = new blockDarkGlass().setBlockName("blockDarkGlass");
        GameRegistry.registerBlock(blockDarkGlass, "blockDarkGlass");

        blockDarkBasicFurnace = new blockDarkBasicFurnace().setBlockName("blockDarkBasicFurnace");
        //GameRegistry.registerBlock(blockDarkBasicFurnace, "blockDarkBasicFurnace");
        blockDarkFlux = new blockDarkFlux().setBlockName("blockDarkFlux").setCreativeTab(DarkCraft.DarkCraftTab);
        GameRegistry.registerBlock(blockDarkFlux, ItemblockDarkFlux.class, blockDarkFlux.getUnlocalizedName().replace("tile.",""));
        blockFancy = new blockFancy().setBlockName("blockFancy");
        GameRegistry.registerBlock(blockFancy, ItemblockFancy.class, blockFancy.getUnlocalizedName().replace("tile.", ""));

        BlockHelper.init();

        ItemHelper.init();

    }


    @Mod.EventHandler()
    public void init(FMLInitializationEvent event)
    {
        //Gui Handler
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        //Tile Entitys
        GameRegistry.registerTileEntity(TileDarkBasicFurnace.class, "TileDarkBasicFurnace");
    }


    @Mod.EventHandler()
    public void postInit(FMLPostInitializationEvent event)
    {

    }

}