package fr.agiusangelo.angeloPL.commands.fun;

import fr.agiusangelo.angeloPL.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasterCmd implements CommandExecutor, TabCompleter {

    private final Main plugin;
    private final Map<String, ItemStack> customItems = new HashMap<>();

    public EasterCmd(Main plugin) {
        this.plugin = plugin;
        loadCustomItems();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String msg, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + command.getName() + " <joueur> <item>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Joueur introuvable !");
            return true;
        }

        String itemName = args[1].toLowerCase();
        if (!customItems.containsKey(itemName)) {
            sender.sendMessage(ChatColor.RED + "Item introuvable ! Essayez: calvace");
            return true;
        }

        ItemStack item = customItems.get(itemName).clone();
        target.getInventory().addItem(item);
        sender.sendMessage(ChatColor.GREEN + "Vous avez donné " + itemName + " à " + target.getName() + " !");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        } else if (args.length == 2) {
            completions.addAll(customItems.keySet());
        }

        return completions;
    }

    private void loadCustomItems() {
        customItems.put("calvace", createCalvaceEgg());
    }

    private ItemStack createCalvaceEgg() {
        ItemStack item = new ItemStack(Material.EGG);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§a§lLa calvace de §e§k K §r§9§l§nCrotale §e§k K");
            meta.setLore(List.of("§3Bien lisse bien montante"));
            meta.addEnchant(Enchantment.KNOCKBACK, 255, true);
            item.setItemMeta(meta);
        }
        return item;
    }
}
