package me.modmuss50.ftb.zombies;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import reborncore.RebornRegistry;
import reborncore.common.network.RegisterPacketEvent;

@Mod(modid = "ftbzombies")
public class FTBZombies {

	BlockController blockController;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		blockController = new BlockController();
		RebornRegistry.registerBlock(blockController, new ResourceLocation("ftbzombies", "controller"));

		GameRegistry.registerTileEntity(TileController.class, new ResourceLocation("ftbzombies", "controller"));

		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void packets(RegisterPacketEvent event){
		event.registerPacket(PacketSpawnSmoke.class, Side.CLIENT);
	}

}
