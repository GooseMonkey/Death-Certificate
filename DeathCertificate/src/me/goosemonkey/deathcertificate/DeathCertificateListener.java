package me.goosemonkey.deathcertificate;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
		
		if (this.plugin.getConfig().getBoolean("Options.PvPKillsOnly") && !this.isPvpKill(event.getEntity().getLastDamageCause()))
			return;
		
		event.getDrops().add(writer.getWrittenCertificate(event));
	}
	
	private boolean isPvpKill(EntityDamageEvent event)
	{
		if (event instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent event2 = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
			
			if (event2.getDamager() instanceof Player)
				return true;
			
			if (event2.getDamager() instanceof Arrow)
			{
				Arrow arrow = (Arrow) event2.getDamager();
				
				return (arrow.getShooter() instanceof Player);
			}
			
			if (event2.getDamager() instanceof ThrownPotion)
			{
				ThrownPotion pot = (ThrownPotion) event2.getDamager();
				
				return (pot.getShooter() instanceof Player);
			}			
		}
		
		return false;
	}
}
