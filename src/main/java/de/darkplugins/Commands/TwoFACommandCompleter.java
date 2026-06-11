package de.darkplugins.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TwoFACommandCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if(isEmpty(args)) {
            suggestions.add("<Code>");
            suggestions.add("unlink");
        } else if (args[0].toLowerCase().startsWith("u")) {
            if (args.length >= 2 && sender.isOp() && args[0].equalsIgnoreCase("unlink")) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    suggestions.add(player.getName());
                }
            } else {
                suggestions.add("unlink");
            }
        }
        return suggestions;
    }

    private boolean isEmpty(String[] args) {
        for (String string : args) {
            if (string != null && !string.isEmpty()) return false;
        }
        return true;
    }
}
