package com.github.groundbreakingmc.kidaypisun.commands;

import com.github.groundbreakingmc.kidaypisun.KidayPisun;
import com.github.groundbreakingmc.kidaypisun.utils.DickUtils;
import com.github.groundbreakingmc.kidaypisun.utils.config.ConfigValues;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class CommandManager implements TabExecutor {

    private final ConfigValues configValues;

    public CommandManager(final KidayPisun plugin) {
        this.configValues = plugin.getConfigValues();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Command for players only!");
            return true;
        }

        final ConfigValues.Dick dick = args.length > 0
                ? this.configValues.getDicks().get(args[0])
                : this.configValues.getDefaultDick();

        if (dick == null) {
            sender.sendMessage("Dick \"" + args[0] + "\" wasn't found");
            return true;
        }

        DickUtils.spawn(player, dick);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player && sender.hasPermission("kidaypisun.use")) {
            return List.copyOf(this.configValues.getDicks().keySet());
        }

        return List.of();
    }
}
