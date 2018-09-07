package me.modmuss50.ftb.zombies.guiTweaks;

import me.modmuss50.ftb.zombies.EnumDifficultly;
import me.modmuss50.ftb.zombies.MapHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class GuiPlay extends GuiScreen {

	public GuiTextField nameField;
	public GuiButton play;

	@Override
	public void initGui() {
		super.initGui();
		this.nameField = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, 45, 200, 20);
		this.nameField.setFocused(true);

		this.buttonList.clear();

		this.buttonList.add(new GuiButton(0, 5, 5, 50, 20, "Back"));
		this.buttonList.add(play = new GuiButton(1, this.width / 2 - 100, 100, 200, 20, "Play"));

		play.enabled = false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		this.drawCenteredString(this.fontRenderer, "Play Project Z", this.width / 2, 20, -1);
		this.drawCenteredString(this.fontRenderer, "Name:", this.width / 2 - 120, 51, -1);
		nameField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char keyChar, int keyCode) throws IOException {
		if (this.nameField.isFocused()) {
			this.nameField.textboxKeyTyped(keyChar, keyCode);
		}
		play.enabled = isNameValid();
	}

	private boolean isNameValid() {
		if (nameField.getText().isEmpty()) {
			return false;
		}
		return nameField.getText().matches("[a-zA-Z0-9 ]*");
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
		}
		if (button.id == 1) {
			MapHandler.play(nameField.getText(), EnumDifficultly.ZOMBIE);
		}
	}
}
