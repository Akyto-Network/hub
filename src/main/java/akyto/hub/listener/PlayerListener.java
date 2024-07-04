package akyto.hub.listener;

import akyto.core.Core;
import akyto.core.utils.CoreUtils;
import akyto.core.utils.item.ItemUtils;
import akyto.hub.Hub;
import akyto.hub.task.RgbArmorTask;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

import akyto.hub.utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.spigotmc.event.entity.EntityDismountEvent;

public class PlayerListener implements Listener {

	@EventHandler
	public void onLeft(final PlayerQuitEvent event) {
		event.setQuitMessage(null);
		if (Hub.getInstance().getPets().containsKey(event.getPlayer().getUniqueId())){
			Hub.getInstance().getPets().get(event.getPlayer().getUniqueId()).remove();
			Hub.getInstance().getPets().remove(event.getPlayer().getUniqueId());
		}
		if (Hub.getInstance().getJollyjumper().containsKey(event.getPlayer().getUniqueId())) {
			Hub.getInstance().getJollyjumper().get(event.getPlayer().getUniqueId()).remove();
			Hub.getInstance().getJollyjumper().remove(event.getPlayer().getUniqueId());
		}
		Utils.removeCooldown(event.getPlayer().getUniqueId());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player player = event.getPlayer();
		Core.API.getManagerHandler().getProfileManager().createProfile(player.getUniqueId());
		Core.API.getDatabaseSetup().loadAsync(player.getUniqueId(), 0, null);
		Utils.applyCooldown(player.getUniqueId());
		player.sendMessage(new String[] {
				ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------------------",
				ChatColor.YELLOW + ChatColor.ITALIC.toString() + ChatColor.BOLD + "You can find our socials here" + ChatColor.GRAY + ":",
				" ",
				ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "Akyto:",
				ChatColor.GRAY + ChatColor.ITALIC.toString() + "(*)" + ChatColor.DARK_GRAY + ChatColor.BOLD + " Discord" + ChatColor.GRAY + ": " + ChatColor.WHITE + "discord.akyto.club",
				ChatColor.GRAY + ChatColor.ITALIC.toString() + "(*)" + ChatColor.DARK_GRAY + ChatColor.BOLD + " Youtube" + ChatColor.GRAY + ": " + ChatColor.WHITE + "www.youtube.com/@AkytoNetwork",
				ChatColor.GRAY + ChatColor.ITALIC.toString() + "(*)" + ChatColor.DARK_GRAY + ChatColor.BOLD + " Website" + ChatColor.GRAY + ": " + ChatColor.WHITE + "http://akyto.club",
				" ",
				ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "SoupWorld:",
				ChatColor.GRAY + ChatColor.ITALIC.toString() + "(*)" + ChatColor.AQUA + ChatColor.BOLD + " Discord" + ChatColor.GRAY + ": " + ChatColor.WHITE + "discord.soupworld.net",
				ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------------------"
				
		});
		player.setFoodLevel(20);
		player.getInventory().clear();
		player.getInventory().setItem(2, ItemUtils.createItems(Material.ENDER_PEARL, ChatColor.GRAY + "Ender-butt"));
		player.getInventory().setItem(4, ItemUtils.createItems(Material.NETHER_STAR, ChatColor.YELLOW + "Select Server"));
		player.getInventory().setItem(6, ItemUtils.createItems(Material.SADDLE, ChatColor.GRAY + "Call Jolly Jumper"));
		player.updateInventory();
		player.setCustomName(CoreUtils.translate(Core.API.getManagerHandler().getProfileManager().getRank(player.getUniqueId()).getColor()) + player.getName());
		player.setCustomNameVisible(true);
		player.teleport(Hub.getInstance().getServer().getWorlds().getFirst().getSpawnLocation());
		Hub.getInstance().getServer().getScheduler().runTaskLater(Hub.getInstance(), () -> {
			if (player.isOnline()) {
				Location loc = player.getLocation();
				Wolf dog = (Wolf) loc.getWorld().spawnEntity(loc, EntityType.WOLF);
				dog.setTamed(true);
				dog.setCollarColor(DyeColor.RED);
				dog.setCustomName(ChatColor.YELLOW + "Dog of " + CoreUtils.translate(Core.API.getManagerHandler().getProfileManager().getRank(player.getUniqueId()).getColor()) + player.getName());
				dog.setCustomNameVisible(true);
				dog.setOwner(player);
				dog.setTarget(player);
				dog.setRemoveWhenFarAway(false);
				Hub.getInstance().getPets().put(player.getUniqueId(), dog);
			}
		}, 20L);
		RgbArmorTask rgbArmor = new RgbArmorTask(player);
		BukkitTask rgbArmorTask = Hub.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(
				Hub.getInstance(),
				rgbArmor,
				5L,
				2L
		);
		rgbArmor.setTask(rgbArmorTask);
	}
	
	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
		if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player player = event.getPlayer();
			ItemStack itemStack = player.getItemInHand();
			if (itemStack.getType() == Material.ENDER_PEARL) {
				event.setCancelled(true);
				event.setUseItemInHand(Event.Result.DENY);
				event.setUseInteractedBlock(Event.Result.DENY);
				if(player.getVehicle() != null) return;
				if (Utils.getCooldown(event.getPlayer().getUniqueId()) > 0L){
					event.getPlayer().sendMessage(ChatColor.RED + "Wait please....");
					return;
				}
				if (player.getGameMode() == GameMode.SURVIVAL) {
					Utils.applyCooldown(player.getUniqueId());
					Item item = player.getWorld().dropItem(player.getLocation().add(0.0D, 0.5D, 0.0D), new ItemStack(Material.ENDER_PEARL, 16));
					item.setPickupDelay(10000);
					item.setVelocity(player.getLocation().getDirection().normalize().multiply(1.5F));
					item.setPassenger(player);
					player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
					Utils.setupEnderpearlRunnable(item);
					player.updateInventory();
				}
			}
			if (event.getItem().getType().equals(Material.NETHER_STAR)) {
				Hub.getInstance().getInventoryManager().refreshInventory();
				event.getPlayer().openInventory(Hub.getInstance().getInventoryManager().getSelector());
			}
			if (event.getItem().getType().equals(Material.SADDLE)) {
				if (event.getPlayer().getVehicle() != null) {
					event.setCancelled(true);
					return;
				}
				if (Utils.getCooldown(event.getPlayer().getUniqueId()) > 0L){
					event.getPlayer().sendMessage(ChatColor.RED + "Wait please....");
					return;
				}
				Utils.spawnHorseAndRide(event.getPlayer());
				event.getPlayer().sendMessage(ChatColor.GRAY + ChatColor.ITALIC.toString() + "Jolly Jumper: " + ChatColor.YELLOW + "Did you call me?");
			}
		}
	}

	@EventHandler
	public void onPlayerDismount(EntityDismountEvent event) {
		Utils.applyCooldown(event.getDismounted().getPassenger().getUniqueId());
		if (Hub.getInstance().getJollyjumper().containsKey(event.getDismounted().getPassenger().getUniqueId())) {
			Hub.getInstance().getJollyjumper().get(event.getDismounted().getPassenger().getUniqueId()).remove();
		}
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onDamage(final EntityDamageEvent event) {
		if (event.getCause().equals(DamageCause.VOID)) {
			event.getEntity().teleport(new Location(event.getEntity().getWorld(), 5502.464D, 102.06250D, 5393.673D, 0.3f, -0.1f));
		}
		event.setDamage(0.0d);
		event.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void onDrop(final PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerPlaceBlockEvent(final BlockPlaceEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
		event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void PlayerBreakBlockEvent(final BlockBreakEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onWeatherChange(final WeatherChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onFoodChange(final FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
}
