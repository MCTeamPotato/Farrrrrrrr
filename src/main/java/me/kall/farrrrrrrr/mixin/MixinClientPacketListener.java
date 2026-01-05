package me.kall.farrrrrrrr.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheRadiusPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener {
    @Inject(method = "handleForgetLevelChunk", at = @At("HEAD"), cancellable = true)
    private void onHandleForgetLevelChunk(CallbackInfo ci) {
        ci.cancel();
    }

    @Redirect(method = "handleSetChunkCacheRadius", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundSetChunkCacheRadiusPacket;getRadius()I"))
    private int onViewDistChange(ClientboundSetChunkCacheRadiusPacket instance) {
        return Minecraft.getInstance().options.renderDistance().get();
    }

    @Redirect(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;chunkRadius()I"))
    private int onJoinGame(ClientboundLoginPacket instance) {
        return Minecraft.getInstance().options.renderDistance().get();
    }
}