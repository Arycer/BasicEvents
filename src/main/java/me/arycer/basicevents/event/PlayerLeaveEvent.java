package me.arycer.basicevents.event;

import me.arycer.basicevents.BasicEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class PlayerLeaveEvent {
    private static final HashMap<LeaveEventListener, Boolean> listeners = new HashMap<>();
    public static void register(LeaveEventListener listener) {
        register(true, listener);
    }

    public static void register(Boolean runsOnMainThread, LeaveEventListener listener) {
        listeners.put(listener, runsOnMainThread);

        BasicEvents.getInstance().getLogger().info("Registered new PlayerLeaveEvent listener: " + listener.getClass().getName());
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

    public interface LeaveEventListener {
        void onPlayerEvent(ServerPlayerEntity player);
    }
}