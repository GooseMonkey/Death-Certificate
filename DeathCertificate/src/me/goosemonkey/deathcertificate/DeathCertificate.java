package me.goosemonkey.deathcertificate;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathCertificate extends JavaPlugin
{
	private String header = "Test Header";
	
	public void onEnable()
	{
		this.getConfig().options().copyDefaults(true);
		this.getConfig().options().copyHeader(true);
		
		this.getConfig().options().header(this.header);
	
		this.saveConfig();
		
		this.getServer().getPluginManager().registerEvents(new DeathCertificateListener(this), this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		try
		{
			if (args[0].equalsIgnoreCase("reload"))
			{
				if (sender.hasPermission("deathcertificate.reload"))
				{
					this.reloadConfig();
					
					sender.sendMessage(ChatColor.YELLOW + "Death Certificate config reloaded.");
					
					return true;
				}
				else
				{
					sender.sendMessage(ChatColor.YELLOW + "You don't have permission.");
					
					return true;
				}
			}
			else
			{
				throw new ArrayIndexOutOfBoundsException();
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			sender.sendMessage(ChatColor.YELLOW + "Death Certificate v" + this.getDescription().getVersion() + " by GooseMonkey97. \n" +
					"http://dev.bukkit.org/server-mods/death-certificate/");
		}
		
		return true;
	}
}
