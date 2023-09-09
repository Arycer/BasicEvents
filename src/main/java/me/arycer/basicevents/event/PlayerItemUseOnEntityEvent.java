package me.arycer.basicevents.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

import java.util.HashMap;

public class PlayerItemUseOnEntityEvent {
    private static final HashMap<ItemUseEventListener, Boolean> listeners = new HashMap<>();
    public static void register(ItemUseEventListener listener) {
        register(true, listener);
    }

    public static void register(Boolean runsOnMainThread, ItemUseEventListener listener) {
        listeners.put(listener, runsOnMainThread);
    }

    public static void run(ServerPlayerEntity player, LivingEntity target, Item item, Hand hand) {
        listeners.forEach((listener, runsOnMainThread) -> {
            if (runsOnMainThread) {
                listener.onPlayerEvent(player, target, item, hand);
            } else {
                new Thread(() -> listener.onPlayerEvent(player, target, item, hand)).start();
            }
        });
    }

    public interface ItemUseEventListener {
        void onPlayerEvent(ServerPlayerEntity player, LivingEntity target, Item item, Hand hand);
    }
}