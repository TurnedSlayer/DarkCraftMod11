package com.turnedslayer.darkcraft.blocks;

/**
 * Created by turnedslayer.
 */
import com.turnedslayer.darkcraft.DarkCraft;
import com.turnedslayer.darkcraft.help.BlockHelper;
import com.turnedslayer.darkcraft.libs.Strings;
import net.minecraft.block.material.Material;




public class blockDarkDiamond extends blockDarkCraft
{
    public blockDarkDiamond()
    {
        super(Material.rock);
        this.setBlockName(Strings.blockDarkDiamond);
        BlockHelper.register(this);
        this.setCreativeTab(DarkCraft.DarkCraftTab);

    }
}
