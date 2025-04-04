package fr.agiusangelo.angeloPL.commands.moderation;

import fr.agiusangelo.angeloPL.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Freeze implements CommandExecutor, Listener, TabCompleter {

    private final Main plugin;
    private static final List<Player> frozenPlayers = new ArrayList<>();

    public Freeze(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Seuls les joueurs peuvent exécuter cette commande !");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /" + command.getName() + " <joueur>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Ce joueur n'est pas en ligne.");
            return true;
        }

        if (frozenPlayers.contains(target)) {
            frozenPlayers.remove(target);
            player.sendMessage(ChatColor.GREEN + "Vous avez unfreeze " + target.getDisplayName());
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
            target.sendTitle(
                    ChatColor.translateAlternateColorCodes('&', "&a&lVous avez été unfreeze !"),
                    ChatColor.translateAlternateColorCodes('&', "&eTu peux désormais bouger !")
            );
            target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
            target.removePotionEffect(PotionEffectType.BLINDNESS);

        } else {
            frozenPlayers.add(target);
            player.sendMessage(ChatColor.GREEN + "Vous avez freeze " + target.getDisplayName());
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);

            target.sendTitle(
                    ChatColor.translateAlternateColorCodes('&', "&c&lVous avez été freeze !"),
                    ChatColor.translateAlternateColorCodes('&', "&eTu ne peux désormais plus bouger !")
            );
            target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_IMITATE_ZOMBIE, 1.0f, 1.0f);
            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false, false));
        }

        return true;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (frozenPlayers.contains(player)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Vous êtes freeze par un staff"));
            event.setCancelled(true);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                completions.add(p.getName());
            }
        }

        return completions;
    }
}
