package akyto.hub.board;

import java.text.DateFormat;
import java.util.*;

import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
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
    private final String title =  ChatColor.WHITE.toString() + ChatColor.BOLD + "Hub";
    DateFormat shortDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, 3);

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
        if (pm.getSettings()[0] != 2) {
            if (pm.isInState(ProfileState.FREE, ProfileState.MOD)) {
                return this.getLobbyBoard(player);
            }
        }
        return null;
    }

    private List<String> getLobbyBoard(final Player player) {
        final List<String> board = new LinkedList<>();
        String spacer = " ";
        board.add(spacer);
        board.add(ChatColor.GRAY.toString() + ChatColor.ITALIC + shortDateFormat.format(new Date()));
        board.add(spacer);
        final int globalOnline = Bukkit.getOnlinePlayers().size() + Hub.getInstance().getServerCount(player, "practice");
        board.add(ChatColor.GRAY + "Global" + ChatColor.GRAY + ": " + ChatColor.WHITE + globalOnline);
        board.add(spacer);
        board.add(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Servers");
        board.add(ChatColor.GRAY + "• " + ChatColor.WHITE + "Practice: " + ChatColor.RED + Hub.getInstance().getServerCount(player, "practice"));
        board.add(ChatColor.GRAY + "• " + ChatColor.WHITE + "HCF: " + ChatColor.RED + Hub.getInstance().getServerCount(player, "hcf"));
        board.add(spacer);
        final String rankString = Core.API.getManagerHandler().getProfileManager().getProfiles().get(player.getUniqueId()).getRank();
        final RankEntry rank = Core.API.getManagerHandler().getRankManager().getRanks().get(rankString);
        board.add(ChatColor.GRAY + "Rank" + ChatColor.GRAY + ": " + (rankString.equals("default") ? ChatColor.GREEN + "Player" : rank.getPrefix().replace("[", "").replace("]", "")));
        board.add(spacer);
        board.add(ChatColor.WHITE.toString() + ChatColor.ITALIC + "akyto.net");
        board.add(spacer);
        return board;
    }
}