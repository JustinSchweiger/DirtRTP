package net.dirtcraft.plugins.dirtrtp.commands;

import net.dirtcraft.plugins.dirtrtp.data.CooldownManager;
import net.dirtcraft.plugins.dirtrtp.utils.Permissions;
import net.dirtcraft.plugins.dirtrtp.utils.Strings;
import net.dirtcraft.plugins.dirtrtp.utils.Utilities;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RtpCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission(Permissions.RELOAD)) {
				sender.sendMessage(Strings.NO_PERMISSION);
				return true;
			}

			Utilities.loadConfig();
			Utilities.registerCommands();
			sender.sendMessage(Strings.RELOAD);
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("chaos-island")) {
			if (!Utilities.config.chaosIslands.enabled) {
				sender.sendMessage(Strings.CHAOS_ISLANDS_DISABLED);
				return true;
			}

			return ChaosIslandCommand.run(sender);
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return false;
		}

		if (!sender.hasPermission(Permissions.RTP)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		List<String> whitelistedWorlds = Utilities.config.whitelistedWorlds.worlds;

		if (whitelistedWorlds.size() == 0) {
			sender.sendMessage(Strings.PREFIX + ChatColor.GRAY + "No worlds are whitelisted for RTP.");
			return true;
		}

		Player player = (Player) sender;
		World world = player.getWorld();

		if (!whitelistedWorlds.contains(world.getName())) {
			sender.sendMessage(Strings.PREFIX + ChatColor.GRAY + "RTP is " + ChatColor.RED + "disabled" + ChatColor.GRAY + " in this world!");
			return true;
		}

		long timeLeft = System.currentTimeMillis() - CooldownManager.getRtpCooldown(player.getUniqueId());

		if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= Utilities.config.general.cooldown || player.hasPermission(Permissions.BYPASS)) {
			if (player.hasPermission(Permissions.BYPASS)) {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BLUE + "You are currently " + ChatColor.RED + "bypassing" + ChatColor.BLUE + " the rtp cooldown!"));
			}

			WorldBorder border = player.getWorld().getWorldBorder();

			Random random = new Random();
			int centerX = (int) border.getCenter().getX();
			int centerZ = (int) border.getCenter().getZ();
			int radius = (int) border.getSize() / 2;

			int minusX = centerX - radius;
			int minusZ = centerZ - radius;
			int plusX = centerX + radius;
			int plusZ = centerZ + radius;

			int x = random.nextInt(plusX - minusX) + minusX;
			int z = random.nextInt(plusZ - minusZ) + minusZ;
			int y = world.getHighestBlockYAt(x, z) + 2;

			player.teleport(new Location(world, x, y, z));
			Utilities.playSuccessSound(player);
			String coords = ChatColor.AQUA + String.valueOf(x) + ChatColor.GRAY + ", " + ChatColor.AQUA + y + ChatColor.GRAY + ", " + ChatColor.AQUA + z;
			player.sendTitle(ChatColor.RED + "DirtCraft " + ChatColor.BLUE + "RTP", coords);
			CooldownManager.setRtpCooldown(player.getUniqueId(), System.currentTimeMillis());
		} else {
			sender.sendMessage(Strings.COOLDOWN.replace("{COOLDOWN}", String.valueOf(Utilities.config.general.cooldown - TimeUnit.MILLISECONDS.toSeconds(timeLeft))));
			Utilities.playErrorSound(player);
		}

		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		List<String> arguments = new ArrayList<>();

		if (args.length > 0) {
			if (sender.hasPermission(Permissions.RELOAD)) {
				arguments.add("reload");
			}

			if (sender.hasPermission(Permissions.CHAOSISLAND) && Utilities.config.chaosIslands.enabled) {
				arguments.add("chaos-island");
			}
		}

		List<String> tabResults = new ArrayList<>();
		for (String argument : arguments) {
			if (argument.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
				tabResults.add(argument);
			}
		}

		return tabResults;
	}
}
