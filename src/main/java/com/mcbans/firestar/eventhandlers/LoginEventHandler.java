package com.mcbans.firestar.eventhandlers;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Texts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.api.responses.AuthenticationResponse;

public class LoginEventHandler {
	@Listener
	public void playerAuth(ClientConnectionEvent.Auth event){
		String name = event.getProfile().getName();
		UUID uuid = event.getProfile().getUniqueId();
		if(uuid!=null){
			// Handle player login
			try {
				URL url = new URL("http://api.mcbans.com/v3/"+MCBansMod.apiKey+"/loginNew/"+uuid.toString()+"/"+event.getConnection().getAddress().getAddress().toString()+"/json");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				InputStream is = connection.getInputStream();
				Scanner fromWeb = new Scanner(is);
				StringBuilder sb = new StringBuilder();
				while(fromWeb.hasNext()){
					sb.append(fromWeb.next());
				}
				String loginData = sb.toString();
				System.out.println(loginData);
				Gson gson = new Gson();
				AuthenticationResponse ar = gson.fromJson(loginData, AuthenticationResponse.class);
				System.out.println("ban response: "+ar.getBanStatus());
				if(ar.getBanStatus().equalsIgnoreCase("l") || ar.getBanStatus().equalsIgnoreCase("g")){
					event.setMessage(Texts.of(ar.getBanReason()));
					event.setCancelled(true);
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}else{
			for(Player p: event.getGame().getServer().getOnlinePlayers()){
				if(p.hasPermission("mcbans.login.error")){
					MCBansMod.sendActionBar(p, "[Error] UUID missing from player login!");
				}
			}
		}
	}
}
