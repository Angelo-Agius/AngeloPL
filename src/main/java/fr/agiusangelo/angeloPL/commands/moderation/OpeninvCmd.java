package fr.agiusangelo.angeloPL.commands.moderation;

import fr.agiusangelo.angeloPL.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OpeninvCmd implements CommandExecutor, Listener, TabCompleter {

    private final Main plugin;

    public OpeninvCmd(Main plugin) {
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
            player.sendMessage(ChatColor.RED + "Usage: /" + command.getName() + " <joueur> <item>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Ce joueur n'est pas en ligne.");
            return true;
        }

        Inventory targetInv = target.getInventory();
        Inventory viewInv = Bukkit.createInventory(null, 54, "Inventaire de " + target.getName());

        for (int i = 0; i < targetInv.getSize(); i++) {
            ItemStack item = targetInv.getItem(i);
            if (item != null) {
                viewInv.setItem(i, item.clone());
            }
        }

        for (int i = 0; i < 4; i++) {
            ItemStack item = target.getInventory().getArmorContents()[i];
            if (item != null) {
                viewInv.setItem(36 + i, item.clone());
            }
        }

        player.openInventory(viewInv);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (Player player: Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }

        return completions;
    }
}
