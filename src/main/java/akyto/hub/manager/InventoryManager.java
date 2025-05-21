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
        this.selector.setItem(0, ItemUtils.createItems(Material.DIAMOND_SWORD, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Practice", Arrays.asList(
                ChatColor.GRAY.toString() + ChatColor.ITALIC + "Parties, Events, ClubFight,",
                ChatColor.GRAY.toString() + ChatColor.ITALIC + "Tournaments, 1v1...",
                " ",
                ChatColor.RED + "Online" + ChatColor.GRAY + ": " + ChatColor.WHITE + Hub.getInstance().getServerCount(Bukkit.getOnlinePlayers().iterator().next(), "practice"))));
        this.selector.setItem(1, ItemUtils.createItems(Material.GOLD_CHESTPLATE, ChatColor.DARK_RED.toString() + ChatColor.BOLD + "HCF", Arrays.asList(
                ChatColor.GRAY.toString() + ChatColor.ITALIC + "Hardcore factions, don't die.",
                ChatColor.GRAY.toString() + ChatColor.ITALIC + "Currently under development.",
                " ",
                ChatColor.RED + "Online" + ChatColor.GRAY + ": " + ChatColor.WHITE + Hub.getInstance().getServerCount(Bukkit.getOnlinePlayers().iterator().next(), "hcf"))));
        this.selector.setItem(2, ItemUtils.createItems(Material.BOOK, ChatColor.DARK_RED + "Disconnect", Arrays.asList(
                " ",
                ChatColor.RED + "Click here to disconnect,",
                ChatColor.RED + "See you soon :p",
                " ")));
    }
}
