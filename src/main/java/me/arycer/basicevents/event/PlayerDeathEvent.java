package me.arycer.basicevents.event;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class PlayerDeathEvent {
    private static final HashMap<DeathEventListener, Boolean> listeners = new HashMap<>();
    public static void register(DeathEventListener listener) {
        register(true, listener);
    }

    public static void register(Boolean runsOnMainThread, DeathEventListener listener) {
        listeners.put(listener, runsOnMainThread);
    }

    public static void run(ServerPlayerEntity player, DamageSource source) {
        listeners.forEach((listener, runsOnMainThread) -> {
            if (runsOnMainThread) {
                listener.onPlayerEvent(player, source);
            } else {
                new Thread(() -> listener.onPlayerEvent(player, source)).start();
            }
        });
    }

    public interface DeathEventListener {
        void onPlayerEvent(ServerPlayerEntity player, DamageSource source);
    }
}
