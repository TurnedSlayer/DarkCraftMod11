package com.turnedslayer.darkcraft.help;

import com.turnedslayer.darkcraft.items.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;


public class ItemHelper
{
    public static Item ItemDarkGem;
    public static Item ItemBluesteelIngot;
    public static Item ItemDiamondInfusedBluesteelIngot;
    public static Item ItemChargedIngot;






    public static void init()
    {


        ItemBluesteelIngot = new ItemBluesteelIngot();
        ItemDiamondInfusedBluesteelIngot = new ItemDiamondInfusedBluesteelIngot();
        ItemChargedIngot = new ItemChargedIngot();
        ItemDarkGem = new ItemDarkGem();



    }


    public static void register(ItemDarkCraft item)
    {
        GameRegistry.registerItem(item, item.getUnwrappedUnlocalizedName(item.getUnlocalizedName()));
    }



}