package akyto.hub.manager;

import akyto.core.utils.item.ItemUtils;
import akyto.hub.Hub;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class InventoryManager {

    @Getter
    final Inventory selector = Bukkit.createInventory(null, InventoryType.FURNACE, ChatColor.GRAY + "Select server:");

    public void refreshInventory() {
        this.selector.setItem(0, ItemUtils.createItems(Material.DIAMOND_SWORD, ChatColor.WHITE + "Practice", Arrays.asList(
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------",
                ChatColor.YELLOW + "Train alone or with your friends",
                " ",
                ChatColor.GOLD + "Online" + ChatColor.GRAY + ": " + ChatColor.WHITE + Hub.getInstance().getServerCount(Bukkit.getOnlinePlayers().iterator().next(), "practice"),
                ChatColor.GOLD + "Status" + ChatColor.GRAY + ": " + ChatColor.RED + "Whitelisted",
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------")));
        this.selector.setItem(1, ItemUtils.createItems(Material.MUSHROOM_SOUP, ChatColor.WHITE + "Soup(World)", Arrays.asList(
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------",
                ChatColor.YELLOW + "Discover skills and kits",
                ChatColor.YELLOW + "and enter the arena to do battle!",
                " ",
                ChatColor.GOLD + "Online" + ChatColor.GRAY + ": " + ChatColor.WHITE + Hub.getInstance().getServerCount(Bukkit.getOnlinePlayers().iterator().next(), "soup"),
                ChatColor.GOLD + "Status" + ChatColor.GRAY + ": " + ChatColor.RED + "Whitelisted",
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------")));
        this.selector.setItem(2, ItemUtils.createItems(Material.BOOK, ChatColor.DARK_RED + "Disconnect", Arrays.asList(
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------",
                ChatColor.RED + "Click here to disconnect,",
                ChatColor.RED + "See you soon :p",
                ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "---------------------")));
    }
}
