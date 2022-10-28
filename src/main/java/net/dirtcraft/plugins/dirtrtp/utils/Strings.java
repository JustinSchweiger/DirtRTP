package net.dirtcraft.plugins.dirtrtp.utils;

import org.bukkit.ChatColor;

public class Strings {
	// ---------------------------------------------------------- GENERAL ----------------------------------------------------------
	public static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.RED + "Dirt" + ChatColor.BLUE + "RTP" + ChatColor.GRAY + "] ";
	public static final String INTERNAL_PREFIX = ChatColor.GRAY + "[" + ChatColor.RED + "Dirt" + ChatColor.BLUE + "RTP" + ChatColor.GRAY + "] ";
	public static final String NO_CONSOLE = PREFIX + ChatColor.RED + "You must be a player to use this command.";
	public static final String NO_PERMISSION = PREFIX + ChatColor.RED + "You do not have permission to use this command.\n";
	public static final String RELOAD = PREFIX + ChatColor.GREEN + "Config reloaded successfully.";
	public static final String COOLDOWN = PREFIX + ChatColor.GRAY + "You must wait " + ChatColor.AQUA + "{COOLDOWN}" + ChatColor.GRAY + " more seconds!";
	public static final String CHAOS_ISLANDS_DISABLED = PREFIX + ChatColor.RED + "Chaos Islands RTP is disabled on this server.";
}
