package com.mcbans.firestar.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.api.requests.Request;

public class HTTPHandler {
	@SuppressWarnings("unchecked")
	public static <T> T execute(Request r) throws InstantiationException, IllegalAccessException, IOException{
		Class<T> s = r.getResponse();
		URL url = new URL("http://api.mcbans.com/v3/"+MCBansMod.bridge.getConfig().getNode("server", "apiKey").getString()+"/");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		OutputStream output = connection.getOutputStream();
		String postData = "";

		Request.info.put("exec", r.getExecution());
		for( Entry<String, String> infoSet : Request.info.entrySet()){
			if(!infoSet.getValue().equals("")){
				postData += (postData.equals("")?"":"&")+infoSet.getKey()+"="+URLEncoder.encode(infoSet.getValue(), "UTF-8");
			}
		}
		output.write(postData.getBytes("UTF-8"));

		InputStream is = connection.getInputStream();
		@SuppressWarnings("resource")
		StringBuilder sb = new StringBuilder();
		byte[] data = new byte[1024];
		int readData = is.read(data);
		while(readData!=-1){
			sb.append(new String(ArrayUtils.subarray(data, 0, readData), "UTF-8"));
			readData = is.read(data);
		}
		String returnData = sb.toString();
		MCBansMod.bridge.getLogger().info(returnData);
		JsonReader reader = new JsonReader(new StringReader(returnData));
		reader.setLenient(true);
		return (new Gson()).fromJson(reader, s);
	}
}
