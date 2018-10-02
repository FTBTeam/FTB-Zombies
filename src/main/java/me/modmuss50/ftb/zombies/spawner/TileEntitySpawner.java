package me.modmuss50.ftb.zombies.spawner;

import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
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

                BlockPos spawnPos = null;
                while (spawnPos == null || !isSpawnValid(spawnPos)){
                    spawnPos = getPos().add(world.rand.nextInt(10) - 5, 0, world.rand.nextInt(10) - 5);
                    int posY = spawnPos.getY();
                    while(!world.isAirBlock(new BlockPos(spawnPos.getX(), posY, spawnPos.getZ()))){
                        posY++;
                        spawnPos = new BlockPos(spawnPos.getX(), posY, spawnPos.getZ());
                    }
                }

                EntityZombieVillager entityZombieVillager = new EntityZombieVillager(world);
                entityZombieVillager.setLocationAndAngles(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0, 0);
                world.spawnEntity(entityZombieVillager);
            }
            cooldown = (world.rand.nextInt(60)) * 20; //Wait a few seconds before trying again
        } else {
            cooldown = 20 * 20; //Wait 20 seconds before trying again
        }
    }

    private boolean isSpawnValid(BlockPos pos){
        if(Math.abs(pos.getY() - getPos().getY()) > 10){
            return false;
        }
        return world.isAirBlock(pos) && world.isAirBlock(pos.up());
    }
}
