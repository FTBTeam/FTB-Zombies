package me.modmuss50.ftb.zombies;

import me.modmuss50.ftb.zombies.commands.FTBZCommand;
import me.modmuss50.ftb.zombies.guiTweaks.GuiEventHandler;
import me.modmuss50.ftb.zombies.timer.ClientHudRenderer;
import me.modmuss50.ftb.zombies.timer.PacketSendTimerData;
import me.modmuss50.ftb.zombies.timer.TimerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
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
	}

	public void server(FMLServerStartingEvent event){
		event.registerServerCommand(new FTBZCommand());
	}


	@SubscribeEvent
	public void loadPacket(RegisterPacketEvent event) {
		event.registerPacket(PacketSendTimerData.class, Side.CLIENT);
		event.registerPacket(PacketFinishRun.class, Side.CLIENT);
	}

}