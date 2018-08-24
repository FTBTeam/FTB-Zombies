package me.modmuss50.ftb.zombies;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import reborncore.common.network.NetworkManager;
import reborncore.common.tile.TileLegacyMachineBase;

import java.util.List;
import java.util.function.Predicate;

public class TileController extends TileLegacyMachineBase {

	@Override
	public void update() {
		super.update();

		if (world.getTotalWorldTime() % 20 != 0 || world.isRemote) {
			return;
		}

		double size = 21;
		AxisAlignedBB bb = new AxisAlignedBB(getPos().getX() - size, getPos().getY(), getPos().getZ() - size, getPos().getX() + size, getPos().getY() + size, getPos().getZ() + size);
		List<EntityZombieVillager> zombies = world.getEntitiesWithinAABB(EntityZombieVillager.class, bb);
		List<EntityVillager> villagers = world.getEntitiesWithinAABB(EntityVillager.class, bb);
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, bb);

		//stops the zombies attacking the villagers
		zombies.forEach(entityZombieVillager -> removeTask(entityZombieVillager, entityAIBase -> entityAIBase instanceof EntityAINearestAttackableTarget));
		//Stops the villagers from being scared of the zombies
		villagers.forEach(entityZombieVillager -> removeTask(entityZombieVillager, entityAIBase -> entityAIBase instanceof EntityAIAvoidEntity));

		if (items.isEmpty()) {
			return;
		}

		for (EntityItem item : items) {
			if (!item.getItem().getItem().getRegistryName().toString().equals("techreborn:dynamiccell")) {
				continue;
			}
			EntityZombieVillager zombieVillager = zombies.get(0);

			EntityVillager newVillager = new EntityVillager(zombieVillager.world);
			newVillager.setProfession(zombieVillager.getForgeProfession());
			newVillager.setLocationAndAngles(zombieVillager.posX, zombieVillager.posY, zombieVillager.posZ, zombieVillager.rotationYaw, zombieVillager.rotationPitch);
			world.spawnEntity(newVillager);

			item.setDead();
			zombieVillager.setDead();

			NetworkManager.sendToAllAround(new PacketSpawnSmoke(newVillager.getPos()), new NetworkRegistry.TargetPoint(world.provider.getDimension(), newVillager.posX, newVillager.posY, newVillager.posZ, 64));

		}
	}

	private void removeTask(EntityLiving entityLiving, Predicate<EntityAIBase> predicate) {
		EntityAIBase aiBase = null;
		for (EntityAITasks.EntityAITaskEntry task : entityLiving.targetTasks.taskEntries) {
			if (predicate.test(task.action)) {
				aiBase = task.action;
				break;
			}
		}
		if (aiBase != null) {
			entityLiving.targetTasks.removeTask(aiBase);
		}

	}

}
