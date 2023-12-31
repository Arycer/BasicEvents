package me.arycer.basicevents.event;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class PlayerJoinEvent {
    private static final HashMap<JoinEventListener, Boolean> listeners = new HashMap<>();
    public static void register(JoinEventListener listener) {
        register(true, listener);
    }

    public static void register(Boolean runsOnMainThread, JoinEventListener listener) {
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

    public interface JoinEventListener {
        void onPlayerEvent(ServerPlayerEntity player);
    }
}