package me.goosemonkey.deathcertificate;

import java.io.File;
import java.util.Calendar;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathCertificate extends JavaPlugin
{
	private String header = "\nPlease see http://dev.bukkit.org/server-mods/death-certificate/pages/configuration/\n" +
			"for configuration help! Some values may not do what you expect them to!";
	private int[] dateOrder;
	
	public void onEnable()
	{
		this.getConfig().options().copyDefaults(true);
		this.getConfig().options().copyHeader(true);
		this.getConfig().options().header(this.header);
		this.saveConfig();
		reload();
		DeathCertificatePlayerHandler.plugin = this;
		this.getServer().getPluginManager().registerEvents(new DeathCertificateListener(this), this);
	}
	
	public void reload() {
		this.reloadConfig();
		dateOrder = new int[2];
		if(getConfig().getBoolean("TimeAndDate.AmericanDateFormat"))
			dateOrder[0] = Calendar.MONTH;
		else dateOrder[0] = Calendar.DATE;
		dateOrder[1] = dateOrder[0] == Calendar.DATE ? Calendar.MONTH : Calendar.DATE;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (args.length == 0)
		{
			sender.sendMessage(ChatColor.YELLOW + "Death Certificate v" + this.getDescription().getVersion() + " by GooseMonkey97 & meiamsome help. \n" +
					"http://dev.bukkit.org/server-mods/death-certificate/");
			if(sender.hasPermission("deathcertificate.reload"))
				sender.sendMessage(ChatColor.YELLOW + "/" + label + " reload "+ChatColor.GOLD + "- reload Death Certificate's configuration.");
		}
		else if(args[0].equalsIgnoreCase("reload"))
		{
			if (sender.hasPermission("deathcertificate.reload"))
			{
				reload();
				sender.sendMessage(ChatColor.YELLOW + "Death Certificate config reloaded.");
			}
			else
			{
				sender.sendMessage(ChatColor.YELLOW + "You don't have permission.");
			}
		}
		else
		{
			sender.sendMessage(ChatColor.YELLOW + "Unknown sub command.");
		}
		return true;
	}

	public String getMortuary(Location location) {
		return getConfig().getString("Mortuary.Default");
		//TODO: have different mortuaries.
	}

	public int[] getDateOrder() {
		return dateOrder;
	}

	public File getFile(Player player) {
		File f = new File(getDataFolder(), "players");
		return new File(f, player.getName());
	}
	

	public String getFriendlyMobName(EntityDamageByEntityEvent event2)
	{
		Entity e = event2.getDamager();
		if(e instanceof Projectile && ((Projectile) e).getShooter()!=null) e = ((Projectile) e).getShooter();
		switch (e.getType()) {
			case ENDER_DRAGON: return "the Enderdragon";
			case PIG_ZOMBIE: return "a Zombie Pigman";
			case PRIMED_TNT: return "TNT";
			case SMALL_FIREBALL: return "a Fireball";
			case SNOWMAN: return "a Snow Golem";
			default:
				String[] name =  e.getType().name().toLowerCase().split("_");
				String ret = "a";
				if(name[0].matches("[aeiou]")) ret += "n";
				for(String str: name)
					ret += " " + str.substring(0,1).toUpperCase() + str.substring(1);
				return ret;
		}
	}
}
