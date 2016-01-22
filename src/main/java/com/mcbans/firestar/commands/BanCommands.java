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

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.google.gson.Gson;
import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.api.HTTPHandler;
import com.mcbans.firestar.api.requests.LocalBan;
import com.mcbans.firestar.api.responses.BanResponse;

public class BanCommands  implements CommandExecutor {

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String player = args.<String>getOne("player").get();
		String admin = src.getName();
		String reason = args.<String>getOne("reason").get();
		if(player.equals("") && reason.equals("")){
			src.sendMessage(Text.builder("Not Formatted Properly").toText());
		}
		try {
			try {
				LocalBan b = new LocalBan(player, "", "", reason, admin);
				BanResponse banResponse = HTTPHandler.execute(b);
				if(banResponse.getError()!=null){
					((Player) src).sendMessage(Text.of("Error:\n"+banResponse.getError())); //  come back for styling
				}else{
					((Player) src).sendMessage(Text.of(banResponse)); //  come back for styling
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return CommandResult.success();
	}

}
