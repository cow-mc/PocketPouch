package farm.the.pocketpouch;

import farm.the.pocketpouch.listeners.InventoryListener;
import farm.the.pocketpouch.listeners.JoinLeaveListener;
import farm.the.pocketpouch.listeners.PlayerDeathListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class PocketPouch extends JavaPlugin {
    public static PocketPouch plugin;
    public static Logger log;
    private PouchManager pouchManager;

    @Override
    public void onEnable() {
        plugin = this;
        log = getLogger();
        loadConfig();
        pouchManager = new PouchManager();

        // TODO onPlayerQuit move extraInv items into main inventory if space is available
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
    }

    @Override
    public void onDisable() {
        pouchManager.saveAll();
    }

    private void loadConfig() {
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            log.info("config.yml not found, creating!");
            saveDefaultConfig();
        } else {
            log.info("config.yml found, loading!");
        }
    }

    public static PouchManager getPouchManager() {
        return plugin.pouchManager;
    }
}
