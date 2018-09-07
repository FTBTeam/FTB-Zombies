package me.modmuss50.ftb.zombies.guiTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class GuiEventHandler {

	@SubscribeEvent
	public static void initGui(GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.getGui() instanceof GuiMainMenu) {
			if (!event.getButtonList().isEmpty()) {
				GuiButton singlePlayerButton = event.getButtonList().stream().filter(guiButton -> guiButton.id == 1).findFirst().orElse(null);
				singlePlayerButton.displayString = "Play Project Z";

				event.getButtonList().remove(event.getButtonList().stream().filter(guiButton -> guiButton.id == 2).findFirst().orElse(null));
				event.getButtonList().remove(event.getButtonList().stream().filter(guiButton -> guiButton.id == 14).findFirst().orElse(null));
				event.getButtonList().remove(event.getButtonList().stream().filter(guiButton -> guiButton.id == 6).findFirst().orElse(null));
				event.getButtonList().remove(event.getButtonList().stream().filter(guiButton -> guiButton.id == 5).findFirst().orElse(null));
			}
		}
		if (event.getGui() instanceof GuiIngameMenu) {
			event.getButtonList().remove(event.getButtonList().stream().filter(guiButton -> guiButton.id == 12).findFirst().orElse(null));
			event.getButtonList().remove(event.getButtonList().stream().filter(guiButton -> guiButton.id == 7).findFirst().orElse(null));
			event.getButtonList().remove(event.getButtonList().stream().filter(guiButton -> guiButton.id == 5).findFirst().orElse(null));
			event.getButtonList().remove(event.getButtonList().stream().filter(guiButton -> guiButton.id == 6).findFirst().orElse(null));

			event.getButtonList().stream().filter(guiButton -> guiButton.id == 0).findFirst().orElse(null).width = 200;

			GuiButton quitButon = event.getButtonList().stream().filter(guiButton -> guiButton.id == 1).findFirst().orElse(null);
			quitButon.displayString = "Quit (Without saving!)";

		}
		if (event.getGui() instanceof GuiWorldSelection) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiPlay());
		}
	}

	@SubscribeEvent
	public static void actionPeformedEvent(GuiScreenEvent.ActionPerformedEvent event) {
		if (event.getGui() instanceof GuiMainMenu) {
			if (event.getButton().id == 1) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiPlay());
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void keyboardInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {
		if (event.getGui() instanceof GuiMainMenu) {
			if (Keyboard.getEventCharacter() == 'o') {
				Minecraft.getMinecraft().displayGuiScreen(new GuiOptions(event.getGui(), Minecraft.getMinecraft().gameSettings));
				event.setCanceled(true);
			}
		}
	}
}
