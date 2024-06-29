package akyto.hub.utils;

import java.util.UUID;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import akyto.hub.Hub;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Utils {
	
	public static void sendServer(final Player player, final String type, final String server) {
	    ByteArrayDataOutput out = ByteStreams.newDataOutput();
	    out.writeUTF(type);
	    out.writeUTF(server);
	    player.sendPluginMessage(Hub.getInstance(), "BungeeCord", out.toByteArray());
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

	public static void setupEnderpearlRunnable(final Item item) {
		(new BukkitRunnable() {
			public void run() {
				if (item.isDead())
					cancel();
				if (item.getVelocity().getX() == 0.0D || item.getVelocity().getY() == 0.0D || item.getVelocity().getZ() == 0.0D) {
					Player player = (Player)item.getPassenger();
					item.remove();
					if (player != null)
						player.teleport(player.getLocation().add(0.0D, 0.5D, 0.0D));
					cancel();
				}
			}
		}).runTaskTimer(Hub.getInstance(), 2L, 1L);
	}

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
		horse.setOwner(player);
		horse.setPassenger(player);
		Hub.getInstance().getJollyjumper().put(player.getUniqueId(), horse);
	}
}
