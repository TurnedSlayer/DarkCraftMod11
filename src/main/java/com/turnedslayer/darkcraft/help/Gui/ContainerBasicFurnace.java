package com.turnedslayer.darkcraft.help.Gui;

import com.turnedslayer.darkcraft.blocks.tiles.TileDarkBasicFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;


public class ContainerBasicFurnace extends Container
{

    private TileDarkBasicFurnace tileDarkBasicFurnace;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private static final String __OBFID = "CL_00001748";

    public ContainerBasicFurnace(InventoryPlayer par1InventoryPlayer, TileDarkBasicFurnace par2TileDarkBasicFurnace)
    {
        this.tileDarkBasicFurnace = par2TileDarkBasicFurnace;
        this.addSlotToContainer(new Slot(par2TileDarkBasicFurnace, 0, 56, 17));
        //this.addSlotToContainer(new Slot(par2TileDarkBasicFurnace, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileDarkBasicFurnace, 1, 116, 35));
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
        }
    }


    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 2)
            {
                if (!this.mergeItemStack(itemstack1, 2, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 != 1 && par2 != 0)
            {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 2, 39, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }

    @SideOnly(Side.CLIENT)
    public void upgradeProgressBar(int slot, int par2){
        if(slot==0) this.tileDarkBasicFurnace.smeltingTime=par2;
        if(slot==1)this.tileDarkBasicFurnace.burnTime=par2;
        if(slot==2)this.tileDarkBasicFurnace.currentItemSmeltingTime=par2;
    }

    @SideOnly(Side.CLIENT)
    public void PowerMeter(int power, int par2)
    {

    }

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.tileDarkBasicFurnace.smeltingTime);
        par1ICrafting.sendProgressBarUpdate(this, 1, this.tileDarkBasicFurnace.burnTime);
        par1ICrafting.sendProgressBarUpdate(this, 2, this.tileDarkBasicFurnace.currentItemSmeltingTime);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.lastCookTime != this.tileDarkBasicFurnace.smeltingTime)
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tileDarkBasicFurnace.smeltingTime);
            }

            if (this.lastBurnTime != this.tileDarkBasicFurnace.burnTime)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.tileDarkBasicFurnace.burnTime);
            }

            if (this.lastItemBurnTime != this.tileDarkBasicFurnace.currentItemSmeltingTime)
            {
                icrafting.sendProgressBarUpdate(this, 2, this.tileDarkBasicFurnace.currentItemSmeltingTime);
            }
        }

        this.lastCookTime = this.tileDarkBasicFurnace.smeltingTime;
        this.lastBurnTime = this.tileDarkBasicFurnace.burnTime;
        this.lastItemBurnTime = this.tileDarkBasicFurnace.currentItemSmeltingTime;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            this.tileDarkBasicFurnace.smeltingTime = par2;
        }

        if (par1 == 1)
        {
            this.tileDarkBasicFurnace.burnTime = par2;
        }

        if (par1 == 2)
        {
            this.tileDarkBasicFurnace.currentItemSmeltingTime = par2;
        }
    }


}