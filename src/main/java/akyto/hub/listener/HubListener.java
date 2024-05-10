package akyto.hub.listener;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;

import akyto.hub.utils.Utils;

public class HubListener implements Listener {
	
	final Inventory inventory = Bukkit.createInventory(null, InventoryType.FURNACE, ChatColor.GRAY + "Select server:");
	
	public HubListener() {
		this.inventory.setItem(0, Utils.createItems(Material.DIAMOND_SWORD, ChatColor.WHITE + "Practice", Arrays.asList(
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------",
				ChatColor.YELLOW + "Train alone or with your friends",
				" ",
				ChatColor.GOLD + "Status" + ChatColor.GRAY + ": " + ChatColor.RED + "Whitelisted",
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------")));
		this.inventory.setItem(1, Utils.createItems(Material.SKULL_ITEM, ChatColor.WHITE + "JJK-Pot", Arrays.asList(
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------",
				ChatColor.YELLOW + "Focused on the jujutsu kaisen theme,",
				ChatColor.YELLOW + " a mixed potion and farming game",
				" ",
				ChatColor.GOLD + "Status" + ChatColor.GRAY + ": " + ChatColor.RED + "Whitelisted",
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------")));
		this.inventory.setItem(2, Utils.createItems(Material.BOOK, ChatColor.DARK_RED + "Disconnect", Arrays.asList(
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------",
				ChatColor.RED + "Click here to disconnect,",
				ChatColor.RED + "See you soon :p",
				ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------")));
	}
	
	@EventHandler
	public void onLeft(final PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		event.setJoinMessage(null);
		event.getPlayer().sendMessage(new String[] {
				ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------------------",
				ChatColor.GRAY + "Welcome to Akyto " + ChatColor.RED + event.getPlayer().getName(),
				ChatColor.DARK_GRAY + ChatColor.ITALIC.toString() + ChatColor.BOLD + "You can find our socials here" + ChatColor.GRAY + ":",
				" ",
				ChatColor.GRAY + ChatColor.ITALIC.toString() + "(*)" + ChatColor.DARK_GRAY + ChatColor.BOLD + " Discord" + ChatColor.GRAY + ": " + ChatColor.WHITE + "discord.akyto.club",
				ChatColor.GRAY + ChatColor.ITALIC.toString() + "(*)" + ChatColor.DARK_GRAY + ChatColor.BOLD + " Youtube" + ChatColor.GRAY + ": " + ChatColor.WHITE + "www.youtube.com/@AkytoNetwork",
				ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "----------------------------------"
				
		});
		event.getPlayer().setFoodLevel(20);
		event.getPlayer().getInventory().clear();
		event.getPlayer().getInventory().setItem(4, Utils.createItems(Material.NETHER_STAR, ChatColor.GRAY + " » " + ChatColor.GOLD + "Select Server:", null));
		event.getPlayer().updateInventory();
		event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 0.315D, 101.00000D, 0.464D, 91.0f, -7.7f));
	}
	
	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) return;
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.getItem().getType().equals(Material.NETHER_STAR)) {
				event.getPlayer().openInventory(inventory);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onDamage(final EntityDamageEvent event) {
		if (event.getCause().equals(DamageCause.VOID)) {
			event.getEntity().teleport(new Location(event.getEntity().getWorld(), 0.315D, 101.00000D, 0.464D, 91.0f, -7.7f));
		}
		event.setDamage(0.0d);
		event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onDrop(final PlayerDropItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onTalk(final AsyncPlayerChatEvent event) {
		if (!event.getPlayer().isOp() || !event.getPlayer().hasPermission("akyto.staff")) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "The chat is disabled in the hub!");
			return;
		}
		event.setFormat("%1$s" + ": " + "%2$s");
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
			Utils.sendServer(Bukkit.getPlayer(event.getWhoClicked().getUniqueId()), "Connect", "practicena");
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
