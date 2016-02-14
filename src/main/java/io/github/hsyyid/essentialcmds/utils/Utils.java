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
package io.github.hsyyid.essentialcmds.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.hsyyid.essentialcmds.EssentialCmds;
import io.github.hsyyid.essentialcmds.api.util.config.ConfigManager;
import io.github.hsyyid.essentialcmds.api.util.config.Configs;
import io.github.hsyyid.essentialcmds.api.util.config.Configurable;
import io.github.hsyyid.essentialcmds.managers.config.Config;
import io.github.hsyyid.essentialcmds.managers.config.HomeConfig;
import io.github.hsyyid.essentialcmds.managers.config.JailConfig;
import io.github.hsyyid.essentialcmds.managers.config.PlayerDataConfig;
import io.github.hsyyid.essentialcmds.managers.config.RulesConfig;
import io.github.hsyyid.essentialcmds.managers.config.SpawnConfig;
import io.github.hsyyid.essentialcmds.managers.config.WarpConfig;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.mutable.entity.FoodData;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

public class Utils
{
	private static Gson gson = new GsonBuilder().create();

	private static Game game = Sponge.getGame();
	private static Configurable mainConfig = Config.getConfig();
	private static Configurable warpsConfig = WarpConfig.getConfig();
	private static Configurable homesConfig = HomeConfig.getConfig();
	private static Configurable rulesConfig = RulesConfig.getConfig();
	private static Configurable jailsConfig = JailConfig.getConfig();
	private static Configurable spawnConfig = SpawnConfig.getConfig();
	private static Configurable playerDataConfig = PlayerDataConfig.getConfig();
	private static ConfigManager configManager = new ConfigManager();

	public static void setSQLPort(String value)
	{
		Configs.setValue(mainConfig, new Object[] { "mysql", "port" }, value);
	}

