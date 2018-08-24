package me.modmuss50.ftb.zombies;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import reborncore.common.network.ExtendedPacketBuffer;
import reborncore.common.network.INetworkPacket;
import techreborn.client.particle.ParticleSmoke;

import java.io.IOException;
import java.util.Random;

public class PacketSpawnSmoke implements INetworkPacket<PacketSpawnSmoke> {

	BlockPos pos;

	public PacketSpawnSmoke(BlockPos pos) {
		this.pos = pos;
	}

	public PacketSpawnSmoke() {
	}

	@Override
	public void writeData(ExtendedPacketBuffer buffer) throws IOException {
		buffer.writeBlockPos(pos);
	}

	@Override
	public void readData(ExtendedPacketBuffer buffer) throws IOException {
		pos = buffer.readBlockPos();
	}

	@Override
	public void processData(PacketSpawnSmoke message, MessageContext context) {
		World world = Minecraft.getMinecraft().world;
		Random random = new Random();
		EnumDyeColor color = EnumDyeColor.BLACK;

		for (int i = 0; i < 10; i++) {
			double mx = (random.nextFloat() - .5) / 25;
			double my = (random.nextFloat() - .5) / 50;
			double mz = (random.nextFloat() - .5) / 25;
			ParticleSmoke particleSmokeLarge = new ParticleSmoke(world, pos.getX() + 0.5, pos.getY() + 0.5 + (i * 0.1), pos.getZ() + 0.5, mx, my, mz);
			particleSmokeLarge.setMaxAge(250);
			if (color != EnumDyeColor.WHITE) {
				float[] rgb = EntitySheep.getDyeRgb(color);
				particleSmokeLarge.setRBGColorF(rgb[0] + (random.nextFloat() / 20), rgb[1] + (random.nextFloat() / 20), rgb[2] + (random.nextFloat() / 20));
			}
			particleSmokeLarge.multipleParticleScaleBy(0.7F);

			Minecraft.getMinecraft().effectRenderer.addEffect(particleSmokeLarge);

			world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		}


	}
}
