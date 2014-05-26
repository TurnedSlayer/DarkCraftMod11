package com.turnedslayer.darkcraft.blocks;

/**
 * Created by turnedslayer.
 */
import com.turnedslayer.darkcraft.DarkCraft;
import com.turnedslayer.darkcraft.help.BlockHelper;
import com.turnedslayer.darkcraft.libs.Strings;
import net.minecraft.block.material.Material;


public class blockDarkDiamondBricks extends blockDarkCraft
{
    public blockDarkDiamondBricks()
    {
        super(Material.rock);
        this.setBlockName(Strings.blockDarkDiamondBricks);
        this.setCreativeTab(DarkCraft.DarkCraftTab);
        BlockHelper.register(this);
    }
}
