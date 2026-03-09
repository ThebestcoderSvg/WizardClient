package com.codex.mixin;

import com.codex.api.module.ModuleManager;
import com.codex.impl.module.world.TimeChanger;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.Properties.class)
public class MixinClientWorld {

    @Inject(method = "getTimeOfDay", at = @At("HEAD"), cancellable = true)
    private void onGetTimeOfDay(CallbackInfoReturnable<Long> cir) {
        // Kept intentionally empty. The actual override happens in the RETURN injection below.
    }
    
    @Inject(method = "getTimeOfDay", at = @At("RETURN"), cancellable = true)
    private void onGetTimeOfDayReturn(CallbackInfoReturnable<Long> cir) {
        TimeChanger timeChanger = ModuleManager.getInstance().getModule(TimeChanger.class);
        if (timeChanger != null && timeChanger.isEnabled()) {
            long modifiedTime = timeChanger.getRenderTime(cir.getReturnValue());
            if (modifiedTime != cir.getReturnValue()) {
                cir.setReturnValue(modifiedTime);
            }
        }
    }
}
