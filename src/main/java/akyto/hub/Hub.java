package akyto.hub;

import aether.Aether;
import akyto.hub.board.SideBoard;
import akyto.hub.command.PetsCommand;
import akyto.hub.listener.InventoryListener;
import akyto.hub.manager.InventoryManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import akyto.hub.command.SendCommand;
import akyto.hub.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Hub extends JavaPlugin implements PluginMessageListener {
	
	@Getter
	public static Hub instance;

	@Getter
	final HashMap<UUID, Long> cooldown = new HashMap<>();
	@Getter
	final HashMap<UUID, Entity> pets = new HashMap<>();
	@Getter
	final HashMap<UUID, Entity> jollyjumper = new HashMap<>();

	@Getter
	final InventoryManager inventoryManager = new InventoryManager();

    public void onEnable() {
		instance = this;
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		this.getCommand("send").setExecutor(new SendCommand());
		this.getCommand("pets").setExecutor(new PetsCommand());
		new Aether(this, new SideBoard(this));
	}

	public void onDisable() {
		for (Iterator<Entity> iterator = getServer().getWorld("world").getEntities().iterator(); iterator.hasNext(); ) {
			Entity entity = iterator.next();
			entity.remove();
		}
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
