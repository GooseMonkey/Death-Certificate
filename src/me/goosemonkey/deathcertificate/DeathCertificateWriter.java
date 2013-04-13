package me.goosemonkey.deathcertificate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class DeathCertificateWriter
{
	DeathCertificate plugin;
	
	public DeathCertificateWriter(DeathCertificate inst)
	{
		plugin = inst;
	}
	
	ItemStack getWrittenCertificate(PlayerDeathEvent event) throws Exception
	{
		Player p = event.getEntity();
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta meta;
		ItemMeta im = book.getItemMeta();
		if(im instanceof BookMeta) {
			meta = (BookMeta) im;
		} else {
			throw new Exception("No way to make a BookMeta!?");
		}
		meta.setAuthor(plugin.getMortuary(p.getLocation()));
		meta.setTitle("Death Certificate");
		List<String> lore = new ArrayList<String>();
		lore.add("Identity: "+p.getName());
		String killer = getKiller(event);
		lore.add("Cause of Death"+ (killer == null? " could not be determined." : (": " + killer)));
		meta.setLore(lore);
		meta.setPages(getBookPages(event));
		
		DeathCertificatePlayerHandler dcph = DeathCertificatePlayerHandler.getHandler(p);
		if(dcph != null) {
			meta = dcph.handleDeath(event, meta);
		}
		
		
		book.setItemMeta(meta);
		return book;
	}
	
	private String getKiller(PlayerDeathEvent event)
	{
		if (event.getEntity().getKiller() != null)
			return ChatColor.BOLD + event.getEntity().getKiller().getName();
		
		if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent event2 = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
			
			return plugin.getFriendlyMobName(event2);
		}
		
		if (event.getEntity().getLastDamageCause() != null)
			switch (event.getEntity().getLastDamageCause().getCause()) {
				case BLOCK_EXPLOSION: return "an Explosion";
				case CONTACT: return "a Cactus";
				case ENTITY_EXPLOSION: return "a Creeper";
				case CUSTOM: return "Herobrine";
				case FALL: return "Falling";
				case FIRE_TICK: return "Fire";
				case VOID: return "the Void"; //Really shouldn't be readable when your stuff falls into the void, no?
				default: 
					String[] name =  event.getEntity().getLastDamageCause().getCause().name().toLowerCase().split("_");
					String ret = "";
					for(String str: name)
						ret += " " + str.substring(0,1).toUpperCase() + str.substring(1);
					return ret.trim();
			}
		
		return null;
	}
	
	
	private List<String> getBookPages(PlayerDeathEvent event)
	{
		List<String> i = new ArrayList<String>();
		
		i.add(ChatColor.ITALIC + "" + ChatColor.UNDERLINE + ChatColor.BLUE + "Death Certificate:" + "\n"
				+ ChatColor.DARK_BLUE + ChatColor.BOLD + event.getEntity().getName() + "\n" + ChatColor.RESET + "\n"
				+ ChatColor.BLACK + ChatColor.ITALIC + "Killed by " + ChatColor.RESET + ChatColor.RED + getKiller(event) + "\n" + ChatColor.RESET + "\n"
				+ ChatColor.BLACK + ChatColor.ITALIC + "On " + ChatColor.RESET + ChatColor.BLUE + this.getDate() + "\n"
				+ ChatColor.BLACK + ChatColor.ITALIC + "at " + ChatColor.RESET + ChatColor.DARK_BLUE + this.getTime());
		
		i.add("Level " + ChatColor.RESET + ChatColor.DARK_GREEN + event.getEntity().getLevel() + "\n"
				+ ChatColor.BLACK + "XP: " + ChatColor.DARK_GREEN + event.getEntity().getTotalExperience());
		return i;
	}
	
	private String getTime()
	{
		Calendar local = Calendar.getInstance();
		
		String timeZone = this.plugin.getConfig().getString("TimeAndDate.TimeZone");
		
		if (!timeZone.equalsIgnoreCase("Default"))
		{
			Calendar foreign = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
			
			foreign.setTimeInMillis(local.getTimeInMillis());
			return (foreign.get(Calendar.HOUR) == 0 ? "12" : foreign.get(Calendar.HOUR)) + ":" +
			(local.get(Calendar.MINUTE) < 10 ? "0" + foreign.get(Calendar.MINUTE) : foreign.get(Calendar.MINUTE)) + " " +
			(foreign.get(Calendar.AM_PM) == Calendar.PM ? "PM" : "AM") + " " +
			foreign.getTimeZone().getDisplayName(false, TimeZone.SHORT);
		}
		
		return (local.get(Calendar.HOUR) == 0 ? "12" : local.get(Calendar.HOUR)) + ":" +
		(local.get(Calendar.MINUTE) < 10 ? "0" + local.get(Calendar.MINUTE) : local.get(Calendar.MINUTE)) + " " +
		(local.get(Calendar.AM_PM) == Calendar.PM ? "PM" : "AM") + " " +
		local.getTimeZone().getDisplayName(false, TimeZone.SHORT);
	}
	
	private String getDate()
	{
		Calendar local = Calendar.getInstance();
		
		String timeZone = this.plugin.getConfig().getString("TimeAndDate.TimeZone");
		int[] dateOrder = plugin.getDateOrder();
				
		if (!timeZone.equalsIgnoreCase("Default"))
		{
			Calendar foreign = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
			
			foreign.setTimeInMillis(local.getTimeInMillis());
			
			return foreign.get(dateOrder[0]) + "/" + (foreign.get(dateOrder[1]) + 1) + "/" + foreign.get(Calendar.YEAR);
		}
		
		return local.get(dateOrder[0]) + "/" + (local.get(dateOrder[1]) + 1) + "/" + local.get(Calendar.YEAR);
	}
}
