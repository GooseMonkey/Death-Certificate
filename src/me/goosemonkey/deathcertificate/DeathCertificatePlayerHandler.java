package me.goosemonkey.deathcertificate;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class DeathCertificatePlayerHandler {
	private static HashMap<Player, DeathCertificatePlayerHandler> instances = new HashMap<Player, DeathCertificatePlayerHandler>();
	static DeathCertificate plugin;
	private DeathCertificatePlayerHandler(Player p) {
		// TODO Auto-generated constructor stub
	}
	
	public void addDamage(EntityDamageEvent e) {
		
	}
	
	public static DeathCertificatePlayerHandler getHandler(Player p) {
		if(plugin.getConfig().getBoolean("details.disabled", false)) return null;
		if(instances.containsKey(p)) return instances.get(p);
		return new DeathCertificatePlayerHandler(p);
	}

}
