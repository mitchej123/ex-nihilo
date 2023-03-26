package exnihilo.compatibility.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftingResult;
import exnihilo.utils.ItemInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RecipeHandlerSieve extends TemplateRecipeHandler {
  private static final int SLOTS_PER_PAGE = 45;

  public class CachedSieveRecipe extends TemplateRecipeHandler.CachedRecipe {
    private final List<PositionedStack> input = new ArrayList<>();

    private final List<PositionedStack> outputs = new ArrayList<>();

    public Point focus;

    public CachedSieveRecipe(List<ItemStack> variations) {
      this(variations, null, null);
    }

    @Override
    public List<PositionedStack> getIngredients() {
      return getCycledIngredients(RecipeHandlerSieve.this.cycleticks / 20, this.input);
    }

    @Override
    public List<PositionedStack> getOtherStacks() {
      return this.outputs;
    }

    @Override
    public PositionedStack getResult() {
      return null;
    }

    public CachedSieveRecipe(List<ItemStack> variations, ItemStack base, ItemStack focus) {
      super();
      PositionedStack pstack = new PositionedStack((base != null) ? base : variations, 74, 4);
      pstack.setMaxSize(1);
      this.input.add(pstack);
      int row = 0;
      int col = 0;
      for (ItemStack v : variations) {
        this.outputs.add(new PositionedStack(v, 3 + 18 * col, 37 + 18 * row));
        if (focus != null && NEIServerUtils.areStacksSameTypeCrafting(focus, v))
          this.focus = new Point(2 + 18 * col, 36 + 18 * row);
        col++;
        if (col > 8) {
          col = 0;
          row++;
        }
      }
    }
  }

  @Override
  public String getRecipeName() {
    return "Ex Nihilo Sieve";
  }

  @Override
  public String getGuiTexture() {
    return "exnihilo:textures/sieveNEI.png";
  }

  private void addCached(List<ItemStack> variations, ItemStack base, ItemStack focus) {
    if (variations.size() > 45) {
      List<List<ItemStack>> parts = new ArrayList<>();
      int size = variations.size();
      for (int i = 0; i < size; i += 45)
        parts.add(new ArrayList<>(variations.subList(i, Math.min(size, i + 45))));
      for (List<ItemStack> part : parts)
        this.arecipes.add(new CachedSieveRecipe(part, base, focus));
    } else {
      this.arecipes.add(new CachedSieveRecipe(variations, base, focus));
    }
  }

  @Override
  public void drawBackground(int recipeIndex) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GuiDraw.changeTexture(getGuiTexture());
    GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 166, 130);
    Point focus = ((CachedSieveRecipe)this.arecipes.get(recipeIndex)).focus;
    if (focus != null)
      GuiDraw.drawTexturedModalRect(focus.x, focus.y, 166, 0, 18, 18);
  }

  @Override
  public int recipiesPerPage() {
    return 1;
  }

  @Override
  public void loadTransferRects() {
    this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(75, 22, 15, 13), "exnihilo.sieve"));
  }

  @Override
  public void loadCraftingRecipes(String outputID, Object... results) {
    if (outputID.equals("exnihilo.sieve")) {
      for (ItemInfo ii : SieveRegistry.getSiftables().keySet()) {
        ItemStack inputStack = ii.getStack();
        ArrayList<ItemStack> resultStack = new ArrayList<>();
        for (SiftingResult s : SieveRegistry.getSiftingOutput(ii))
          resultStack.add(new ItemStack(s.item, 1, s.meta));
        addCached(resultStack, inputStack, null);
      }
    } else {
      super.loadCraftingRecipes(outputID, results);
    }
  }

  @Override
  public void loadCraftingRecipes(ItemStack result) {
    HashSet<ItemInfo> completed = new HashSet<>();
    for (ItemInfo ii : SieveRegistry.getSources(result)) {
      if (!completed.contains(ii)) {
        HashMap<ItemInfo, Integer> stored = new HashMap<>();
        for (SiftingResult results : SieveRegistry.getSiftingOutput(Block.getBlockFromItem(ii.getItem()), ii.getMeta())) {
          ItemInfo current = new ItemInfo(results.item, results.meta);
          if (stored.containsKey(current)) {
            stored.put(current, stored.get(current) + 1);
            continue;
          }
          stored.put(current, 1);
        }
        ArrayList<ItemStack> resultVars = new ArrayList<>();
        for (ItemInfo info : stored.keySet()) {
          ItemStack stack = info.getStack();
          stack.stackSize = stored.get(info);
          resultVars.add(stack);
        }
        addCached(resultVars, ii.getStack(), result);
        completed.add(ii);
      }
    }
  }

  @Override
  public void loadUsageRecipes(ItemStack ingredient) {
    HashMap<ItemInfo, Integer> stored = new HashMap<>();
    if (Block.getBlockFromItem(ingredient.getItem()) == Blocks.air)
      return;
    if (!SieveRegistry.registered(Block.getBlockFromItem(ingredient.getItem()), ingredient.getItemDamage()))
      return;
    for (SiftingResult results : SieveRegistry.getSiftingOutput(Block.getBlockFromItem(ingredient.getItem()), ingredient.getItemDamage())) {
      ItemInfo current = new ItemInfo(results.item, results.meta);
      if (stored.containsKey(current)) {
        stored.put(current, stored.get(current) + 1);
        continue;
      }
      stored.put(current, 1);
    }
    ArrayList<ItemStack> resultVars = new ArrayList<>();
    for (ItemInfo info : stored.keySet()) {
      ItemStack stack = info.getStack();
      stack.stackSize = stored.get(info);
      resultVars.add(stack);
    }
    addCached(resultVars, ingredient, ingredient);
  }

  private void addCached(List<ItemStack> variations) {
    addCached(variations, null, null);
  }

  @Override
  public List<String> handleItemTooltip(GuiRecipe<?> guiRecipe, ItemStack itemStack, List<String> currenttip, int recipe) {
    super.handleItemTooltip(guiRecipe, itemStack, currenttip, recipe);
    CachedSieveRecipe crecipe = (CachedSieveRecipe)this.arecipes.get(recipe);
    Point mouse = GuiDraw.getMousePosition();
    Point offset = guiRecipe.getRecipePosition(recipe);
    Point relMouse = new Point(mouse.x - (guiRecipe.width - 176) / 2 - offset.x, mouse.y - (guiRecipe.height - 166) / 2 - offset.y);
    if (itemStack != null && relMouse.y > 34) {
      currenttip.add("Drop Chance:");
      ItemStack sourceStack = (crecipe.input.get(0)).item;
      Block inBlock = Block.getBlockFromItem(sourceStack.getItem());
      int meta = sourceStack.getItemDamage();
      for (SiftingResult smash : SieveRegistry.getSiftingOutput(inBlock, meta)) {
        if (NEIServerUtils.areStacksSameTypeCrafting(itemStack, new ItemStack(smash.item, 1, smash.meta))) {
          int chance = (int)Math.round(100.0D / smash.rarity);
          currenttip.add("  * " + chance + "%");
        }
      }
    }
    return currenttip;
  }
}
