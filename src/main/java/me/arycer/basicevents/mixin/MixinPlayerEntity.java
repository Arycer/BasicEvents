package me.arycer.basicevents.mixin;

import me.arycer.basicevents.BasicEvents;
import me.arycer.basicevents.event.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinPlayerEntity {
    @Unique
    private final ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

    @Inject(method = "onSpawn", at = @At("RETURN"))
    private void onPlayerJoin(CallbackInfo info) {
        var connectedPlayers = BasicEvents.getInstance().connectedPlayers;
        if (!connectedPlayers.contains(player)) {
            connectedPlayers.add(player);
            PlayerJoinEvent.run(player);
        } else {
            PlayerRespawnEvent.run(player);
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

    @Inject(method = "onDeath", at = @At("RETURN"))
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        PlayerDeathEvent.run(player, damageSource);
        lastPos = null;
    }

    @Inject(method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDLjava/util/Set;FF)Z", at = @At("RETURN"))
    private void onPlayerTeleport(ServerWorld world, double destX, double destY, double destZ, Set<PositionFlag> flags, float yaw, float pitch, CallbackInfoReturnable<Boolean> cir) {
        PlayerTeleportEvent.run(player, new Vec3d(destX, destY, destZ), new Vec2f(yaw, pitch));
    }

    @Inject(method = "attack", at = @At("HEAD"))
    private void onPlayerAttack(Entity target, CallbackInfo ci) {
        PlayerAttackEvent.run(player, target);
    }
}
