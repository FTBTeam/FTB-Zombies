package me.modmuss50.ftb.zombies;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = FTBZombies.MOD_ID, value = Side.CLIENT)
public class FTBZombiesClientEventHandler
{
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(FTBZombiesBlocks.CONTROLLER), 0, new ModelResourceLocation(FTBZombiesBlocks.CONTROLLER.getRegistryName(), "normal"));
	}
}