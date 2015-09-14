package com.mcbans.firestar.commands.specs;

import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.text.Text.Literal;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;
import org.spongepowered.api.util.command.spec.CommandSpec.Builder;

import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.commands.BanCommands;
import com.mcbans.firestar.commands.TestCommand;

public class MCBansCommandSpecs {
	public MCBansCommandSpecs( MCBansMod plugin, GameInitializationEvent event ){
		event.getGame().getCommandDispatcher().register(
			plugin, 
			CommandSpec.builder()
				.executor(new TestCommand())
				.description(Texts.of("MCBans test command"))
				.arguments(
					GenericArguments.onlyOne(GenericArguments.string(Texts.of("login")))
				).build(), 
			"mcbans"
		);
		event.getGame().getCommandDispatcher().register(
			plugin, 
			CommandSpec.builder()
				.executor(new BanCommands())
				.description(Texts.of("MCBans ban command"))
				.arguments(
					GenericArguments.onlyOne(GenericArguments.string(Texts.of("player"))),
					GenericArguments.remainingJoinedStrings(Texts.of("reason"))
				).build(), 
			"ban"
		);
	}
}
