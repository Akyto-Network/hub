package akyto.hub.listener;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import akyto.hub.Hub;
import akyto.hub.utils.Utils;
import kezuk.npaper.kPaper;
import kezuk.npaper.handler.MovementHandler;
import net.minecraft.server.v1_7_R4.PacketPlayInFlying;

public class HubListener implements Listener {
	
	private Hub main;
	final Inventory inventory = Bukkit.createInventory(null, InventoryType.FURNACE, ChatColor.GRAY + "Select server:");
	
	public HubListener(final Hub main) {
		this.main = main;
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
        kPaper.INSTANCE.addMovementHandler(new MovementHandler() {
            public void handleUpdateLocation(final Player player, final Location location, final Location location1, final PacketPlayInFlying packetPlayInFlying) {
    			player.teleport(location1);
    			player.openInventory(inventory);
            }
            public void handleUpdateRotation(final Player player, final Location location, final Location location1, final PacketPlayInFlying packetPlayInFlying) {
    			player.teleport(location1);
    			player.openInventory(inventory);
            }
        });
	}
	
	@EventHandler
	public void onLogin(final PlayerLoginEvent event) {
		if (!this.main.getBungeeIp().equals(event.getRealAddress().getHostAddress())) {
        	event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Please use the correct ip!");
        	return;
		}
	}
	
	@EventHandler
	public void onLeft(final PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		event.setJoinMessage(null);
		event.getPlayer().openInventory(this.inventory);
		event.getPlayer().teleport(new Location(event.getPlayer().getWorld(), 0.00000d, 101.000d, 0.00000d, 0f, 0f));
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

}
