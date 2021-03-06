package io.github.matho97.lockdown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
//import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class LockdownCommandExecutor implements CommandExecutor{

	private Lockdown plugin;
	public String lockdown = ChatColor.RED + "[" + ChatColor.GOLD + "LockDown" + ChatColor.RED + "] " + ChatColor.WHITE;
	public String notenough = lockdown + ChatColor.YELLOW + "Not enough arguments!";
	public String toomany = lockdown + ChatColor.YELLOW + "Too many arguments!";
	// Easier chat coloring during string broadcasts and such. Seeing as we do it so much in here ;)
	public ChatColor red = ChatColor.RED;
	public ChatColor yellow = ChatColor.YELLOW;
	public ChatColor green = ChatColor.GREEN;
	public ChatColor white = ChatColor.WHITE;
	public ChatColor purple = ChatColor.LIGHT_PURPLE;
	public ChatColor darkpurple = ChatColor.DARK_PURPLE;
	public ChatColor gold = ChatColor.GOLD;

	public boolean ldtask = false;
	public boolean ldtimer = false;
	public boolean ldscheduler = false;
	
	public int delay;
	public int count = 0;
	
	public LockdownCommandExecutor(Lockdown plugin) {
		this.plugin = plugin;
	}
	
	@Override	
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args){
		/**
		 * Stores the command methods
		 */
		if (cmd.getName().equalsIgnoreCase("lockdown")){
				if (args.length == 0){
					sender.sendMessage(gold + "----------------- " + lockdown + ChatColor.WHITE + " Help Page " + gold + "----------------");
					sender.sendMessage("/lockdown" + yellow + " - Shows this help page.");
					sender.sendMessage("/lockdown set <1|2>" + yellow + " - Sets the 2 warp points, 1 is the prison, 2 is when it's over.");
					sender.sendMessage("/lockdown reload" + yellow + " - Reloads the configuration files.");
					sender.sendMessage("/lockdown on <amount of time> <s|m>" + yellow + " - Sets the prison into lockdown mode, s = seconds, m = minutes");
					sender.sendMessage("/lockdown off" + yellow + " - Cancels the lockdown.");
					//sender.sendMessage("");
					return true;
				}
				/**
				 * Sets the two locations needed for the teleportation
				 */
				if (args[0].equalsIgnoreCase("set")){
					if(sender.hasPermission("lockdown.set")){
					
						Player player = sender.getServer().getPlayer(sender.getName());
						if(args.length == 1){
							sender.sendMessage(notenough);
							sender.sendMessage(lockdown + "Usage: /lockdown set <1|2>");
							return true;
						} else if (args.length == 3){
							sender.sendMessage(toomany);
							sender.sendMessage(lockdown + "Usage: /lockdown set <1|2>");
							return true;
						}
						/**
						 * Sets location one
						 */
						if (args[1].equalsIgnoreCase("1")){
							
							Double x = player.getLocation().getX();
							Double y = player.getLocation().getY();
							Double z = player.getLocation().getZ();
							
							Float pitch = player.getLocation().getPitch();
							Float yaw = player.getLocation().getYaw();
							
							//Location spawn = player.getBedSpawnLocation();
							
							plugin.getConfig().set(plugin.location1 + ".X", x);
							plugin.getConfig().set(plugin.location1 + ".Y", y);
							plugin.getConfig().set(plugin.location1 + ".Z", z);
							
							plugin.getConfig().set(plugin.location1 + ".Pitch", pitch);
							plugin.getConfig().set(plugin.location1 + ".Yaw", yaw);
							
							plugin.getConfig().set(plugin.spawn1 + ".X", x);
							plugin.getConfig().set(plugin.spawn1 + ".Y", y);
							plugin.getConfig().set(plugin.spawn1 + ".Z", z);
							
							plugin.saveConfig();

	
							String posX = x.toString();
							String posY = y.toString();
							String posZ = z.toString();
							
							sender.sendMessage(lockdown + purple + "Location 1 has been set at " + green + posX.substring(0, 3) + ", " + posY.substring(0, 3) + ", " + posZ.substring(0, 3));
							
							return true;
						} else 
						/**
						 * Sets location two
						 */
						if (args[1].equalsIgnoreCase("2")){
							World world = player.getWorld();
							Double x = (player.getLocation().getX());
							Double y = (player.getLocation().getY());
							Double z = (player.getLocation().getZ());
							Float pitch = (player.getLocation().getPitch());
							Float yaw = (player.getLocation().getYaw());
							
							Location spawn = world.getSpawnLocation();
							Double spawnX = spawn.getX();
							Double spawnY = spawn.getY();
							Double spawnZ = spawn.getZ();
							
							plugin.getConfig().set(plugin.location2 + ".X", x);
							plugin.getConfig().set(plugin.location2 + ".Y", y);
							plugin.getConfig().set(plugin.location2 + ".Z", z);
							plugin.getConfig().set(plugin.location2 + ".Pitch", pitch);
							plugin.getConfig().set(plugin.location2 + ".Yaw", yaw);
							
							plugin.getConfig().set(plugin.spawn2 + ".X", spawnX);
							plugin.getConfig().set(plugin.spawn2 + ".Y", spawnY);
							plugin.getConfig().set(plugin.spawn2 + ".Z", spawnZ);
							
							plugin.saveConfig();
	
							String posX = x.toString();
							String posY = y.toString();
							String posZ = z.toString();
								
							sender.sendMessage(lockdown + purple + "Location 2 has been set at " + green + posX.substring(0, 3) + ", " + posY.substring(0, 3) + ", " + posZ.substring(0, 3));
							return true;
						}
					}
				} else
				/**
				 * Reloads the configuration file
				 */
				if (args[0].equalsIgnoreCase("reload")){
					if(sender.hasPermission("lockdown.reload")){
						if (args.length == 2){
							sender.sendMessage(toomany);
							sender.sendMessage(lockdown + "Usage: /lockdown reload");
							return true;
						}
						plugin.reloadConfig();
						sender.sendMessage(lockdown + green + "Configuration has been reloaded successfully!");
						return true;
					}
				} else
				/**
				 * Turns on lockdown	
				 */
				if (args[0].equalsIgnoreCase("on")){
					if(sender.hasPermission("lockdown.execute")){
						if (args.length <= 2){
							sender.sendMessage(notenough);
							sender.sendMessage(lockdown + "Usage: /lockdown on <amount of time> <s|m>");
							return true;
						} else if (args.length == 4){
							sender.sendMessage(toomany);
							sender.sendMessage(lockdown + "Usage: /lockdown on <amount of time> <s|m>");
							return true;
						} else if (args[2].equalsIgnoreCase("s")|| args[2].equalsIgnoreCase("m")){
							/**
							 * Sets the teleport coordinates and teleports all online players
							 */
						    Double sx = plugin.getConfig().getDouble(plugin.location1 + ".X");
						    Double sy = plugin.getConfig().getDouble(plugin.location1 + ".Y");
						    Double sz = plugin.getConfig().getDouble(plugin.location1 + ".Z");
						    
						    Double spitch = plugin.getConfig().getDouble(plugin.location1 + ".Pitch");
						    Double syaw = plugin.getConfig().getDouble(plugin.location1 + ".Yaw");
						    
						    Double spawnX = plugin.getConfig().getDouble(plugin.spawn1 + ".X");
						    Double spawnY = plugin.getConfig().getDouble(plugin.spawn1 + ".Y");
						    Double spawnZ = plugin.getConfig().getDouble(plugin.spawn1 + ".Z");
						    
						    Float pitch = spitch.floatValue();
							Float yaw = syaw.floatValue();
							
							Double px = plugin.getConfig().getDouble(plugin.location2 + ".X");
							Double py = plugin.getConfig().getDouble(plugin.location2 + ".Y");
							Double pz = plugin.getConfig().getDouble(plugin.location2 + ".Z");
							
							if(sx == null||sy == null||sz == null ||px == null||py == null||pz == null){
								sender.sendMessage(lockdown + "You have not set all of the teleportation points!");
								sender.sendMessage(lockdown + "Do /lockdown set 1 and /lockdown set 2, to set the 2 teleportation points.");
								sender.sendMessage(lockdown + "===== Also, Remember: =====");
								sender.sendMessage(lockdown + "Point 1 is where users tp to during lockdown!");
								sender.sendMessage(lockdown + "Point 2 is where users tp to " + red + "AFTER " + ChatColor.WHITE +  "lockdown!");
								return true;
							} else {
							
							for(Player players : Bukkit.getOnlinePlayers()){
								Location teleportloc = new Location(players.getWorld(), sx, sy, sz, yaw, pitch);
								
								if((players.hasPermission("lockdown.immune"))){
									players.setBedSpawnLocation(new Location(players.getWorld(), spawnX, spawnY, spawnZ), true);
									players.teleport(teleportloc);
								}
							}
							Bukkit.broadcastMessage(lockdown + yellow + "The prison is now under lockdown, you will not be able to leave this area!");
							
							if (args[1] == null){
								delay = 5;
							} else {
								delay = Integer.parseInt(args[1]);
							}
							
							/***
							 * Sets the delay and message before executing the second teleportation 
							 */
							if (args[2] == null){
								sender.sendMessage(lockdown + "You need to choose if you want the delay in seconds or minutes! s or m.");
								return true;
							} else if (args[2].equalsIgnoreCase("m")){
								Bukkit.broadcastMessage(lockdown + ChatColor.GRAY + "Server has been put in lockdown for " + delay + " minute(s).");

								ldtask = true;
								@SuppressWarnings("unused")
								BukkitTask task = new LockdownTask(plugin).runTaskLater(plugin, delay * 1200);
								return true;
							} else if (args[2].equalsIgnoreCase("s")){
								Bukkit.broadcastMessage(lockdown + ChatColor.GRAY + "Server has been put in lockdown for " + delay + " second(s).");
								
								ldtask = true;
								@SuppressWarnings("unused")
								BukkitTask task = new LockdownTask(plugin).runTaskLater(plugin, delay * 20);
								return true;
							}
						}
					} else {						
						sender.sendMessage(lockdown + "The argument " + "'" + args[2] + "'" + " is not accepted!");
						sender.sendMessage(lockdown + "Use 's' for seconds and 'm' for minutes");
						return true;
						}
					}
				 } else
				/**
				 * Turns off lockdown
				 */
				if(args[0].equalsIgnoreCase("off")){
					if(sender.hasPermission("lockdown.execute")){
						if(args.length == 1){
							if (ldtask == true){
								ldtask = false;
								for (Player players : Bukkit.getServer().getOnlinePlayers()){
									players.sendMessage(lockdown + "Lockdown has been canceled by " + red + sender.getName());
									return true;
								}
							} else
							sender.sendMessage(lockdown + "The prison is not in lockdown!");
							return true;
						} else
						sender.sendMessage(toomany);
						sender.sendMessage("Usage: /lockdown off");
						return true;
					}
					return false;
				} else if (args[0].equalsIgnoreCase("something")){
					
					return true;
				}
			return false;
		} //end of lockdown command
		 //If this has happened the function will return true. 
	        // If this hasn't happened the a value of false will be returned.
		return false; 
	}

}
