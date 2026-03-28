package ru.blatfan.blatapi.common.itemskin;

import lombok.Getter;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.model.armor.ArmorModel;
import ru.blatfan.blatapi.client.registry.BAModels;
import ru.blatfan.blatapi.utils.ColorHelper;
import ru.blatfan.blatapi.utils.collection.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ItemSkin {
    public String id;
    public Color color;
    public List<ItemSkinEntry> skinEntries = new ArrayList<>();

    public ItemSkin(String id) {
        this.id = id;
        this.color = Color.WHITE;
    }

    public ItemSkin(String id, Color color) {
        this.id = id;
        this.color = color;
    }
    
    public String getTranslatedName() {
        return getTranslatedName(id);
    }

    public String getTranslatedLoreName() {
        return getTranslatedLoreName(id);
    }

    public static String getTranslatedName(String id) {
        int i = id.indexOf(":");
        String modId = id.substring(0, i);
        String monogramId = id.substring(i + 1);
        return "item_skin."  + modId + "." + monogramId;
    }

    public static String getTranslatedLoreName(String id) {
        return getTranslatedName(id) + ".lore";
    }

    public static Component getSkinName(ItemSkin skin) {
        Color color = skin.getColor();

        return Text.create(skin.getTranslatedName()).withStyle(Style.EMPTY.withColor(ColorHelper.getColor(color.getRed(), color.getGreen(), color.getBlue())));
    }

    public static Component getSkinComponent(ItemSkin skin) {
        return Text.create("lore.blatapi.skin").withStyle(Style.EMPTY.withColor(ColorHelper.getColor(249, 210, 129))).space().add(getSkinName(skin));
    }

    public Component getSkinName() {
        return getSkinName(this);
    }

    public Component getSkinComponent() {
        return getSkinComponent(this);
    }

    public boolean canApplyOnItem(ItemStack itemStack) {
        for (ItemSkinEntry skinEntry : getSkinEntries()) {
            if (skinEntry.canApplyOnItem(itemStack)) return true;
        }
        return false;
    }

    public ItemStack applyOnItem(ItemStack itemStack) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        nbt.putString("skin", this.getId());
        return itemStack;
    }

    public static ItemSkin getSkinFromItem(ItemStack itemStack) {
        CompoundTag nbt = itemStack.getOrCreateTag();
        if (nbt.contains("skin")) {
            return ItemSkinHandler.getSkin(nbt.getString("skin"));
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public ArmorModel getArmorModel(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel _default) {
        for (ItemSkinEntry skinEntry : getSkinEntries()) {
            if (skinEntry.canApplyOnItem(itemStack)) return skinEntry.getArmorModel(entity, itemStack, armorSlot, _default);
        }
        return BAModels.EMPTY_ARMOR;
    }

    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        for (ItemSkinEntry skinEntry : getSkinEntries()) {
            if (skinEntry.canApplyOnItem(stack)) return skinEntry.getArmorTexture(stack, entity, slot, type);
        }
        return BlatApi.MOD_ID + ":textures/models/armor/skin/empty.png";
    }

    @OnlyIn(Dist.CLIENT)
    public String getItemModelName(ItemStack stack) {
        for (ItemSkinEntry skinEntry : getSkinEntries()) {
            if (skinEntry.canApplyOnItem(stack)) return skinEntry.getItemModelName(stack);
        }
        return null;
    }
    
    public void addSkinEntry(ItemSkinEntry skinEntry) {
        skinEntries.add(skinEntry);
    }

    public void setupSkinEntries() {

    }

    @OnlyIn(Dist.CLIENT)
    public static boolean isDefaultModel(Entity entity) {
        if (entity instanceof AbstractClientPlayer player) {
            return player.getModelName().equals("default");
        }

        return true;
    }
}
