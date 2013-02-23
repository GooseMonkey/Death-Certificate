package me.goosemonkey.deathcertificate;

import java.util.Calendar;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

	public String getMorturary(Location location) {
		// TODO Auto-generated method stub
		return "test";
	}

	public int[] getDateOrder() {
		return dateOrder;
	}
}
