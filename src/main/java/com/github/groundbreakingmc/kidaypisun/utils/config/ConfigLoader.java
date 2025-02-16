package com.github.groundbreakingmc.kidaypisun.utils.config;

import com.github.groundbreakingmc.kidaypisun.KidayPisun;
import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class ConfigLoader {

    private final KidayPisun plugin;

    public ConfigLoader(final KidayPisun plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration get(final String fileName) {
        return this.get(fileName, 0);
    }

    public FileConfiguration get(final String fileName, final double fileVersion) {
        return this.get(fileName, fileVersion, true);
    }

    public FileConfiguration get(final String fileName, final boolean setDefaults) {
        return this.get(fileName, 0, setDefaults);
    }

    public FileConfiguration get(final String fileName, final double fileVersion, final boolean setDefaults) {
        final File file = new File(this.plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            this.plugin.saveResource(fileName, false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (fileVersion != 0) {
            config = this.checkVersion(config, fileName, fileVersion);
        }
        if (setDefaults) {
            this.setDefaults(config, fileName);
        }

        return config;
    }

    private void setDefaults(final FileConfiguration config, final String fileName) {
        try (final InputStream defConfigStream = this.plugin.getResource(fileName)) {
            if (defConfigStream != null) {
                config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
            }
        } catch (final IOException ex) {
            this.plugin.getLogger().warning("Error loading default configuration: " + ex.getMessage());
        }
    }

    private FileConfiguration checkVersion(final FileConfiguration config, final String fileName, final double fileVersion) {
        final double configVersion = config.getDouble("settings.config-version", 0);

        if (configVersion != fileVersion) {
            createBackupAndUpdate(fileName);
            return get(fileName, fileVersion);
        }

        return config;
    }

    private void createBackupAndUpdate(final String fileName) {
        final File folder = this.plugin.getDataFolder();
        if (!folder.exists() && !folder.mkdirs()) {
            this.plugin.getLogger().warning("An error occurred while creating the backups folder!");
            return;
        }

        final File file = new File(folder, fileName);
        final int backupNumber = folder.listFiles().length;
        final File backupFile = new File(folder, fileName.substring(0, 4) + "_backup_" + backupNumber + ".yml");

        if (file.renameTo(backupFile)) {
            this.plugin.saveResource(fileName, true);
        } else {
            this.plugin.getLogger().warning("Your configuration file \"" + fileName + "\" is outdated, but creating a new one isn't possible.");
        }
    }
}
