package me.modmuss50.ftb.zombies;

import me.modmuss50.ftb.zombies.api.ZombieSave;
import me.modmuss50.ftb.zombies.timer.TimerHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.network.NetworkManager;
import reborncore.common.network.RegisterPacketEvent;

import java.io.IOException;
import java.util.function.Predicate;

public class FTBZombiesEventHandler {

	@SubscribeEvent
	public static void registerPackets(RegisterPacketEvent event) {
		event.registerPacket(PacketSpawnSmoke.class, Side.CLIENT);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void setHealth(TickEvent.PlayerTickEvent event){
		if(!FTBZombiesConfig.convention){
			return;
		}
		event.player.getFoodStats().setFoodLevel(20);
		event.player.getFoodStats().setFoodSaturationLevel(10F);
	}

	@SubscribeEvent
	public static void livingUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			if (player.world.isRemote) {
				if (player.onGround && !player.capabilities.isFlying && player.moveForward > 0F && !player.isInsideOfMaterial(Material.WATER)) {
					float speed = 0.15F;
					player.moveRelative(0F, 1F, speed, 0F);
				}
			}
			if(((EntityPlayer) event.getEntity()).isPotionActive(MobEffects.STRENGTH)){
				((EntityPlayer) event.getEntity()).removePotionEffect(MobEffects.STRENGTH);
			}
			player.setHealth(20F);
		} else if (event.getEntity() instanceof EntityZombieVillager && !event.getEntity().world.isRemote){
			removeTask((EntityLiving) event.getEntityLiving(), entityAIBase -> entityAIBase instanceof EntityAINearestAttackableTarget);
			EntityZombieVillager zombieVillager = (EntityZombieVillager) event.getEntity();

			zombieVillager.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 60));
			zombieVillager.enablePersistence();
			zombieVillager.setAlwaysRenderNameTag(true);
			zombieVillager.setProfession(1);
			zombieVillager.setChild(false);

			zombieVillager.setBreakDoorsAItask(false);

			if(zombieVillager.isPotionActive(MobEffects.STRENGTH)){ //Wait for the splash potion to be active, and then cure the zombie
				cure(zombieVillager);
			}
		} else if (event.getEntity() instanceof EntityVillager){
			removeTask((EntityLiving) event.getEntityLiving(), entityAIBase -> entityAIBase instanceof EntityAIAvoidEntity);
		}
	}

	private static void cure(EntityZombieVillager zombieVillager){
		EntityVillager newVillager = new EntityVillager(zombieVillager.world);
		newVillager.setProfession(zombieVillager.getForgeProfession());
		newVillager.setLocationAndAngles(zombieVillager.posX, zombieVillager.posY, zombieVillager.posZ, zombieVillager.rotationYaw, zombieVillager.rotationPitch);
		if (zombieVillager.hasCustomName())
		{
			newVillager.setCustomNameTag(zombieVillager.getCustomNameTag());
			newVillager.setAlwaysRenderNameTag(true);
		}
		zombieVillager.world.spawnEntity(newVillager);
		newVillager.enablePersistence();

		zombieVillager.setDead();

		if(!FTBZombiesConfig.convention){
			return;
		}

		GameContoller.savedCount++;

		NetworkManager.sendToAllAround(new PacketSpawnSmoke(newVillager.getPos()), new NetworkRegistry.TargetPoint(zombieVillager.world.provider.getDimension(), newVillager.posX, newVillager.posY, newVillager.posZ, 64));

		HttpExecutorService.queue(() -> {
			try {
				ZombieSave.put(new ZombieSave(TimerHandler.getStoppedTime(), "zombie" + GameContoller.savedCount));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private static void removeTask(EntityLiving entityLiving, Predicate<EntityAIBase> predicate) {
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

	@SubscribeEvent
	public static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) throws IOException {
		event.player.inventory.clear();
		event.player.inventory.addItemStackToInventory(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.STRENGTH));
	}

	@SubscribeEvent
	public static void playerRightClick(PlayerInteractEvent event){
		if(event.getItemStack().getItem() instanceof ItemSplashPotion && !event.getEntityPlayer().isCreative()){
			event.getItemStack().setCount(2);
		}
	}

	@SubscribeEvent
	public static void interact(PlayerInteractEvent.EntityInteract event) {
		if(!event.getEntityPlayer().isCreative()){
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void attack(LivingAttackEvent event) {
		if(event.getSource().getTrueSource() instanceof EntityPlayer){
			if(!((EntityPlayer) event.getSource().getTrueSource()).isCreative()){
				event.setCanceled(true);
			}
		}
	}

}