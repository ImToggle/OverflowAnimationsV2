package org.polyfrost.overflowanimations.mixin;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.objectweb.asm.Opcodes;
import org.polyfrost.overflowanimations.config.OldAnimationsSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class ModelBipedMixin {

    @Shadow public ModelRenderer bipedRightArm;
    @Shadow public ModelRenderer bipedLeftArm;

    @Inject(
            method = "setRotationAngles",
            at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/model/ModelRenderer;rotateAngleY:F", shift = At.Shift.AFTER),
            slice = @Slice(
                    from = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/model/ModelBiped;heldItemRight:I", ordinal = 0),
                    to = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/model/ModelBiped;heldItemRight:I", ordinal = 2)
            )
    )
    private void overflowAnimations$reAssignArmPosition(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (!OldAnimationsSettings.oldArmPosition || !OldAnimationsSettings.INSTANCE.enabled) { return; }
        bipedRightArm.rotateAngleY = 0.0f;
    }

    @Redirect(method = "setRotationAngles", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/model/ModelRenderer;rotateAngleZ:F", ordinal = 0))
    private void overflowAnimations$removeField(ModelRenderer instance, float value) {
        if (!OldAnimationsSettings.wackyArms || !OldAnimationsSettings.INSTANCE.enabled) {
            bipedRightArm.rotateAngleZ = 0.0f;
        }
    }

    @Redirect(method = "setRotationAngles", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/model/ModelRenderer;rotateAngleZ:F", ordinal = 1))
    private void overflowAnimations$removeField2(ModelRenderer instance, float value) {
        if (!OldAnimationsSettings.wackyArms || !OldAnimationsSettings.INSTANCE.enabled) {
            bipedLeftArm.rotateAngleZ = 0.0f;
        }
    }

    @Redirect(method = "setRotationAngles", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/model/ModelRenderer;rotateAngleZ:F", ordinal = 2))
    private void overflowAnimations$removeField3(ModelRenderer instance, float value) {
        if (!OldAnimationsSettings.wackyArms || !OldAnimationsSettings.INSTANCE.enabled) {
            bipedRightArm.rotateAngleZ = 0.0f;
        }
    }

    @Inject(method = "setRotationAngles", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/MathHelper;cos(F)F", ordinal = 2))
    private void overflowAnimations$wackyArms(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo ci) {
        if (OldAnimationsSettings.wackyArms && OldAnimationsSettings.INSTANCE.enabled) {
            bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount;
            bipedRightArm.rotateAngleZ = (MathHelper.cos(limbSwing * 0.2312F) + 1.0F) * 1.0F * limbSwingAmount;
            bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount;
            bipedLeftArm.rotateAngleZ = (MathHelper.cos(limbSwing * 0.2812F) - 1.0F) * 1.0F * limbSwingAmount;
        }
    }

}
