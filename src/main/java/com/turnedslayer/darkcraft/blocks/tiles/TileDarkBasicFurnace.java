package com.turnedslayer.darkcraft.blocks.tiles;

import cofh.api.energy.EnergyStorage;
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
import net.minecraft.tileentity.TileEntity;

/**
 * Created by TurnedSlayer.
 */
public class TileDarkBasicFurnace extends TileEntity
{

    private final EnergyStorage storage;
    private String localizedName;
    private ItemStack[] slots = new ItemStack[3];
    private static final int[] slots_top = new int[]{0};
    private static final int[] slots_sides = new int[]{1};
    private static final int[] slots_bottom = new int[]{2,1};
    public int furnaceSpeed = 200;
    public int burnTime;
    public int currentItemSmeltingTime;
    public int smeltingTime;

    public TileDarkBasicFurnace()
    {
        super();
        this.storage = new EnergyStorage(32000, 10000);


    }

    public int getSizeInventory(){
        return this.slots.length;
    }

    public ItemStack getStackInSlot(int i)
    {
        return this.slots[i];
    }

    public ItemStack decrStackSize(int i, int j) {
        if(this.slots[i]!=null){
            ItemStack itemstack;
            if(this.slots[i].stackSize<=j){
                itemstack=this.slots[i];
                this.slots[i]=null;
                return itemstack;
            }else{
                itemstack=this.slots[i].splitStack(j);
                if(this.slots[i].stackSize==0){
                    this.slots[i]=null;
                }
                return itemstack;
            }
        }
        return null;
    }

    public ItemStack getStackInSlotOnClosing(int i) {
        if(this.slots[i]!=null){
            ItemStack itemstack=this.slots[i];
            this.slots[i]=null;
            return itemstack;
        }
        return null;
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        this.slots[i]=itemstack;
        if(itemstack!=null && itemstack.stackSize>this.getInventoryStackLimit()){
            itemstack.stackSize=this.getInventoryStackLimit();
        }
    }

    public int getInventoryStackLimit() {
    return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord)!=this ? false : entityplayer.getDistanceSq((double)this.xCoord+0.5D, (double)this.yCoord+0.5D, (double)this.zCoord+0.5D)<=64.0D;
    }

     public void closeChest() {

    }

    public boolean isSmelting(){
        return this.burnTime>0;
    }

    private boolean canGrind(){
        if(this.slots[0] == null){
            return false;
        }else{
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);

            if(itemstack == null) return false;
            if(this.slots[2] == null) return true;
            if(!this.slots[2].isItemEqual(itemstack)) return false;

            int result = this.slots[2].stackSize+itemstack.stackSize;

            return (result<=getInventoryStackLimit()&&result<=itemstack.getMaxStackSize());
        }
    }

    private void grindItem(){
        if(this.canGrind()){
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.slots[0]);
            if(this.slots[2]==null){
                this.slots[2]=itemstack.copy();
            }else if(this.slots[2].isItemEqual(itemstack)){
                this.slots[2].stackSize+=itemstack.stackSize;
            }
            this.slots[0].stackSize--;

            if(this.slots[0].stackSize<=0){
                this.slots[0]=null;
            }
        }
    }

    public void updateEntity(){
        boolean flag = this.burnTime >0;
        boolean flag1 = false;

        if(this.burnTime>0){
            this.burnTime--;
        }

        if(!this.worldObj.isRemote){
            if(this.burnTime==0 && this.canGrind()){
                this.currentItemSmeltingTime = this.burnTime = getItemGrindTime(this.slots[1]);
                if(this.burnTime>0){
                    flag1=true;
                    if(this.slots[1] !=null){
                        this.slots[1].stackSize--;
                        if(this.slots[1].stackSize == 0){
                            this.slots[1] = this.slots[1].getItem().getContainerItem(this.slots[1]);
                        }
                    }
                }
            }

            if(this.isSmelting()&&canGrind()){
                this.smeltingTime++;

                if(this.smeltingTime == this.furnaceSpeed){
                    this.smeltingTime=0;
                    this.grindItem();
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
            this.onInventory();
        }
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return i==2 ? false : (i==1 ? isItemFuel(itemstack):true);
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
        return var1==0 ? slots_bottom : (var1==1 ? slots_top : slots_sides);
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

    }
    public void writeToNBT(NBTTagCompound nbt){

    }



}