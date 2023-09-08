package me.arycer.basicevents.event;

import me.arycer.basicevents.BasicEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class PlayerMoveEvent {
    private static final HashMap<MoveEventListener, Boolean> listeners = new HashMap<>();
    public static void register(MoveEventListener listener) {
        register(true, listener);
    }

    public static void register(Boolean runsOnMainThread, MoveEventListener listener) {
        listeners.put(listener, runsOnMainThread);

        BasicEvents.getInstance().getLogger().info("Registered new PlayerMoveEvent listener: " + listener.getClass().getName());
    }

    public static void run(ServerPlayerEntity player, Vec3d lastPos) {
        listeners.forEach((listener, runsOnMainThread) -> {
            if (runsOnMainThread) {
                listener.onPlayerEvent(player, lastPos);
            } else {
                new Thread(() -> listener.onPlayerEvent(player, lastPos)).start();
            }
        });
    }

    public interface MoveEventListener {
        void onPlayerEvent(ServerPlayerEntity player, Vec3d lastPos);
    }
}
