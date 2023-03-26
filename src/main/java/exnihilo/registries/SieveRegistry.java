package exnihilo.registries;

import exnihilo.ENBlocks;
import exnihilo.ENItems;
import exnihilo.ExNihilo;
import exnihilo.registries.helpers.SiftReward;
import exnihilo.registries.helpers.SiftingResult;
import exnihilo.utils.ItemInfo;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class SieveRegistry {

    @Deprecated
    public static final ArrayList<SiftReward> rewards = new ArrayList<>();

    public static HashMap<ItemInfo, ArrayList<SiftingResult>> getSiftables() {
        return siftables;
    }

    private static final HashMap<ItemInfo, ArrayList<SiftingResult>> siftables = new HashMap<>();

    public static void register(Block source, int sourceMeta, Item output, int outputMeta, int rarity) {
        if (source == null || output == null) return;
        if (rarity > 0) {
            SiftReward entry = new SiftReward(source, sourceMeta, output, outputMeta, rarity);
            if (source != null) rewards.add(entry);
            ItemInfo iteminfo = new ItemInfo(source, sourceMeta);
            ArrayList<SiftingResult> res = siftables.get(iteminfo);
            if (res == null) res = new ArrayList<>();
            res.add(new SiftingResult(output, outputMeta, rarity));
            siftables.put(new ItemInfo(source, sourceMeta), res);
        } else {
            ItemStack inputStack = new ItemStack(source, sourceMeta);
            ItemStack outputStack = new ItemStack(output, outputMeta);
            ExNihilo.log.info("Block "
                + inputStack.getDisplayName()
                + " with reward "
                + outputStack.getDisplayName()
                + " was not added. Reason: Chance 0");
        }
    }

    public static void register(Block source, Item output, int outputMeta, int rarity) {
        register(source, 0, output, outputMeta, rarity);
    }

    @Deprecated
    public static ArrayList<SiftReward> getRewards(Block block, int meta) {
        ArrayList<SiftReward> rewardList = new ArrayList<>();
        for (SiftReward reward : rewards) {
            if (reward.source == block && reward.sourceMeta == meta) rewardList.add(reward);
        }
        return rewardList;
    }

    public static ArrayList<SiftingResult> getSiftingOutput(Block block, int meta) {
        return siftables.get(new ItemInfo(block, meta));
    }

    public static ArrayList<SiftingResult> getSiftingOutput(ItemInfo info) {
        return siftables.get(info);
    }

    @Deprecated
    public static boolean Contains(Block block, int meta) {
        for (SiftReward reward : rewards) {
            if (reward.source == block && (reward.sourceMeta == meta || reward.ignoreMeta)) return true;
        }
        return false;
    }

    public static boolean registered(Block block, int meta) {
        return siftables.containsKey(new ItemInfo(block, meta));
    }

    public static boolean registered(Block block) {
        return siftables.containsKey(new ItemInfo(block, 32767));
    }

    @Deprecated
    public static boolean Contains(Block block) {
        for (SiftReward reward : rewards) {
            if (reward.source == block && reward.ignoreMeta) return true;
        }
        return false;
    }

    public static void unregisterReward(Block block, int meta, Item output, int outputMeta) {
        ItemInfo iteminfo = new ItemInfo(block, meta);
        ArrayList<SiftingResult> res = siftables.get(iteminfo);
        if (res == null) return;
        res.removeIf(sr -> sr.item == output && sr.meta == outputMeta);
        if (res.isEmpty()) siftables.remove(iteminfo);
    }

    public static void unregisterRewardFromAllBlocks(Item output, int outputMeta) {
        for (ItemInfo iteminfo : siftables.keySet())
            unregisterReward(Block.getBlockFromItem(iteminfo.getItem()), iteminfo.getMeta(), output, outputMeta);
    }

    public static void unregisterAllRewardsFromBlock(Block block, int meta) {
        siftables.remove(new ItemInfo(block, meta));
    }

    public static void load(Configuration config) {}

    public static void registerRewards() {
        register(Blocks.dirt, 0, ENItems.Stones, 0, 1);
        register(Blocks.dirt, 0, ENItems.Stones, 0, 1);
        register(Blocks.dirt, 0, ENItems.Stones, 0, 2);
        register(Blocks.dirt, 0, ENItems.Stones, 0, 2);
        register(Blocks.dirt, 0, ENItems.Stones, 0, 3);
        register(Blocks.dirt, 0, ENItems.Stones, 0, 3);
        register(Blocks.dirt, 0, Items.wheat_seeds, 0, 15);
        register(Blocks.dirt, 0, ENItems.GrassSeeds, 0, 15);
        register(Blocks.dirt, 0, Items.melon_seeds, 0, 32);
        register(Blocks.dirt, 0, Items.pumpkin_seeds, 0, 32);
        register(Blocks.dirt, 0, ENItems.SeedsSugarcane, 0, 32);
        register(Blocks.dirt, 0, ENItems.SeedsCarrot, 0, 64);
        register(Blocks.dirt, 0, ENItems.SeedsPotato, 0, 64);
        register(Blocks.dirt, 0, ENItems.SeedsOak, 0, 64);
        register(Blocks.dirt, 0, ENItems.SeedsAcacia, 0, 90);
        register(Blocks.dirt, 0, ENItems.SeedsSpruce, 0, 90);
        register(Blocks.dirt, 0, ENItems.SeedsBirch, 0, 90);
        register(Blocks.gravel, 0, Items.flint, 0, 4);
        register(Blocks.gravel, 0, Items.coal, 0, 8);
        register(Blocks.gravel, 0, Items.dye, 4, 20);
        register(Blocks.gravel, 0, Items.diamond, 0, 128);
        register(Blocks.gravel, 0, Items.emerald, 0, 150);
        register(Blocks.sand, 0, Items.dye, 3, 32);
        register(Blocks.sand, 0, ENItems.SeedsCactus, 0, 32);
        register(Blocks.sand, 0, ENItems.SeedsJungle, 0, 64);
        register(Blocks.sand, 0, ENItems.Spores, 0, 128);
        register(Blocks.soul_sand, 0, Items.quartz, 0, 1);
        register(Blocks.soul_sand, 0, Items.quartz, 0, 3);
        register(Blocks.soul_sand, 0, Items.nether_wart, 0, 20);
        register(Blocks.soul_sand, 0, Items.ghast_tear, 0, 64);
        register(ENBlocks.Dust, 0, Items.dye, 15, 5);
        register(ENBlocks.Dust, 0, Items.redstone, 0, 8);
        register(ENBlocks.Dust, 0, Items.gunpowder, 0, 15);
        register(ENBlocks.Dust, 0, Items.glowstone_dust, 0, 16);
        register(ENBlocks.Dust, 0, Items.blaze_powder, 0, 20);
    }

    public static void registerOreDictAdditions(String[] names) {
        if (names != null) for (String input : names) {
            String[] current = input.split(":");
            for (ItemStack stack : OreDictionary.getOres(current[0])) {
                Item reward = (Item) Item.itemRegistry.getObject(current[1] + ":" + current[2]);
                if (Block.getBlockFromItem(stack.getItem()) != null) register(Block.getBlockFromItem(stack.getItem()),
                    stack.getItemDamage(),
                    reward,
                    Integer.parseInt(current[3]),
                    Integer.parseInt(current[4]));
            }
        }
    }

    public static void registerNonDictAdditions(String[] names) {
        if (names != null) for (String input : names) {
            String[] current = input.split(":");
            if (current.length == 7 && Block.blockRegistry.getObject(current[0] + ":" + current[1]) != null) {
                Block source = (Block) Block.blockRegistry.getObject(current[0] + ":" + current[1]);
                Item reward = (Item) Item.itemRegistry.getObject(current[3] + ":" + current[4]);
                register(
                    source,
                    Integer.parseInt(current[2]),
                    reward,
                    Integer.parseInt(current[5]),
                    Integer.parseInt(current[6]));
            }
        }
    }

    public static ArrayList<ItemInfo> getSources(ItemStack reward) {
        ArrayList<ItemInfo> res = new ArrayList<>();
        for (ItemInfo entry : siftables.keySet()) {
            for (SiftingResult sift : siftables.get(entry)) {
                if ((new ItemInfo(sift.item, sift.meta)).equals(new ItemInfo(reward))) res.add(entry);
            }
        }
        return res;
    }
}
