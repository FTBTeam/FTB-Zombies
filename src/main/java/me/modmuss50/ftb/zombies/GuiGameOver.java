package me.modmuss50.ftb.zombies;

import me.modmuss50.ftb.zombies.timer.Timer;
import me.modmuss50.ftb.zombies.timer.TimerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

public class GuiGameOver extends GuiScreen {
	int saved;
	int total;
	long time;

	public GuiGameOver(int saved, int total, long time) {
		this.saved = saved;
		this.total = total;
		this.time = time;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 210, "Thank you for playing."));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);

		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

		this.drawCenteredString(fontRenderer, "Game Over!", this.width / 2, 10, Color.RED.getRGB());

		this.drawCenteredString(fontRenderer, "Well done you saved:", this.width / 2, 100, Color.WHITE.getRGB());
		this.drawCenteredString(fontRenderer, saved + " out of " + total + " villagers from the zombies", this.width / 2, 110, Color.GREEN.getRGB());
		this.drawCenteredString(fontRenderer, "In " + TimerHandler.getNiceTimeFromLong(time), this.width / 2, 120, Color.YELLOW.getRGB());
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			button.enabled = false;
			this.mc.world.sendQuittingDisconnectingPacket();
			this.mc.loadWorld(null);
			this.mc.displayGuiScreen(new GuiMainMenu());
		}
		super.actionPerformed(button);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
