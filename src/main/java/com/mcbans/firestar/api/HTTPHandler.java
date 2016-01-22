package com.mcbans.firestar.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.api.requests.Request;
import com.mcbans.firestar.api.responses.BanResponse;

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
		
		r.info.put("exec", r.getExecution());
		for( Entry<String, String> infoSet : r.info.entrySet()){
			if(!infoSet.getValue().equals("")){
				postData += (postData.equals("")?"":"&")+infoSet.getKey()+"="+URLEncoder.encode(infoSet.getValue(), "UTF-8");
			}
		}
		output.write(postData.getBytes("UTF-8"));
		
		InputStream is = connection.getInputStream();
		@SuppressWarnings("resource")
		Scanner fromWeb = new Scanner(is);
		StringBuilder sb = new StringBuilder();
		while(fromWeb.hasNext()){
			sb.append(fromWeb.next());
		}
		String returnData = sb.toString();
		MCBansMod.bridge.getLogger().info(returnData);
		return (new Gson()).fromJson(returnData, s);
	}
}
