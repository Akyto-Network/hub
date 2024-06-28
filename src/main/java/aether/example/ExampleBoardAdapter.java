package aether.example;

import aether.scoreboard.Board;
import aether.scoreboard.BoardAdapter;
import aether.scoreboard.cooldown.BoardCooldown;
import aether.scoreboard.cooldown.BoardFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExampleBoardAdapter implements BoardAdapter, Listener
{
    public ExampleBoardAdapter(final JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public String getTitle(final Player player) {
        return "&6&lAether";
    }
    
    @Override
    public List<String> getScoreboard(final Player player, final Board board, final Set<BoardCooldown> cooldowns) {
        final List<String> strings = new ArrayList<>();
        strings.add("&7&m-------------------");
        for (final BoardCooldown cooldown : cooldowns) {
            if (cooldown.getId().equals("enderpearl")) {
                strings.add("&e&lEnderpearl&7:&c " + cooldown.getFormattedString(BoardFormat.SECONDS));
            }
        }
        strings.add("&7&m-------------------&r");
        if (strings.size() == 2) {
            return null;
        }
        return strings;
    }
    
    @EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Board board = Board.getByPlayer(player);
        if (event.getAction().name().contains("RIGHT")) {
            if (event.getItem() == null) {
                return;
            }
            if (event.getItem().getType() != Material.ENDER_PEARL) {
                return;
            }
            if (board == null) {
                return;
            }
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                return;
            }
            final BoardCooldown cooldown = board.getCooldown("enderpearl");
            if (cooldown != null) {
                event.setCancelled(true);
                player.updateInventory();
                player.sendMessage(ChatColor.DARK_PURPLE + "You must wait " + cooldown.getFormattedString(BoardFormat.SECONDS) + " seconds before enderpearling again!");
                return;
            }
            new BoardCooldown(board, "enderpearl", 16.0);
        }
    }
}
