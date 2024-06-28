package aether;

import aether.event.BoardCreateEvent;
import aether.scoreboard.Board;
import aether.scoreboard.BoardAdapter;
import aether.scoreboard.BoardEntry;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Getter
public class Aether implements Listener
{
    private final JavaPlugin plugin;
    private final AetherOptions options;
    BoardAdapter adapter;
    
    public Aether(final JavaPlugin plugin, final BoardAdapter adapter, final AetherOptions options) {
        this.options = options;
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.setAdapter(adapter);
        this.run();
    }
    
    public Aether(final JavaPlugin plugin, final BoardAdapter adapter) {
        this(plugin, adapter, AetherOptions.defaultOptions());
    }
    
    public Aether(final JavaPlugin plugin) {
        this(plugin, null, AetherOptions.defaultOptions());
    }
    
    private void run() {
        new BukkitRunnable() {
            public void run() {
                if (Aether.this.adapter == null) {
                    return;
                }
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    final Board board = Board.getByPlayer(player);
                    if (board != null) {
                        final List<String> scores = Aether.this.adapter.getScoreboard(player, board, board.getCooldowns());
                        final List<String> translatedScores = new ArrayList<>();
                        if (scores == null) {
                            if (board.getEntries().isEmpty()) {
                                continue;
                            }
                            for (final BoardEntry boardEntry : board.getEntries()) {
                                boardEntry.remove();
                            }
                            board.getEntries().clear();
                        }
                        else {
                            for (final String line : scores) {
                                translatedScores.add(ChatColor.translateAlternateColorCodes('&', line));
                            }
                            if (!Aether.this.options.scoDARK_PURPLEirectionDown()) {
                                Collections.reverse(scores);
                            }
                            final Scoreboard scoreboard = board.getScoreboard();
                            final Objective objective = board.getObjective();
                            if (!objective.getDisplayName().equals(Aether.this.adapter.getTitle(player))) {
                                objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', Aether.this.adapter.getTitle(player)));
                            }
                        Block_12:
                            for (int i = 0; i < scores.size(); ++i) {
                                final String text = scores.get(i);
                                int position;
                                if (Aether.this.options.scoDARK_PURPLEirectionDown()) {
                                    position = 15 - i;
                                }
                                else {
                                    position = i + 1;
                                }
                                while (true) {
                                    for (final BoardEntry boardEntry2 : new ArrayList<BoardEntry>(board.getEntries())) {
                                        final Score score = objective.getScore(boardEntry2.getKey());
                                        if (score != null && boardEntry2.getText().equals(ChatColor.translateAlternateColorCodes('&', text)) && score.getScore() == position) {
                                            continue Block_12;
                                        }
                                    }
                                    final int positionToSearch = Aether.this.options.scoDARK_PURPLEirectionDown() ? (15 - position) : (position - 1);
                                    Iterator<BoardEntry> iterator = board.getEntries().iterator();
                                    while (iterator.hasNext()) {
                                        final BoardEntry boardEntry3 = iterator.next();
                                        final int entryPosition = scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(boardEntry3.getKey()).getScore();
                                        if (!Aether.this.options.scoDARK_PURPLEirectionDown() && entryPosition > scores.size()) {
                                            iterator.remove();
                                            boardEntry3.remove();
                                        }
                                    }
                                    final BoardEntry entry = board.getByPosition(positionToSearch);
                                    if (entry == null) {
                                        new BoardEntry(board, text).send(position);
                                    }
                                    else {
                                        entry.setText(text).setup().send(position);
                                    }
                                    if (board.getEntries().size() > scores.size()) {
                                        iterator = board.getEntries().iterator();
                                        while (iterator.hasNext()) {
                                            final BoardEntry boardEntry4 = iterator.next();
                                            if (!translatedScores.contains(boardEntry4.getText()) || Collections.frequency(board.getBoardEntriesFormatted(), boardEntry4.getText()) > 1) {
                                                iterator.remove();
                                                boardEntry4.remove();
                                            }
                                        }
                                    }
                                }
                            }
                            player.setScoreboard(scoreboard);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(this.plugin, 20L, 2L);
    }
    
    public void setAdapter(final BoardAdapter adapter) {
        this.adapter = adapter;
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final Board board = Board.getByPlayer(player);
            if (board != null) {
                Board.getBoards().remove(board);
            }
            Bukkit.getPluginManager().callEvent(new BoardCreateEvent(new Board(player, this, this.options), player));
        }
    }
    
    @EventHandler
    public void onPlayerJoinEvent(final PlayerJoinEvent event) {
        if (Board.getByPlayer(event.getPlayer()) == null) {
            Bukkit.getPluginManager().callEvent(new BoardCreateEvent(new Board(event.getPlayer(), this, this.options), event.getPlayer()));
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(final PlayerQuitEvent event) {
        final Board board = Board.getByPlayer(event.getPlayer());
        if (board != null) {
            Board.getBoards().remove(board);
        }
    }

}
