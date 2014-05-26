package com.turnedslayer.darkcraft.help.Gui;

import com.turnedslayer.darkcraft.blocks.TileDarkBasicFurnace;
import com.turnedslayer.darkcraft.libs.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBasicFurnace extends GuiContainer
{
    /**
     * See TestGUI
     */
    private static final ResourceLocation backgroundimage = new ResourceLocation(References.MODID.toLowerCase() + ":" + "textures/gui/BasicFurnace.png");

    public GuiBasicFurnace(InventoryPlayer inventoryPlayer, TileDarkBasicFurnace tileDarkBasicFurnace)
    {
        super(new ContainerBasicFurnace(inventoryPlayer, tileDarkBasicFurnace));
        xSize = 250;
        ySize = 250;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        //Bind Texture
        this.mc.getTextureManager().bindTexture(backgroundimage);
        // set the x for the texture, Total width - textureSize / 2
        par2 = (this.width - xSize) / 2;
        // set the y for the texture, Total height - textureSize - 30 (up) / 2,
        int j = (this.height - ySize) / 2;
        // draw the texture
        drawTexturedModalRect(par2, j, 0, 0, xSize,  ySize);
    }
}