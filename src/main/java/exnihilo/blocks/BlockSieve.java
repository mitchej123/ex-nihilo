package exnihilo.blocks;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exnihilo.blocks.tileentities.TileEntitySieve;
import exnihilo.blocks.tileentities.TileEntitySieve.SieveMode;
import exnihilo.data.BlockData;
import exnihilo.data.ModData;
import exnihilo.registries.SieveRegistry;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BlockSieve extends BlockContainer {
    public static final int SIEVE_RADIUS = 1;
    public static IIcon meshIcon;

    public BlockSieve() {
        super(Material.wood);
        setCreativeTab(CreativeTabs.tabDecorations);
        setHardness(2.0F);
        setBlockName(ModData.ID + "." + BlockData.SIEVE_KEY);
        GameRegistry.registerTileEntity(TileEntitySieve.class, ModData.ID + "." + BlockData.SIEVE_KEY);
    }

    public BlockSieve(Material material) {
        super(material);
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = Blocks.planks.getIcon(0, 0);
        meshIcon = register.registerIcon(ModData.TEXTURE_LOCATION + ":" + "IconSieveMesh");
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabs, List subItems) {
        for (int i = 0; i < 6; i++)
            subItems.add(new ItemStack(item, 1, i));
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySieve();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (player == null) return false;
        final PlayerInteractEvent e = new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, x, y, z, side, world);
        if (MinecraftForge.EVENT_BUS.post(e) || e.getResult() == Event.Result.DENY || e.useBlock == Event.Result.DENY)
            return false;



        final TileEntitySieve sieve = (TileEntitySieve) world.getTileEntity(x, y, z);
        ItemStack held = player.getCurrentEquippedItem();
        if (sieve.mode == SieveMode.EMPTY && held != null) {
            if (SieveRegistry.registered(Block.getBlockFromItem(held.getItem()), held.getItemDamage())) {
                sieve.addSievable(Block.getBlockFromItem(held.getItem()), held.getItemDamage());
                held = removeCurrentItem(player);

                outerloop: // Labeling the for loop, not where to break to.
                for(int dx= -SIEVE_RADIUS ; dx <= SIEVE_RADIUS ; dx++) {
                    for(int dz = -SIEVE_RADIUS ; dz <= SIEVE_RADIUS ; dz++ ) {
                        if(held == null)
                            break outerloop; // Ran out of Items
                        final TileEntity otherTE = world.getTileEntity(x + dx, y, z + dz);
                        if(!(otherTE instanceof TileEntitySieve otherSieve))
                            continue; // Not a sieve

                        if (otherSieve.mode == SieveMode.EMPTY) {
                            otherSieve.addSievable(Block.getBlockFromItem(held.getItem()), held.getItemDamage());
                            held = removeCurrentItem(player);
                        }

                    }
                }
            }
            return true;
        }
        final int progress = sieve.getProgress();
        if (world.isRemote || isHuman(player) || ModData.ALLOW_SIEVE_AUTOMATION) {
            for (int dx = -SIEVE_RADIUS; dx <= SIEVE_RADIUS; dx++) {
                for (int dz = -SIEVE_RADIUS; dz <= SIEVE_RADIUS; dz++) {
                    final TileEntity te = world.getTileEntity(x + dx, y, z + dz);
                    if ((te instanceof TileEntitySieve teSieve) && teSieve.mode == SieveMode.FILLED && teSieve.getProgress() == progress) {
                        teSieve.ProcessContents(false);
                    }
                }
            }
        }

        return true;
    }

    private boolean isHuman(EntityPlayer player) {
        return player instanceof net.minecraft.entity.player.EntityPlayerMP && !(player instanceof FakePlayer);
    }

    private ItemStack removeCurrentItem(EntityPlayer player) {
        ItemStack item = player.getCurrentEquippedItem();
        if (!player.capabilities.isCreativeMode) {
            item.stackSize--;
            if (item.stackSize == 0) item = null;
        }
        return item;
    }
}
