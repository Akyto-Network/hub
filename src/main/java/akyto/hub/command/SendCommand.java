package akyto.hub.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import akyto.hub.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class SendCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) return false;
		if (args.length != 2) {
			sender.sendMessage(ChatColor.RED + "/send <player> <server>");
			return false;
		}
		final Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(ChatColor.RED + args[0] + "isn't online!");
			return false;
		}
		sender.sendMessage(ChatColor.RED + "You've been sended " + target.getName() + " to " + args[1]);
		Utils.sendServer(target, "Connect", args[1]);
		return false;
	}

}
