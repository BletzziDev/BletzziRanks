package com.bletzzi.ranks.registry;

import com.bletzzi.ranks.RanksPlugin;
import com.bletzzi.ranks.model.RankModel;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class RankRegistry {
    @Getter private static final LinkedHashMap<Integer, RankModel> ranks = new LinkedHashMap<>();

    public static void load(final FileConfiguration file) {
        /*
            Percorrer todas as seções dentro da seção ranks para pegar os dados de cada rank
         */
        file.getConfigurationSection("ranks").getKeys(false).forEach(key -> {
            /*
            /   Percorrer todas as seções dentro da ranks.RANK.prices para obter o id de cada economia
            /   e obter posteriormente o valor e salvar eles dentro do mapa "prices".
             */
            final HashMap<String, Double> prices = new HashMap<>();
            file.getConfigurationSection(String.format("ranks.%s.prices", key)).getKeys(false).forEach(currencyId -> {
                prices.put(currencyId, file.getDouble(String.format("ranks.%s.prices.%s", key, currencyId)));
            });

            /*
               Salvando os dados da config em uma classe RankModel
             */
            final RankModel rank = new RankModel(
                    file.getInt(String.format("ranks.%s.order", key)),
                    file.getString(String.format("ranks.%s.name", key)).replace("&", "§"),
                    prices,
                    file.getStringList(String.format("ranks.%s.commands", key))
            );

            // Salvando a classe do rank dentro do mapa de ranks
            ranks.put(rank.getOrder(), rank);
        });

        RanksPlugin.log(String.format("Foram registrados %s ranks na configuração!", ranks.size()));
    }
}