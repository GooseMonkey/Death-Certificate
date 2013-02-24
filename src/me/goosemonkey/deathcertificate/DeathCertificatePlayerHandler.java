package me.goosemonkey.deathcertificate;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.BookMeta;

public class DeathCertificatePlayerHandler {
	private static HashMap<Player, DeathCertificatePlayerHandler> instances = new HashMap<Player, DeathCertificatePlayerHandler>();
	static DeathCertificate plugin;
	public static DeathCertificatePlayerHandler getHandler(Player p) {
		if(plugin.getConfig().getBoolean("details.disabled", false)) return null;
		if(instances.containsKey(p)) return instances.get(p);
		return new DeathCertificatePlayerHandler(p);
	}
	
	private Player player;
	private HashMap<DamageCause, Integer> damage = new HashMap<DamageCause, Integer>();
	private DeathCertificatePlayerHandler(Player p) {
		player = p;
	}
	
	public void addDamage(EntityDamageEvent e) {
		if(!e.getEntity().equals(player)) throw new IllegalArgumentException();
		damage.put(e.getCause(), e.getDamage());
	}
	
	public BookMeta handleDeath(PlayerDeathEvent e, BookMeta meta) {
		if(!e.getEntity().equals(player)) throw new IllegalArgumentException();
		//meta.
		//TODO: Add things to the meta
		return meta;
	}
	
	

}
