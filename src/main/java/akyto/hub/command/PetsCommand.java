package akyto.hub.command;

import akyto.core.utils.CoreUtils;
import akyto.hub.Hub;
import akyto.hub.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "/pets <rename/change> <value/>");
            return false;
        }
        final Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("rename")) {
            if (Hub.getInstance().getPets().get(player.getUniqueId()) == null) {
                sender.sendMessage(ChatColor.RED + "You doesn't have any pets!");
                return false;
            }
            Hub.getInstance().getPets().get(player.getUniqueId()).setCustomName(CoreUtils.translate(args[1]));
        }
        return false;
    }

}
