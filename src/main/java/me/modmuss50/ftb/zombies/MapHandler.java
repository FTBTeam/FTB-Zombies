package me.modmuss50.ftb.zombies;

import me.modmuss50.ftb.zombies.api.IDRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MapHandler {

	public static void play(String name, EnumDifficultly difficultly) throws IOException {
		//Resets the run manager
		GameContoller.setIdRequest(null);

		GameContoller.savedCount = 0;
		GameContoller.totalCount = 0;

		GameContoller.hasFinished = false;
		try {
			GameContoller.updateRequest(difficultly.name().toLowerCase(), name.replace(" ", "_"));
		} catch (IOException ex) {
			System.out.println("Do you have a connection to the server?");
			ex.printStackTrace();

			GameContoller.idRequest = new IDRequest();
			GameContoller.idRequest.status = "success";
			GameContoller.idRequest.run = new IDRequest.Run();
			GameContoller.idRequest.run.id = 999999999;
			GameContoller.idRequest.run.secret = "offline";
			GameContoller.idRequest.run.name = name;
			GameContoller.idRequest.run.difficulty = difficultly.name().toLowerCase();
		}

		Minecraft mc = Minecraft.getMinecraft();

		File saveDir = new File(mc.mcDataDir, "saves");
		File worldDir = new File(saveDir, "world");
		File mapDir = new File(mc.mcDataDir, "map");

		if (!mapDir.exists()) {
			mapDir.mkdir();
		}
		if (worldDir.exists()) {
			FileUtils.deleteDirectory(worldDir);
		}
		FileUtils.copyDirectory(mapDir, worldDir);

		WorldInfo worldInfo = getWorldInfo("world", saveDir);
		WorldSummary worldSummary = new WorldSummary(worldInfo, "world", worldInfo.getWorldName(), 0L, false);
		FMLClientHandler.instance().tryLoadExistingWorld(null, worldSummary);

	}

	public static WorldInfo getWorldInfo(String saveName, File savesDirectory) {
		File worldDir = new File(savesDirectory, saveName);
		if (!worldDir.exists()) {
			return null;
		} else {
			File file2 = new File(worldDir, "level.dat");
			return getWorldData(file2);
		}
	}

	public static WorldInfo getWorldData(File level) {
		try {
			NBTTagCompound levelNBT = CompressedStreamTools.readCompressed(new FileInputStream(level));
			NBTTagCompound dataNBT = levelNBT.getCompoundTag("Data");
			return new WorldInfo(dataNBT);
		} catch (Exception exception) {
			throw new RuntimeException("Failed to read world data", exception);
		}
	}

}
