package com.turnedslayer.darkcraft.help.Gui;

import com.turnedslayer.darkcraft.blocks.tiles.TileDarkBasicFurnace;
import com.turnedslayer.darkcraft.libs.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiBasicFurnace extends GuiContainer
{
    private TileDarkBasicFurnace DarkFurnace;
    private static final ResourceLocation backgroundimage = new ResourceLocation(References.MODID.toLowerCase() + ":" + "textures/gui/Dark Furnace.png");

    public GuiBasicFurnace(InventoryPlayer inventoryPlayer, TileDarkBasicFurnace tileDarkBasicFurnace)
    {
        super(new ContainerBasicFurnace(inventoryPlayer, tileDarkBasicFurnace));
        xSize = 176;
        ySize = 164;
        this.DarkFurnace = tileDarkBasicFurnace;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(backgroundimage);
        int var4 = (this.width - this.xSize)/2;
        int var5 = (this.height - this.ySize)/2;
        this.drawTexturedModalRect(var4, var5,0,0,this.xSize,this.ySize);
        int var6;
        if(this.DarkFurnace.isSmelting()){
            var6 = this.DarkFurnace.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(var4+56,var5+48-var6,176,12-var6,14,var6+2);
        }
        var6 = this.DarkFurnace.getCookProgressScaled(24);
        this.drawTexturedModalRect(var4+79,var5+34,176,14,var6+1,16);
    }
}