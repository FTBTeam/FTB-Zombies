package me.modmuss50.ftb.zombies;

import me.modmuss50.ftb.zombies.api.IDRequest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GameContoller {

	public static int eggCount = 0;
	public static boolean hasFinished = false;
	public static int savedCount;
	public static int totalCount = 6;
	public static IDRequest idRequest;
	public static String macAddr;

	static {
		try {
			macAddr = getMac();
		} catch (Exception e) {
			macAddr = "unknown";
		}
	}

	public static IDRequest getIdRequest() {
		return idRequest;
	}

	public static void setIdRequest(IDRequest idRequest) {
		idRequest = idRequest;
	}

	public static void updateRequest(String difficulty, String username) throws IOException {
		IDRequest request = IDRequest.get(difficulty, username);
		if (request == null || !request.status.equals("success")) {
			//fuck
			//TODO go back to main menu or something
			System.out.println("Shit has hit the fan");
			return;
		}
		setIdRequest(request);
	}

	public static String getAPI() {
		return FTBZombiesConfig.general.api_url;
	}

	public static String getMac() throws UnknownHostException, SocketException {
		InetAddress ip = InetAddress.getLocalHost();
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		byte[] mac = network.getHardwareAddress();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		}
		return sb.toString();
	}

}
