package com.mcdrum.dev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;

/**
 * @Author Hunter Sharpe
 */
public class ClockCommand implements CommandExecutor {

    private static ClockCommand cc;

    public static ClockCommand getInstance(){
        return cc;
    }

    private FileConfiguration config;
    private boolean isRunning = false;
    private int n;
    private int countdown;
    private int max;
    public List<Integer> integerList = (List<Integer>) ClockCommands.getInstance().getConfig().getIntegerList("time");
    Iterator<Integer> iterator = integerList.iterator();

    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(String.format("%sCommand Usage %s: %s/clockcommands %s<%sstart%s|%sstop%s|%sreset%s>", ChatColor.RED, ChatColor.GRAY, ChatColor.BLUE, ChatColor.GRAY, ChatColor.BLUE, ChatColor.GRAY, ChatColor.BLUE, ChatColor.GRAY, ChatColor.BLUE, ChatColor.GRAY));
            return true;
        }
        if (args[0].equalsIgnoreCase("start")) {
            if (!config.getBoolean("enabled")) {
                sender.sendMessage(String.format("%sClock Commands are not enabled%s!", ChatColor.RED, ChatColor.DARK_RED));
                return true;
            }
            n = Bukkit.getScheduler().scheduleSyncRepeatingTask(ClockCommands.getInstance(), new BukkitRunnable() {
                @Override
                public void run() {
                    isRunning = true;
                    max = config.getInt("max-time");

                    while(iterator.hasNext()){
                        countdown = iterator.next();
                        if(countdown >= max){
                            isRunning = false;
                            Bukkit.getScheduler().cancelTask(n);
                            return;
                        }
                        String command = config.getString("time." + iterator.next());
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                }
            }, 0L, 20L);
        }
        if (args[0].equalsIgnoreCase("stop")) {
            if(isRunning == false){
                sender.sendMessage("Clock Commands is not running!");
                return true;
            }
            Bukkit.getScheduler().cancelTask(n);
            sender.sendMessage("Stopped the current Clock Commands task.");
        }
        if(args[0].equalsIgnoreCase("set")){
            if(args.length != 3){
                sender.sendMessage("Command usage: /cc set <time> <commands>");
                return true;
            }
            if(!isInt(args[1])){
                sender.sendMessage(args[1] + " is not a valid time! Time must be an integer!");
            }

            config.set("time", args[1]);
            config.set("time." + args[1] + ".commands", args[2]);
            ClockCommands.getInstance().saveConfig();
        }
        if(args[0].equalsIgnoreCase("del")){
            if(args.length != 2){
                sender.sendMessage(args[1] + " does not exist in the config.");
                return true;
            }
            if(!isInt(args[1])){
                sender.sendMessage("Not a valid integer!");
                return true;
            }
            config.set("time." + args[1], null);
            ClockCommands.getInstance().saveConfig();
            sender.sendMessage("Time deleted from the config!");
        }
        return true;
    }
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
