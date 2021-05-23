package com.qtech.bubbleblaster.item.inventory;

import com.qtech.bubbleblaster.entity.player.PlayerEntity;
import com.qtech.bubbleblaster.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerInventory extends AbstractInventory {
    private final PlayerEntity player;
    private final List<PlayerEntity> watchers = new ArrayList<>();
    private Item[] items = new Item[size()];

    public PlayerInventory(PlayerEntity player) {
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    @SuppressWarnings("EmptyMethod")
    public void openInventoryTo() {

    }

    public List<PlayerEntity> getWatchers() {
        return Collections.unmodifiableList(watchers);
    }

    @Override
    public int size() {
        return 18;
    }

    @Override
    public Item[] getItems() {
        return this.items;
    }

    @Override
    public void setItem(int slot, Item item) {
        this.items[slot] = item;
    }

    @Override
    public void clear() {
        this.items = new Item[size()];
    }

    @Override
    public Item getItem(int slot) {
        return this.items[slot];
    }

    @Override
    public void removeItem(int slot) {
        this.items[slot] = null;
    }

    @Override
    public void tick() {
        for (Item item : this.items) {
            if (item != null) {
                item.onEntityTick(this.player);
                item.tick();
            }
        }
    }
}
