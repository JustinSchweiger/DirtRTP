package net.dirtcraft.plugins.dirtrtp;

import net.dirtcraft.plugins.dirtrtp.utils.Utilities;
import org.bukkit.plugin.java.JavaPlugin;

public final class DirtRtp extends JavaPlugin {

	private static DirtRtp plugin;

	public static DirtRtp getPlugin() {
		return plugin;
	}

	@Override
	public void onEnable() {
		plugin = this;
		Utilities.loadConfig();
		Utilities.registerCommands();
	}

	@Override
	public void onDisable() {

	}
}