	public static void addMute(UUID playerUUID)
	{
		Connection c;
		Statement stmt;

		try
		{
			if (Utils.useMySQL())
			{
				SqlService sql = game.getServiceManager().provide(SqlService.class).get();
				String host = getMySQLHost();
				String port = getMySQLPort();
				String username = getMySQLUsername();
				String password = getMySQLPassword();
				String database = getMySQLDatabase();

				DataSource datasource = sql.getDataSource("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password);

				String executeString = "CREATE TABLE IF NOT EXISTS MUTES " + "(UUID TEXT PRIMARY KEY     NOT NULL)";
				execute(executeString, datasource);

				executeString = "INSERT INTO MUTES (UUID) " + "VALUES ('" + playerUUID.toString() + "');";
				execute(executeString, datasource);
			}
			else
			{
				try
				{
					Class.forName("org.sqlite.JDBC");
				}
				catch (ClassNotFoundException exception)
				{
					EssentialCmds.getEssentialCmds().getLogger().error("[EssentialCmds]: You do not have ANY database software installed! Mutes will not work or be saved until this is fixed.");
				}

				c = DriverManager.getConnection("jdbc:sqlite:Mutes.db");
				stmt = c.createStatement();

				String sql = "CREATE TABLE IF NOT EXISTS MUTES " + "(UUID TEXT PRIMARY KEY     NOT NULL)";
				stmt.executeUpdate(sql);

				sql = "INSERT INTO MUTES (UUID) " + "VALUES ('" + playerUUID.toString() + "');";
				stmt.executeUpdate(sql);

				stmt.close();
				c.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void removeMute(UUID playerUUID)
	{
		Connection c;
		Statement stmt;

		try
		{
			if (Utils.useMySQL())
			{
				SqlService sql = game.getServiceManager().provide(SqlService.class).get();
				String host = getMySQLHost();
				String port = getMySQLPort();
				String username = getMySQLUsername();
				String password = getMySQLPassword();
				String database = getMySQLDatabase();

				DataSource datasource = sql.getDataSource("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password);

				String executeString = "CREATE TABLE IF NOT EXISTS MUTES " + "(UUID TEXT PRIMARY KEY     NOT NULL)";
				execute(executeString, datasource);

				executeString = "DELETE FROM MUTES WHERE UUID='" + playerUUID.toString() + "';";
				execute(executeString, datasource);
			}
			else
			{
				try
				{
					Class.forName("org.sqlite.JDBC");
				}
				catch (ClassNotFoundException exception)
				{
					EssentialCmds.getEssentialCmds().getLogger().error("[EssentialCmds]: You do not have ANY database software installed! Mutes will not work or be saved until this is fixed.");
				}

				c = DriverManager.getConnection("jdbc:sqlite:Mutes.db");
				stmt = c.createStatement();

				String sql = "CREATE TABLE IF NOT EXISTS MUTES " + "(UUID TEXT PRIMARY KEY     NOT NULL)";
				stmt.executeUpdate(sql);

				sql = "DELETE FROM MUTES WHERE UUID='" + playerUUID.toString() + "';";
				stmt.executeUpdate(sql);

				stmt.close();
				c.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void startAFKService()
	{
		Scheduler scheduler = game.getScheduler();
		Task.Builder taskBuilder = scheduler.createTaskBuilder();

		taskBuilder.execute(() -> {
			for (Player player : game.getServer().getOnlinePlayers())
			{
				if (!player.hasPermission("essentialcmds.afk.exempt"))
				{
					for (AFK afk : EssentialCmds.afkList)
					{
						if (afk.getPlayer().getUniqueId().equals(player.getUniqueId()) && ((System.currentTimeMillis() - afk.lastMovementTime) > Utils.getAFK()) && !afk.getMessaged())
						{
							for (Player p : game.getServer().getOnlinePlayers())
							{
								p.sendMessage(Text.of(TextColors.BLUE, player.getName(), TextColors.GOLD, " is now AFK."));
								Optional<FoodData> data = p.get(FoodData.class);

								if (data.isPresent())
								{
									FoodData food = data.get();
									afk.setFood(food.foodLevel().get());
								}
							}
							afk.setMessaged(true);
							afk.setAFK(true);
						}

						if (afk.getAFK())
						{
							Player p = afk.getPlayer();
							Optional<FoodData> data = p.get(FoodData.class);

							if (data.isPresent())
							{
								FoodData food = data.get();

								if (food.foodLevel().get() < afk.getFood())
								{
									Value<Integer> foodLevel = food.foodLevel().set(afk.getFood());
									food.set(foodLevel);
									p.offer(food);
								}
							}

							if (!(p.hasPermission("essentialcmds.afk.kick.false")) && Utils.getAFKKick() && afk.getLastMovementTime() >= Utils.getAFKKickTimer())
							{
								p.kick(Text.of(TextColors.GOLD, "Kicked for being AFK too long."));
							}
						}
					}
				}
			}
		}).interval(1, TimeUnit.SECONDS).name("EssentialCmds - AFK").submit(game.getPluginManager().getPlugin("EssentialCmds").get().getInstance().get());
	}

	public static String getMySQLPort()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("mysql", "port");
		if (configManager.getString(node).isPresent())
			return node.getString();
		setSQLPort("8080");
		return "8080";
	}

	public static String getMySQLDatabase()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("mysql", "database");
		if (configManager.getString(node).isPresent())
			return node.getString();
		setSQLDatabase("Mutes");
		return "Mutes";
	}

	public static boolean isTeleportCooldownEnabled()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("teleport", "cooldown", "enabled");
		if (configManager.getBoolean(node).isPresent())
			return node.getBoolean();
		setTeleportCooldownEnabled(false);
		return false;
	}

	public static void setTeleportCooldownEnabled(boolean value)
	{
		Configs.setValue(mainConfig, new Object[] { "teleport", "cooldown", "enabled" }, value);
	}

	public static long getTeleportCooldown()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("teleport", "cooldown", "timer");
		if (configManager.getLong(node).isPresent())
			return node.getLong();
		setTeleportCooldown(10);
		return 10l;
	}

	public static void setTeleportCooldown(long value)
	{
		Configs.setValue(mainConfig, new Object[] { "teleport", "cooldown", "timer" }, value);
	}

	public static String getMySQLPassword()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("mysql", "password");
		if (configManager.getString(node).isPresent())
			return node.getString();
		setSQLPass("cat");
		return "cat";
	}

	public static String getMySQLUsername()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("mysql", "username");
		if (configManager.getString(node).isPresent())
			return node.getString();
		setSQLUsername("HassanS6000");
		return "HassanS6000";
	}

	public static String getMySQLHost()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("mysql", "host");
		if (configManager.getString(node).isPresent())
			return node.getString();
		setSQLHost("");
		return "";
	}

	public static boolean useMySQL()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("mysql", "use");
		if (configManager.getBoolean(node).isPresent())
			return node.getBoolean();
		setUseMySQL(false);
		return false;
	}

	public static void setUseMySQL(boolean value)
	{
		Configs.setValue(mainConfig, new Object[] { "mysql", "use" }, value);
	}

