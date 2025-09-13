package com.bletzzi.ranks.database;

import com.bletzzi.ranks.RanksPlugin;
import com.bletzzi.ranks.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserRepository {
    private static final HashMap<UUID, User> users = new HashMap<>();
    private static Connection connection;

    public static void init(final Connection connection) throws SQLException {
        UserRepository.connection = connection;

        final PreparedStatement stmt = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `bletzziranks_users` ( `uuid` VARCHAR(36), `json` TEXT, PRIMARY KEY (`uuid`) );");
        stmt.execute();
        stmt.close();

        findAll().forEach(user -> users.put(user.getUuid(), user));
        RanksPlugin.log(String.format("§eForam carregados %s usuários do banco de dados.", users.size()));
    }

    public static User find(final UUID uuid) {
        return users.computeIfAbsent(uuid, (key) -> new User(uuid, 1));
    }

    public static void update(final User user) {
        try {
            final PreparedStatement stmt = connection.prepareStatement("REPLACE INTO `bletzziranks_users` ( `uuid`, `json` ) VALUES ( ?, ? );");
            stmt.setString(1, user.getUuid().toString());
            stmt.setString(2, RanksPlugin.getGson().toJson(user, User.class));
            stmt.execute();
            stmt.close();
        } catch(SQLException x) { x.printStackTrace(); }
    }

    public static List<User> findAll() {
        try {
            final PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `bletzziranks_users`;");
            if(!stmt.execute()) return Collections.emptyList();

            final List<User> userList = new ArrayList<>();
            final ResultSet result = stmt.getResultSet();
            while(result.next()) {
                userList.add(RanksPlugin.getGson().fromJson(
                        result.getString("json"),
                        User.class
                ));
            }

            result.close();
            stmt.close();

            return userList;
        } catch(SQLException x) {
            x.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static void saveAll() {
        users.values().forEach(UserRepository::update);
        RanksPlugin.log(String.format("§bForam salvos §f%s §busuários no banco de dados.", users.size()));
    }
}