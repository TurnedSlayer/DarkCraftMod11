package com.turnedslayer.darkcraft.blocks;

import com.turnedslayer.darkcraft.DarkCraft;
import com.turnedslayer.darkcraft.help.BlockHelper;
import com.turnedslayer.darkcraft.libs.BlockData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by TurnedSlayer.
 */
@BlockData(itemBlockClass = ItemblockDarkFlux.class)
public class blockDarkFlux extends blockDarkCraft{
    @SideOnly(Side.CLIENT)
    public IIcon[] icons;

    public blockDarkFlux()
    {
        super(Material.iron);
        GameRegistry.registerBlock(this, "block");
        this.setCreativeTab(DarkCraft.DarkCraftTab);
        setBlockName("block");
        icons = new IIcon[6];


    }

    @Override
    public IIcon getIcon (int side, int meta)
    {
        switch (meta % 2)
        {
            case 0:
                return icons[1];
            case 1:
                return icons[0];
            default:
                break;
        }
        return super.getIcon(side, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        for(int i = 0; i < icons.length; i++)
        {
            String name;
            switch(i)
            {
                case 0: name = "0"; break;
                case 1: name = "1"; break;
                default: name = "0";
            }
            icons[i] = iconRegister.registerIcon(getUnwrappedUnlocalizedName(super.getUnlocalizedName()) + name);
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List par3list)
    {
        for (int i = 0; i < 2; i++)
        {
            par3list.add(new ItemStack(item, 1, i));
        }
    }

    public static final String[] NAMES = { "DarkFlux", "DarkFluxCrystal" };

    public static ItemStack blockDarkFlux;
    public static ItemStack blockDarkFluxCrystal;

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {

        return false;
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {

        return true;
    }


    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (!world.isRemote)
        {
            if (world.isBlockIndirectlyGettingPowered(x, y, z) && world.getBlockMetadata(x, y, z) == 0)
            {
                world.setBlock(x, y, z, BlockHelper.blockDarkFlux, 1, 2);
            }
            if(!world.isBlockIndirectlyGettingPowered(x, y, z) && world.getBlockMetadata(x, y, z) == 1)
            {
                world.setBlock(x, y, z, BlockHelper.blockDarkFluxCrystal, 0, 2);
            }
        }
    }



}
