package com.mcbans.firestar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.chat.ChatTypes;

import com.mcbans.firestar.commands.specs.MCBansCommandSpecs;
import com.mcbans.firestar.eventhandlers.LoginEventHandler;

@Plugin(id = "MCB", name = "MCBans Banning Plugin", version="0.1")
public class MCBansMod {
	public static String prefix = "[MCBans]";
	public static String apiKey = "APIKEY";
	@Listener
    public void preinit(GamePreInitializationEvent event) {
		System.out.println("MCBANS STARTING! ALPHA TEST BUILD");
		
    }
    
	@Listener
    public void init(GameInitializationEvent event) {
        event.getGame().getEventManager().registerListeners(this, new LoginEventHandler());
        MCBansCommandSpecs mcbSpec = (new MCBansCommandSpecs(this, event));
    }
    
	@Listener
    public void postinit(GamePostInitializationEvent event) {
    	
    }
    public static void sendActionBar(Player p, String message){
    	p.sendMessage(ChatTypes.ACTION_BAR, Texts.of(prefix+" "+message));
    }
    
    
}
