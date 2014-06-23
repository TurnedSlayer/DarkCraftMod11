package com.turnedslayer.darkcraft.blocks;

import com.turnedslayer.darkcraft.DarkCraft;
import com.turnedslayer.darkcraft.help.ItemHelper;
import com.turnedslayer.darkcraft.libs.References;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by TurnedSlayer.
 */
public class BlockDarkOre extends BlockDarkCraft {

    public BlockDarkOre() {
        super(Material.rock);
        this.setCreativeTab(DarkCraft.DarkCraftTab);
        this.setBlockTextureName(References.RESOURCESPREFIX + "blockDarkOre");
        GameRegistry.registerBlock(this, "blockDarkOre");
        this.setBlockName("blockDarkOre");
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();

        drops.add(2, new ItemStack(ItemHelper.ItemDarkGem));
        drops.add(new ItemStack(DarkCraft.BlockDarkOre));


        return drops;
    }
}
