package org.polyfrost.overflowanimations.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.polyfrost.overflowanimations.config.OldAnimationsSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityLivingBase.class, priority = 980)
public abstract class EntityLivingBaseMixin extends Entity {

    public EntityLivingBaseMixin(World worldIn) {
        super(worldIn);
    }

    @Shadow public abstract boolean isPotionActive(Potion potionIn);
    @Shadow public abstract PotionEffect getActivePotionEffect(Potion potionIn);

    @Shadow public float swingProgress;

    @Shadow public float renderYawOffset;

    @Inject(method = "getArmSwingAnimationEnd()I", at = @At("HEAD"), cancellable = true)
    public void modifySwingSpeed(CallbackInfoReturnable<Integer> cir) {
        OldAnimationsSettings settings = OldAnimationsSettings.INSTANCE;
        if (settings.enabled) {
            if (isPotionActive(Potion.digSpeed)) {
                cir.setReturnValue(Math.max((int) (6 - (1 + getActivePotionEffect(Potion.digSpeed).getAmplifier()) * (settings.itemSwingSpeedHaste)), 1));
            } else if (isPotionActive(Potion.digSlowdown)) {
                cir.setReturnValue(Math.max((int) (6 + (1 + getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 * (settings.itemSwingSpeedFatigue)), 1));
            } else {
                cir.setReturnValue(Math.max((int) (6 * Math.exp(-settings.itemSwingSpeed)), 1));
            }
        }
    }

    @ModifyArg(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;updateDistance(FF)F"), index = 0)
    public float modifyYaw(float p_1101461) {
        double d0 = posX - prevPosX;
        double d1 = posZ - prevPosZ;
        float f = (float)(d0 * d0 + d1 * d1);
        float h = renderYawOffset;
        if (f > 0.0025000002F) {
            float f1 = (float) MathHelper.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
            float g = MathHelper.abs(MathHelper.wrapAngleTo180_float(rotationYaw) - f1);
            h = f1 - (95.0F < g && g < 265.0F ? 180.0F : 0.0F);
        }
        if (swingProgress > 0.0F) {
            h = rotationYaw;
        }
        return OldAnimationsSettings.modernMovement ? h : p_1101461;
    }

}