package me.modmuss50.ftb.zombies;

import reborncore.common.registration.RebornRegistry;
import reborncore.common.registration.impl.ConfigRegistry;

@RebornRegistry(modID =  FTBZombies.MOD_ID)
public class FTBZombiesConfig {
	@ConfigRegistry
	public static String api_url = "https://go.alwa.io/";

	@ConfigRegistry
	public static boolean convention = false;
}