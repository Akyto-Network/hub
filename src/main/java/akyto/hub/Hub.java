package akyto.hub;

import org.bukkit.plugin.java.JavaPlugin;

import akyto.hub.command.SendCommand;
import akyto.hub.listener.HubListener;
import lombok.Getter;

public class Hub extends JavaPlugin{
	
	@Getter
	public static Hub instance;
	@Getter
	final String bungeeIp = "172.93.104.4";
	
	public void onEnable() {
		instance = this;
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getPluginManager().registerEvents(new HubListener(), this);
		this.getCommand("send").setExecutor(new SendCommand());
	}
}
