package ru.blatfan.blatapi.common.events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

public abstract class BlatEvent extends Event {

  /**
   * Create this in your mods constructor or any time after that. Instance does not need to be stored
   */
  public BlatEvent() {
    /**
     * NOT loaded by default load this into your event bus to pull in
     *
     *
     * Import @MinecraftForge and run
     *
     * MinecraftForge.EVENT_BUS.register(new CapabilityEvents());
     *
     * inside of the event @FMLCommonSetupEvent
     */
    MinecraftForge.EVENT_BUS.register(this);
  }
}
