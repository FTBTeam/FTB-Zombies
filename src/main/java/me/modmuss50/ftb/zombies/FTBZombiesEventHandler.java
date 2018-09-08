package me.modmuss50.ftb.zombies;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import reborncore.common.network.RegisterPacketEvent;

import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = FTBZombies.MOD_ID)
public class FTBZombiesEventHandler {
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new BlockController().setRegistryName("controller").setUnlocalizedName(FTBZombies.MOD_ID + ".controller").setCreativeTab(CreativeTabs.TOOLS));
		GameRegistry.registerTileEntity(TileController.class, new ResourceLocation(FTBZombies.MOD_ID, "controller"));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemBlock(FTBZombiesBlocks.CONTROLLER).setRegistryName("controller"));
	}

	@SubscribeEvent
	public static void registerPackets(RegisterPacketEvent event) {
		event.registerPacket(PacketSpawnSmoke.class, Side.CLIENT);
	}

	@SubscribeEvent
	public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if(!event.player.getEntityData().getBoolean("PlayedLoginSound")) {
			SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(new ResourceLocation(FTBZombiesConfig.general.login));

			if (soundEvent != null) {
				event.player.world.playSound(null, event.player.getPosition(), soundEvent, SoundCategory.PLAYERS, 1F, 1F);
				event.player.getEntityData().setBoolean("PlayedLoginSound", true);
			}
		}
	}

	@SubscribeEvent
	public static void setHealth(TickEvent.PlayerTickEvent event){
		event.player.setHealth(100);
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
		} else if (event.getEntity() instanceof EntityZombieVillager){
			removeTask((EntityLiving) event.getEntityLiving(), entityAIBase -> entityAIBase instanceof EntityAINearestAttackableTarget);
		} else if (event.getEntity() instanceof EntityVillager){
			removeTask((EntityLiving) event.getEntityLiving(), entityAIBase -> entityAIBase instanceof EntityAIAvoidEntity);
		}
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

}