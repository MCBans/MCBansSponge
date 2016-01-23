package com.mcbans.firestar.commands;

import java.io.IOException;
import java.util.Optional;

import org.spongepowered.api.Sponge;
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
import com.mcbans.firestar.api.requests.LocalBan;
import com.mcbans.firestar.api.responses.BanResponse;

public class LocalCommand implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String player = (String) args.getOne("player").get();
		String admin = src.getName();
		if(!src.hasPermission("mcbans.ban.local")){
			MCBansMod.getBridge();
			MCBansMod.sendMessage(src, Text.builder("You do not have permission for this command!").color(TextColors.RED).toText());
			return CommandResult.empty();
		}
		String reason = ((String) args.getOne("reason").get());
		if(player.equals("") || reason.equals("")){
			MCBansMod.getBridge();
			MCBansMod.sendMessage(src, Text.builder("Not Formatted Properly").color(TextColors.RED).toText());
			return CommandResult.empty();
		}
		(new Thread(new LocalCommand.Threaded(player, admin, reason, src))).start();;
		Optional<Player> p = Sponge.getGame().getServer().getPlayer(player);
		if(p.isPresent()){
			p.get().kick(Text.of(reason));
		}
		return CommandResult.success();
	}
	public static class Threaded implements Runnable{
		String player;
		String admin;
		String reason;
		CommandSource src;
		public Threaded(String p, String a, String r, CommandSource s){
			player = p;
			admin = a;
			src = s;
			reason = r;
		}
		@Override
		public void run() {
			try {
				LocalBan b = new LocalBan(player, "", "", reason, admin);
				BanResponse banResponse = HTTPHandler.execute(b);
				if(banResponse.getError()!=null){
					MCBansMod.getBridge();
					MCBansMod.sendMessage(src, Text.builder("Error: "+banResponse.getError()).color(TextColors.RED).toText()); //  come back for styling
				}else if(banResponse.getResult().equals("n") || banResponse.getResult().equals("a")){
					MCBansMod.getBridge();
					MCBansMod.sendMessage(src, Text.builder("Error: "+banResponse.getMsg()).color(TextColors.RED).toText());
				}else{
					MCBansMod.getBridge();
					MCBansMod.sendMessage(src, Text.builder("Success: "+banResponse.getMsg()).color(TextColors.GREEN).toText()); //  come back for styling
				}
			} catch (InstantiationException | IllegalAccessException | IOException e) {
				e.printStackTrace();
			}
		}

	}
}
