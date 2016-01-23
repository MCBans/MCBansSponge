package com.mcbans.firestar.commands;

import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.mcbans.firestar.api.requests.Request;

public class BanCommand implements CommandExecutor {

	@Override
	@SuppressWarnings("static-access")
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String player = (String) args.getOne("player").get();
		String admin = src.getName();
		if(player.equalsIgnoreCase("t") || player.equalsIgnoreCase("g")){
			String[] parts = ((String) args.getOne("reason").get()).split(" ");
			Request b = null;
			String reason = "";
			switch(player){
			case "t":
				if(!src.hasPermission("mcbans.ban.temp")){
					MCBansMod.getBridge();
					MCBansMod.sendMessage(src, Text.builder("You do not have permission for this command!").color(TextColors.RED).toText());
					return CommandResult.empty();
				}
				String number = parts[0];
				parts = ArrayUtils.remove(parts, 0);
				String unit = parts[0];
				parts = ArrayUtils.remove(parts, 0);
				player = parts[0];
				parts = ArrayUtils.remove(parts, 0);
				reason = StringUtils.join(parts, " ");
				MCBansMod.bridge.getLogger().info(player+"="+number+"="+unit+"="+reason+"=");
				if(player.equals("") || reason.equals("") || number.equals("") || unit.equals("")){
					src.sendMessage(Text.builder("Not Formatted Properly").toText());
					return CommandResult.empty();
				}
				(new Thread(new TempCommand.Threaded(player, admin, reason, number, unit, src))).start();
				break;
			case "g":
				if(!src.hasPermission("mcbans.ban.global")){
					MCBansMod.getBridge();
					MCBansMod.sendMessage(src, Text.builder("You do not have permission for this command!").color(TextColors.RED).toText());
					return CommandResult.empty();
				}
				player = parts[0];
				parts = ArrayUtils.remove(parts, 0);
				reason = StringUtils.join(parts, " ");
				if(player.equals("") || reason.equals("")){
					src.sendMessage(Text.builder("Not Formatted Properly").toText());
					return CommandResult.empty();
				}
				(new Thread(new GlobalCommand.Threaded(player, admin, reason, src))).start();
				break;
			}
			Optional<Player> p = Sponge.getGame().getServer().getPlayer(player);
			if(p.isPresent()){
				p.get().kick(Text.of(reason));
			}
		}else{
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
		}
		return CommandResult.success();
	}

}
