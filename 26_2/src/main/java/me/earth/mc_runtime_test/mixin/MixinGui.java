package me.earth.mc_runtime_test.mixin;

import me.earth.mc_runtime_test.McRuntimeTest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void setScreenHook(@Nullable Screen screen, CallbackInfo ci) {
        if (!McRuntimeTest.screenHook()) {
            return;
        }

        if (screen instanceof ErrorScreen) {
            throw new RuntimeException("Error Screen " + screen);
        } else if (screen instanceof DeathScreen && minecraft.player != null) {
            minecraft.player.respawn();
        }
    }
}
