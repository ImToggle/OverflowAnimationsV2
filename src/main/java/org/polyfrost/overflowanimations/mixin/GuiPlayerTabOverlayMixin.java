package org.polyfrost.overflowanimations.mixin;

import org.polyfrost.overflowanimations.config.OldAnimationsSettings;
import org.polyfrost.overflowanimations.hooks.TabOverlayHook;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiPlayerTabOverlay.class, priority = 999)
public abstract class GuiPlayerTabOverlayMixin {

    @Shadow
    @Final
    private static Ordering<NetworkPlayerInfo> field_175252_a;

    @Inject(method = "renderPlayerlist", at = @At("HEAD"), cancellable = true)
    public void overflowAnimations$renderOldTab(int width, Scoreboard scoreboardIn, ScoreObjective var37, CallbackInfo ci) {
        if (OldAnimationsSettings.INSTANCE.tabMode == 0 && OldAnimationsSettings.INSTANCE.enabled) {
            ci.cancel();
            TabOverlayHook.renderOldTab(((GuiPlayerTabOverlay) (Object) this), var37, field_175252_a);
        }
    }

    @ModifyVariable(method = "renderPlayerlist", at = @At("STORE"), index = 11)
    private boolean overflowAnimations$disablePlayerHead(boolean original) {
        return (OldAnimationsSettings.INSTANCE.tabMode != 2 || !OldAnimationsSettings.INSTANCE.enabled) && original;
    }
}
