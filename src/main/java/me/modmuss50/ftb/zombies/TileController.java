package me.modmuss50.ftb.zombies;

import me.modmuss50.ftb.zombies.timer.ClientHudRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import reborncore.common.network.NetworkManager;
import reborncore.common.tile.TileLegacyMachineBase;

import java.util.List;
import java.util.function.Predicate;

public class TileController extends TileLegacyMachineBase {
	@GameRegistry.ObjectHolder("techreborn:dynamiccell")
	public static Item DYNAMICCELL = Items.AIR;

	public int topZombieCount = 0;
	public int topVillagerCount = 0;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		topZombieCount = nbt.getInteger("ZombieCount");
		topVillagerCount = nbt.getInteger("VillagerCount");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		nbt.setInteger("ZombieCount", topZombieCount);
		nbt.setInteger("VillagerCount", topVillagerCount);
		return nbt;
	}

	@Override
	public void update() {
		super.update();

		if (world.getTotalWorldTime() % 20 != 0 || world.isRemote) {
			return;
		}

		double size = FTBZombiesConfig.general.controller_radius;
		AxisAlignedBB bb = new AxisAlignedBB(getPos().getX() - size, getPos().getY(), getPos().getZ() - size, getPos().getX() + size + 1, getPos().getY() + FTBZombiesConfig.general.controller_height, getPos().getZ() + size + 1);
		List<EntityZombieVillager> zombies = world.getEntitiesWithinAABB(EntityZombieVillager.class, bb);
		List<EntityVillager> villagers = world.getEntitiesWithinAABB(EntityVillager.class, bb);

		//stops the zombies attacking the villagers
		zombies.forEach(entityZombieVillager -> removeTask(entityZombieVillager, entityAIBase -> entityAIBase instanceof EntityAINearestAttackableTarget));
		//Stops the villagers from being scared of the zombies
		villagers.forEach(entityZombieVillager -> removeTask(entityZombieVillager, entityAIBase -> entityAIBase instanceof EntityAIAvoidEntity));

		if(topVillagerCount != -1) {
			int prev = topVillagerCount;
			topVillagerCount = Math.max(topVillagerCount, villagers.size());

			if (prev < topVillagerCount && topVillagerCount >= FTBZombiesConfig.general.zombie_count) {
				SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation(FTBZombiesConfig.general.villager_sound));
				if(event != null) {
					world.playSound(null, getPos(), event, SoundCategory.BLOCKS, 8F, 1F);
				}
				topVillagerCount = -1;
			}

			if(prev != topVillagerCount){
				markDirty();
			}
		}

		//This is a client side only mod, so this should be fine I guess
		ClientHudRenderer.totalCount = zombies.size() + villagers.size();
		ClientHudRenderer.savedCount = villagers.size();


		if (zombies.isEmpty()) {
			return;
		}

		if(topZombieCount != -1) {
			int prev = topZombieCount;
			topZombieCount = Math.max(topZombieCount, zombies.size());

			if (prev < topZombieCount && topZombieCount >= FTBZombiesConfig.general.zombie_count) {
				SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation(FTBZombiesConfig.general.zombie_sound));
				if(event != null) {
					world.playSound(null, getPos(), event, SoundCategory.BLOCKS, 8F, 1F);
				}
				topZombieCount = -1;
			}

			if(prev != topZombieCount){
				markDirty();
			}
		}

		for(EntityZombieVillager zombieVillager : zombies) {
			zombieVillager.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 60));
			zombieVillager.enablePersistence();
			zombieVillager.setAlwaysRenderNameTag(true);
			zombieVillager.setProfession(1);

			if(!zombieVillager.hasCustomName()) {
				zombieVillager.setCustomNameTag(FTBZombiesConfig.general.names[world.rand.nextInt(FTBZombiesConfig.general.names.length)]);
			}
		}

		for (EntityItem item : world.getEntitiesWithinAABB(EntityItem.class, bb)) {
			if (item.getItem().getItem() == DYNAMICCELL) {
				EntityZombieVillager zombieVillager = zombies.get(0);

				EntityVillager newVillager = new EntityVillager(zombieVillager.world);
				newVillager.setProfession(zombieVillager.getForgeProfession());
				newVillager.setLocationAndAngles(zombieVillager.posX, zombieVillager.posY, zombieVillager.posZ, zombieVillager.rotationYaw, zombieVillager.rotationPitch);
				if (zombieVillager.hasCustomName())
				{
					newVillager.setCustomNameTag(zombieVillager.getCustomNameTag());
					newVillager.setAlwaysRenderNameTag(true);
				}
				world.spawnEntity(newVillager);
				newVillager.enablePersistence();

				item.getItem().shrink(1);

				if(item.getItem().isEmpty()) {
					item.setDead();
				}

				zombieVillager.setDead();

				NetworkManager.sendToAllAround(new PacketSpawnSmoke(newVillager.getPos()), new NetworkRegistry.TargetPoint(world.provider.getDimension(), newVillager.posX, newVillager.posY, newVillager.posZ, 64));
				break;
			}
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
