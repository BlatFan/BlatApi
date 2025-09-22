package ru.blatfan.blatapi.common.guide_book;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.ForgeEventFactory;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.guide_book.pages.EmptyPage;
import ru.blatfan.blatapi.utils.RecipeUtil;
import ru.blatfan.blatapi.utils.Text;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;

@Getter
public abstract class GuideBookPage {
    private static final Map<ResourceLocation, Function<JsonObject, GuideBookPage>> TYPES = new HashMap<>();
    private final Component title;
    private final Color titleColor;
    private final boolean separator;
    
    private final List<RenderItem> renderItems = new ArrayList<>();
    
    public static void addPageType(ResourceLocation type, Function<JsonObject, GuideBookPage> deserialize){
        if(!TYPES.containsKey(type))
            TYPES.put(type, deserialize);
    }
    
    public GuideBookPage(Component title, Color titleColor, boolean separator) {
        this.title = title;
        this.titleColor = titleColor;
        this.separator = separator;
    }
    
    public int getHeight(){
        return height()+(!renderItems.isEmpty() ? renderItems.size() % 5 * 18 : 0);
    }
    protected abstract int height();
    
    public void renderPage(GuiGraphics gui, int x, int y, int mX, int mY, float partialTick){
        Font font = GuideClient.font;
        if(separator) {
            gui.setColor(1, 1, 1, 0.7f);
            int xs = (GuideClient.pageWidth/2)-font.width(getTitle())/2-4;
            gui.blitNineSlicedSized(GuideClient.guideBookData.getTexture(), x+8, y + (font.lineHeight / 2), xs-8, 4,
                8, 24, 4, 144, 32, 256, 256);
            gui.blitNineSlicedSized(GuideClient.guideBookData.getTexture(), x+8+xs+font.width(getTitle()), y + (font.lineHeight / 2), xs, 4,
                8, 24, 4, 144, 32, 256, 256);
            gui.setColor(1, 1, 1, 1);
        }
        gui.drawCenteredString(font, getTitle(), x+(GuideClient.pageWidth/2), y, getTitleColor().getRGB());
        
        render(gui, x, y+ font.lineHeight+2, mX, mY, partialTick);
        
        for (int i = 0; i < renderItems.size(); i++) {
            int column = i % 7;
            int row = i / 7;
            if (height() + row * 18 > GuideClient.pageHeight) return;
            renderItems.get(i).render(gui, x + 4 + column * 18, y + height() + row * 18, mX, mY);
        }
    }
    protected abstract void render(GuiGraphics gui, int x, int y, int mX, int mY, float partialTick);
    public boolean mouseClicked(double mX, double mY, int button){
        return false;
    }
    
    public static GuideBookPage json(JsonElement element){
        JsonObject json = element.getAsJsonObject();
        ResourceLocation type = ResourceLocation.tryParse(json.get("type").getAsString());
        GuideBookPage page = TYPES.containsKey(type) ? TYPES.get(type).apply(json) : new EmptyPage();
        if(json.has("item_page"))
            for(JsonElement el : json.get("item_page").getAsJsonArray())
                page.renderItems.add(RenderItem.deserialize(el));
        return page;
    }
    
    private record RenderItem(ItemStack stack, boolean pedestal, List<Component> tooltips){
        public static RenderItem deserialize(JsonElement element){
            JsonObject json = element.getAsJsonObject();
            ItemStack stack = RecipeUtil.itemStackFromJson(json);
            List<Component> tooltips = new ArrayList<>();
            if(json.has("tooltips")) for(JsonElement el : json.get("tooltips").getAsJsonArray()) tooltips.add(Text.create(el.getAsString()));
            boolean pedestal = !json.has("pedestal") || json.get("pedestal").getAsBoolean();
            
            return new RenderItem(stack, pedestal, tooltips);
        }
        
