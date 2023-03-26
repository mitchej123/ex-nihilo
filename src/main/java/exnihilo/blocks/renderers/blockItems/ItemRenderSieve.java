package exnihilo.blocks.renderers.blockItems;

import exnihilo.blocks.BlockSieve;
import exnihilo.blocks.models.ModelSieve;
import exnihilo.blocks.models.ModelSieveMesh;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class ItemRenderSieve implements IItemRenderer {

    private final ModelSieve model;

    private final ModelSieveMesh mesh;

    public ItemRenderSieve(ModelSieve model, ModelSieveMesh mesh) {
        this.model = model;
        this.mesh = mesh;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return Objects.requireNonNull(type) != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return Objects.requireNonNull(type) != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        renderTable(type, item);
        renderMesh(type);
    }

    private void renderTable(IItemRenderer.ItemRenderType type, ItemStack item) {
        GL11.glPushMatrix();
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        switch (type) {
            case EQUIPPED -> GL11.glTranslatef(-0.5F, -1.5F, 0.5F);
            case EQUIPPED_FIRST_PERSON -> GL11.glTranslatef(0F, -1.6F, 0.6F);
            case ENTITY, INVENTORY -> GL11.glTranslatef(0F, -1.0F, 0F);
            default -> GL11.glTranslatef(0F, 0F, 0F);
        }
        bindTexture(Block.getBlockFromItem(item.getItem()), item.getItemDamage());
        this.model.simpleRender(0.0625F);
        GL11.glPopMatrix();
    }

    private void renderMesh(IItemRenderer.ItemRenderType type) {
        GL11.glPushMatrix();
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        switch (type) {
            case EQUIPPED -> GL11.glTranslatef(-0.5F, -0.69F, 0.5F);
            case EQUIPPED_FIRST_PERSON -> GL11.glTranslatef(-0.5F, -0.79F, 0.5F);
            default -> GL11.glTranslatef(0F, -0.2F, 0F);
        }
        bindMeshTexture();
        this.mesh.render(BlockSieve.meshIcon);
        GL11.glPopMatrix();
    }

    protected void bindTexture(Block block, int meta) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        if (texturemanager != null) if (meta >= 0) texturemanager.bindTexture(this.model.getSieveTexture(block, meta));
    }

    protected void bindMeshTexture() {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        if (texturemanager != null) texturemanager.bindTexture(TextureMap.locationBlocksTexture);
    }
}
