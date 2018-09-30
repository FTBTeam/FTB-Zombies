package me.modmuss50.ftb.zombies.spawner;

import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntitySpawner extends TileEntity implements ITickable {

    int cooldown = 0;

    @Override
    public void update() {
        if (cooldown > 0) {
            cooldown--;
            return;
        }
        if (world.isRemote || world.getTotalWorldTime() % 20 != 0) {
            return;
        }
        double size = 16;
        AxisAlignedBB bb = new AxisAlignedBB(getPos().getX() - size, getPos().getY(), getPos().getZ() - size, getPos().getX() + size + 1, getPos().getY() + size, getPos().getZ() + size + 1);
        List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayerMP.class, bb);
        if (!players.isEmpty()) {
            return; //Player too close
        }
        List<EntityZombieVillager> villagers = world.getEntitiesWithinAABB(EntityZombieVillager.class, bb);
        if (villagers.size() < 4) {
            int amountToSpawn = 4 - villagers.size();
            for (int i = 0; i < amountToSpawn; i++) {
                BlockPos spawnPos = getPos().offset(EnumFacing.HORIZONTALS[world.rand.nextInt(4)], world.rand.nextInt(4));
                EntityZombieVillager entityZombieVillager = new EntityZombieVillager(world);
                entityZombieVillager.setLocationAndAngles(spawnPos.getX(), world.getTopSolidOrLiquidBlock(spawnPos).getY(), spawnPos.getZ(), 0, 0);
                world.spawnEntity(entityZombieVillager);
            }
            cooldown = 30 * 20; //Wait 30 seconds before trying again
        } else {
            cooldown = 10 * 20; //Wait 10 seconds before trying again
        }
    }
}
