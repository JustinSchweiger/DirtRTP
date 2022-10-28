package net.dirtcraft.plugins.dirtrtp.utils;

import com.moandjiezana.toml.Toml;
import net.dirtcraft.plugins.dirtrtp.DirtRtp;
import net.dirtcraft.plugins.dirtrtp.commands.ChaosIslandCommand;
import net.dirtcraft.plugins.dirtrtp.commands.RtpCommand;
import net.dirtcraft.plugins.dirtrtp.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

public class Utilities {
	public static Config config;

	public static String format(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void loadConfig() {
		if (!DirtRtp.getPlugin().getDataFolder().exists()) {
			DirtRtp.getPlugin().getDataFolder().mkdirs();
		}
		File file = new File(DirtRtp.getPlugin().getDataFolder(), "config.toml");
		if (!file.exists()) {
			try {
				Files.copy(DirtRtp.getPlugin().getResource("config.toml"), file.toPath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		config = new Toml(new Toml().read(DirtRtp.getPlugin().getResource("config.toml"))).read(file).to(Config.class);
	}

	public static void registerCommands() {
		DirtRtp.getPlugin().getCommand("rtp").setExecutor(new RtpCommand());
		DirtRtp.getPlugin().getCommand("rtp").setTabCompleter(new RtpCommand());
	}

	public static void log(Level level, String msg) {
		String consoleMessage;
		if (Level.INFO.equals(level)) {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.WHITE + msg;
		} else if (Level.WARNING.equals(level)) {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.YELLOW + msg;
		} else if (Level.SEVERE.equals(level)) {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.RED + msg;
		} else {
			consoleMessage = Strings.INTERNAL_PREFIX + ChatColor.GRAY + msg;
		}

		if (!config.general.coloredDebug) {
			consoleMessage = ChatColor.stripColor(msg);
		}

		DirtRtp.getPlugin().getServer().getConsoleSender().sendMessage(consoleMessage);
	}

	public static void disablePlugin() {
		DirtRtp.getPlugin().getServer().getPluginManager().disablePlugin(DirtRtp.getPlugin());
	}

	public static void playSuccessSound(CommandSender sender) {
		if (!(sender instanceof Player)) {
			return;
		}

		Player player = (Player) sender;
		if (Utilities.config.sound.playSuccessSound) {
			String sound = Utilities.config.sound.successSound;
			if (sound == null) {
				sound = "minecraft:entity.enderman.teleport";
			}
			player.playSound(player.getLocation(), sound, 1, 1);
		}
	}

	public static void playErrorSound(CommandSender sender) {
		if (!(sender instanceof Player)) {
			return;
		}

		Player player = (Player) sender;
		if (Utilities.config.sound.playErrorSound) {
			String sound = Utilities.config.sound.errorSound;
			if (sound == null) {
				sound = "minecraft:block.anvil.place";
			}
			player.playSound(player.getLocation(), sound, 1, 1);
		}
	}
}
