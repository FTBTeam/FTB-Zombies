package me.modmuss50.ftb.zombies;

import net.minecraftforge.common.config.Config;

@Config(modid = FTBZombies.MOD_ID, category = "")
public class FTBZombiesConfig {
	public static final General general = new General();

	public static class General {
		public String api_url = "https://go.alwa.io/";
		public int controller_radius = 21;
		public int controller_height = 22;
		public int zombie_count = 10;
		public String login = "minecraft:entity.parrot.fly";
		public String zombie_sound = "minecraft:entity.enderdragon.growl";
		public String villager_sound = "minecraft:ui.toast.challenge_complete";
		public String[] names = {
				"Dr. Zombie",
				"Mister Villager"
		};
	}
}