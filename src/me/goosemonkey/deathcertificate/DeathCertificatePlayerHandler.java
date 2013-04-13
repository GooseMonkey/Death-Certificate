package me.goosemonkey.deathcertificate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.BookMeta;

public class DeathCertificatePlayerHandler {
	private static HashMap<Player, DeathCertificatePlayerHandler> instances = new HashMap<Player, DeathCertificatePlayerHandler>();
	static DeathCertificate plugin;
	public static DeathCertificatePlayerHandler getHandler(Player p) {
		if(plugin.getConfig().getBoolean("Details.Enabled", false)) return null;
		if(p == null) return null;
		if(instances.containsKey(p)) return instances.get(p);
		return new DeathCertificatePlayerHandler(p);
	}
	
	private Player player;
	private YamlConfiguration file;
	private Map<String, Object> damage;
	
	private DeathCertificatePlayerHandler(Player p) {
		player = p;
		file = YamlConfiguration.loadConfiguration(plugin.getFile(player));
		ConfigurationSection cs = file.getConfigurationSection("Damagers");
		if(cs != null) damage = cs.getValues(false);
		else damage = new HashMap<String, Object>();
		instances.put(p, this);
	}
	
	public void addDamage(EntityDamageEvent e) {
		if(!e.getEntity().equals(player)) throw new IllegalArgumentException();
		String damageName = "";
		if (e instanceof EntityDamageByEntityEvent) {
			damageName = plugin.getFriendlyMobName((EntityDamageByEntityEvent) e);
		} else switch (e.getCause()) {
			case BLOCK_EXPLOSION: damageName = "an Explosion";
			case CONTACT: damageName = "a Cactus";
			case ENTITY_EXPLOSION: damageName = "a Creeper";
			case CUSTOM: damageName = "Herobrine";
			case FALL: damageName = "Falling";
			case FIRE_TICK: damageName = "Fire";
			case VOID: damageName = "the Void"; //Really shouldn't be readable when your stuff falls into the void, no?
			default: 
				String[] name =  e.getEntity().getLastDamageCause().getCause().name().toLowerCase().split("_");
				String ret = "";
				for(String str: name)
					ret += " " + str.substring(0,1).toUpperCase() + str.substring(1);
				damageName = ret.trim();
		}
		damage.put(""+damage.size(),damageName + ": "+ (((double)e.getDamage())/2));
	}
	
	public BookMeta handleDeath(PlayerDeathEvent e, BookMeta meta) {
		if(!e.getEntity().equals(player)) throw new IllegalArgumentException();
		String page = "";
		int lines = 0;
		if(damage.size() > 0) {
			page = "Injuries Sustained: \nHearts - Cause\n";
			lines = 2;
			for(String s: damage.keySet()) {
				if(!(damage.get(s) instanceof String)) continue;
				String[] dmg = ((String)damage.get(s)).split(": ");
				String message = dmg[1] + " - " + dmg[0] + "\n";
				if(page.length() + message.length() > 256 || lines > 12) {
					meta.addPage(page);
					page = "Injuries Sustained (cont.): \n";
					lines = 1;
				}
				page += message;
				lines ++;
			}
			if(page.matches(".*\n.*\n,*")) meta.addPage(page);
			damage.clear();
		}
		//TODO: Add more things to the meta
		return meta;
	}
	
	public void save() throws IOException {
		file.set("Damagers", null);
		ConfigurationSection cs = file.createSection("Damagers");
		for(String s : damage.keySet()) 
			cs.set(s, damage.get(s));
		file.save(plugin.getFile(player));
	}
	

}
