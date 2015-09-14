package com.mcbans.firestar.commands;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.api.responses.AuthenticationResponse;

public class TestCommand implements CommandExecutor  {

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String name = (String) args.getOne("login").get();
		if(name!=null){
			// Handle player login
			try {
				URL url = new URL("http://api.mcbans.com/v3/"+MCBansMod.apiKey+"/loginNew/"+name+"/127.0.0.1/json");
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
				MCBansMod.sendActionBar((Player) src, "Ban response: "+ar.getBanStatus());
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}else{
			MCBansMod.sendActionBar( (Player) src, "[Error] UUID missing from player login!");
			/*for(Player p: game.getServer().getOnlinePlayers()){
				if(p.hasPermission("mcbans.login.error")){
					MCBansMod.sendActionBar(src, "[Error] UUID missing from player login!");
				}
			}*/
		}
		return null;
	}


}
