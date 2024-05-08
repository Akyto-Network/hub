package akyto.hub.utils;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import akyto.hub.Hub;
import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;

public class Utils {
	
	public static void sendServer(final Player player, final String type, final String server) {
	    ByteArrayDataOutput out = ByteStreams.newDataOutput();
	    out.writeUTF(type);
	    out.writeUTF(server);
	    player.sendPluginMessage(Hub.getInstance(), "BungeeCord", out.toByteArray());
	}

	public static ItemStack createItems(final Material material, final String name, final List<String> lore) {
		final ItemStack item = new ItemStack(material);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
