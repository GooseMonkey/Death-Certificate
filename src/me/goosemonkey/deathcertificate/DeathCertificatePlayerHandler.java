package me.goosemonkey.deathcertificate;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class DeathCertificatePlayerHandler {
	private static HashMap<Player, DeathCertificatePlayerHandler> instances = new HashMap<Player, DeathCertificatePlayerHandler>();

	private DeathCertificatePlayerHandler(Player p) {
		// TODO Auto-generated constructor stub
	}
	
	public static DeathCertificatePlayerHandler getHandler(Player p) {
		if(instances.containsKey(p)) return instances.get(p);
		return new DeathCertificatePlayerHandler(p);
	}

}
