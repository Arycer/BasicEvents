package me.arycer.basicevents.event;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class PlayerRespawnEvent {
    private static final HashMap<RespawnEventListener, Boolean> listeners = new HashMap<>();
    public static void register(RespawnEventListener listener) {
        register(true, listener);
    }

    public static void register(Boolean runsOnMainThread, RespawnEventListener listener) {
        listeners.put(listener, runsOnMainThread);
    }

    public static void run(ServerPlayerEntity player) {
        listeners.forEach((listener, runsOnMainThread) -> {
            if (runsOnMainThread) {
                listener.onPlayerEvent(player);
            } else {
                new Thread(() -> listener.onPlayerEvent(player)).start();
            }
        });
    }

    public interface RespawnEventListener {
        void onPlayerEvent(ServerPlayerEntity player);
    }
}