package com.mcbans.firestar.eventhandlers;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.api.responses.AuthenticationResponse;

public class LoginEventHandler {
	public static Map<UUID, AuthenticationResponse> respones = new HashMap<UUID, AuthenticationResponse>();

	@Listener
	public void playerAuth(ClientConnectionEvent.Auth event){
		String name = event.getProfile().getName();
		UUID uuid = event.getProfile().getUniqueId();
		if(uuid!=null){
			// Handle player login
			try {
				URL url = new URL("http://api.mcbans.com/v3/"+MCBansMod.bridge.getConfig().getNode("server", "apiKey").getString()+"/loginNew/"+uuid.toString()+"/"+event.getConnection().getAddress().getAddress().toString().replace("/", "")+"/json");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
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
				AuthenticationResponse ar = (new Gson()).fromJson(reader, AuthenticationResponse.class);
				if(ar.getBanStatus().equalsIgnoreCase("t") || ar.getBanStatus().equalsIgnoreCase("l") || (ar.getBanStatus().equalsIgnoreCase("g") && MCBansMod.bridge.getConfig().getNode("login", "block-zero-reputation").getBoolean())){
					event.setMessage(Text.of(ar.getBanReason()));
					event.setCancelled(true);
					return;
				}
				if(MCBansMod.bridge.getConfig().getNode("login", "block-minimum-reputation").getBoolean()){
					if(ar.playerRep<MCBansMod.bridge.getConfig().getNode("login", "minimum-reputation").getDouble() && MCBansMod.bridge.getConfig().getNode("login", "block-minimum-reputation").getBoolean()){
						event.setMessage(Text.of("You do not meet the minimum reputation requirements for this server!"));
						event.setCancelled(true);
						return;
					}
				}
				if(MCBansMod.bridge.getConfig().getNode("login", "block-maximum-alts").getBoolean()){
					if(ar.getAltCount()>MCBansMod.bridge.getConfig().getNode("login", "maximum-alts").getInt()){
						event.setMessage(Text.of("Too many alternate accounts, connection rejected!"));
						event.setCancelled(true);
						return;
					}
				}
				respones.put(uuid, ar);
			} catch (IOException e) {
				MCBansMod.bridge.getLogger().error("MCBans API Server Error");
			}
		}else{
			for(Player p: Sponge.getGame().getServer().getOnlinePlayers()){
				if(p.hasPermission("mcbans.login.error")){
					p.sendMessage(Text.of("[MCBans Error] UUID missing from player login!")); // come back later for styling
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	@Listener
	public void playerJoin(ClientConnectionEvent.Join event){
		Player player = event.getTargetEntity();
		UUID uuid = player.getProfile().getUniqueId();
		if(LoginEventHandler.respones.containsKey(uuid)){
			AuthenticationResponse ar = LoginEventHandler.respones.get(uuid);
			LoginEventHandler.respones.remove(uuid);
			if(ar.is_mcbans_mod.equalsIgnoreCase("y")){
				MCBansMod.getBridge();
				MCBansMod.sendMessage(player, Text.builder("You are logged in as a MCBans staff member").color(TextColors.WHITE).toText());
				for(Player p: Sponge.getGame().getServer().getOnlinePlayers()){
					if(p.hasPermission("mcbans.login.view.mcbans")){
						MCBansMod.getBridge();
						MCBansMod.sendMessage(p, Text.builder(player.getName()+" is a MCBans staff member").color(TextColors.GOLD).toText());
					}
				}
			}
			if(!ar.connectMessage.equals("")){
				MCBansMod.sendActionBar(player, Text.builder(ar.connectMessage).color(TextColors.WHITE).toText());
			}
			if(ar.isPremium()){
				if(ar.altList.size()>0){
					String alts = "";
					for(String s: ar.altList){
						alts+=((!alts.equals(""))?", ":"")+s;
					}
					for(Player p: Sponge.getGame().getServer().getOnlinePlayers()){
						if(p.hasPermission("mcbans.login.view.alts")){
							MCBansMod.sendMessage(
									p,
									Text.builder(player.getName())
									.color(TextColors.LIGHT_PURPLE)
									.append(
											Text.builder(" Alternates ["+alts+"]")
											.color(TextColors.WHITE)
											.toText()
											).toText()
									);
						}
					}
				}
				if(ar.nameChanges.size()>0){
					String previous = "";
					for(String s: ar.nameChanges){
						previous+=((!previous.equals(""))?", ":"")+s;
					}
					for(Player p: Sponge.getGame().getServer().getOnlinePlayers()){
						if(p.hasPermission("mcbans.login.view.previous")){
							MCBansMod.sendMessage(
									p,
									Text.builder(player.getName())
									.color(TextColors.LIGHT_PURPLE)
									.append(
											Text.builder(" Previous ["+previous+"]")
											.color(TextColors.WHITE)
											.toText()
											).toText()
									);
						}
					}
				}
			}
			if(ar.getBans().size()>0){
				MCBansMod.sendActionBar(player, Text.builder("You have bans on your record!").color(TextColors.WHITE).toText());
				try {
					MCBansMod.sendMessage(
							player, 
							Text
							.builder("Click here to check your ban status")
							.onClick(
									TextActions
									.openUrl(
											new URL(
													"http://mcbans.com/player/"+(uuid.toString().replace("-", ""))
													)
											)
									)
							.onHover(TextActions.showText(Text.of("Lookup player on MCBans.com")))
							.color(TextColors.RED)
							.style(TextStyles.UNDERLINE)
							.toText()
							);
				} catch (MalformedURLException e) {
					MCBansMod.bridge.getLogger().error("URL malformed in mcbans player link");
				}

				switch(MCBansMod.bridge.getConfig().getNode("join", "show-ban-type").getInt()){
				case 1:
					for(Player p: Sponge.getGame().getServer().getOnlinePlayers()){
						if(p.hasPermission("mcbans.login.view.bans")){
							MCBansMod.sendMessage(
									p,
									Text.builder("")
									.color(TextColors.WHITE)
									.toText()
									);
							try {
								MCBansMod.sendMessage(
										p,
										Text.builder(player.getName())
										.color(TextColors.LIGHT_PURPLE)
										.append(
												Text.builder(" has bans ["+ar.playerRep+"/10]")
												.color(TextColors.WHITE)
												.toText()
												)
										.append(
												Text.builder(" [L]")
												.onHover(TextActions.showText(Text.of("Lookup player on MCBans.com")))
												.onClick(TextActions.openUrl(new URL("http://mcbans.com/player/"+(uuid.toString().replace("-", "")))))
												.color(TextColors.GREEN)
												.toText()
												).toText()
										);
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							}
							int x = 0;
							for(HashMap<String, String> ban : ar.getBans()){
								x++;
								try {
									MCBansMod.sendMessage(
											p,
											Text.builder(x+": "+ban.get("type")+" - "+ban.get("server")+" \""+ban.get("reason")+"\"")
											.color(TextColors.WHITE)
											.append(
													Text.builder(" [L]")
													.onHover(TextActions.showText(Text.of("Lookup ban on MCBans.com")))
													.onClick(TextActions.openUrl(new URL("http://mcbans.com/ban/"+ban.get("id"))))
													.color(TextColors.GREEN)
													.toText()
													).toText()
											);
								} catch (MalformedURLException e) {
									e.printStackTrace();
								}
							}
							MCBansMod.sendMessage(
									p,
									Text.builder("")
									.color(TextColors.WHITE)
									.toText()
									);
						}
					}
					break;
				case 2:
					for(Player p: Sponge.getGame().getServer().getOnlinePlayers()){
						if(p.hasPermission("mcbans.login.view.bans")){
							MCBansMod.sendMessage(
									p,
									Text.builder(player.getName())
									.color(TextColors.LIGHT_PURPLE)
									.append(
											Text.builder(" has bans ["+ar.playerRep+"/10]")
											.color(TextColors.WHITE)
											.toText()
											)
									.append(
											Text.builder(" [C]")
											.onHover(TextActions.showText(Text.of("Lookup the player")))
											.onClick(TextActions.runCommand("/lup "+player.getName()))
											.color(TextColors.GREEN)
											.toText()
											).toText()
									);
						}
					}
					break;
				}

			}else{
				if(MCBansMod.bridge.getConfig().getNode("join", "show-good").getBoolean()){
					for(Player p: Sponge.getGame().getServer().getOnlinePlayers()){
						if(p.hasPermission("mcbans.login.view.good")){
							try {
								MCBansMod.sendMessage(
										p,
										Text.builder(player.getName())
										.color(TextColors.LIGHT_PURPLE)
										.append(
												Text.builder(" ["+ar.playerRep+"/10]")
												.color(TextColors.WHITE)
												.toText()
												)
										.append(
												Text.builder(" [L]")
												.onHover(TextActions.showText(Text.of("Lookup player on MCBans.com")))
												.onClick(TextActions.openUrl(new URL("http://mcbans.com/player/"+(uuid.toString().replace("-", "")))))
												.color(TextColors.GREEN)
												.toText()
												).toText()
										);
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}
