package net.minecraft.server;

// CraftBukkit start
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
// CraftBukkit end

public class EntitySkeleton extends EntityMonster {

    private static final ItemStack a = new ItemStack(Item.BOW, 1);

    public EntitySkeleton(World world) {
        super(world);
        this.texture = "/mob/skeleton.png";
    }

    protected String g() {
        return "mob.skeleton";
    }

    protected String h() {
        return "mob.skeletonhurt";
    }

    protected String i() {
        return "mob.skeletonhurt";
    }

    public void u() {
        if (this.world.d()) {
            float f = this.c(1.0F);

            if (f > 0.5F && this.world.isChunkLoaded(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                // CraftBukkit start
                CraftServer server = ((WorldServer) this.world).getServer();
                org.bukkit.entity.Entity entity = this.getBukkitEntity();

                EntityCombustEvent event = new EntityCombustEvent(entity);
                server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.fireTicks = 300;
                }
                // CraftBukkit end
            }
        }

        super.u();
    }

    protected void a(Entity entity, float f) {
        if (f < 10.0F) {
            double d0 = entity.locX - this.locX;
            double d1 = entity.locZ - this.locZ;

            if (this.attackTicks == 0) {
                EntityArrow entityarrow = new EntityArrow(this.world, this);

                ++entityarrow.locY;
                double d2 = entity.locY - 0.20000000298023224D - entityarrow.locY;
                float f1 = MathHelper.a(d0 * d0 + d1 * d1) * 0.2F;

                this.world.makeSound(this, "random.bow", 1.0F, 1.0F / (this.random.nextFloat() * 0.4F + 0.8F));
                this.world.addEntity(entityarrow);
                entityarrow.a(d0, d2 + (double) f1, d1, 0.6F, 12.0F);
                this.attackTicks = 30;
            }

            this.yaw = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
            this.e = true;
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    protected int j() {
        return Item.ARROW.id;
    }

    protected void r() {
        // CraftBukkit start - whole method
        List<org.bukkit.inventory.ItemStack> loot = new ArrayList<org.bukkit.inventory.ItemStack>();

        int count = this.random.nextInt(3);
        if (count > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.ARROW, count));
        }

        count = this.random.nextInt(3);
        if (count > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.BONE, count));
        }

        CraftWorld cworld = ((WorldServer) world).getWorld();
        Server server = ((WorldServer) world).getServer();
        CraftEntity entity = (CraftEntity) getBukkitEntity();

        EntityDeathEvent event = new EntityDeathEvent(entity, loot);
        server.getPluginManager().callEvent(event);

        for (org.bukkit.inventory.ItemStack stack: event.getDrops()) {
            cworld.dropItemNaturally(entity.getLocation(), stack);
        }
        // CraftBukkit end
    }
}