	public static String getLastTimePlayerJoined(UUID uuid)
	{
		CommentedConfigurationNode node = Configs.getConfig(playerDataConfig).getNode("player", uuid.toString(), "time");
		if (configManager.getString(node).isPresent())
			return node.getString();
		return "";
	}

	public static void setLastTimePlayerJoined(UUID uuid, String time)
	{
		Configs.setValue(playerDataConfig, new Object[] { "player", uuid.toString(), "time" }, time);
	}

	public static String getLoginMessage()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("message", "login");
		if (configManager.getString(node).isPresent())
			return node.getString();
		setLoginMessage("");
		return "";
	}

	public static void setLoginMessage(String value)
	{
		Configs.setValue(mainConfig, new Object[] { "message", "login" }, value);
	}

	public static String getDisconnectMessage()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("message", "disconnect");
		if (configManager.getString(node).isPresent())
			return node.getString();
		setDisconnectMessage("");
		return "";
	}

	public static void setDisconnectMessage(String value)
	{
		Configs.setValue(mainConfig, new Object[] { "message", "disconnect" }, value);
	}

	public static String getFirstJoinMsg()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("message", "firstjoin");

		if (configManager.getString(node).isPresent())
			return node.getString();

		setFirstJoinMsg("&4Welcome &a@p &4to the server!");
		return "&4Welcome &a@p &4to the server!";
	}

	public static void setFirstJoinMsg(String value)
	{
		Configs.setValue(mainConfig, new Object[] { "message", "firstjoin" }, value);
	}

	public static String getNick(Player player)
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("nick", player.getUniqueId().toString());
		if (configManager.getString(node).isPresent())
			return node.getString();
		else
			return player.getName();
	}

	public static void setNick(String value, UUID playerUuid)
	{
		Configs.setValue(mainConfig, new Object[] { "nick", playerUuid.toString() }, value);
	}

	public static boolean unsafeEnchanmentsEnabled()
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("unsafeenchantments", "enabled");
		if (configManager.getBoolean(node).isPresent())
			return node.getBoolean();
		setUnsafeEnchanmentsEnabled(false);
		return false;
	}

	public static void setUnsafeEnchanmentsEnabled(boolean value)
	{
		Configs.setValue(mainConfig, new Object[] { "unsafeenchantments", "enabled" }, value);
	}

	public static void setSQLDatabase(String value)
	{
		Configs.setValue(mainConfig, new Object[] { "mysql", "database" }, value);
	}

	public static void setSQLHost(String value)
	{
		Configs.setValue(mainConfig, new Object[] { "mysql", "host" }, value);
	}

	public static void setSQLPass(String value)
	{
		Configs.setValue(mainConfig, new Object[] { "mysql", "password" }, value);
	}

	public static void setSQLUsername(String value)
	{
		Configs.setValue(mainConfig, new Object[] { "mysql", "username" }, value);
	}

	public static void execute(String execute, DataSource datasource)
	{
		try
		{
			Connection connection = datasource.getConnection();
			Statement statement = connection.createStatement();
			statement.execute(execute);
			statement.close();
			connection.close();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void readMutes()
	{
		try
		{
			if (Utils.useMySQL())
			{
				SqlService sql = game.getServiceManager().provide(SqlService.class).get();
				String host = getMySQLHost();
				String port = getMySQLPort();
				String username = getMySQLUsername();
				String password = getMySQLPassword();
				String database = getMySQLDatabase();

				DataSource datasource = sql.getDataSource("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + username + "&password=" + password);

				String executeString = "CREATE TABLE IF NOT EXISTS MUTES " + "(UUID TEXT PRIMARY KEY  NOT NULL)";
				execute(executeString, datasource);

				DatabaseMetaData metadata = datasource.getConnection().getMetaData();
				ResultSet rs = metadata.getTables(null, null, "Mutes", null);
				Set<UUID> muteList = Sets.newHashSet();

				while (rs.next())
				{
					String uuid = rs.getString("uuid");
					muteList.add(UUID.fromString(uuid));
				}

				EssentialCmds.muteList = muteList;
				rs.close();
			}
			else
			{
				Connection c;
				Statement stmt;

				try
				{
					Class.forName("org.sqlite.JDBC");
				}
				catch (ClassNotFoundException exception)
				{
					System.out.println("[EssentialCmds]: You do not have ANY database software installed! Mutes will not work or be saved until this is fixed.");
				}

				c = DriverManager.getConnection("jdbc:sqlite:Mutes.db");
				c.setAutoCommit(false);
				stmt = c.createStatement();

				String sql = "CREATE TABLE IF NOT EXISTS MUTES " + "(UUID TEXT PRIMARY KEY     NOT NULL)";
				stmt.executeUpdate(sql);

				ResultSet rs = stmt.executeQuery("SELECT * FROM MUTES;");
				Set<UUID> muteList = Sets.newHashSet();

				while (rs.next())
				{
					String uuid = rs.getString("uuid");
					muteList.add(UUID.fromString(uuid));
				}

				EssentialCmds.muteList = muteList;

				rs.close();
				stmt.close();
				c.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void addRule(String rule)
	{
		CommentedConfigurationNode node = Configs.getConfig(rulesConfig).getNode("rules", "rules");
		String formattedItem = (rule + ",");

		if (configManager.getString(node).isPresent())
		{
			String items = node.getString();

			if (!items.contains(formattedItem))
				Configs.setValue(rulesConfig, node.getPath(), items + formattedItem);
			return;
		}

		Configs.setValue(rulesConfig, node.getPath(), formattedItem);
	}

	public static void removeRule(int ruleIndex)
	{
		CommentedConfigurationNode node = Configs.getConfig(rulesConfig).getNode("rules", "rules");
		String ruleToRemove = getRules().get(ruleIndex);

		if (configManager.getString(node).isPresent() && ruleToRemove != null)
		{
			String items = node.getString();
			Configs.setValue(rulesConfig, node.getPath(), items.replace(ruleToRemove + ",", ""));
		}
	}

	public static boolean removeJail(int number)
	{
		if (Configs.getConfig(jailsConfig).getNode(new Object[] { "jails", String.valueOf(number), "X" }).getValue() != null)
		{
			Configs.removeChild(jailsConfig, new Object[] { "jails" }, String.valueOf(number));
			return true;
		}

		return false;
	}

	public static void teleportPlayerToJail(Player player, int number)
	{
		UUID worldUuid = UUID.fromString(Configs.getConfig(jailsConfig).getNode("jails", String.valueOf(number), "world").getString());
		double x = Configs.getConfig(jailsConfig).getNode("jails", String.valueOf(number), "X").getDouble();
		double y = Configs.getConfig(jailsConfig).getNode("jails", String.valueOf(number), "Y").getDouble();
		double z = Configs.getConfig(jailsConfig).getNode("jails", String.valueOf(number), "Z").getDouble();
		World world = Sponge.getServer().getWorld(worldUuid).orElse(null);

		if (world != null)
		{
			Location<World> location = new Location<World>(world, x, y, z);

			if (player.getWorld().getUniqueId().equals(worldUuid))
			{
				player.setLocation(location);
			}
			else
			{
				player.transferToWorld(world.getUniqueId(), location.getPosition());
			}
		}
	}

	public static int getNumberOfJails()
	{
		return Configs.getConfig(jailsConfig).getNode("jails").getChildrenMap().values().size();
	}

	public static void addJail(Location<World> location)
	{
		int number = Utils.getNumberOfJails() + 1;
		Configs.getConfig(jailsConfig).getNode("jails", String.valueOf(number), "world").setValue(location.getExtent().getUniqueId().toString());
		Configs.getConfig(jailsConfig).getNode("jails", String.valueOf(number), "X").setValue(location.getX());
		Configs.getConfig(jailsConfig).getNode("jails", String.valueOf(number), "Y").setValue(location.getY());
		Configs.getConfig(jailsConfig).getNode("jails", String.valueOf(number), "Z").setValue(location.getZ());
		Configs.saveConfig(jailsConfig);
	}

	public static void setHome(UUID userName, Location<World> playerLocation, String worldName, String homeName)
	{
		String playerName = userName.toString();

		Configs.getConfig(homesConfig).getNode("home", "users", playerName, homeName, "world").setValue(worldName);
		Configs.getConfig(homesConfig).getNode("home", "users", playerName, homeName, "X").setValue(playerLocation.getX());
		Configs.getConfig(homesConfig).getNode("home", "users", playerName, homeName, "Y").setValue(playerLocation.getY());
		Configs.getConfig(homesConfig).getNode("home", "users", playerName, homeName, "Z").setValue(playerLocation.getZ());

		CommentedConfigurationNode node = Configs.getConfig(homesConfig).getNode("home", "users", playerName, "homes");
		String formattedItem = (homeName + ",");

		if (configManager.getString(node).isPresent())
		{
			String items = node.getString();
			if (!items.contains(formattedItem))
				Configs.setValue(homesConfig, node.getPath(), items + formattedItem);
			return;
		}

		Configs.setValue(homesConfig, node.getPath(), formattedItem);
	}

	public static void addBlacklistItem(String itemId)
	{
		CommentedConfigurationNode node = Configs.getConfig(mainConfig).getNode("essentialcmds", "items", "blacklist");
		String formattedItem = (itemId + ",");

		if (configManager.getString(node).isPresent())
		{
			String items = node.getString();
			Configs.setValue(mainConfig, node.getPath(), items + formattedItem);
			return;
		}

		Configs.setValue(mainConfig, node.getPath(), formattedItem);
	}

	public static void removeBlacklistItem(String itemId)
	{
		ConfigurationNode blacklistNode = Configs.getConfig(mainConfig).getNode("essentialcmds", "items", "blacklist");

		String blacklist = blacklistNode.getString();
		String newValue = blacklist.replace(itemId + ",", "");

		Configs.setValue(mainConfig, blacklistNode.getPath(), newValue);
	}

	public static ArrayList<Mail> getMail()
	{
		String json;

		try
		{
			json = readFile("Mail.json", StandardCharsets.UTF_8);
		}
		catch (Exception e)
		{
			return new ArrayList<>();
		}

		if (json != null && json.length() > 0)
		{
			return new ArrayList<>(Arrays.asList(gson.fromJson(json, Mail[].class)));
		}
		else
		{
			return new ArrayList<>();
		}
	}

	static String readFile(String path, Charset encoding) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void addMail(String senderName, String recipientName, String message)
	{
		if (Utils.getMail() != null)
		{
			ArrayList<Mail> currentMail = Utils.getMail();

			currentMail.add(new Mail(recipientName, senderName, message));
			String json;

			try
			{
				json = gson.toJson(currentMail);

				// Assume default encoding.
				FileWriter fileWriter = new FileWriter("Mail.json");

				// Always wrap FileWriter in BufferedWriter.
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

				bufferedWriter.write(json);
				bufferedWriter.flush();

				// Always close files.
				bufferedWriter.close();
			}
			catch (Exception ex)
			{
				System.out.println("Could not save JSON file!");
			}
		}
	}

	public static void removeMail(Mail mail)
	{
		if (Utils.getMail() != null)
		{
			ArrayList<Mail> currentMail = Utils.getMail();

			Mail mailToRemove = null;

			for (Mail m : currentMail)
			{
				if (m.getRecipientName().equals(mail.getRecipientName()) && m.getSenderName().equals(mail.getSenderName()) && m.getMessage().equals(mail.getMessage()))
				{
					mailToRemove = m;
					break;
				}
			}

			if (mailToRemove != null)
			{
				currentMail.remove(mailToRemove);
			}

			String json;

			try
			{
				json = gson.toJson(currentMail);

				// Assume default encoding.
				FileWriter fileWriter = new FileWriter("Mail.json");

				// Always wrap FileWriter in BufferedWriter.
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

				bufferedWriter.write(json);
				bufferedWriter.flush();

				// Always close files.
				bufferedWriter.close();
			}
			catch (Exception ex)
			{
				System.out.println("Could not save JSON file!");
			}
		}
	}

	public static void setWarp(Location<World> playerLocation, String warpName)
	{
		Configs.getConfig(warpsConfig).getNode("warps", warpName, "world").setValue(playerLocation.getExtent().getUniqueId().toString());
		Configs.getConfig(warpsConfig).getNode("warps", warpName, "X").setValue(playerLocation.getBlockX());
		Configs.getConfig(warpsConfig).getNode("warps", warpName, "Y").setValue(playerLocation.getBlockY());
		Configs.getConfig(warpsConfig).getNode("warps", warpName, "Z").setValue(playerLocation.getBlockZ());
		Configs.saveConfig(warpsConfig);

		CommentedConfigurationNode node = Configs.getConfig(warpsConfig).getNode("warps", "warps");
		String format = warpName + ",";

		if (configManager.getString(node).isPresent())
		{
			String items = node.getString();
			if (!items.contains(format))
				Configs.setValue(warpsConfig, node.getPath(), items + format);
		}
		else
		{
			Configs.setValue(warpsConfig, node.getPath(), format);
		}
	}

	public static String getJoinMsg()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode((Object[]) ("message.join").split("\\."));
		if (valueNode.getValue() != null)
		{
			return valueNode.getString();
		}
		else
		{
			Utils.setJoinMsg("&4Welcome");
			return "&4Welcome";
		}
	}

	public static String getFirstChatCharReplacement()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode((Object[]) ("chat.firstcharacter").split("\\."));
		if (valueNode.getValue() != null)
		{
			return valueNode.getString();
		}
		else
		{
			Utils.setFirstChatCharReplacement("<");
			return "<";
		}
	}

	public static String getLastChatCharReplacement()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode((Object[]) ("chat.lastcharacter").split("\\."));
		if (valueNode.getValue() != null)
		{
			return valueNode.getString();
		}
		else
		{
			Utils.setLastChatCharReplacement(">");
			return ">";
		}
	}

	public static double getAFK()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode((Object[]) ("afk.timer").split("\\."));
		if (valueNode.getDouble() != 0)
		{
			return valueNode.getDouble();
		}
		else
		{
			Utils.setAFK(30000);
			ConfigurationNode valNode = Configs.getConfig(mainConfig).getNode((Object[]) ("afk.timer").split("\\."));
			return valNode.getDouble();
		}
	}

	public static boolean getAFKKick()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode((Object[]) ("afk.kick.use").split("\\."));

		if (valueNode.getValue() != null)
		{
			return valueNode.getBoolean();
		}
		else
		{
			Utils.setAFKKick(false);
			return false;
		}
	}

	public static double getAFKKickTimer()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode((Object[]) ("afk.kick.timer").split("\\."));

		if (valueNode.getValue() != null)
		{
			return valueNode.getDouble();
		}
		else
		{
			Utils.setAFKKickTimer(30000);
			ConfigurationNode valNode = Configs.getConfig(mainConfig).getNode((Object[]) ("afk.timer").split("\\."));
			return valNode.getDouble();
		}
	}

	public static void setSpawn(Location<World> playerLocation, String worldName)
	{
		Configs.getConfig(spawnConfig).getNode("spawn", "X").setValue(playerLocation.getX());
		Configs.getConfig(spawnConfig).getNode("spawn", "Y").setValue(playerLocation.getY());
		Configs.getConfig(spawnConfig).getNode("spawn", "Z").setValue(playerLocation.getZ());
		Configs.getConfig(spawnConfig).getNode("spawn", "world").setValue(worldName);

		Configs.saveConfig(spawnConfig);
	}

	public static void setJoinMsg(String msg)
	{
		Configs.getConfig(mainConfig).getNode("message", "join").setValue(msg);
		Configs.saveConfig(mainConfig);
	}

	public static void setFirstChatCharReplacement(String replacement)
	{
		Configs.getConfig(mainConfig).getNode("chat", "firstcharacter").setValue(replacement);
		Configs.saveConfig(mainConfig);
	}

	public static void setLastChatCharReplacement(String replacement)
	{
		Configs.getConfig(mainConfig).getNode("chat", "lastcharacter").setValue(replacement);
		Configs.saveConfig(mainConfig);
	}

	public static void setAFK(double length)
	{
		Configs.getConfig(mainConfig).getNode("afk", "timer").setValue(length);
		Configs.saveConfig(mainConfig);
	}

	public static void setAFKKick(boolean val)
	{
		Configs.getConfig(mainConfig).getNode("afk", "kick", "use").setValue(val);
		Configs.saveConfig(mainConfig);
	}

	public static void setAFKKickTimer(double length)
	{
		Configs.getConfig(mainConfig).getNode("afk", "kick", "timer").setValue(length);
		Configs.saveConfig(mainConfig);
	}

	public static void setLastTeleportOrDeathLocation(UUID userName, Location<World> playerLocation)
	{
		String playerName = userName.toString();

		Configs.getConfig(playerDataConfig).getNode("back", "users", playerName, "lastDeath", "X").setValue(playerLocation.getX());
		Configs.getConfig(playerDataConfig).getNode("back", "users", playerName, "lastDeath", "Y").setValue(playerLocation.getY());
		Configs.getConfig(playerDataConfig).getNode("back", "users", playerName, "lastDeath", "Z").setValue(playerLocation.getZ());
		Configs.getConfig(playerDataConfig).getNode("back", "users", playerName, "lastDeath", "worldUUID").setValue(playerLocation.getExtent().getUniqueId().toString());

		Configs.saveConfig(playerDataConfig);
	}

	public static Location<World> getLastTeleportOrDeathLocation(Player player)
	{
		try
		{
			String playerName = player.getUniqueId().toString();
			ConfigurationNode xNode = Configs.getConfig(playerDataConfig).getNode((Object[]) ("back.users." + playerName + "." + "lastDeath.X").split("\\."));
			double x = xNode.getDouble();
			ConfigurationNode yNode = Configs.getConfig(playerDataConfig).getNode((Object[]) ("back.users." + playerName + "." + "lastDeath.Y").split("\\."));
			double y = yNode.getDouble();
			ConfigurationNode zNode = Configs.getConfig(playerDataConfig).getNode((Object[]) ("back.users." + playerName + "." + "lastDeath.Z").split("\\."));
			double z = zNode.getDouble();
			ConfigurationNode worldNode = Configs.getConfig(playerDataConfig).getNode((Object[]) ("back.users." + playerName + "." + "lastDeath.worldUUID").split("\\."));
			UUID worldUUID = UUID.fromString(worldNode.getString());
			Optional<World> world = game.getServer().getWorld(worldUUID);

			if (world.isPresent())
				return new Location<>(world.get(), x, y, z);
			else
				return null;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static ArrayList<String> getRules()
	{
		ConfigurationNode valueNode = Configs.getConfig(rulesConfig).getNode((Object[]) ("rules.rules").split("\\."));

		if (valueNode.getValue() == null)
			return Lists.newArrayList();

		String list = valueNode.getString();
		ArrayList<String> rulesList = Lists.newArrayList();
		boolean finished = false;
		int endIndex = list.indexOf(",");

		if (endIndex != -1)
		{
			String substring = list.substring(0, endIndex);
			rulesList.add(substring);

			while (!finished)
			{
				int startIndex = endIndex;
				endIndex = list.indexOf(",", startIndex + 1);

				if (endIndex != -1)
				{
					String substrings = list.substring(startIndex + 1, endIndex);
					rulesList.add(substrings);
				}
				else
				{
					finished = true;
				}
			}
		}

		return rulesList;
	}

	public static List<String> getBlacklistItems()
	{
		ConfigurationNode valueNode = Configs.getConfig(mainConfig).getNode("essentialcmds", "items", "blacklist");

		if (valueNode.getValue() == null || valueNode.getString().length() == 0)
		{
			return Lists.newArrayList();
		}

		String list = valueNode.getString();

		List<String> itemList = Lists.newArrayList();
		boolean finished = false;
		int endIndex = list.indexOf(",");

		if (endIndex != -1)
		{
			String substring = list.substring(0, endIndex);
			itemList.add(substring);

			while (!finished)
			{
				int startIndex = endIndex;
				endIndex = list.indexOf(",", startIndex + 1);

				if (endIndex != -1)
				{
					String substrings = list.substring(startIndex + 1, endIndex);
					itemList.add(substrings);
				}
				else
				{
					finished = true;
				}
			}
		}

		return itemList;
	}

	public static ArrayList<String> getHomes(UUID userName)
	{
		String playerName = userName.toString();
		ConfigurationNode valueNode = Configs.getConfig(homesConfig).getNode("home", "users", playerName, "homes");

		if (valueNode.getValue() == null)
		{
			return Lists.newArrayList();
		}

		String list = valueNode.getString();

		ArrayList<String> homeList = new ArrayList<>();
		boolean finished = false;
		int endIndex = list.indexOf(",");

		if (endIndex != -1)
		{
			String substring = list.substring(0, endIndex);
			homeList.add(substring);

			while (!finished)
			{
				int startIndex = endIndex;
				endIndex = list.indexOf(",", startIndex + 1);

				if (endIndex != -1)
				{
					String substrings = list.substring(startIndex + 1, endIndex);
					homeList.add(substrings);
				}
				else
				{
					finished = true;
				}
			}
		}

		return homeList;
	}

	public static ArrayList<String> getWarps()
	{
		ConfigurationNode valueNode = Configs.getConfig(warpsConfig).getNode("warps", "warps");

		if (valueNode.getValue() == null)
		{
			return Lists.newArrayList();
		}

		String list = valueNode.getString();

		ArrayList<String> warpList = new ArrayList<>();
		boolean finished = false;
		int endIndex = list.indexOf(",");

		if (endIndex != -1)
		{
			String substring = list.substring(0, endIndex);
			warpList.add(substring);

			while (!finished)
			{
				int startIndex = endIndex;
				endIndex = list.indexOf(",", startIndex + 1);
				if (endIndex != -1)
				{
					String substrings = list.substring(startIndex + 1, endIndex);
					warpList.add(substrings);
				}
				else
				{
					finished = true;
				}
			}
		}
		else
		{
			warpList.add(list);
		}

		return warpList;
	}

	public static Location<World> getHome(UUID uuid, String homeName)
	{
		String worldName = Configs.getConfig(homesConfig).getNode("home", "users", uuid.toString(), homeName, "world").getString();
		World world = Sponge.getServer().getWorld(worldName).orElse(null);
		double x = Configs.getConfig(homesConfig).getNode("home", "users", uuid.toString(), homeName, "X").getDouble();
		double y = Configs.getConfig(homesConfig).getNode("home", "users", uuid.toString(), homeName, "Y").getDouble();
		double z = Configs.getConfig(homesConfig).getNode("home", "users", uuid.toString(), homeName, "Z").getDouble();

		if (world != null)
			return new Location<World>(world, x, y, z);
		else
			return null;
	}

	public static Location<World> getWarp(String warpName)
	{
		UUID worldUuid = UUID.fromString(Configs.getConfig(warpsConfig).getNode("warps", warpName, "world").getString());
		World world = Sponge.getServer().getWorld(worldUuid).orElse(null);
		double x = Configs.getConfig(warpsConfig).getNode("warps", warpName, "X").getDouble();
		double y = Configs.getConfig(warpsConfig).getNode("warps", warpName, "Y").getDouble();
		double z = Configs.getConfig(warpsConfig).getNode("warps", warpName, "Z").getDouble();

		if (world != null)
			return new Location<World>(world, x, y, z);
		else
			return null;
	}

	public static boolean isHomeInConfig(UUID playerUuid, String homeName)
	{
		ConfigurationNode valueNode = Configs.getConfig(homesConfig).getNode("home", "users", playerUuid.toString(), homeName, "X");
		Object inConfig = valueNode.getValue();
		return inConfig != null;
	}

	public static boolean isWarpInConfig(String warpName)
	{
		ConfigurationNode valueNode = Configs.getConfig(warpsConfig).getNode("warps", warpName, "X");
		Object inConfig = valueNode.getValue();
		return inConfig != null;
	}

	public static boolean isSpawnInConfig()
	{
		ConfigurationNode valueNode = Configs.getConfig(spawnConfig).getNode("spawn", "X");
		Object inConfig = valueNode.getValue();
		return inConfig != null;
	}

	public static Location<World> getSpawn()
	{
		World world = null;
		CommentedConfigurationNode worldNode = Configs.getConfig(spawnConfig).getNode("spawn", "world");
		if (configManager.getString(worldNode).isPresent())
			world = Sponge.getServer().getWorld(configManager.getString(worldNode).get()).orElse(null);

		double x = 0, y = 0, z = 0;

		CommentedConfigurationNode xNode = Configs.getConfig(spawnConfig).getNode("spawn", "X");
		if (configManager.getDouble(xNode).isPresent())
			x = xNode.getDouble();

		CommentedConfigurationNode yNode = Configs.getConfig(spawnConfig).getNode("spawn", "Y");
		if (configManager.getDouble(yNode).isPresent())
			y = yNode.getDouble();

		CommentedConfigurationNode zNode = Configs.getConfig(spawnConfig).getNode("spawn", "Z");
		if (configManager.getDouble(zNode).isPresent())
			z = zNode.getDouble();

		if (world != null)
			return new Location<>(world, x, y, z);
		else
			return null;
	}

	public static boolean isLastDeathInConfig(Player player)
	{
		return Utils.getLastTeleportOrDeathLocation(player) != null;
	}

	public static void deleteHome(Player player, String homeName)
	{
		ConfigurationNode homeNode = Configs.getConfig(homesConfig).getNode("home", "users", player.getUniqueId().toString(), "homes");
		String homes = homeNode.getString();
		homes = homes.replace(homeName + ",", "");
		Configs.setValue(homesConfig, homeNode.getPath(), homes);
		Configs.removeChild(homesConfig, new Object[] { "home", "users", player.getUniqueId().toString() }, homeName);
	}

	public static void deleteWarp(String warpName)
	{
		ConfigurationNode warpNode = Configs.getConfig(warpsConfig).getNode("warps", "warps");
		String warps = warpNode.getString();
		warps = warps.replace(warpName + ",", "");
		Configs.setValue(warpsConfig, warpNode.getPath(), warps);
		Configs.removeChild(warpsConfig, new Object[] { "warps" }, warpName);
	}
}
