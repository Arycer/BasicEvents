package me.arycer.basicevents.event;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class PlayerAttackEvent {
    private static final HashMap<AttackEventListener, Boolean> listeners = new HashMap<>();
    public static void register(AttackEventListener listener) {
        register(true, listener);
    }

    public static void register(Boolean runsOnMainThread, AttackEventListener listener) {
        listeners.put(listener, runsOnMainThread);
    }

    public static void run(ServerPlayerEntity player, Entity target) {
        listeners.forEach((listener, runsOnMainThread) -> {
            if (runsOnMainThread) {
                listener.onPlayerEvent(player, target);
            } else {
                new Thread(() -> listener.onPlayerEvent(player, target)).start();
            }
        });
    }

    public interface AttackEventListener {
        void onPlayerEvent(ServerPlayerEntity player, Entity target);
    }
}