package com.qsoftware.bubbles.item;

import com.qsoftware.bubbles.item.inventory.PlayerInventory;

public class AmmoItem extends ItemType {
    public AmmoItem() {

    }

    public void inventoryTick(PlayerInventory inventory) {
        inventory.getPlayer();
    }
}
