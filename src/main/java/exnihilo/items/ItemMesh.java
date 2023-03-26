package exnihilo.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMesh extends Item {
  public ItemMesh() {
    setCreativeTab(CreativeTabs.tabMisc);
  }

  @Override
  public String getUnlocalizedName() {
    return "exnihilo.mesh";
  }

  @Override
  public String getUnlocalizedName(ItemStack item) {
    return "exnihilo.mesh";
  }

  @Override
  public void registerIcons(IIconRegister register) {
    this.itemIcon = register.registerIcon("exnihilo:IconMesh");
  }
}
