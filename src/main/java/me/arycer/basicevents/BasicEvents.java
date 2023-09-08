package me.arycer.basicevents;

import lombok.Getter;
import me.arycer.basicevents.event.PlayerJoinEvent;
import me.arycer.basicevents.event.PlayerLeaveEvent;
import me.arycer.basicevents.event.PlayerMoveEvent;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BasicEvents implements DedicatedServerModInitializer {
    public static final String MOD_ID = "BasicEvents";

    @Getter
    private final Logger logger = LogManager.getLogger(MOD_ID);

    private static BasicEvents INSTANCE;

    public List<ServerPlayerEntity> connectedPlayers = new ArrayList<>();

    @Override
    public void onInitializeServer() {
        INSTANCE = this;

        PlayerJoinEvent.register(player -> {
            player.sendMessage(Text.of(String.format("Welcome to the server, %s!", player.getName().getString())), false);
        });

        PlayerLeaveEvent.register(player -> {
            logger.info(String.format("%s left the server!", player.getName().getString()));
        });

        PlayerMoveEvent.register((player, lastPos) -> {
            logger.info(String.format("%s moved from %s to %s", player.getName().getString(), lastPos, player.getPos()));
        });
    }

    public static BasicEvents getInstance() {
        return INSTANCE;
    }
}
