package exnihilo.blocks.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exnihilo.network.VanillaPacket;
import exnihilo.particles.ParticleSieve;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftingResult;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class TileEntitySieve extends TileEntity {

    private static final float MIN_RENDER_CAPACITY = 0.7F;
    private static final float MAX_RENDER_CAPACITY = 0.9F;
    private static final float PROCESSING_INTERVAL = 0.075F;
    private static final int UPDATE_INTERVAL = 20;

    public Block content;
    public int contentMeta = 0;
    private float volume = 0.0F;
    public SieveMode mode = SieveMode.EMPTY;

    private int timer = 0;
    private boolean update = false;
    private boolean particleMode = false;
    private int timesClicked = 0;

    public enum SieveMode {
        EMPTY(0),
        FILLED(1);

        public final int value;

        SieveMode(int v) {
            this.value = v;
        }
    }

    public TileEntitySieve() {
    }

    public void addSievable(Block block, int blockMeta) {
        this.content = block;
        this.contentMeta = blockMeta;
        this.mode = SieveMode.FILLED;
        this.volume = 1.0F;
        VanillaPacket.sendTileEntityUpdate(this);
    }

    public int getProgress() {
        return timesClicked;
    }
    public boolean isSieveSimilarToInput(TileEntitySieve sieve) {
        return sieve.mode == this.mode && sieve.timesClicked == this.timesClicked;
    }

    @Override
    public void updateEntity() {
        if (this.worldObj.isRemote && this.particleMode) spawnFX(this.content, this.contentMeta);
        this.timer++;
        if (this.timer >= UPDATE_INTERVAL) {
            this.timesClicked = 0;
            this.timer = 0;
            disableParticles();
            if (this.update) update();
        }
    }

    public void ProcessContents(final boolean creative) {
        if (creative) {
            this.volume = 0.0F;
        } else {
            this.timesClicked++;
            if (this.timesClicked <= 6) this.volume -= PROCESSING_INTERVAL;
        }
        if (this.volume <= 0.0F) {
            this.mode = SieveMode.EMPTY;
            if (!this.worldObj.isRemote) {
                final ArrayList<SiftingResult> rewards = SieveRegistry.getSiftingOutput(this.content, this.contentMeta);
                if (rewards != null && rewards.size() > 0) {
                    for (final SiftingResult reward : rewards) {
                        if (this.worldObj.rand.nextInt(reward.rarity) == 0) {
                            final EntityItem entityitem = new EntityItem(this.worldObj, this.xCoord + 0.5D, this.yCoord + 1.5D, this.zCoord + 0.5D, new ItemStack(reward.item, 1, reward.meta));
                            final double f3 = 0.05F;
                            entityitem.motionX = this.worldObj.rand.nextGaussian() * f3;
                            entityitem.motionY = 0.2D;
                            entityitem.motionZ = this.worldObj.rand.nextGaussian() * f3;
                            this.worldObj.spawnEntityInWorld(entityitem);
                        }
                    }
                }
            }
        } else {
            this.particleMode = true;
        }
        this.update = true;
    }

    @SideOnly(Side.CLIENT)
    private void spawnFX(Block block, int blockMeta) {
        if (block != null) {
            final IIcon icon = block.getIcon(0, blockMeta);
            for (int x = 0; x < 4; x++) {
                final ParticleSieve dust = new ParticleSieve(this.worldObj, this.xCoord + 0.8D * this.worldObj.rand.nextFloat() + 0.15D, this.yCoord + 0.69D, this.zCoord + 0.8D * this.worldObj.rand.nextFloat() + 0.15D, 0.0D, 0.0D, 0.0D, icon);
                (Minecraft.getMinecraft()).effectRenderer.addEffect(dust);
            }
        }
    }

    public float getVolume() {
        return this.volume;
    }

    public float getAdjustedVolume() {
        final float capacity = MAX_RENDER_CAPACITY - MIN_RENDER_CAPACITY;
        float adjusted = this.volume * capacity;
        adjusted += MIN_RENDER_CAPACITY;
        return adjusted;
    }

    private void update() {
        this.update = false;
        VanillaPacket.sendTileEntityUpdate(this);
    }

    private void disableParticles() {
        this.particleMode = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        switch (compound.getInteger("mode")) {
            case 0 -> this.mode = SieveMode.EMPTY;
            case 1 -> this.mode = SieveMode.FILLED;
        }
        if (!compound.getString("content").equals("")) {
            this.content = (Block) Block.blockRegistry.getObject(compound.getString("content"));
        } else {
            this.content = null;
        }
        this.contentMeta = compound.getInteger("contentMeta");
        this.volume = compound.getFloat("volume");
        this.particleMode = compound.getBoolean("particles");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("mode", this.mode.value);
        if (this.content == null) {
            compound.setString("content", "");
        } else {
            compound.setString("content", Block.blockRegistry.getNameForObject(this.content));
        }
        compound.setInteger("contentMeta", this.contentMeta);
        compound.setFloat("volume", this.volume);
        compound.setBoolean("particles", this.particleMode);
    }

    @Override
    public Packet getDescriptionPacket() {
        final NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, this.blockMetadata, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        final NBTTagCompound tag = pkt.func_148857_g();
        readFromNBT(tag);
    }
}
