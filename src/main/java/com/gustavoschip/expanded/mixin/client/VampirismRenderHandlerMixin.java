package com.gustavoschip.expanded.mixin.client;

import com.gustavoschip.expanded.attachment.ModAttachments;
import de.teamlapen.vampirism.entity.player.VampirismPlayerAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(targets = "de.teamlapen.vampirism.client.renderer.RenderHandler", remap = false)
public abstract class VampirismRenderHandlerMixin {
    @Unique
    private Entity expanded$currentBloodVisionEntity;

    @Inject(method = "onRenderLivingPost", at = @At("HEAD"))
    private void expanded$captureBloodVisionEntity(RenderLivingEvent.Post<?, ?> event, CallbackInfo ci) {
        this.expanded$currentBloodVisionEntity = event.getEntity();
    }

    @ModifyArgs(
            method = "onRenderLivingPost",
            at = @At(
                    value = "INVOKE",
                    target = "Lde/teamlapen/vampirism/client/renderer/RenderHandler$OutlineBuffer;setColor(IIII)V",
                    remap = false
            )
    )
    private void expanded$setGreenBloodVisionColorForPoisonousPlayers(Args args) {
        Entity entity = this.expanded$currentBloodVisionEntity;
        if (!(entity instanceof Player player) || !player.getData(ModAttachments.POISONOUS_BLOOD_ATTACHMENT)) {
            return;
        }

        Player viewer = Minecraft.getInstance().player;
        if (viewer == null || !VampirismPlayerAttributes.get(viewer).getVampSpecial().blood_vision_garlic) {
            return;
        }

        args.set(0, 0x07);
        args.set(1, 0xE0);
        args.set(2, 0x07);
    }

    @Inject(method = "onRenderLivingPost", at = @At("RETURN"))
    private void expanded$clearBloodVisionEntity(RenderLivingEvent.Post<?, ?> event, CallbackInfo ci) {
        this.expanded$currentBloodVisionEntity = null;
    }
}
