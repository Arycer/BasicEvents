package me.arycer.basicevents.mixin;

import me.arycer.basicevents.BasicEvents;
import me.arycer.basicevents.event.PlayerJoinEvent;
import me.arycer.basicevents.event.PlayerLeaveEvent;
import me.arycer.basicevents.event.PlayerMoveEvent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class MixinPlayerEntity {
    @Unique
    private final ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

    @Inject(method = "onSpawn", at = @At("RETURN"))
    private void onPlayerJoin(CallbackInfo info) {
        var connectedPlayers = BasicEvents.getInstance().connectedPlayers;
        if (!connectedPlayers.contains(player)) {
            connectedPlayers.add(player);
            PlayerJoinEvent.run(player);
        }
    }

    @Inject(method = "onDisconnect", at = @At("RETURN"))
    private void onPlayerLeave(CallbackInfo info) {
        var connectedPlayers = BasicEvents.getInstance().connectedPlayers;
        connectedPlayers.remove(player);
        PlayerLeaveEvent.run(player);
    }

    @Unique
    private Vec3d lastPos = null;
    @Inject(method = "tick", at = @At("RETURN"))
    private void onPlayerMove(CallbackInfo info) {
        if (lastPos == null) {
            lastPos = player.getPos();
        } else if (lastPos != player.getPos()) {
            PlayerMoveEvent.run(player, lastPos);
            lastPos = player.getPos();
        }
    }
}
