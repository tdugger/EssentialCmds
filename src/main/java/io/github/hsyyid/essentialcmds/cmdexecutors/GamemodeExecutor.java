/*
 * This file is part of EssentialCmds, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015 - 2015 HassanS6000
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.hsyyid.essentialcmds.cmdexecutors;

import io.github.hsyyid.essentialcmds.internal.CommandExecutorBase;
import org.apache.commons.lang3.math.NumberUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

import javax.annotation.Nonnull;

public class GamemodeExecutor extends CommandExecutorBase
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		String gamemode = ctx.<String> getOne("gamemode").get();
		Optional<Player> optionalPlayer = ctx.<Player> getOne("player");

		if (!optionalPlayer.isPresent())
		{
			if (src instanceof Player)
			{
				Player player = (Player) src;

				if (NumberUtils.isNumber(gamemode))
				{
					if (Integer.parseInt(gamemode) == 1)
					{
						GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.CREATIVE);
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Successfully changed " + player.getName() + "'s gamemode."));
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to creative"));
						player.offer(data);
					}
					else if (Integer.parseInt(gamemode) == 0)
					{
						GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.SURVIVAL);
						player.offer(data);
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Successfully changed " + player.getName() + "'s gamemode."));
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to survival"));
					}
					else if (Integer.parseInt(gamemode) == 2)
					{
						GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.ADVENTURE);
						player.offer(data);
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Successfully changed " + player.getName() + "'s gamemode."));
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to adventure"));
					}
					else if (Integer.parseInt(gamemode) == 3)
					{
						GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.SPECTATOR);
						player.offer(data);
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Successfully changed " + player.getName() + "'s gamemode."));
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to spectator"));
					}
					else
					{
						player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, gamemode + " does not appear to be a gamemode!"));
					}
				}
				else
				{
					if (gamemode.equals("creative") || gamemode.equals("c"))
					{
						GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.CREATIVE);
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Successfully changed " + player.getName() + "'s gamemode."));
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to creative"));
						player.offer(data);
					}
					else if (gamemode.equals("survival") || gamemode.equals("s"))
					{
						GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.SURVIVAL);
						player.offer(data);
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Successfully changed " + player.getName() + "'s gamemode."));
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to survival"));
					}
					else if (gamemode.equals("adventure") || gamemode.equals("a"))
					{
						GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.ADVENTURE);
						player.offer(data);
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Successfully changed " + player.getName() + "'s gamemode."));
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to adventure"));
					}
					else if (gamemode.equals("spectator") || gamemode.equals("spec"))
					{
						GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.SPECTATOR);
						player.offer(data);
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Successfully changed " + player.getName() + "'s gamemode."));
						player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to spectator"));
					}
					else
					{
						player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, gamemode + " does not appear to be a gamemode!"));
					}
				}
			}
			else
			{
				src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /gamemode!"));
			}
		}
		else if (src.hasPermission("essentialcmds.gamemode.others"))
		{
			Player player = optionalPlayer.get();

			if (NumberUtils.isNumber(gamemode))
			{
				if (Integer.parseInt(gamemode) == 1)
				{
					GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.CREATIVE);
					player.offer(data);
					player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to creative"));
				}
				else if (Integer.parseInt(gamemode) == 0)
				{
					GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.SURVIVAL);
					player.offer(data);
					player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to survival"));
				}
				else if (Integer.parseInt(gamemode) == 2)
				{
					GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.ADVENTURE);
					player.offer(data);
					player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to adventure"));
				}
				else if (Integer.parseInt(gamemode) == 3)
				{
					GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.SPECTATOR);
					player.offer(data);
					player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to spectator"));
				}
				else
				{
					player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, gamemode + " does not appear to be a gamemode!"));
				}
			}
			else
			{
				if (gamemode.equals("creative") || gamemode.equals("c"))
				{
					GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.CREATIVE);
					player.offer(data);
					player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to creative"));
				}
				else if (gamemode.equals("survival") || gamemode.equals("s"))
				{
					GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.SURVIVAL);
					player.offer(data);
					player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to survival"));
				}
				else if (gamemode.equals("adventure") || gamemode.equals("a"))
				{
					GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.ADVENTURE);
					player.offer(data);
					player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to adventure"));
				}
				else if (gamemode.equals("spectator") || gamemode.equals("spec"))
				{
					GameModeData data = player.getGameModeData().set(Keys.GAME_MODE, GameModes.SPECTATOR);
					player.offer(data);
					player.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set your gamemode to spectator"));
				}
				else
				{
					player.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, gamemode + " does not appear to be a gamemode!"));
				}
			}
		}

		return CommandResult.success();
	}

	@Nonnull
	@Override
	public String[] getAliases()
	{
		return new String[] { "gamemode", "gm" };
	}

	@Nonnull
	@Override
	public CommandSpec getSpec()
	{
		return CommandSpec
			.builder()
			.description(Text.of("Gamemode Command"))
			.permission("essentialcmds.gamemode.use")
			.arguments(GenericArguments.seq(
				GenericArguments.onlyOne(GenericArguments.string(Text.of("gamemode"))), 
				GenericArguments.onlyOne(GenericArguments.optional(GenericArguments.player(Text.of("player"))))))
			.executor(this).build();
	}
}
