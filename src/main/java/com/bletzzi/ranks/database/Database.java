package com.bletzzi.ranks.database;

import com.bletzzi.ranks.RanksPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    @Getter private final Connection connection;

    public Database(String host, String database, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?autoReconnect=true&keepAlive=true&useSSL=false", host, database), user, password);
        Bukkit.getScheduler().runTaskTimer(RanksPlugin.getPlugin(), this::keepAlive, 6000L, 6000L);
    }

    private void keepAlive() {
        try { connection.prepareStatement("SELECT 1;").execute(); } catch(Exception x) { x.printStackTrace(); }
    }
}