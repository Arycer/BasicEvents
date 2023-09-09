package me.arycer.basicevents.event;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class PlayerTeleportEvent {
    private static final HashMap<TeleportEventListener, Boolean> listeners = new HashMap<>();
    public static void register(TeleportEventListener listener) {
        register(true, listener);
    }

    public static void register(Boolean runsOnMainThread, TeleportEventListener listener) {
        listeners.put(listener, runsOnMainThread);
    }

    public static void run(ServerPlayerEntity player, Vec3d pos, Vec2f rotation) {
        listeners.forEach((listener, runsOnMainThread) -> {
            if (runsOnMainThread) {
                listener.onPlayerEvent(player, pos, rotation);
            } else {
                new Thread(() -> listener.onPlayerEvent(player, pos, rotation)).start();
            }
        });
    }

    public interface TeleportEventListener {
        void onPlayerEvent(ServerPlayerEntity player, Vec3d pos, Vec2f rotation);
    }
}
