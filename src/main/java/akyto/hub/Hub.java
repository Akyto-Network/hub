package akyto.hub;

import aether.Aether;
import akyto.hub.board.SideBoard;
import akyto.hub.server.ServerInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import akyto.hub.command.SendCommand;
import akyto.hub.listener.HubListener;
import lombok.Getter;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitTask;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;

public class Hub extends JavaPlugin implements PluginMessageListener {
	
	@Getter
	public static Hub instance;

	@Getter
	private HashMap<String, ServerInfo> serverInfoMap = new HashMap<>();

    public void onEnable() {
		instance = this;
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		this.getServer().getPluginManager().registerEvents(new HubListener(), this);
		this.getCommand("send").setExecutor(new SendCommand());
		new Aether(this, new SideBoard(this));
	}

	private final HashMap<String, Integer> serverCount = new HashMap<>();

	public void getCount(Player player, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(server);

		player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		int bytesRead = 0;
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		bytesRead += 2 + subchannel.length();
		if (subchannel.equals("PlayerCount")) {
			int remainingBytes = message.length - bytesRead;
			if (remainingBytes < 4) {
				return;
			}
			String server = in.readUTF();
			bytesRead += 2 + server.length();
			remainingBytes = message.length - bytesRead;
			if (remainingBytes < 4) {
				return;
			}
			serverCount.put(server, in.readInt());
		}
	}

	public int getServerCount(Player p, String server) {
		getCount(p, server);
		if (serverCount.get(server) != null) {
			return serverCount.get(server);
		}
		return 0;
	}
}
