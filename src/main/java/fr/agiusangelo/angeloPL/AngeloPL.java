package fr.agiusangelo.angeloPL;

import org.bukkit.plugin.java.JavaPlugin;

public final class AngeloPL extends JavaPlugin {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\033[0m";

    @Override
    public void onEnable() {
        getLogger().info(ANSI_GREEN + "AngeloPL has been enabled" + ANSI_RESET);
    }

    @Override
    public void onDisable() {
        getLogger().info(ANSI_GREEN + "AngeloPL has been enabled" + ANSI_RESET);
    }
}
