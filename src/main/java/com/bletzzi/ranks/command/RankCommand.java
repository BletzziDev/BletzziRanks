package com.bletzzi.ranks.command;

import com.bletzzi.ranks.database.UserRepository;
import com.bletzzi.ranks.model.RankModel;
import com.bletzzi.ranks.model.User;
import com.bletzzi.ranks.registry.RankRegistry;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand {
    @Command(name = "rank")
    public void root(final Context<CommandSender> context) {
        final CommandSender sender = context.getSender();
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar o /rank");
            return;
        }

        final User user = UserRepository.find(((Player) sender).getUniqueId());
        final RankModel rank = RankRegistry.getRanks().get(user.getRank());
        sender.sendMessage("§aO seu rank é: " + rank.getName());
    }

    @Command(name = "rank.set", permission = "ranks.set")
    public void set(final Context<CommandSender> context, final Player target, final int order) {
        final User user = UserRepository.find(target.getUniqueId());
        user.setRank(order);
    }
}