package akyto.hub.utils;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import akyto.hub.Hub;

public class Utils {
	
	public static void sendServer(final Player player, final String type, final String server) {
	    ByteArrayDataOutput out = ByteStreams.newDataOutput();
	    out.writeUTF(type);
	    out.writeUTF(server);
	    player.sendPluginMessage(Hub.getInstance(), "BungeeCord", out.toByteArray());
	}

	//TODO: REWORK THE VELOCITY WHEN I JUMP WITH THE HORSE IS FUCKED!
	public static void spawnHorseAndRide(Player player) {
		Location loc = player.getLocation();
		World world = player.getWorld();
		EntityType type = EntityType.HORSE;
		Horse horse = (Horse) world.spawnEntity(loc, type);
		horse.getInventory().addItem(new ItemStack(Material.SADDLE));
		horse.setVariant(Horse.Variant.HORSE);
		horse.setColor(Horse.Color.WHITE);
		horse.setAdult();
		horse.setCarryingChest(true);
		horse.setTamed(true);
		horse.setTicksLived(20);
		horse.setPassenger(player);
	}

	public static boolean hasCooldown(final UUID uuid) {
		return Hub.getInstance().getCooldown().get(uuid) > System.currentTimeMillis();
	}

	public static long getCooldown(final UUID uuid) {
		return Math.max(0L, Hub.getInstance().getCooldown().get(uuid) - System.currentTimeMillis());
	}

	public static void applyCooldown(final UUID uuid) {
		Hub.getInstance().getCooldown().put(uuid, System.currentTimeMillis() + 3L * 1000L);
	}

	public static void removeCooldown(final UUID uuid) {
		Hub.getInstance().getCooldown().remove(uuid);
	}
}
