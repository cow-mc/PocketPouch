package farm.the.pocketpouch.listeners;

import farm.the.pocketpouch.PocketPouch;
import org.bukkit.Achievement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().removeAchievement(Achievement.OPEN_INVENTORY);
        PocketPouch.getPouchManager().loadIfExists(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        PocketPouch.getPouchManager().saveAndUnload(e.getPlayer().getUniqueId());

        // save config to disk every so often; alternative: a repeating task
        long now = System.currentTimeMillis();
        if ((now - PocketPouch.plugin.getLastSave()) > PocketPouch.plugin.getSaveInterval()) {
            PocketPouch.plugin.setLastSave(now);
            PocketPouch.getPouchManager().saveAll();
        }
    }
}
