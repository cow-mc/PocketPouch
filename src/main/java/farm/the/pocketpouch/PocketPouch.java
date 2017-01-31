package farm.the.pocketpouch;

import farm.the.pocketpouch.listeners.InventoryListener;
import farm.the.pocketpouch.listeners.JoinLeaveListener;
import farm.the.pocketpouch.listeners.PlayerDeathListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PocketPouch extends JavaPlugin {
    public static PocketPouch plugin;
    public static Logger log;
    private long saveInterval;
    private long lastSave;
    private PouchManager pouchManager;

    @Override
    public void onEnable() {
        plugin = this;
        log = getLogger();
        loadConfig();
        pouchManager = new PouchManager();
        // minimum save interval: 5 minutes
        saveInterval = TimeUnit.MINUTES.toMillis(Math.max(5, PocketPouch.plugin.getConfig().getInt("save-interval")));
        lastSave = System.currentTimeMillis();

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

    public long getSaveInterval() {
        return saveInterval;
    }

    public long getLastSave() {
        return lastSave;
    }

    public void setLastSave(long lastSave) {
        this.lastSave = lastSave;
    }

    public static PouchManager getPouchManager() {
        return plugin.pouchManager;
    }
}
