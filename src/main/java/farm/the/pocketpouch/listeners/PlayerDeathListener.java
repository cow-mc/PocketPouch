package farm.the.pocketpouch.listeners;

import farm.the.pocketpouch.PocketPouch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class PlayerDeathListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!e.getKeepInventory()) {
            // 'keep inventory' is off, drop items from extra inventory
            List<ItemStack> drops = e.getDrops();
            ItemStack[] extraInv = PocketPouch.getPouchManager().getAndClear(e.getEntity().getUniqueId());
            if (extraInv != null) {
                drops.addAll(Arrays.asList(extraInv));
            }
        }
    }
}
