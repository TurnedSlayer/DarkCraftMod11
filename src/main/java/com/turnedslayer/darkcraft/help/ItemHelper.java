package com.turnedslayer.darkcraft.help;

import com.turnedslayer.darkcraft.items.itemDarkCraft;
import com.turnedslayer.darkcraft.items.itemDarkDiamond;
import com.turnedslayer.darkcraft.items.itemDarkFlux;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;


public class ItemHelper
{
    public static Item itemDarkDiamond;
    public static Item itemDarkFlux;

    public static void init()
    {
        itemDarkFlux = new itemDarkFlux();
        itemDarkDiamond = new itemDarkDiamond();



    }


    public static void register(itemDarkCraft item)
    {
        GameRegistry.registerItem(item, item.getUnwrappedUnlocalizedName(item.getUnlocalizedName()));
    }


}