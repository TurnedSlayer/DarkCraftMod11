package com.turnedslayer.darkcraft.help.Gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Created by TurnedSlayer.
 */
public class SlotIn extends Slot
{
    public SlotIn(IInventory inventory, int slotIndex, int x, int y)
    {
        super(inventory, slotIndex, x, y);
    }
}
