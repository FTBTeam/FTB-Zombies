package me.modmuss50.ftb.zombies;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;

import java.io.IOException;

public class PacketFinishRun implements INetworkPacket<PacketFinishRun> {

	public int saved;
	public int total;
	public long time;

	public PacketFinishRun(int saved, int total, long time) {
		this.saved = saved;
		this.total = total;
		this.time = time;
	}

	public PacketFinishRun() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		extendedPacketBuffer.writeInt(saved);
		extendedPacketBuffer.writeInt(total);
		extendedPacketBuffer.writeLong(time);
	}

	@Override
	public void readData(ExtendedPacketBuffer extendedPacketBuffer) throws IOException {
		saved = extendedPacketBuffer.readInt();
		total = extendedPacketBuffer.readInt();
		time = extendedPacketBuffer.readLong();
	}

	@Override
	public void processData(PacketFinishRun packetFinishRun, MessageContext messageContext) {
		openGameOver();
	}

	@SideOnly(Side.CLIENT)
	public void openGameOver() {
		Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiGameOver(saved, total, time)));
	}
}
