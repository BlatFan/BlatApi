package ru.blatfan.blatapi.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.blatfan.blatapi.client.event.BAClientEvents;
import ru.blatfan.blatapi.client.gui.components.SubCreativeTabButton;
import ru.blatfan.blatapi.common.creativetab.MultiCreativeTab;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin {

    @Inject(at = @At("HEAD"), method = "selectTab")
    private void blatapi$selectTab(CreativeModeTab tab, CallbackInfo ci) {
        for (SubCreativeTabButton sb : BAClientEvents.subCreativeTabButtons) {
            sb.refreshSubVisible(tab);
        }
    }

    @Inject(at = @At("HEAD"), method = "mouseScrolled", cancellable = true)
    private void blatapi$mouseScrolled(double mouseX, double mouseY, double delta, CallbackInfoReturnable<Boolean> cir) {
        CreativeModeInventoryScreen self = (CreativeModeInventoryScreen) ((Object) this);
        int i = self.getGuiLeft();
        int j = self.getGuiTop();
        if (mouseX >= i - 26 && mouseY >= j && mouseX <= i && mouseY < j + 138) {
            if (CreativeModeInventoryScreen.selectedTab instanceof MultiCreativeTab multiCreativeTab) {
                if (multiCreativeTab.getSortedSubTabs().size() > 6) {
                    int add = (int) -delta;
                    multiCreativeTab.scroll = multiCreativeTab.scroll + add;
                    if (multiCreativeTab.scroll < 0) {
                        multiCreativeTab.scroll = 0;
                    } else if (multiCreativeTab.scroll > multiCreativeTab.getSortedSubTabs().size() - 6 && multiCreativeTab.getSortedSubTabs().size() > 6) {
                        multiCreativeTab.scroll = multiCreativeTab.getSortedSubTabs().size() - 6;
                    } else {
                        Minecraft.getInstance().player.playNotifySound(SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.NEUTRAL, 0.1f, 2.0f);
                    }
                    for (SubCreativeTabButton sb : BAClientEvents.subCreativeTabButtons) {
                        if (multiCreativeTab.getSortedSubTabs().contains(sb.subTab)) {
                            sb.refreshSub();
                        }
                    }
                }
                cir.setReturnValue(true);
            }
        }
    }
}
