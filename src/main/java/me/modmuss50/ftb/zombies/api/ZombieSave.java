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

public class ZombieSave {
	public String secret;
	public long time;
	public String achievement;

	public ZombieSave(long time, String achievement) {
		this.secret = GameContoller.getIdRequest().run.secret;
		this.time = time;
		this.achievement = achievement;
	}

	//{"status":"success","message":"Achievement recorded."}
	//http://ftb-achievement.redstone.tech/run/15/achievement/list
	public static void put(ZombieSave zombieSave) throws IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPut request = new HttpPut(GameContoller.getAPI() + "run/" + GameContoller.getIdRequest().run.id + "/achievement");
		request.setEntity(new ByteArrayEntity(SerializationUtil.GSON.toJson(zombieSave).getBytes()));
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		String responseStr = EntityUtils.toString(entity, "UTF-8");
	}
}
