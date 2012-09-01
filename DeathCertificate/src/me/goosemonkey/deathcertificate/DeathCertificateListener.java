package me.goosemonkey.deathcertificate;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathCertificateListener implements Listener
{
	private DeathCertificate plugin;
	DeathCertificateWriter writer;
	
	DeathCertificateListener(DeathCertificate inst)
	{
		this.plugin = inst;
		
		writer = new DeathCertificateWriter(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		if (this.plugin.getConfig().getBoolean("Options.IgnoreSuicide") && event.getEntity().getLastDamageCause().getCause() == DamageCause.SUICIDE)
			return;
		
		if (this.plugin.getConfig().getBoolean("Options.RequirePermission") && !event.getEntity().hasPermission("deathcertificate.drop"))
			return;
		
		event.getDrops().add(writer.getWrittenCertificate(event));
	}
}
