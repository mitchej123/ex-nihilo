package exnihilo;

import exnihilo.data.WorldData;
import exnihilo.world.WorldProviderDefaultVoid;
import exnihilo.world.WorldProviderEndVoid;
import exnihilo.world.WorldProviderHellVoid;
import net.minecraftforge.common.DimensionManager;

public class World {
  public static void registerWorldProviders() {
    if (WorldData.hijackNether) {
      DimensionManager.unregisterProviderType(-1);
      DimensionManager.registerProviderType(-1, WorldProviderHellVoid.class, true);
    } 
    if (WorldData.hijackOverworld) {
      DimensionManager.unregisterProviderType(0);
      DimensionManager.registerProviderType(0, WorldProviderDefaultVoid.class, true);
    } 
    if (WorldData.hijackEnd) {
      DimensionManager.unregisterProviderType(1);
      DimensionManager.registerProviderType(1, WorldProviderEndVoid.class, true);
    } 
  }
}
