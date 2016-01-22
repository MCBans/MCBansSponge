package com.mcbans.firestar.commands.specs;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.text.Text;

import com.mcbans.firestar.MCBansMod;
import com.mcbans.firestar.commands.BanCommand;
import com.mcbans.firestar.commands.GlobalCommand;
import com.mcbans.firestar.commands.TempCommand;
import com.mcbans.firestar.commands.TestCommand;
import com.mcbans.firestar.commands.UnbanCommand;

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
				.executor(new BanCommand())
				.description(Text.of("MCBans ban command"))
				.arguments(
					GenericArguments.onlyOne(GenericArguments.string(Text.of("player"))),
					GenericArguments.remainingJoinedStrings(Text.of("reason"))
				).build(), 
			"ban"
		);
		Sponge.getGame().getCommandManager().register(
			plugin, 
			CommandSpec.builder()
				.executor(new GlobalCommand())
				.description(Text.of("MCBans global ban command"))
				.arguments(
					GenericArguments.onlyOne(GenericArguments.string(Text.of("player"))),
					GenericArguments.remainingJoinedStrings(Text.of("reason"))
				).build(), 
			"gban"
		);
		Sponge.getGame().getCommandManager().register(
			plugin, 
			CommandSpec.builder()
				.executor(new TempCommand())
				.description(Text.of("MCBans temp ban command"))
				.arguments(
					GenericArguments.onlyOne(GenericArguments.string(Text.of("number"))),
					GenericArguments.onlyOne(GenericArguments.string(Text.of("unit"))),
					GenericArguments.onlyOne(GenericArguments.string(Text.of("player"))),
					GenericArguments.remainingJoinedStrings(Text.of("reason"))
				).build(), 
			"tban"
		);
		Sponge.getGame().getCommandManager().register(
			plugin, 
			CommandSpec.builder()
			.executor(new UnbanCommand())
			.description(Text.of("MCBans unban command"))
			.arguments(
				GenericArguments.onlyOne(GenericArguments.string(Text.of("player")))
			).build(), 
			"unban"
		);
	}
}
