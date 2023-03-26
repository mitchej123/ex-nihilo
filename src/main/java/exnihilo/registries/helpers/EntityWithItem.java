package exnihilo.registries.helpers;

import java.beans.ConstructorProperties;
import java.util.Objects;

import net.minecraft.item.ItemStack;

public class EntityWithItem {

    private Class entity;

    private ItemStack drops;

    private String particle;

    public void setEntity(Class entity) {
        this.entity = entity;
    }

    public void setDrops(ItemStack drops) {
        this.drops = drops;
    }

    public void setParticle(String particle) {
        this.particle = particle;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof EntityWithItem other)) return false;
        if (!other.canEqual(this)) return false;
        Object this$entity = getEntity(), other$entity = other.getEntity();
        if (!Objects.equals(this$entity, other$entity)) return false;
        Object this$drops = getDrops(), other$drops = other.getDrops();
        if (!Objects.equals(this$drops, other$drops)) return false;
        Object this$particle = getParticle(), other$particle = other.getParticle();
        return Objects.equals(this$particle, other$particle);
    }

    protected boolean canEqual(Object other) {
        return other instanceof EntityWithItem;
    }

    public int hashCode() {
        int result = 1;
        Object $entity = getEntity();
        result = result * 59 + (($entity == null) ? 0 : $entity.hashCode());
        Object $drops = getDrops();
        result = result * 59 + (($drops == null) ? 0 : $drops.hashCode());
        Object $particle = getParticle();
        return result * 59 + (($particle == null) ? 0 : $particle.hashCode());
    }

    public String toString() {
        return "EntityWithItem(entity=" + getEntity() + ", drops=" + getDrops() + ", particle=" + getParticle() + ")";
    }

    @ConstructorProperties({ "entity", "drops", "particle" })
    public EntityWithItem(Class entity, ItemStack drops, String particle) {
        this.entity = entity;
        this.drops = drops;
        this.particle = particle;
    }

    public Class getEntity() {
        return this.entity;
    }

    public ItemStack getDrops() {
        return this.drops;
    }

    public String getParticle() {
        return this.particle;
    }
}
