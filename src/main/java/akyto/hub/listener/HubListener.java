package akyto.hub.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import akyto.core.Core;
import akyto.core.utils.item.ItemUtils;
import akyto.hub.Hub;
import akyto.hub.server.ServerInfo;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;

import akyto.hub.utils.Utils;
import org.spigotmc.event.entity.EntityDismountEvent;

public class HubListener implements Listener {
	
	final Inventory inventory = Bukkit.createInventory(null, InventoryType.FURNACE, ChatColor.GRAY + "Select server:");

	private void refreshInventory() {
		this.inventory.setItem(0, ItemUtils.createItems(Material.DIAMOND_SWORD, ChatColor.WHITE + "Practice", Arrays.asList(
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------",
				ChatColor.YELLOW + "Train alone or with your friends",
				" ",
				ChatColor.GOLD + "Online" + ChatColor.GRAY + ": " + ChatColor.WHITE + Hub.getInstance().getServerCount(Bukkit.getOnlinePlayers().iterator().next(), "practice"),
				ChatColor.GOLD + "Status" + ChatColor.GRAY + ": " + ChatColor.RED + "Whitelisted",
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------")));
		this.inventory.setItem(1, ItemUtils.createItems(Material.MUSHROOM_SOUP, ChatColor.WHITE + "Soup(World)", Arrays.asList(
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------",
				ChatColor.YELLOW + "Discover skills and kits",
				ChatColor.YELLOW + "and enter the arena to do battle!",
				" ",
				ChatColor.GOLD + "Online" + ChatColor.GRAY + ": " + ChatColor.WHITE + Hub.getInstance().getServerCount(Bukkit.getOnlinePlayers().iterator().next(), "soup"),
				ChatColor.GOLD + "Status" + ChatColor.GRAY + ": " + ChatColor.RED + "Whitelisted",
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------")));
		this.inventory.setItem(2, ItemUtils.createItems(Material.BOOK, ChatColor.DARK_RED + "Disconnect", Arrays.asList(
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------",
				ChatColor.RED + "Click here to disconnect,",
				ChatColor.RED + "See you soon :p",
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------")));
	}

	@EventHandler
	public void onLeft(final PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Utils.removeCooldown(event.getPlayer().getUniqueId());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Core.API.getManagerHandler().getProfileManager().createProfile(event.getPlayer().getUniqueId());
		Core.API.getDatabaseSetup().loadAsync(event.getPlayer().getUniqueId(), 0, null);
		Utils.applyCooldown(event.getPlayer().getUniqueId());
		event.getPlayer().sendMessage(new String[] {
				ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------------------",
				ChatColor.YELLOW + ChatColor.ITALIC.toString() + ChatColor.BOLD + "You can find our socials here" + ChatColor.GRAY + ":",
				" ",
				ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + "Akyto:",
				ChatColor.GRAY + ChatColor.ITALIC.toString() + "(*)" + ChatColor.DARK_GRAY + ChatColor.BOLD + " Discord" + ChatColor.GRAY + ": " + ChatColor.WHITE + "discord.akyto.club",
				ChatColor.GRAY + ChatColor.ITALIC.toString() + "(*)" + ChatColor.DARK_GRAY + ChatColor.BOLD + " Youtube" + ChatColor.GRAY + ": " + ChatColor.WHITE + "www.youtube.com/@AkytoNetwork",
				ChatColor.GRAY + ChatColor.ITALIC.toString() + "(*)" + ChatColor.DARK_GRAY + ChatColor.BOLD + " Website" + ChatColor.GRAY + ": " + ChatColor.WHITE + "http://.akyto.club",
				" ",
				ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "SoupWorld:",
				ChatColor.GRAY + ChatColor.ITALIC.toString() + "(*)" + ChatColor.AQUA + ChatColor.BOLD + " Discord" + ChatColor.GRAY + ": " + ChatColor.WHITE + "discord.soupworld.net",
				ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------------------"
				
		});
		event.getPlayer().setFoodLevel(20);
		event.getPlayer().getInventory().clear();
		event.getPlayer().getInventory().setItem(0, ItemUtils.createItems(Material.SADDLE, ChatColor.GRAY + "Call your mount"));
		event.getPlayer().getInventory().setItem(1, ItemUtils.createItems(Material.ENDER_PEARL, ChatColor.GRAY + "Ender-butt"));
		event.getPlayer().getInventory().setItem(4, ItemUtils.createItems(Material.NETHER_STAR, ChatColor.GRAY + "Select Server:"));
		event.getPlayer().getInventory().setItem(8, ItemUtils.createItems(Material.BLAZE_POWDER, ChatColor.GRAY + "Vanish Others Player"));
		event.getPlayer().updateInventory();
		event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 5502.464D, 102.06250D, 5393.673D, 0.3f, -0.1f));
	}
	
	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
		if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
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
			if (event.getItem().getType().equals(Material.NETHER_STAR)) {
				this.refreshInventory();
				event.getPlayer().openInventory(inventory);
			}
		}
	}

	@EventHandler
	public void onPlayerDismount(EntityDismountEvent event) {
		Utils.applyCooldown(event.getDismounted().getPassenger().getUniqueId());
		Entity entity = event.getDismounted();
		if (entity instanceof EntityHorse){
			entity.remove();
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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClickEvent(final InventoryClickEvent event) {
		if (event.getClickedInventory() == null) return;
		if (!event.getClickedInventory().getName().equals(this.inventory.getName())) {
			event.setCancelled(true);
			return;
		}
		event.setCancelled(true);
		if (event.getCurrentItem().getType().equals(Material.DIAMOND_SWORD)) {
			Utils.sendServer(Bukkit.getPlayer(event.getWhoClicked().getUniqueId()), "Connect", "practice");
			return;
		}
		if (event.getCurrentItem().getType().equals(Material.MUSHROOM_SOUP)) {
			Utils.sendServer(Bukkit.getPlayer(event.getWhoClicked().getUniqueId()), "Connect", "soup");
			return;
		}
		if (event.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
			return;
		}
		if (event.getCurrentItem().getType().equals(Material.BOOK)) {
			Bukkit.getPlayer(event.getWhoClicked().getUniqueId()).kickPlayer(ChatColor.RED + "Hoping to see you again soon on our platforms :p");
			return;
		}
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
