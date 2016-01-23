package com.mcbans.firestar.commands;

import java.io.IOException;
import java.util.regex.PatternSyntaxException;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.api.HTTPHandler;
import com.mcbans.firestar.api.requests.LookupPlayer;
import com.mcbans.firestar.api.responses.PlayerLookupResponse;

public class LookupCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!src.hasPermission("mcbans.lookup")){
			MCBansMod.getBridge();
			MCBansMod.sendMessage(src, Text.builder("You do not have permission for this command!").color(TextColors.RED).toText());
			return CommandResult.empty();
		}
		String player = (String) args.getOne("target").get();
		String admin = src.getName();
		boolean foundMatch = false;
		try {
			foundMatch = player.matches("([a-zA-Z0-9-]{0,36})");
		} catch (PatternSyntaxException ex) {}
		if(foundMatch){
			(new Thread(new LookupCommand.Threaded("", player, admin, src))).start();
		}else{
			(new Thread(new LookupCommand.Threaded(player, "", admin, src))).start();
		}
		return CommandResult.success();
	}

	public static class Threaded implements Runnable{
		String player;
		String admin;
		String uuid;
		CommandSource src;
		public Threaded(String p, String a, String u, CommandSource s){
			player = p;
			uuid = u;
			admin = a;
			src = s;
		}

		@SuppressWarnings("static-access")
		@Override
		public void run() {
			try {
				LookupPlayer b = new LookupPlayer(player, uuid, admin);
				PlayerLookupResponse lookupResponse = HTTPHandler.execute(b);
				MCBansMod.getBridge();
				MCBansMod.sendMessage(src, Text.of(""));
				MCBansMod.getBridge();
				MCBansMod.sendMessage(
						src, 
						Text
						.builder(lookupResponse.getPlayer())
						.color(TextColors.LIGHT_PURPLE)
						.append(
								Text
								.builder(" has the reputation "+lookupResponse.getReputation()+"/10")
								.color(TextColors.WHITE)
								.toText()
								)
						.toText()
						);
				MCBansMod.getBridge();
				MCBansMod.sendMessage(
						src, 
						Text
						.builder("UUID: "+lookupResponse.getUuid())
						.color(TextColors.WHITE)
						.toText()
						);
				if(lookupResponse.getGlobal().size()==0 && lookupResponse.getLocal().size()==0){
					MCBansMod.getBridge();
					MCBansMod.sendMessage(src, Text.builder("No bans on record!").color(TextColors.WHITE).toText());
				}
				if(lookupResponse.getGlobal().size()>0){
					MCBansMod.getBridge();
					MCBansMod.sendMessage(src, Text.builder("----Global Bans----").color(TextColors.RED).toText());
					for(String s: lookupResponse.getGlobal()){
						MCBansMod.getBridge();
						MCBansMod.sendMessage(src, Text.builder("  "+s).color(TextColors.WHITE).toText());
					}
				}
				if(lookupResponse.getLocal().size()>0){
					MCBansMod.getBridge();
					MCBansMod.sendMessage(src, Text.builder("----Local Bans----").color(TextColors.AQUA).toText());
					for(String s: lookupResponse.getLocal()){
						MCBansMod.getBridge();
						MCBansMod.sendMessage(src, Text.builder("  "+s).color(TextColors.WHITE).toText());
					}
				}
				MCBansMod.getBridge();
				MCBansMod.sendMessage(src, Text.of(""));
			} catch (InstantiationException | IllegalAccessException | IOException e) {
				e.printStackTrace();
			}
		}

	}

}
