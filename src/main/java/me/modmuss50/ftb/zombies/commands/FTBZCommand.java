package me.modmuss50.ftb.zombies.commands;

import me.modmuss50.ftb.zombies.commands.timer.CommandTimer;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by Mark on 05/02/2017.
 */
public class FTBZCommand extends CommandTreeBase {
	public FTBZCommand() {
		addSubcommand(new CommandTimer());

	}

	@Override
	public String getName() {
		return "ftbz";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return getName();
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
}
