package ru.blatfan.blatapi.fluffy_fur.common.itemskin;

import ru.blatfan.blatapi.fluffy_fur.FluffyFur;
import ru.blatfan.blatapi.fluffy_fur.client.model.armor.ArmorModel;
import ru.blatfan.blatapi.fluffy_fur.registry.client.FluffyFurModels;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemSkinEntry {

    public boolean canApplyOnItem(ItemStack itemStack) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public ArmorModel getArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel _default) {
        return FluffyFurModels.EMPTY_ARMOR;
    }

    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return FluffyFur.MOD_ID + ":textures/models/armor/skin/empty.png";
    }

    @OnlyIn(Dist.CLIENT)
    public String getItemModelName(ItemStack stack) {
        return null;
    }
}
