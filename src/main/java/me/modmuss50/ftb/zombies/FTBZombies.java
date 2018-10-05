package me.modmuss50.ftb.zombies;

import me.modmuss50.ftb.zombies.commands.FTBZCommand;
import me.modmuss50.ftb.zombies.guiTweaks.GuiEventHandler;
import me.modmuss50.ftb.zombies.spawner.BlockSpawner;
import me.modmuss50.ftb.zombies.spawner.BlockSpawner2;
import me.modmuss50.ftb.zombies.spawner.TileEntitySpawner;
import me.modmuss50.ftb.zombies.spawner.TileEntitySpawner2;
import me.modmuss50.ftb.zombies.timer.ClientHudRenderer;
import me.modmuss50.ftb.zombies.timer.PacketSendTimerData;
import me.modmuss50.ftb.zombies.timer.TimerEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import reborncore.RebornRegistry;
import reborncore.common.network.RegisterPacketEvent;

@Mod(modid = FTBZombies.MOD_ID, name = "FTB-Zombies", dependencies = "required-after:reborncore;required-after:techreborn")
public class FTBZombies {
	public static final String MOD_ID = "ftbzombies";


	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(TimerEvent.class);
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT){
			MinecraftForge.EVENT_BUS.register(ClientHudRenderer.class);
			MinecraftForge.EVENT_BUS.register(GuiEventHandler.class);
		}
		MinecraftForge.EVENT_BUS.register(FTBZombiesEventHandler.class);

		BlockSpawner spawner = new BlockSpawner();
		RebornRegistry.registerBlock(spawner, new ResourceLocation(MOD_ID, "spawner"));
		GameRegistry.registerTileEntity(TileEntitySpawner.class, new ResourceLocation(MOD_ID, "spawner"));

		BlockSpawner2 spawner2 = new BlockSpawner2();
		RebornRegistry.registerBlock(spawner2, new ResourceLocation(MOD_ID, "spawner2"));
		GameRegistry.registerTileEntity(TileEntitySpawner2.class, new ResourceLocation(MOD_ID, "spawner2"));
	}

	@Mod.EventHandler
	public void server(FMLServerStartingEvent event){
		event.registerServerCommand(new FTBZCommand());
	}


	@SubscribeEvent
	public void loadPacket(RegisterPacketEvent event) {
		event.registerPacket(PacketSendTimerData.class, Side.CLIENT);
		event.registerPacket(PacketFinishRun.class, Side.CLIENT);
	}

}