package me.modmuss50.ftb.zombies.api;

import me.modmuss50.ftb.zombies.GameContoller;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import reborncore.common.util.serialization.SerializationUtil;

import java.io.IOException;

public class IDRequest {
	public String status;
	public Run run;

	/**
	 * {
	 * "status": "success",
	 * "run": {
	 * "id": 11,
	 * "secret": "ftb.5a9aced6982393.26534714",
	 * "name": "modmuss50",
	 * "start": 1520094934,
	 * "difficulty": "normal"
	 * }
	 * }
	 */

	public static IDRequest get(String difficulty, String username) throws IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPut request = new HttpPut(GameContoller.getAPI() + "run/" + difficulty + "/ident/" + username);
		request.setEntity(new ByteArrayEntity(SerializationUtil.GSON.toJson(new JsonModel(GameContoller.macAddr)).getBytes()));
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		String responseStr = EntityUtils.toString(entity, "UTF-8");
		System.out.println(responseStr);
		return SerializationUtil.GSON_FLAT.fromJson(responseStr, IDRequest.class);
	}

	public static class Run {
		public int id;
		public String secret;
		public String name;
		public long start;
		public String difficulty;
		public long maxTime;
	}

	public static class JsonModel {
		String mac;

		public JsonModel(String mac) {
			this.mac = mac;
		}
	}

}
