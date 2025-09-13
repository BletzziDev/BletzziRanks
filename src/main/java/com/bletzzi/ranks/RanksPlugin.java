package com.bletzzi.ranks;

import com.bletzzi.ranks.command.RankCommand;
import com.bletzzi.ranks.database.Database;
import com.bletzzi.ranks.database.UserRepository;
import com.bletzzi.ranks.registry.RankRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.saiintbrisson.bukkit.command.BukkitFrame;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class RanksPlugin extends JavaPlugin {
    @Getter private static RanksPlugin plugin;
    @Getter private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final BukkitFrame bukkitFrame = new BukkitFrame(this);

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        RankRegistry.load(getConfig());
        initDatabase();

        bukkitFrame.registerCommands(new RankCommand());

        Bukkit.getScheduler().runTaskTimer(this, UserRepository::saveAll, 6000L, 6000L);

        log("§aPlugin ligado com sucesso!");
    }

    @Override
    public void onDisable() {
        UserRepository.saveAll();
        log("§cPlugin desligado com sucesso!");
    }

    public void initDatabase() {
        try {
            final FileConfiguration file = getConfig();
            UserRepository.init(new Database(
                    file.getString("database.host"),
                    file.getString("database.database"),
                    file.getString("database.user"),
                    file.getString("database.password")
            ).getConnection());
        } catch(SQLException x) { x.printStackTrace(); }
    }

    public static void log(final String message) {
        Bukkit.getConsoleSender().sendMessage("§b[BletzziRanks] §f" + message);
    }
}
