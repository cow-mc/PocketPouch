package farm.the.pocketpouch;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PouchManager {
    private static final int CRAFTING_SLOT_MIN = 1;
    private static final int CRAFTING_SLOT_MAX = 5;
    private static final ItemStack AIR = new ItemStack(Material.AIR);

    private Map<UUID, ItemStack[]> extraInventory;
    private Set<UUID> hasInventoryOpen;

    PouchManager() {
        this.extraInventory = new HashMap<>();
        this.hasInventoryOpen = new HashSet<>();
    }

    public void updatePlayerInv(UUID player, Inventory inventory) {
        ItemStack[] extraInv = extraInventory.get(player);
        if (extraInv != null) {
            for (int slot = CRAFTING_SLOT_MIN; slot < CRAFTING_SLOT_MAX; slot++) {
                inventory.setItem(slot, extraInv[slot - 1]);
            }
        }
    }

    public ItemStack[] getAndClear(UUID player) {
        PocketPouch.plugin.getConfig().set(player.toString(), null);
        return extraInventory.remove(player);
    }

    public void store(UUID player, ItemStack[] craftingSlots) {
        ItemStack[] extraInv = new ItemStack[4];

        for (int slot = CRAFTING_SLOT_MIN; slot < CRAFTING_SLOT_MAX; slot++) {
            if (craftingSlots[slot] == null || craftingSlots[slot].getType() == Material.AIR) {
                craftingSlots[slot] = AIR;
            }
            extraInv[slot - CRAFTING_SLOT_MIN] = craftingSlots[slot].clone();
        }
        extraInventory.put(player, extraInv);
    }

    public boolean hasInventoryOpen(UUID player) {
        return hasInventoryOpen.contains(player);
    }

    public void setInventoryOpen(UUID player, boolean isOpen) {
        if (isOpen) {
            hasInventoryOpen.add(player);
        } else {
            hasInventoryOpen.remove(player);
        }
    }

    public void loadIfExists(UUID player) {
        ItemStack[] extraInv = new ItemStack[4];
        int slot = 0;

        List<?> items = PocketPouch.plugin.getConfig().getList(player.toString());
        if (items != null) {
            for (Object item : items) {
                if (item instanceof ItemStack) {
                    extraInv[slot] = ((ItemStack) item);
                    slot++;
                    if (slot > 4) {
                        break;
                    }
                } else {
                    PocketPouch.log.severe("Found invalid config entry with key '" + player + "': " + item);
                }
            }
        }
        extraInventory.put(player, extraInv);
    }

    public void saveAndUnload(UUID player) {
        ItemStack[] extraInv = extraInventory.remove(player);
        if (extraInv != null) {
            if (!isAllAir(extraInv)) {
                PocketPouch.plugin.getConfig().set(player.toString(), Arrays.asList(extraInv));
            } else {
                // 2x2 crafting slots contain only air; clear entry from file
                PocketPouch.plugin.getConfig().set(player.toString(), null);
            }
        }
    }

    private boolean isAllAir(ItemStack[] extraInv) {
        for (ItemStack item : extraInv) {
            if (item != null && item.getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }

    public void saveAll() {
        for (Map.Entry<UUID, ItemStack[]> entry : extraInventory.entrySet()) {
            FileConfiguration config = PocketPouch.plugin.getConfig();
            String uuid = entry.getKey().toString();
            if (!isAllAir(entry.getValue())) {
                config.set(uuid, Arrays.asList(entry.getValue()));
            } else {
                // 2x2 crafting slots contain only air; clear entry from file
                config.set(uuid, null);
            }
        }
        PocketPouch.plugin.saveConfig();
    }
}
