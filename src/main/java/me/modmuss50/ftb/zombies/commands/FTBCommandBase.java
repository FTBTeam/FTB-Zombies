package me.modmuss50.ftb.zombies.commands;

import me.modmuss50.ftb.zombies.timer.TimerHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Created by modmuss50 on 13/02/2017.
 */
public abstract class FTBCommandBase extends CommandBase {

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		TimerHandler.syncWithAll();
	}
}
