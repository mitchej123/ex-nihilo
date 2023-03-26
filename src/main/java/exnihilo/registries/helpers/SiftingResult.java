package exnihilo.registries.helpers;

import java.beans.ConstructorProperties;
import net.minecraft.item.Item;

public class SiftingResult {
  public final Item item;

  public final int meta;

  public final int rarity;

  @ConstructorProperties({"item", "meta", "rarity"})
  public SiftingResult(Item item, int meta, int rarity) {
    this.item = item;
    this.meta = meta;
    this.rarity = rarity;
  }
}
