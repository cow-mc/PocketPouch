package farm.the.pocketpouch.listeners;

import farm.the.pocketpouch.PocketPouch;
import org.bukkit.Achievement;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

public class InventoryListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getType() == InventoryType.CRAFTING && PocketPouch.getPouchManager().hasInventoryOpen(e.getPlayer().getUniqueId())) {
            // InvCloseEvent would trigger onPlayerDisconnect, whether or not the player inventory was open before
            // without this additional check the server wouldn't know if player inventory is open or not
            // thus it would overwrite the extra inventory with air
            PocketPouch.getPouchManager().setInventoryOpen(e.getPlayer().getUniqueId(), false);

            PocketPouch.getPouchManager().store(e.getPlayer().getUniqueId(), e.getInventory().getContents());

            // prevent items in 2x2 crafting field to be dropped
            e.getInventory().clear();
        }
    }

    @EventHandler
    public void onPlayerInventoryOpenEvent(PlayerAchievementAwardedEvent e) {
        if (e.getAchievement().equals(Achievement.OPEN_INVENTORY)) {
            e.setCancelled(true);

            // only update inventory from non-creative players
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                PocketPouch.getPouchManager().setInventoryOpen(e.getPlayer().getUniqueId(), true);
                PocketPouch.getPouchManager().updatePlayerInv(e.getPlayer().getUniqueId(), e.getPlayer().getOpenInventory().getTopInventory());
            }
        }
    }
}
