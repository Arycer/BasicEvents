package me.arycer.basicevents.mixin;

import me.arycer.basicevents.event.PlayerItemUseEvent;
import me.arycer.basicevents.event.PlayerItemUseOnBlockEvent;
import me.arycer.basicevents.event.PlayerItemUseOnEntityEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class MixinItem {
    @Unique
    private final Item item = (Item) (Object) this;

    @Inject(method = "use", at = @At("RETURN"))
    private void onItemUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!(user instanceof ServerPlayerEntity player)) return;
        PlayerItemUseEvent.run(player, item, hand);
    }

    @Inject(method = "useOnEntity", at = @At("RETURN"))
    private void onItemUseOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!(user instanceof ServerPlayerEntity player)) return;
        PlayerItemUseOnEntityEvent.run(player, entity, item, hand);
    }

    @Inject(method = "useOnBlock", at = @At("RETURN"))
    private void onItemUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        var user = context.getPlayer();
        var hand = context.getHand();

        if (!(user instanceof ServerPlayerEntity player)) return;
        PlayerItemUseOnBlockEvent.run(player, item, context, hand);
    }
}
