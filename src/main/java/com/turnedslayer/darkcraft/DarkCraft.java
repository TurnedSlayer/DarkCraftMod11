package com.turnedslayer.darkcraft;

import com.turnedslayer.darkcraft.blocks.BlockDarkBasicFurnace;
import com.turnedslayer.darkcraft.blocks.BlockDarkGlass;
import com.turnedslayer.darkcraft.blocks.BlockDarkOre;
import com.turnedslayer.darkcraft.blocks.tiles.TileDarkBasicFurnace;
import com.turnedslayer.darkcraft.help.BlockHelper;
import com.turnedslayer.darkcraft.help.Gui.GuiHandler;
import com.turnedslayer.darkcraft.help.ItemHelper;
import com.turnedslayer.darkcraft.help.worldGen.EventManager;
import com.turnedslayer.darkcraft.libs.References;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;


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
    // public static Block blockFancy;
    public static Block blockDarkBasicFurnaceActive;
    public static Block BlockDarkOre;

    //Items
    public static Item BluesteelHelmet;
    public static Item BluesteelChest;
    public static Item BluesteelLeggings;
    public static Item BluesteelBoots;
    public static Item BluesteelPickaxe;
    public static Item BluesteelAxe;
    public static Item BluesteelSword;
    public static Item BluesteelShovel;
    public static Item BluesteelHoe;

    static ItemArmor.ArmorMaterial BluesteelArmorMaterial = EnumHelper.addArmorMaterial("BluesteelArmorMaterial", 50, new int[]{6, 16, 12, 6}, 30);

    @Mod.Metadata
    public static ModMetadata metadata;

    @Mod.Instance
    public static DarkCraft instance;


    @Mod.EventHandler()
    public void preInit(FMLPreInitializationEvent event)
    {
        //Blocks
        blockDarkGlass = new BlockDarkGlass().setBlockName("blockDarkGlass");
        GameRegistry.registerBlock(blockDarkGlass, "blockDarkGlass");
        blockDarkBasicFurnace = new BlockDarkBasicFurnace().setBlockName("blockDarkBasicFurnace");
        BlockDarkOre = new BlockDarkOre().setBlockName("blockDarkOre");

        //Armor
        BluesteelHelmet = new com.turnedslayer.darkcraft.items.armour.BluesteelHelmet(BluesteelArmorMaterial, 0);
        GameRegistry.registerItem(BluesteelHelmet, "Bluesteel Helmet");
        BluesteelChest = new com.turnedslayer.darkcraft.items.armour.BluesteelChest(BluesteelArmorMaterial, 1);
        GameRegistry.registerItem(BluesteelChest, "Bluesteel Chest");
        BluesteelLeggings = new com.turnedslayer.darkcraft.items.armour.BluesteelLeggings(BluesteelArmorMaterial, 2);
        GameRegistry.registerItem(BluesteelLeggings, "Bluesteel Leg");
        BluesteelBoots = new com.turnedslayer.darkcraft.items.armour.BluesteelBoots(BluesteelArmorMaterial, 3);
        GameRegistry.registerItem(BluesteelBoots, "Bluesteel Boots");



        BlockHelper.init();

        ItemHelper.init();

        GameRegistry.registerWorldGenerator(new EventManager(), 0);



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