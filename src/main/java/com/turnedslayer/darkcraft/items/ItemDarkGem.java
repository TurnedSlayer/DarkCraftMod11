package com.turnedslayer.darkcraft.items;

import com.turnedslayer.darkcraft.DarkCraft;
import com.turnedslayer.darkcraft.help.ItemHelper;
import com.turnedslayer.darkcraft.libs.References;
import net.minecraft.client.renderer.texture.IIconRegister;


/**
 * Created by TurnedSlayer.
 */
public class ItemDarkGem extends ItemDarkCraft {

 public ItemDarkGem()
 {
     super();
     this.setUnlocalizedName("itemDarkGem");
     this.setCreativeTab(DarkCraft.DarkCraftTab);
     ItemHelper.register(this);

 }

}
