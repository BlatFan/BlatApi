package ru.blatfan.blatapi.fluffy_fur.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import ru.blatfan.blatapi.utils.ICapacity;

import java.util.function.Supplier;

public abstract class CapacitySync<T extends ICapacity<T>> {
    private final T capacity;
    
    protected CapacitySync(T capacity) {
        this.capacity = capacity;
    }
    
    public void toBuf(FriendlyByteBuf buf){
        buf.writeNbt((CompoundTag) capacity.toNBT());
    }
    
    public abstract T get(Player player);
    
    public static <T extends ICapacity<T>> void handler(CapacitySync<T> message, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            if (!contextSupplier.get().getDirection().getReceptionSide().isServer()) {
                T data = message.get(Minecraft.getInstance().player);
                data.copy(message.capacity);
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}