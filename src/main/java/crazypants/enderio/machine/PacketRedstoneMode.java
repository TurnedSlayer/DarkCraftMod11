package crazypants.enderio.machine;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import crazypants.enderio.network.IPacketEio;

public class PacketRedstoneMode implements IPacketEio {

  private int x;
  private int y;
  private int z;
  private RedstoneControlMode mode;

  public PacketRedstoneMode() {
  }

  public PacketRedstoneMode(IRedstoneModeControlable cont, int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
    mode = cont.getRedstoneControlMode();
  }

  public PacketRedstoneMode(AbstractMachineEntity ent) {
    x = ent.xCoord;
    y = ent.yCoord;
    z = ent.zCoord;
    mode = ent.getRedstoneControlMode();
  }

  @Override
  public void encode(ChannelHandlerContext ctx, ByteBuf buf) {
    buf.writeInt(x);
    buf.writeInt(y);
    buf.writeInt(z);
    buf.writeShort((short) mode.ordinal());
  }

  @Override
  public void decode(ChannelHandlerContext ctx, ByteBuf buf) {
    x = buf.readInt();
    y = buf.readInt();
    z = buf.readInt();
    short ordinal = buf.readShort();
    mode = RedstoneControlMode.values()[ordinal];
  }

  @Override
  public void handleClientSide(EntityPlayer player) {
    handle(player);
  }

  @Override
  public void handleServerSide(EntityPlayer player) {
    handle(player);
  }

  private void handle(EntityPlayer player) {
    TileEntity te = player.worldObj.getTileEntity(x, y, z);
    if(te instanceof IRedstoneModeControlable) {
      IRedstoneModeControlable me = (IRedstoneModeControlable) te;
      me.setRedstoneControlMode(mode);
      player.worldObj.markBlockForUpdate(x, y, z);
    }
  }

}
