package com.turnedslayer.darkcraft.help;

import com.turnedslayer.darkcraft.items.ItemBluesteelIngot;
import com.turnedslayer.darkcraft.items.ItemDarkCraft;
import com.turnedslayer.darkcraft.items.ItemDarkGem;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;


public class ItemHelper
{
    public static Item itemDarkDiamond;
    public static Item ItemBluesteelIngot;





    public static void init()
    {

        itemDarkDiamond = new ItemDarkGem();
        ItemBluesteelIngot = new ItemBluesteelIngot();




    }


    public static void register(ItemDarkCraft item)
    {
        GameRegistry.registerItem(item, item.getUnwrappedUnlocalizedName(item.getUnlocalizedName()));
    }



}