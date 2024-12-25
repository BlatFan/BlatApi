package ru.blatfan.blatapi.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class MobUtil {

  public static void removeAttackTargets(LivingEntity attacker) {
    attacker.setLastHurtByMob(null);
    attacker.setLastHurtMob(null);
    if (attacker instanceof Mob mob) {
      mob.setTarget(null);
    }
  }

  public static void disablePickupLoot(Mob mob) {
    if (mob.canPickUpLoot()) {
      mob.setCanPickUpLoot(false);
    }
  }
}
