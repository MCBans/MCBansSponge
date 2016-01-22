package com.mcbans.firestar.commands.specs;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.text.Text;

import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.commands.BanCommands;
import com.mcbans.firestar.commands.TestCommand;

public class MCBansCommandSpecs {
	public MCBansCommandSpecs( MCBansMod plugin, GameInitializationEvent event ){
		Sponge.getGame().getCommandManager().register(
			plugin, 
			CommandSpec.builder()
				.executor(new TestCommand())
				.description(Text.of("MCBans test command"))
				.arguments(
					GenericArguments.onlyOne(GenericArguments.string(Text.of("login")))
				).build(), 
			"mcbans"
		);
		Sponge.getGame().getCommandManager().register(
			plugin, 
			CommandSpec.builder()
				.executor(new BanCommands())
				.description(Text.of("MCBans ban command"))
				.arguments(
					GenericArguments.onlyOne(GenericArguments.string(Text.of("player"))),
					GenericArguments.remainingJoinedStrings(Text.of("reason"))
				).build(), 
			"ban"
		);
	}
}
