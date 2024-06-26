package org.polyfrost.overflowanimations.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import org.polyfrost.overflowanimations.config.ItemPositionAdvancedSettings;
import org.polyfrost.overflowanimations.config.OldAnimationsSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderEntityItem.class)
public class RenderEntityItemMixin_CustomPositions {

    @Inject(method = "func_177077_a", at = @At("TAIL"))
    public void overflowAnimations$droppedItemTransforms(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_, CallbackInfoReturnable<Integer> cir) {
        OldAnimationsSettings settings = OldAnimationsSettings.INSTANCE;
        ItemPositionAdvancedSettings advanced = OldAnimationsSettings.advancedSettings;
        if (OldAnimationsSettings.globalPositions && settings.enabled) {
            GlStateManager.translate(
                    advanced.droppedPositionX,
                    advanced.droppedPositionY,
                    advanced.droppedPositionZ
            );
            GlStateManager.rotate(advanced.droppedRotationPitch, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(advanced.droppedRotationYaw, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(advanced.droppedRotationRoll, 0.0f, 0.0f, 1.0f);
            GlStateManager.scale(
                    1.0f * Math.exp(advanced.droppedScale),
                    1.0f * Math.exp(advanced.droppedScale),
                    1.0f * Math.exp(advanced.droppedScale)
            );
        }
    }

}
