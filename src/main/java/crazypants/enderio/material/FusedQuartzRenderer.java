package crazypants.enderio.material;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import crazypants.enderio.EnderIO;
import crazypants.enderio.machine.painter.PainterUtil;
import crazypants.enderio.machine.painter.TileEntityCustomBlock;
import crazypants.render.ConnectedTextureRenderer;
import crazypants.render.ConnectedTextureRenderer.DefaultTextureCallback;
import crazypants.render.CustomCubeRenderer;
import crazypants.render.RenderUtil;

public class FusedQuartzRenderer implements ISimpleBlockRenderingHandler {

  static int renderPass;

  private ConnectedTextureRenderer connectedTextureRenderer = new ConnectedTextureRenderer();

  @Override
  public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
    renderer.setOverrideBlockTexture(EnderIO.blockFusedQuartz.getItemIcon(metadata));
    renderer.renderBlockAsItem(Block.glass, 0, 1);
    renderer.clearOverrideBlockTexture();
  }

  @Override
  public boolean shouldRender3DInInventory() {
    return true;
  }

  @Override
  public int getRenderId() {
    return BlockFusedQuartz.renderId;
  }

  @Override
  public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    int meta = blockAccess.getBlockMetadata(x, y, z);
    if((meta == 0 && renderPass != 0) || (meta == 1 && renderPass == 0)) {
      TileEntityCustomBlock tecb = null;
      TileEntity te = blockAccess.getBlockTileEntity(x, y, z);
      if(te instanceof TileEntityCustomBlock) {
        tecb = (TileEntityCustomBlock) te;
      }
      renderFrame(blockAccess, x, y, z, tecb, false, meta);
    }
    return true;
  }

  public void renderFrameItem(ItemStack stack) {
    RenderUtil.bindBlockTexture();
    Tessellator.instance.startDrawingQuads();
    TileEntityCustomBlock tecb = new TileEntityCustomBlock();
    tecb.setSourceBlockId(PainterUtil.getSourceBlockId(stack));
    tecb.setSourceBlockMetadata(PainterUtil.getSourceBlockMetadata(stack));
    renderFrame(null, 0, 0, 0, tecb, true, stack.getItemDamage());
    Tessellator.instance.draw();
  }

  private void renderFrame(IBlockAccess blockAccess, int x, int y, int z, TileEntityCustomBlock tecb, boolean forceAllEdges, int meta) {

    if(blockAccess == null) {
      //No lighting
      Icon texture = EnderIO.blockFusedQuartz.getItemIcon(meta);
      for (ForgeDirection face : ForgeDirection.VALID_DIRECTIONS) {
        if(tecb != null && tecb.getSourceBlockId() > 0) {
          texture = tecb.getSourceBlock().getIcon(face.ordinal(), tecb.getSourceBlockMetadata());
        }
        RenderUtil.renderConnectedTextureFace(blockAccess, x, y, z, face, texture, forceAllEdges);
      }
      return;
    }

    CustomCubeRenderer.instance.setOverrideTexture(EnderIO.blockFusedQuartz.getIcon(0, meta));

    if(tecb != null && tecb.getSourceBlock() != null) {
      connectedTextureRenderer.setEdgeTexureCallback(new DefaultTextureCallback(tecb.getSourceBlock(), tecb.getSourceBlockMetadata()));
      CustomCubeRenderer.instance.renderBlock(blockAccess, EnderIO.blockFusedQuartz, x, y, z,
          connectedTextureRenderer);
    } else {
      connectedTextureRenderer.setEdgeTexture(EnderIO.blockFusedQuartz.getDefaultFrameIcon(meta));
      CustomCubeRenderer.instance.renderBlock(blockAccess, EnderIO.blockFusedQuartz, x, y, z, connectedTextureRenderer);
    }

    CustomCubeRenderer.instance.setOverrideTexture(null);
  }
}
