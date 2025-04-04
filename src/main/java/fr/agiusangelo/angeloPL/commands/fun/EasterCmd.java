package fr.agiusangelo.angeloPL.commands.fun;

import fr.agiusangelo.angeloPL.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasterCmd implements CommandExecutor, TabCompleter, Listener {

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
            sender.sendMessage(ChatColor.RED + "Item introuvable ! Essayez: calvace, fredonEgg");
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
        customItems.put("fredon", createSpawnFredonEgg());
    }

    private ItemStack createCalvaceEgg() {
        ItemStack item = new ItemStack(Material.EGG);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§a§lLa calvace de §e§kK §r§9§l§nCrotale§r §e§kK");
            meta.setLore(List.of("§3Bien lisse bien montante"));
            meta.addEnchant(Enchantment.KNOCKBACK, 255, true);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createSpawnFredonEgg() {
        ItemStack item = new ItemStack(Material.VILLAGER_SPAWN_EGG);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§a§lOeuf d'apparition de §6§kK §r§5§l§n§mMr Fredon§r §6§kK");
            meta.setLore(List.of("§d§oFait apparaître §5§l§n§mMr Fredon§r §6§kK"));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onPlayerUseFredonEgg(PlayerInteractEvent event) {
        if (event.getItem() == null || event.getItem().getType() != Material.VILLAGER_SPAWN_EGG) return;
        ItemMeta meta = event.getItem().getItemMeta();
        if (meta == null || !meta.getDisplayName().contains("Mr Fredon")) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        Location loc = player.getTargetBlockExact(5) != null ? player.getTargetBlockExact(5).getLocation().add(0.5, 1, 0.5) : player.getLocation();

        Villager mobNbt = (Villager) player.getWorld().spawnEntity(loc, EntityType.VILLAGER);
        mobNbt.setCustomName("§6§kK §r§5§l§n§mMr Fredon§r §6§kK");
        mobNbt.setCustomNameVisible(true);
        mobNbt.setProfession(Villager.Profession.MASON);
        mobNbt.setVillagerType(Villager.Type.PLAINS);
        mobNbt.setVillagerLevel(5);

        ArmorStand mobArmor = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        mobArmor.setInvisible(true);
        mobArmor.setInvulnerable(true);
        mobArmor.setBasePlate(false);
        mobArmor.setGravity(false);
        mobArmor.setSmall(false);

        ItemStack chstplt = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chstpltMeta = (LeatherArmorMeta) chstplt.getItemMeta();
        chstpltMeta.setColor(Color.fromRGB(0, 0, 255));
        chstplt.setItemMeta(chstpltMeta);

        ItemStack lgng = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta lgngMeta = (LeatherArmorMeta) lgng.getItemMeta();
        lgngMeta.setColor(Color.fromRGB(0, 0, 255));
        lgng.setItemMeta(lgngMeta);

        mobArmor.getEquipment().setChestplate(chstplt);
        mobArmor.getEquipment().setLeggings(lgng);
        mobArmor.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));

        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if (mobNbt.isDead()) {
                    mobArmor.remove();
                    return;
                }
                mobArmor.teleport(mobNbt.getLocation());
            }
        }, 0L, 1L);
    }
}
