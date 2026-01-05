package me.kall.farrrrrrrr.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class MixinOptions {
    @Mutable @Shadow @Final private OptionInstance<Integer> renderDistance;
    @Shadow protected Minecraft minecraft;

    @Shadow public static Component genericValueLabel(Component text, Component value) {
        throw new RuntimeException();
    }


    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.renderDistance = new OptionInstance<>(
                "options.renderDistance",
                OptionInstance.noTooltip(),
                (arg, integer) -> genericValueLabel(arg, Component.translatable("options.chunks", integer)),
                new OptionInstance.IntRange(2, this.minecraft.is64Bit() && Runtime.getRuntime().maxMemory() >= 1000000000L ? 1024 : 512),
                this.minecraft.is64Bit() ? 128 : 32,
                integer -> Minecraft.getInstance().levelRenderer.needsUpdate()
        );
    }
}