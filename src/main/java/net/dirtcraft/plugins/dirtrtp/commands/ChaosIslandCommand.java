package net.dirtcraft.plugins.dirtrtp.commands;

import net.dirtcraft.plugins.dirtrtp.data.CooldownManager;
import net.dirtcraft.plugins.dirtrtp.utils.Permissions;
import net.dirtcraft.plugins.dirtrtp.utils.Strings;
import net.dirtcraft.plugins.dirtrtp.utils.Utilities;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ChaosIslandCommand implements CommandExecutor {
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return run(sender);
	}

	public static boolean run(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Strings.NO_CONSOLE);
			return false;
		}

		if (!sender.hasPermission(Permissions.CHAOSISLAND)) {
			sender.sendMessage(Strings.NO_PERMISSION);
			return true;
		}

		List<String> whitelistedWorlds = Utilities.config.whitelistedWorlds.chaosIslandWorlds;

		if (whitelistedWorlds.size() == 0) {
			sender.sendMessage(Strings.PREFIX + ChatColor.GRAY + "No worlds are whitelisted for RTP.");
			return true;
		}

		Player player = (Player) sender;
		World world = player.getWorld();

		if (!whitelistedWorlds.contains(world.getName())) {
			sender.sendMessage(Strings.PREFIX + ChatColor.GRAY + "Chaos Island RTP is " + ChatColor.RED + "disabled" + ChatColor.GRAY + " in this world!");
			return true;
		}

		long timeLeft = System.currentTimeMillis() - CooldownManager.getChaosCooldown(player.getUniqueId());

		if (TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= Utilities.config.chaosIslands.cooldown || player.hasPermission(Permissions.BYPASS)) {
			if (player.hasPermission(Permissions.BYPASS)) {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BLUE + "You are currently " + ChatColor.RED + "bypassing" + ChatColor.BLUE + " the rtp cooldown!"));
			}

			Random random = new Random();
			int randomX = random.nextInt(101) - random.nextInt(101);
			int randomZ = random.nextInt(101) - random.nextInt(101);

			int islandX = Utilities.config.chaosIslands.distance * randomX;
			int islandZ = Utilities.config.chaosIslands.distance * randomZ;
			int y = world.getHighestBlockYAt(islandX, islandZ) + 3;

			player.teleport(new Location(world, islandX, y, islandZ));
			Utilities.playSuccessSound(player);
			String coords = ChatColor.AQUA + String.valueOf(islandX) + ChatColor.GRAY + ", " + ChatColor.AQUA + y + ChatColor.GRAY + ", " + ChatColor.AQUA + islandZ;
			player.sendTitle(ChatColor.RED + "DirtCraft " + ChatColor.BLUE + "Chaos Island RTP", coords);
			CooldownManager.setChaosCooldown(player.getUniqueId(), System.currentTimeMillis());
		} else {
			sender.sendMessage(Strings.COOLDOWN.replace("{COOLDOWN}", String.valueOf(Utilities.config.general.cooldown - TimeUnit.MILLISECONDS.toSeconds(timeLeft))));
			Utilities.playErrorSound(player);
		}

		return true;
	}
}
