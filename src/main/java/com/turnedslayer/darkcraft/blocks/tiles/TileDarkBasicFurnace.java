package com.turnedslayer.darkcraft.blocks.tiles;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.turnedslayer.darkcraft.blocks.blockDarkBasicFurnace;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by TurnedSlayer.
 */
public class TileDarkBasicFurnace extends TileEntity implements IInventory, IEnergyHandler
{

    protected EnergyStorage storage = new EnergyStorage(10000);
    private String localizedName;
    private static final int[] furnaceItemStacks_top = new int[]{0};
    private static final int[] furnaceItemStacks_sides = new int[]{1};
    private static final int[] furnaceItemStacks_bottom = new int[]{2,1};
    public int furnaceSpeed = 200;
    public int burnTime;
    public int currentItemSmeltingTime;
    public int smeltingTime;
    private ItemStack[] furnaceItemStacks = new ItemStack[2];
    private String field_145958_o;

    public TileDarkBasicFurnace()
    {
        super();



    }



    public ItemStack decrStackSize(int i, int j) {
        if(this.furnaceItemStacks[i]!=null){
            ItemStack itemstack;
            if(this.furnaceItemStacks[i].stackSize<=j){
                itemstack=this.furnaceItemStacks[i];
                this.furnaceItemStacks[i]=null;
                return itemstack;
            }else{
                itemstack=this.furnaceItemStacks[i].splitStack(j);
                if(this.furnaceItemStacks[i].stackSize==0){
                    this.furnaceItemStacks[i]=null;
                }
                return itemstack;
            }
        }
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int i) {
        if(this.furnaceItemStacks[i]!=null){
            ItemStack itemstack=this.furnaceItemStacks[i];
            this.furnaceItemStacks[i]=null;
            return itemstack;
        }
        return null;
    }



    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        this.furnaceItemStacks[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    public int getInventoryStackLimit() {
    return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && entityplayer.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2) {
        return false;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? this.field_145958_o : "ContainerBasicFurnace";
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.furnaceItemStacks[par1];
    }

    public int getSizeInventory()
    {
        return this.furnaceItemStacks.length;
    }

    public boolean isSmelting(){
        return this.burnTime>0;
    }
/*
    private boolean canGrind(){
        if(this.furnaceItemStacks[0] == null){
            return false;
        }else{
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);

            if(itemstack == null) return false;
            if(this.furnaceItemStacks[2] == null) return true;
            if(!this.furnaceItemStacks[2].isItemEqual(itemstack)) return false;

            int result = this.furnaceItemStacks[2].stackSize+itemstack.stackSize;

            return (result<=getInventoryStackLimit()&&result<=itemstack.getMaxStackSize());
        }
    }

    private void grindItem(){
        if(this.canGrind()){
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
            if(this.furnaceItemStacks[2]==null){
                this.furnaceItemStacks[2]=itemstack.copy();
            }else if(this.furnaceItemStacks[2].isItemEqual(itemstack)){
                this.furnaceItemStacks[2].stackSize+=itemstack.stackSize;
            }
            this.furnaceItemStacks[0].stackSize--;

            if(this.furnaceItemStacks[0].stackSize<=0){
                this.furnaceItemStacks[0]=null;
            }
        }
    }
*/
    public void updateEntity(){
        boolean flag = this.burnTime >0;
        boolean flag1 = false;

        if(this.burnTime>0){
            this.burnTime--;
        }

        if(!this.worldObj.isRemote){
            if(this.burnTime==0 ){
                this.currentItemSmeltingTime = this.burnTime = getItemGrindTime(this.furnaceItemStacks[1]);
                if(this.burnTime>0){
                    flag1=true;
                    if(this.furnaceItemStacks[1] !=null){
                        this.furnaceItemStacks[1].stackSize--;
                        if(this.furnaceItemStacks[1].stackSize == 0){
                            this.furnaceItemStacks[1] = this.furnaceItemStacks[1].getItem().getContainerItem(this.furnaceItemStacks[1]);
                        }
                    }
                }
            }

            if(this.isSmelting()){
                this.smeltingTime++;

                if(this.smeltingTime == this.furnaceSpeed){
                    this.smeltingTime=0;
                    //this.grindItem();
                    flag1=true;
                }
            }else{
                this.smeltingTime=0;
            }

            if(flag!=this.burnTime>0){
                flag1=true;
                blockDarkBasicFurnace.updateBlockState(this.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }
        if(flag1){

            this.markDirty();



        }
    }



    private boolean isItemFuel(ItemStack itemstack) {
        return getItemGrindTime(itemstack)>0;
    }

    private int getItemGrindTime(ItemStack itemstack) {
        if(itemstack==null){
            return 0;
        }else{
            int i=itemstack.stackSize;
            Item item = itemstack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                    return 150;
                }

                if (block.getMaterial() == Material.wood)
                {
                    return 300;
                }

                if (block == Blocks.coal_block)
                {
                    return 16000;
                }
            }

            if(item instanceof ItemTool &&((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
            if(item instanceof ItemSword &&((ItemSword)item).getToolMaterialName().equals("WOOD"))return 200;
            if(item instanceof ItemHoe && ((ItemHoe)item).equals("WOOD"))return 200;
            if(item == Items.stick) return 100;
            if(item == Items.coal) return 1600;
            if(item == Items.lava_bucket)return 20000;
            if(item == Items.blaze_rod)return 2400;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;


            return GameRegistry.getFuelValue(itemstack);
        }
    }

    public int[] getAccessibleSlotsFromSide(int var1) {
        return var1==0 ? furnaceItemStacks_bottom : (var1==1 ? furnaceItemStacks_top : furnaceItemStacks_sides);
    }

    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return this.isItemValidForSlot(i, itemstack);
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j!=0 || i!=1 || itemstack.getItem()==Items.bucket;
    }

    public int getBurnTimeRemainingScaled(int i) {
        if(this.currentItemSmeltingTime==0){
            this.currentItemSmeltingTime=this.furnaceSpeed;
        }
        return this.burnTime*i/this.currentItemSmeltingTime;
    }

    public int getCookProgressScaled(int i) {
        return this.smeltingTime*i/this.furnaceSpeed;
    }

    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        storage.readFromNBT(nbt);

    }
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        storage.writeToNBT(nbt);
    }


    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return storage.receiveEnergy(maxReceive, false);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return storage.extractEnergy(maxExtract, false);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return storage.getEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return true;
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();

        writeToNBT(nbtTag);

        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }
}
