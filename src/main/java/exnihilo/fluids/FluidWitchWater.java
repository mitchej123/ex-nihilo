package exnihilo.fluids;

import exnihilo.registries.ColorRegistry;
import net.minecraftforge.fluids.Fluid;

public class FluidWitchWater extends Fluid {
  public FluidWitchWater(String fluidName) {
    super(fluidName);
  }

  @Override
  public int getColor() {
    return ColorRegistry.color("witchwater").toInt();
  }
}
