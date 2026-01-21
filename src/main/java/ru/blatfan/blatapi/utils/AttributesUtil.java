package ru.blatfan.blatapi.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import ru.blatfan.blatapi.BlatApi;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

@UtilityClass
public class AttributesUtil {
  static final Random RAND = new Random();
  public static final UUID DEFAULT_ID = UUID.fromString("9d4de1b0-8aad-4fe4-8c1d-70ad8dca88fa");
  public static final UUID MULT_ID = UUID.fromString("52910bd7-250a-438b-8441-0ff42d72b8f9");
  public static final UUID ID_STEP_HEIGHT = UUID.fromString("3ffcfb9a-413c-4dfb-b697-132006c9f346");
  static final float VANILLA = 0.6F;

  public static void disableStepHeight(Player player) {
    AttributeInstance attr = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
    attr.removeModifier(ID_STEP_HEIGHT);
  }

  public static void enableStepHeight(Player player) {
    float newVal;
    if (player.isCrouching())
      newVal = 0.9F - VANILLA;
    else
      newVal = 1.0F + (1F / 16F) - VANILLA;
    
    AttributeInstance attr = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
    AttributeModifier oldModifier = attr.getModifier(AttributesUtil.ID_STEP_HEIGHT);
    double old = oldModifier == null ? 0 : oldModifier.getAmount();
    if (newVal != old) {
      AttributesUtil.setStepHeightInternal(player, newVal);
    }
  }

  private static void setStepHeightInternal(Player player, double newVal) {
    //    player.maxUpStep = 0.6F; // LivingEntity.class constructor defaults to this
    AttributeInstance attr = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get());
    attr.removeModifier(ID_STEP_HEIGHT);
    if (newVal != 0) {
      AttributeModifier healthModifier = new AttributeModifier(ID_STEP_HEIGHT, BlatApi.MOD_ID, newVal, AttributeModifier.Operation.ADDITION);
      attr.addPermanentModifier(healthModifier);
    }
  }

  public static int add(Attribute attribute, Collection<ServerPlayer> players, int integer) {
    for (ServerPlayer playerIn : players) {
      updateAttrModifierBy(attribute, DEFAULT_ID, playerIn, integer);
    }
    return 0;
  }

  public static int addRandom(Attribute attribute, Collection<ServerPlayer> players, int min, int max) {
    for (ServerPlayer playerIn : players) {
      updateAttrModifierBy(attribute, DEFAULT_ID, playerIn, RAND.nextInt(min, max));
    }
    return 0;
  }

  public static int multiply(Attribute attribute, Collection<ServerPlayer> players, double integer) {
    for (ServerPlayer playerIn : players) {
      multiplyAttrModifierBy(attribute, playerIn, integer);
    }
    return 0;
  }

  public static int reset(Attribute attribute, Collection<ServerPlayer> players) {
    for (ServerPlayer playerIn : players) {
      AttributeInstance attr = playerIn.getAttribute(attribute);
      attr.removeModifier(DEFAULT_ID);
      attr.removeModifier(MULT_ID);
    }
    return 0;
  }

  //ench
  public static void removePlayerReach(UUID id, Player player) {
    AttributeInstance attr = player.getAttribute(ForgeMod.BLOCK_REACH.get());
    attr.removeModifier(id);
  }

  // ench
  
  /**
   * vanilla is 5, so +11 it becomes 16
   * @param id
   * @param player
   * @param reachBoost
   */
  public static void setPlayerReach(UUID id, Player player, int reachBoost) {
    removePlayerReach(id, player);
    AttributeInstance attr = player.getAttribute(ForgeMod.BLOCK_REACH.get());
    AttributeModifier enchantment = new AttributeModifier(id, "ReachBLATAPI", reachBoost, AttributeModifier.Operation.ADDITION);
    attr.addPermanentModifier(enchantment);
  }

  public static void updateAttrModifierBy(Attribute attr, UUID id, Player playerIn, int value) {
    AttributeInstance attribute = playerIn.getAttribute(attr);
    AttributeModifier olModifier = attribute.getModifier(id);
    double old = olModifier == null ? 0 : olModifier.getAmount();
    double newVal = value + old;
    attribute.removeModifier(id);
    AttributeModifier modifier = new AttributeModifier(id, "Bonus", newVal, AttributeModifier.Operation.ADDITION);
    attribute.addPermanentModifier(modifier);
    if (attr == Attributes.MAX_HEALTH && playerIn.getHealth() > attribute.getValue())
      playerIn.setHealth((float) attribute.getValue());
  }

  public static void multiplyAttrModifierBy(Attribute attr, Player playerIn, double value) {
    AttributeInstance attribute = playerIn.getAttribute(attr);
    //what is our value 
    attribute.removeModifier(MULT_ID);
    AttributeModifier modifier = new AttributeModifier(MULT_ID, "Bonus", value, AttributeModifier.Operation.MULTIPLY_BASE);
    attribute.addPermanentModifier(modifier);
    if (attr == Attributes.MAX_HEALTH && playerIn.getHealth() > attribute.getValue())
      playerIn.setHealth((float) attribute.getValue());
  }

  public static int setHearts(Collection<ServerPlayer> players, int finalHearts) {
    for (ServerPlayer playerIn : players) {
      setHearts(finalHearts, playerIn);
    }
    return 0;
  }

  private static void setHearts(int finalHearts, ServerPlayer playerIn) {
    int modifiedHearts = finalHearts - 10;
    AttributeInstance healthAttribute = playerIn.getAttribute(Attributes.MAX_HEALTH);
    healthAttribute.removeModifier(DEFAULT_ID);
    AttributeModifier healthModifier = new AttributeModifier(DEFAULT_ID, "HP Bonus", (modifiedHearts * 2), AttributeModifier.Operation.ADDITION);
    healthAttribute.addPermanentModifier(healthModifier);
    if (playerIn.getHealth() > healthAttribute.getValue()) {
      playerIn.setHealth((float) healthAttribute.getValue());
    }
  }
}
