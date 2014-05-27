package com.turnedslayer.darkcraft.help.Gui;

import com.turnedslayer.darkcraft.blocks.tiles.TileDarkBasicFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;



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
        this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileDarkBasicFurnace, 2, 116, 35));
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

    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0, this.tileDarkBasicFurnace.smeltingTime);
        par1ICrafting.sendProgressBarUpdate(this, 1, this.tileDarkBasicFurnace.burnTime);
        par1ICrafting.sendProgressBarUpdate(this, 2, this.tileDarkBasicFurnace.currentItemSmeltingTime);
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1)
    {
        return true;
    }
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int slotIndex)
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void upgradeProgressBar(int slot, int par2){
        if(slot==0) this.tileDarkBasicFurnace.smeltingTime=par2;
        if(slot==1)this.tileDarkBasicFurnace.burnTime=par2;
        if(slot==2)this.tileDarkBasicFurnace.currentItemSmeltingTime=par2;
    }
    public ItemStack transferStackInSlot(EntityPlayer player){
        return null;
    }

}