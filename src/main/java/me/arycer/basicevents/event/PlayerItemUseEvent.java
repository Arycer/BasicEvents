package me.arycer.basicevents.event;

import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

import java.util.HashMap;

public class PlayerItemUseEvent {
    private static final HashMap<ItemUseEventListener, Boolean> listeners = new HashMap<>();
    public static void register(ItemUseEventListener listener) {
        register(true, listener);
    }

    public static void register(Boolean runsOnMainThread, ItemUseEventListener listener) {
        listeners.put(listener, runsOnMainThread);
    }

    public static void run(ServerPlayerEntity player, Item item, Hand hand) {
        listeners.forEach((listener, runsOnMainThread) -> {
            if (runsOnMainThread) {
                listener.onPlayerEvent(player, item, hand);
            } else {
                new Thread(() -> listener.onPlayerEvent(player, item, hand)).start();
            }
        });
    }

    public interface ItemUseEventListener {
        void onPlayerEvent(ServerPlayerEntity player, Item item, Hand hand);
    }
}