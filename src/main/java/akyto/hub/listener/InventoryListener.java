package akyto.hub.listener;

import akyto.hub.Hub;
import akyto.hub.utils.Utils;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClickEvent(final InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        event.setCancelled(true);
        if (event.getClickedInventory().equals(event.getWhoClicked().getInventory())) return;
        if (event.getClickedInventory().equals(Hub.getInstance().getInventoryManager().getSelector())) {
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
    }
}
