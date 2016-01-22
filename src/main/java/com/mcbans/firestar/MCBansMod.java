package com.mcbans.firestar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;

import com.google.inject.Inject;
import com.mcbans.firestar.commands.specs.MCBansCommandSpecs;
import com.mcbans.firestar.eventhandlers.LoginEventHandler;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "MCBans", name = "MCBans", version="0.1")
public class MCBansMod {

	public static final String NAME = "MCBans";
	public static MCBansMod bridge = null;
	
	@Inject
    @DefaultConfig(sharedRoot = true)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;
	
	@Inject
    @DefaultConfig(sharedRoot = true)
    private File defaultConfig;
	
	private ConfigurationNode config = null;
	
	@Inject
    private PluginContainer pluginContainer;
	
	public File getDefaultConfig() {
        return this.defaultConfig;
    }
    public ConfigurationLoader<CommentedConfigurationNode> getConfigManager() {
        return this.configManager;
    }
    
	@Listener
    public void preinit(GamePreInitializationEvent event) {
		this.getLogger().info("MCBans picking servers");
		this.getLogger().info("api.mcbans.com");
    }
    
	@Listener
    public void init(GameInitializationEvent event) {
        try {
        	if (!getDefaultConfig().exists()) {
				this.config = getConfigManager().load();
				getDefaultConfig().createNewFile();
				this.config.getNode("Version").setValue(1);
				
				// Server configs
	            this.config.getNode("server", "apiKey").setValue("<APIKEYHERE>");
	            this.config.getNode("server", "prefix").setValue("MCBans");
	            
	            // Auth configs
	            this.config.getNode("login", "block-zero-reputation").setValue(true);
	            
	            this.config.getNode("login", "block-minimum-reputation").setValue(false);
	            this.config.getNode("login", "minimum-reputation").setValue(4);
	            
	            this.config.getNode("login", "block-maximum-alts").setValue(false);
	            this.config.getNode("login", "maximum-alts").setValue(5);
	            this.config.getNode("join", "show-good").setValue(false);
	            
	            // Join configs
	            this.config.getNode("join", "show-ban-type").setValue(1);
	            
	            
	            
	            getConfigManager().save(this.config);
	            this.getLogger().info("Created default config file, enter your servers api key ( found at http://my.mcbans.com/servers )");
        	}
        	this.config = getConfigManager().load();
		} catch (IOException e) {
			this.getLogger().error("Issue with the creation of the config file");
		}
		Sponge.getGame().getEventManager().registerListeners(this, new LoginEventHandler());
        MCBansCommandSpecs mcbSpec = (new MCBansCommandSpecs(this, event));
    }
    
	@Listener
    public void postinit(GamePostInitializationEvent event) {
		bridge = this;
		MCBansMod.bridge.config.getNode("").getString();
    }
	
	public Logger getLogger(){
		return pluginContainer.getLogger();
	}
	
	
	
    public static MCBansMod getBridge() {
		return bridge;
	}
	public ConfigurationNode getConfig() {
		return config;
	}
	public PluginContainer getPluginContainer() {
		return pluginContainer;
	}
	public static void sendActionBar(Player p, Text message){
    	p.sendMessage(
			ChatTypes.ACTION_BAR, 
			Text.builder(MCBansMod.bridge.getConfig().getNode("server", "prefix").getString()).color(TextColors.AQUA).append(
				Text.builder("> ").color(TextColors.WHITE).toText()
			).append(message).toText());
    }
    public static void sendMessage(CommandSource p, Text message){
    	p.sendMessage(Text.builder(MCBansMod.bridge.getConfig().getNode("server", "prefix").getString()).color(TextColors.AQUA).append(
				Text.builder("> ").color(TextColors.WHITE).toText()
			).append(message).toText());
    }
    
}
