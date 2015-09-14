package com.mcbans.firestar.commands;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import com.google.gson.Gson;
import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.api.responses.BanResponse;

public class BanCommands  implements CommandExecutor {

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String player = args.<String>getOne("player").get();
		String admin = src.getName();
		String reason = args.<String>getOne("reason").get();
		if(player.equals("") && reason.equals("")){
			src.sendMessage(Texts.of("Not Formatted Properly"));
		}
		try {
			URL url = new URL("http://api.mcbans.com/v3/"+MCBansMod.apiKey+"/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			Map<String,String> postData = new HashMap<String,String>();
			postData.put("exec", "localBan");
			postData.put("player", player);
			postData.put("admin", admin);
			OutputStream output = connection.getOutputStream();
			String postDataString = "";
			for(Entry<String,String> d : postData.entrySet()){
				postDataString += ((!postDataString.equals(""))?"&":"")+d.getKey()+"="+d.getValue();
			}
			//MCBansMod.sendActionBar((Player) src, postDataString);
		    output.write(postDataString.getBytes("UTF-8"));
			InputStream is = connection.getInputStream();
			Scanner fromWeb = new Scanner(is);
			StringBuilder sb = new StringBuilder();
			while(fromWeb.hasNext()){
				sb.append(fromWeb.next());
			}
			String returnData = sb.toString();
			
			Gson gson = new Gson();
			BanResponse ar = gson.fromJson(returnData, BanResponse.class);
			//MCBansMod.sendActionBar((Player) src, "API Response: "+ar.getMsg());
			if(ar.getError()!=null){
				MCBansMod.sendActionBar((Player) src, "Error:\n"+ar.getError());
			}else{
				MCBansMod.sendActionBar((Player) src, returnData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CommandResult.success();
	}

}