        public void render(GuiGraphics gui, int x, int y, int mX, int mY){
            Font font = GuideClient.mc.font;
            if(pedestal){
                gui.pose().pushPose();
                gui.pose().translate(0, 0, -50);
                gui.renderFakeItem(GuideClient.guideBookData.getPedestal(), x, y+6);
                gui.pose().popPose();
            }
            gui.renderItem(stack, x, y);
            gui.renderItemDecorations(font, stack, x, y);
            if(mX>=x && mX<=x+16 && mY>=y && mY<=y+16) {
                boolean v = false;
                List<Component> s = new ArrayList<>(tooltips);
                for(Component c : s) if(c.getString().equals("item_tooltips")) {
                    v = true;
                    s.remove(c);
                }
                
                gui.renderComponentTooltip(font, getTooltipLines(stack, s, v, GuideClient.player, GuideClient.tooltipFlag), mX, mY);
            }
        }
        
        private List<Component> getTooltipLines(ItemStack stack, List<Component> addition, boolean vanilla, Player pPlayer, TooltipFlag pIsAdvanced) {
            List<Component> list = Lists.newArrayList();
            MutableComponent mutablecomponent = Component.empty().append(stack.getHoverName()).withStyle(stack.getRarity().getStyleModifier());
            if (stack.hasCustomHoverName()) {
                mutablecomponent.withStyle(ChatFormatting.ITALIC);
            }
            
            list.add(mutablecomponent);
            if (!pIsAdvanced.isAdvanced() && !stack.hasCustomHoverName() && stack.is(Items.FILLED_MAP)) {
                Integer integer = MapItem.getMapId(stack);
                if (integer != null) {
                    list.add(Component.literal("#" + integer).withStyle(ChatFormatting.GRAY));
                }
            }
            
            int j = stack.getHideFlags();
            if (stack.shouldShowInTooltip(j, ItemStack.TooltipPart.ADDITIONAL) && vanilla) {
                stack.getItem().appendHoverText(stack, pPlayer == null ? null : pPlayer.level(), list, pIsAdvanced);
            }
            
            if (stack.hasTag()) {
                if (stack.shouldShowInTooltip(j, ItemStack.TooltipPart.UPGRADES) && pPlayer != null && vanilla) {
                    ArmorTrim.appendUpgradeHoverText(stack, pPlayer.level().registryAccess(), list);
                }
                
                if (stack.shouldShowInTooltip(j, ItemStack.TooltipPart.ENCHANTMENTS) && vanilla) {
                    ItemStack.appendEnchantmentNames(list, stack.getEnchantmentTags());
                }
                
                if (stack.getTag().contains("display", 10)) {
                    CompoundTag compoundtag = stack.getTag().getCompound("display");
                    if (stack.shouldShowInTooltip(j, ItemStack.TooltipPart.DYE) && compoundtag.contains("color", 99) && vanilla) {
                        if (pIsAdvanced.isAdvanced()) {
                            list.add(Component.translatable("item.color", String.format(Locale.ROOT, "#%06X", compoundtag.getInt("color"))).withStyle(ChatFormatting.GRAY));
                        } else {
                            list.add(Component.translatable("item.dyed").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                        }
                    }
                    
                    if (compoundtag.getTagType("Lore") == 9) {
                        ListTag listtag = compoundtag.getList("Lore", 8);
                        
                        if(vanilla)
                            for(int i = 0; i < listtag.size(); ++i) {
                                String s = listtag.getString(i);
                                
                                try {
                                    MutableComponent mutablecomponent1 = Component.Serializer.fromJson(s);
                                    if (mutablecomponent1 != null) {
                                        list.add(ComponentUtils.mergeStyles(mutablecomponent1, ItemStack.LORE_STYLE));
                                    }
                                } catch (Exception exception) {
                                    compoundtag.remove("Lore");
                                }
                            }
                    }
                }
            }
            list.addAll(addition);
            
            if (stack.shouldShowInTooltip(j, ItemStack.TooltipPart.MODIFIERS) && vanilla) {
                for(EquipmentSlot equipmentslot : EquipmentSlot.values()) {
                    Multimap<Attribute, AttributeModifier> multimap = stack.getAttributeModifiers(equipmentslot);
                    if (!multimap.isEmpty()) {
                        list.add(CommonComponents.EMPTY);
                        list.add(Component.translatable("item.modifiers." + equipmentslot.getName()).withStyle(ChatFormatting.GRAY));
                        
                        for(Map.Entry<Attribute, AttributeModifier> entry : multimap.entries()) {
                            AttributeModifier attributemodifier = entry.getValue();
                            double d0 = attributemodifier.getAmount();
                            boolean flag = false;
                            if (pPlayer != null) {
                                if (attributemodifier.getId() == Item.BASE_ATTACK_DAMAGE_UUID) {
                                    d0 += pPlayer.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                                    d0 += EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED);
                                    flag = true;
                                } else if (attributemodifier.getId() == Item.BASE_ATTACK_SPEED_UUID) {
                                    d0 += pPlayer.getAttributeBaseValue(Attributes.ATTACK_SPEED);
                                    flag = true;
                                }
                            }
                            
                            double d1;
                            if (attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                                if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
                                    d1 = d0 * 10.0D;
                                } else {
                                    d1 = d0;
                                }
                            } else {
                                d1 = d0 * 100.0D;
                            }
                            
                            if (flag) {
                                list.add(CommonComponents.space().append(Component.translatable("attribute.modifier.equals." + attributemodifier.getOperation().toValue(), stack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
                            } else if (d0 > 0.0D) {
                                list.add(Component.translatable("attribute.modifier.plus." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().getDescriptionId())).withStyle(ChatFormatting.BLUE));
                            } else if (d0 < 0.0D) {
                                d1 *= -1.0D;
                                list.add(Component.translatable("attribute.modifier.take." + attributemodifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), Component.translatable(entry.getKey().getDescriptionId())).withStyle(ChatFormatting.RED));
                            }
                        }
                    }
                }
            }
            
            if (stack.hasTag() && vanilla) {
                if (stack.shouldShowInTooltip(j, ItemStack.TooltipPart.UNBREAKABLE) && stack.getTag().getBoolean("Unbreakable")) {
                    list.add(Component.translatable("item.unbreakable").withStyle(ChatFormatting.BLUE));
                }
                
                if (stack.shouldShowInTooltip(j, ItemStack.TooltipPart.CAN_DESTROY) && stack.getTag().contains("CanDestroy", 9) && vanilla) {
                    ListTag listtag1 = stack.getTag().getList("CanDestroy", 8);
                    if (!listtag1.isEmpty()) {
                        list.add(CommonComponents.EMPTY);
                        list.add(Component.translatable("item.canBreak").withStyle(ChatFormatting.GRAY));
                        
                        for(int k = 0; k < listtag1.size(); ++k) {
                            list.addAll(stack.expandBlockState(listtag1.getString(k)));
                        }
                    }
                }
                
                if (stack.shouldShowInTooltip(j, ItemStack.TooltipPart.CAN_PLACE) && stack.getTag().contains("CanPlaceOn", 9) && vanilla) {
                    ListTag listtag2 = stack.getTag().getList("CanPlaceOn", 8);
                    if (!listtag2.isEmpty()) {
                        list.add(CommonComponents.EMPTY);
                        list.add(Component.translatable("item.canPlace").withStyle(ChatFormatting.GRAY));
                        
                        for(int l = 0; l < listtag2.size(); ++l) {
                            list.addAll(stack.expandBlockState(listtag2.getString(l)));
                        }
                    }
                }
            }
            
            if (stack.isDamaged()) {
                list.add(Component.translatable("item.durability", stack.getMaxDamage() - stack.getDamageValue(), stack.getMaxDamage()));
            }
            if (pIsAdvanced.isAdvanced()) {
                list.add(Component.literal(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY));
                if (stack.hasTag()) {
                    list.add(Component.translatable("item.nbt_tags", stack.getTag().getAllKeys().size()).withStyle(ChatFormatting.DARK_GRAY));
                }
            }
            
            if (pPlayer != null && !stack.getItem().isEnabled(pPlayer.level().enabledFeatures())) {
                list.add(ItemStack.DISABLED_ITEM_TOOLTIP);
            }
            
            ForgeEventFactory.onItemTooltip(stack, pPlayer, list, pIsAdvanced);
            return list;
        }
    }
}