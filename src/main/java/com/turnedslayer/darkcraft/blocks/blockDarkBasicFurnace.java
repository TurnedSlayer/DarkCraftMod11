package com.turnedslayer.darkcraft.blocks;import com.turnedslayer.darkcraft.DarkCraft;import com.turnedslayer.darkcraft.blocks.tiles.TileDarkBasicFurnace;import com.turnedslayer.darkcraft.help.Textures;import cpw.mods.fml.common.registry.GameRegistry;import cpw.mods.fml.relauncher.Side;import cpw.mods.fml.relauncher.SideOnly;import net.minecraft.block.BlockContainer;import net.minecraft.block.material.Material;import net.minecraft.client.renderer.texture.IIconRegister;import net.minecraft.entity.EntityLivingBase;import net.minecraft.entity.player.EntityPlayer;import net.minecraft.item.ItemStack;import net.minecraft.tileentity.TileEntity;import net.minecraft.util.IIcon;import net.minecraft.util.MathHelper;import net.minecraft.world.World;import net.minecraftforge.common.util.ForgeDirection;public class BlockDarkBasicFurnace extends BlockContainer{    private static boolean isActive;    private static boolean keepInventory;    @SideOnly(Side.CLIENT)    public IIcon[] icons;    public BlockDarkBasicFurnace()    {        super(Material.iron);        this.setBlockName("blockDarkBasicFurnace");        this.setCreativeTab(DarkCraft.DarkCraftTab);        GameRegistry.registerBlock(this, "blockDarkBasicFurnace");    }    @Override    public boolean hasTileEntity(int meta)    {        return true;    }    @Override    public TileEntity createNewTileEntity(World var1, int var2)    {        return new TileDarkBasicFurnace();    }    @Override    @SideOnly(Side.CLIENT)    public void registerBlockIcons(IIconRegister iconRegister)    {        this.blockIcon = iconRegister.registerIcon(String.valueOf(Textures.BasicFurnace));        icons = new IIcon[4];        for(int count = 0; count < icons.length; count++)        {            String name;            switch(count)            {                case 0: name = "0"; break;                case 1: name = "1"; break;                case 2: name = "2"; break;                default: name = "0";            }            icons[count] = iconRegister.registerIcon(Textures.BasicFurnace + name);        }    }    @Override    @SideOnly(Side.CLIENT)    public IIcon getIcon(int side, int meta)    {        //front icon        if(side == meta){ return icons[0]; }        //top icon        else if (side == ForgeDirection.UP.ordinal()){ return icons[1]; }        //bottom icon        else if (side == ForgeDirection.DOWN.ordinal()){ return icons[1]; }        //sides icon        else { return icons[2]; }    }    @Override    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)    {        world.scheduleBlockUpdate(x, y, z, this, 1);        int direction = 0;        int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;        if (facing == 0)        {            direction = ForgeDirection.NORTH.ordinal();        }        else if (facing == 1)        {            direction = ForgeDirection.EAST.ordinal();        }        else if (facing == 2)        {            direction = ForgeDirection.SOUTH.ordinal();        }        else if (facing == 3)        {            direction = ForgeDirection.WEST.ordinal();        }        world.setBlockMetadataWithNotify(x, y, z, direction, 0);    }    @Override    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par1, float par2, float par3, float par4)    {        if (!world.isRemote)        {            entityPlayer.openGui(DarkCraft.instance, 0, world, x, y, z);        }        return true;    }    @Override    public void onBlockDestroyedByPlayer(World p_149664_1_, int p_149664_2_, int p_149664_3_, int p_149664_4_, int p_149664_5_)    {        super.onBlockDestroyedByPlayer(p_149664_1_, p_149664_2_, p_149664_3_, p_149664_4_, p_149664_5_);    }    public static void updateBlockState(boolean active, World worldObj, int xCoord, int yCoord, int zCoord) {        int i = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);        TileEntity tileentity = worldObj.getTileEntity(xCoord, yCoord, zCoord);        keepInventory=true;        if(active){            worldObj.setBlock(xCoord, yCoord, zCoord, DarkCraft.blockDarkBasicFurnace);        }else{            worldObj.setBlock(xCoord, yCoord, zCoord, DarkCraft.blockDarkBasicFurnaceActive);        }        keepInventory=false;        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 2);        if(tileentity!=null){            tileentity.validate();            worldObj.setTileEntity(xCoord, yCoord, zCoord, tileentity);        }    }}