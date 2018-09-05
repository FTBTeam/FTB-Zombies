package me.modmuss50.ftb.zombies.commands.timer;

import me.modmuss50.ftb.zombies.commands.FTBCommandBase;
import me.modmuss50.ftb.zombies.timer.TimerHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by modmuss50 on 14/02/2017.
 */
public class CommandStart extends FTBCommandBase {
	@Override
	public String getName() {
		return "start";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "start";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		TimerHandler.startTimer(TimerHandler.getStoppedTime());
		sender.sendMessage(new TextComponentString("Timer started"));
		super.execute(server, sender, args);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
