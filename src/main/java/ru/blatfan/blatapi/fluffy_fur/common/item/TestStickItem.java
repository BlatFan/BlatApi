package ru.blatfan.blatapi.fluffy_fur.common.item;

import ru.blatfan.blatapi.fluffy_fur.common.network.FluffyFurPacketHandler;
import ru.blatfan.blatapi.fluffy_fur.common.network.item.TestStickPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class TestStickItem extends Item {

    public TestStickItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag nbt = stack.getOrCreateTag();

        if (!level.isClientSide()) {
            if (!nbt.contains("mode")) {
                nbt.putInt("mode", 0);
            }

            int mode = nbt.getInt("mode");

            if (player.isShiftKeyDown()) {
                nbt.putInt("mode", (mode + 1) % getModes());
                mode = nbt.getInt("mode");
                player.displayClientMessage(getModeComponent(mode), true);
            }
            FluffyFurPacketHandler.sendToTracking(level, player.blockPosition(), new TestStickPacket(player.getEyePosition(), player.getLookAngle(), mode));
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flags) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (!nbt.contains("mode")) {
            nbt.putInt("mode", 0);
        }

        int mode = nbt.getInt("mode");

        list.add(getModeComponent(mode));
    }

    public int getModes() {
        return 29;
    }

    public Component getModeComponent(int mode) {
        return Component.literal(String.valueOf(mode + 1) + "/" + String.valueOf(getModes()));
    }
}
