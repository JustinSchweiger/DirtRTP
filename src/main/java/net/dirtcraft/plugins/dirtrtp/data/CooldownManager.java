package net.dirtcraft.plugins.dirtrtp.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
	private static final Map<UUID, Long> rtpCooldowns = new HashMap<>();
	private static final Map<UUID, Long> chaosCooldowns = new HashMap<>();

	public static void setRtpCooldown(UUID uuid, long time) {
		if (time < 1) {
			rtpCooldowns.remove(uuid);
		} else {
			rtpCooldowns.put(uuid, time);
		}
	}

	public static long getRtpCooldown(UUID uuid) {
		return rtpCooldowns.getOrDefault(uuid, 0L);
	}

	public static void setChaosCooldown(UUID uuid, long time) {
		if (time < 1) {
			chaosCooldowns.remove(uuid);
		} else {
			chaosCooldowns.put(uuid, time);
		}
	}

	public static long getChaosCooldown(UUID uuid) {
		return chaosCooldowns.getOrDefault(uuid, 0L);
	}
}
