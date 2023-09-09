package me.arycer.basicevents;

import lombok.Getter;
import me.arycer.basicevents.event.*;
import me.arycer.basicevents.util.TextUtil;
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
            Text welcome = TextUtil.parseText(String.format("&l&aWelcome &r&7to the server, &a&l%s&7!", player.getName().getString()));
            player.sendMessage(welcome, false);
        });

        PlayerItemUseEvent.register((player, item, hand) -> {
            Text itemUse = TextUtil.parseText(String.format("&l&7%s &r&7used &l&7%s&7!", player.getName().getString(), item.getName().getString()));
            player.sendMessage(itemUse, false);
        });

        PlayerItemUseOnEntityEvent.register((player, target, item, hand) -> {
            Text itemUse = TextUtil.parseText(String.format("&l&7%s &r&7used &l&7%s &r&7on &l&7%s&7!", player.getName().getString(), item.getName().getString(), target.getName().getString()));
            player.sendMessage(itemUse, false);
        });

        PlayerAttackEvent.register((player, target) -> {
            Text attack = TextUtil.parseText(String.format("&l&7%s &r&7attacked &l&7%s&7!", player.getName().getString(), target.getName().getString()));
            player.sendMessage(attack, false);
        });

        PlayerItemUseOnBlockEvent.register((player, item, context, hand) -> {
            Text itemUse = TextUtil.parseText(String.format("&l&7%s &r&7used &l&7%s &r&7on &l&7%s&7!", player.getName().getString(), item.getName().getString(), context.getBlockPos().toString()));
            player.sendMessage(itemUse, false);
        });
    }

    public static BasicEvents getInstance() {
        return INSTANCE;
    }
}
