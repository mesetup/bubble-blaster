package com.qtech.bubbleblaster.item.inventory;

import com.qtech.bubbleblaster.item.Item;

public interface IInventory {
    int size();

    Item[] getItems();

    void setItem(int index, Item item);

    void clear();

    Item getItem(int slot);

    void removeItem(int index);

    void tick();
}
