package exnihilo.items.hammers;

import exnihilo.data.ItemData;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHammerDiamond extends ItemHammerBase {
  public ItemHammerDiamond() {
    super(Item.ToolMaterial.EMERALD);
  }

  @Override
  public String getUnlocalizedName() {
    return "exnihilo." + ItemData.HAMMER_UNLOCALIZED_NAMES[4];
  }

  @Override
  public String getUnlocalizedName(ItemStack item) {
    return "exnihilo." + ItemData.HAMMER_UNLOCALIZED_NAMES[4];
  }

  @Override
  public void registerIcons(IIconRegister register) {
    this.itemIcon = register.registerIcon("exnihilo:HammerDiamond");
  }
}
