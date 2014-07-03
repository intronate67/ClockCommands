package com.mcdrum.dev;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @Author Hunter Sharpe
 */
public class ClockCommands extends JavaPlugin{

    private static ClockCommands cc;

    public static ClockCommands getInstance(){
        return cc;
    }
    FileConfiguration config = getConfig();
    @Override
    public void onEnable(){
        if(!getDataFolder().exists()) getDataFolder().mkdir();
        getCommand("cc").setExecutor(new ClockCommands());
        config.set("time",ClockCommand.getInstance().integerList);
    }
    @Override
        public void onDisable(){

    }

}
