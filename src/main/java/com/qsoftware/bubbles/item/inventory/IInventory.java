package com.qsoftware.bubbles.item.inventory;

import com.qsoftware.bubbles.item.Item;

public interface IInventory {
    int size();

    abstract Item[] getItems();

    void setItem(int index, Item item);

    void clear();

    Item getItem(int slot);

    void removeItem(int index);

    void tick();
}
