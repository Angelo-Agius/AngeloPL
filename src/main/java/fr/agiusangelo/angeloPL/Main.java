package fr.agiusangelo.angeloPL;

import fr.agiusangelo.angeloPL.commands.fun.EasterCmd;
import fr.agiusangelo.angeloPL.commands.moderation.Freeze;
import fr.agiusangelo.angeloPL.commands.moderation.OpeninvCmd;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\033[0m";

    @Override
    public void onEnable() {
        getLogger().info(ANSI_GREEN + "AngeloPL has been enabled" + ANSI_RESET);

        getCommand("easter").setExecutor(new EasterCmd(this));

        getCommand("openinv").setExecutor(new OpeninvCmd(this));
        getCommand("freeze").setExecutor(new Freeze(this));

        getServer().getPluginManager().registerEvents(new OpeninvCmd(this),this);
        getServer().getPluginManager().registerEvents(new Freeze(this), this);
        getServer().getPluginManager().registerEvents(new EasterCmd(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info(ANSI_GREEN + "AngeloPL has been enabled" + ANSI_RESET);
    }
}
