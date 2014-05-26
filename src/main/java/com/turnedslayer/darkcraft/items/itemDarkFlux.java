package com.turnedslayer.darkcraft.items;

import com.turnedslayer.darkcraft.DarkCraft;
import com.turnedslayer.darkcraft.help.ItemHelper;
import net.minecraft.item.Item;

/**
 * Created by TurnedSlayer.
 */
public class itemDarkFlux extends itemDarkCraft {


    public itemDarkFlux() {
        super();
        this.setUnlocalizedName("itemDarkFlux");
        this.setCreativeTab(DarkCraft.DarkCraftTab);
        ItemHelper.register(this);
    }
}

