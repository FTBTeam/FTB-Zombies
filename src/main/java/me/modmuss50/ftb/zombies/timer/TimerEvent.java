package me.modmuss50.ftb.zombies.timer;

import me.modmuss50.ftb.zombies.GameContoller;
import me.modmuss50.ftb.zombies.PacketFinishRun;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import reborncore.common.network.NetworkManager;

import java.io.IOException;

public class TimerEvent {

	public static boolean hasFinished = false;

	@SubscribeEvent
	public static void worldLoadEvent(WorldEvent.Load event){
		TimerHandler.reset();
		hasFinished = false;
		GameContoller.savedCount = 0;
		GameContoller.totalCount = 0;
	}

	@SubscribeEvent
	public static void playerJoin(PlayerEvent.PlayerLoggedInEvent event) throws IOException {
		TimerHandler.syncWith((EntityPlayerMP) event.player);
		if (!TimerHandler.isActive()) {
			TimerHandler.startTimer(TimerHandler.getStoppedTime());
			TimerHandler.syncWithAll();
		}
	}

	@SubscribeEvent
	public static void worldTick(TickEvent.WorldTickEvent event) {
		long timeLeft = Timer.maxTime - TimerHandler.getTimeDifference();
		if ((timeLeft <= 0 || (GameContoller.totalCount != 0 && GameContoller.savedCount != 0 && GameContoller.savedCount == GameContoller.totalCount)) && !hasFinished) {
			hasFinished = true;
			TimerHandler.stop();
			NetworkManager.sendToAll(new PacketFinishRun(GameContoller.savedCount, GameContoller.totalCount, TimerHandler.getTimeDifference()));
		}
	}

}
