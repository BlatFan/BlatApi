package ru.blatfan.blatapi.common.itemskin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.model.armor.ArmorModel;
import ru.blatfan.blatapi.client.registry.BAModels;

public class ItemSkinEntry {

    public boolean canApplyOnItem(ItemStack itemStack) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public ArmorModel getArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel _default) {
        return BAModels.EMPTY_ARMOR;
    }

    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return BlatApi.MOD_ID + ":textures/models/armor/skin/empty.png";
    }

    @OnlyIn(Dist.CLIENT)
    public String getItemModelName(ItemStack stack) {
        return null;
    }
}
