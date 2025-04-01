package fr.agiusangelo.angeloPL;

import fr.agiusangelo.angeloPL.commands.EasterCmd;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\033[0m";

    @Override
    public void onEnable() {
        getLogger().info(ANSI_GREEN + "AngeloPL has been enabled" + ANSI_RESET);
        getCommand("easter").setExecutor(new EasterCmd(this));
    }

    @Override
    public void onDisable() {
        getLogger().info(ANSI_GREEN + "AngeloPL has been enabled" + ANSI_RESET);
    }
}
