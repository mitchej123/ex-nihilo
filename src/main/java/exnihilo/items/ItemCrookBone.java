package exnihilo.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCrookBone extends ItemCrook {
  public ItemCrookBone() {
    super(Item.ToolMaterial.STONE);
    setMaxDamage((int)(getMaxDamage() * 3.1F));
  }

  @Override
  public String getUnlocalizedName() {
    return "exnihilo.crook_bone";
  }

  @Override
  public String getUnlocalizedName(ItemStack item) {
    return "exnihilo.crook_bone";
  }

  @Override
  public void registerIcons(IIconRegister register) {
    this.itemIcon = register.registerIcon("exnihilo:CrookBone");
  }
}
