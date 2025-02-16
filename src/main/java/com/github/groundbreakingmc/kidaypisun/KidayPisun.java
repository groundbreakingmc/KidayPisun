package com.github.groundbreakingmc.kidaypisun;

import com.github.groundbreakingmc.kidaypisun.commands.CommandManager;
import com.github.groundbreakingmc.kidaypisun.listeners.FallingBlackChangeListener;
import com.github.groundbreakingmc.kidaypisun.utils.config.ConfigValues;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;

@Getter
public final class KidayPisun extends JavaPlugin {

    public static final NamespacedKey NAMESPACED_KEY = new NamespacedKey("kidaypisun", "kidaypisun");
    public static final PersistentDataType<BlockData, BlockData> PERSISTENT_DATA_TYPE = createPersistentDataType();

    private final ConfigValues configValues = new ConfigValues(this);

    @Override
    public void onEnable() {
        this.configValues.setupValues();
        this.setupCommand();
        this.registerEvent();
    }

    private void setupCommand() {
        final PluginCommand pisunCommand = super.getCommand("pisun");
        final TabExecutor executor = new CommandManager(this);
        pisunCommand.setExecutor(executor);
        pisunCommand.setTabCompleter(executor);
    }

    private void registerEvent() {
        final Listener listener = new FallingBlackChangeListener(this);
        super.getServer().getPluginManager()
                .registerEvents(listener, this);
    }

    private static PersistentDataType<BlockData, BlockData> createPersistentDataType() {
        try {
            final Constructor<?> constructor = PersistentDataType.PrimitivePersistentDataType.class
                    .getDeclaredConstructor(Class.class);

            constructor.setAccessible(true);

            return (PersistentDataType<BlockData, BlockData>) constructor.newInstance(BlockData.class);
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
