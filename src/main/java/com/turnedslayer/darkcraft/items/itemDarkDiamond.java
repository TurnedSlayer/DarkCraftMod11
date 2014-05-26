package com.turnedslayer.darkcraft.items;

import com.turnedslayer.darkcraft.DarkCraft;
import com.turnedslayer.darkcraft.help.ItemHelper;


/**
 * Created by TurnedSlayer.
 */
public class itemDarkDiamond extends itemDarkCraft{

 public itemDarkDiamond() {
     super();
     this.setUnlocalizedName("itemDarkDiamond");
     //setUnlocalizedName("itemDarkDiamond");
     this.setCreativeTab(DarkCraft.DarkCraftTab);
     ItemHelper.register(this);

 }
/*
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        icons = new IIcon[2];

        for (int i = 0; i < icons.length; i++)
        {
            icons[i] = par1IconRegister.registerIcon(DarkCraft.modid + ":" + (this.getUnlocalizedName().substring(5)) + i);
        }
    }

    public static final String[] names = new String[] { "first", "second" };

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
        int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 15);
        return super.getUnlocalizedName() + "." + names[i];
    }

    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return icons[par1];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int x = 0; x < 2; x++)
        {
            par3List.add(new ItemStack(this, 1, x));
        }
    }

*/
}
