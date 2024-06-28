package akyto.hub.board;

import java.util.*;

import aether.scoreboard.Board;
import aether.scoreboard.BoardAdapter;
import aether.scoreboard.cooldown.BoardCooldown;
import akyto.core.rank.RankEntry;
import akyto.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import akyto.core.Core;
import akyto.core.profile.Profile;
import akyto.core.profile.ProfileState;

public class SideBoard implements BoardAdapter {
    private final Hub plugin;
    private final String spacer = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------------------";
    private final String title =  ChatColor.WHITE.toString() + ChatColor.BOLD + "Hub";

    public SideBoard(final Hub plugin) {  this.plugin = plugin; }

    @Override
    public String getTitle(final Player player) {
        return title;
    }

    @Override
    public List<String> getScoreboard(final Player player, final Board board, final Set<BoardCooldown> cooldowns) {
        final Profile pm = Core.API.getManagerHandler().getProfileManager().getProfiles().get(player.getUniqueId());

        if (pm == null) {
            this.plugin.getLogger().warning(player.getName() + "'s player data is null");
            return null;
        }

        // If player enabled scoreboard
        if (pm.getSettings().get(0)) {
            if (pm.isInState(ProfileState.FREE, ProfileState.MOD)) {
                return this.getLobbyBoard(player);
            }
        }
        return null;
    }

    private List<String> getLobbyBoard(final Player player) {
        final List<String> board = new LinkedList<>();

        board.add(spacer);
        final int globalOnline = Bukkit.getOnlinePlayers().size() + Hub.getInstance().getServerCount(player, "practice");
        board.add(ChatColor.GRAY + "Global" + ChatColor.GRAY + ": " + ChatColor.WHITE + globalOnline);
        board.add(" ");
        board.add(ChatColor.RED + "Practice" + ChatColor.GRAY + ": " + ChatColor.WHITE + Hub.getInstance().getServerCount(player, "practice"));
        board.add(ChatColor.AQUA + "Soup" + ChatColor.GRAY + ": " + ChatColor.WHITE + Hub.getInstance().getServerCount(player, "soup"));
        board.add(" ");
        final String rankString = Core.API.getManagerHandler().getProfileManager().getProfiles().get(player.getUniqueId()).getRank();
        final RankEntry rank = Core.API.getManagerHandler().getRankManager().getRanks().get(rankString);
        board.add(ChatColor.GRAY + "Rank" + ChatColor.GRAY + ": " + (rankString.equals("default") ? ChatColor.GREEN + "Player" : rank.getPrefix()));
        board.add(" ");
        board.add(ChatColor.WHITE.toString() + ChatColor.ITALIC + "akyto.club");
        board.add(spacer);
        return board;
    }
}