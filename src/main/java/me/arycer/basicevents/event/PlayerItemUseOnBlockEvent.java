package me.arycer.basicevents.event;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

import java.util.HashMap;

public class PlayerItemUseOnBlockEvent {
    private static final HashMap<ItemUseEventListener, Boolean> listeners = new HashMap<>();
    public static void register(ItemUseEventListener listener) {
        register(true, listener);
    }

    public static void register(Boolean runsOnMainThread, ItemUseEventListener listener) {
        listeners.put(listener, runsOnMainThread);
    }

    public static void run(ServerPlayerEntity player, Item item, ItemUsageContext context, Hand hand) {
        listeners.forEach((listener, runsOnMainThread) -> {
            if (runsOnMainThread) {
                listener.onPlayerEvent(player, item, context, hand);
            } else {
                new Thread(() -> listener.onPlayerEvent(player, item, context, hand)).start();
            }
        });
    }

    public interface ItemUseEventListener {
        void onPlayerEvent(ServerPlayerEntity player, Item item, ItemUsageContext context, Hand hand);
    }
}