package com.turnedslayer.darkcraft.help.Gui;

import com.turnedslayer.darkcraft.blocks.tiles.TileDarkBasicFurnace;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;


public class ContainerBasicFurnace extends Container
{
    private TileDarkBasicFurnace tile;

    public ContainerBasicFurnace(InventoryPlayer inventory, TileDarkBasicFurnace tileDarkBasicFurnace)
    {
        tile = tileDarkBasicFurnace;
        bindPlayerInventory(inventory);
    }

    private void bindPlayerInventory(InventoryPlayer inventoryPlayer)
    {
        int id = 0;
        int id2 = 0;

        for(int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, id, i * 18 + 8, 189)); //Adds player hotbar
            id++;
        }
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inventoryPlayer, id ,j * 18 + 8, i * 18 + 131 )); //Adds player inventory
                id++;
            }
        }

        for(int i = 0; i < 3; i ++)
        {
            for(int j = 0; j < 2; j++)
            {
                addSlotToContainer(new SlotIn(tile, id2, i * 18 + 62, j * 18 + 21)); //Adds custon slots
                id2++;
            }
        }
        addSlotToContainer(new SlotIn(tile, id2, 81, 95)); //Adds custom output
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

}