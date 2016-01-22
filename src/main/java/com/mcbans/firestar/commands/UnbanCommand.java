package com.mcbans.firestar.commands;

import java.io.IOException;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.api.HTTPHandler;
import com.mcbans.firestar.api.requests.GlobalBan;
import com.mcbans.firestar.api.requests.Unban;
import com.mcbans.firestar.api.responses.ActionResponse;
import com.mcbans.firestar.api.responses.BanResponse;

public class UnbanCommand implements CommandExecutor {

	@SuppressWarnings("static-access")
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!src.hasPermission("mcbans.unban")){
			MCBansMod.getBridge().sendMessage(src, Text.builder("You do not have permission for this command!").color(TextColors.RED).toText());
			return CommandResult.empty();
		}
		String player = (String) args.getOne("player").get();
		(new Thread(new UnbanCommand.Threaded(player, src.getName(), src))).start();
		return CommandResult.success();
	}
	public static class Threaded implements Runnable{
		String player;
		String admin;
		CommandSource src;
		public Threaded(String p, String a, CommandSource s){
			player = p;
			admin = a;
			src = s;
		}
		@Override
		public void run() {
			Unban ub = new Unban( player, src.getName());
			try {
				ActionResponse actionResponse = HTTPHandler.execute(ub);
				if(actionResponse.getResult().equals("y")){
					MCBansMod.getBridge().sendMessage(src, Text.builder("Success: Unbanned "+actionResponse.getPlayer()).color(TextColors.GREEN).toText()); //  come back for styling
				}else{
					MCBansMod.getBridge().sendMessage(src, Text.builder("Error: "+actionResponse.getMsg()).color(TextColors.RED).toText()); //  come back for styling
				}
			} catch (InstantiationException | IllegalAccessException | IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
