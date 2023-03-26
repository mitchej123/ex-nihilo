package exnihilo.items.hammers;

import exnihilo.data.ItemData;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHammerWood extends ItemHammerBase {
  public ItemHammerWood() {
    super(Item.ToolMaterial.WOOD);
  }

  @Override
  public String getUnlocalizedName() {
    return "exnihilo." + ItemData.HAMMER_UNLOCALIZED_NAMES[0];
  }

  @Override
  public String getUnlocalizedName(ItemStack item) {
    return "exnihilo." + ItemData.HAMMER_UNLOCALIZED_NAMES[0];
  }

  @Override
  public void registerIcons(IIconRegister register) {
    this.itemIcon = register.registerIcon("exnihilo:HammerWood");
  }
}
