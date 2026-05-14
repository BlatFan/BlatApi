package ru.blatfan.blatapi.common.events;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.common.cap.IPlayerSkin;
import ru.blatfan.blatapi.common.cap.PlayerSkinProvider;
import ru.blatfan.blatapi.common.network.BlatApiPacketHandler;
import ru.blatfan.blatapi.common.network.BloodPacket;
import ru.blatfan.blatapi.common.network.playerskin.PlayerSkinUpdatePacket;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = BlatApi.MOD_ID)
public class BlatApiEvents {
    private static final List<Player> playerSkinUpdate = new ArrayList<>();

    @SubscribeEvent
    public static void attachEntityCaps(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) event.addCapability(BlatApi.loc("player_skin"), new PlayerSkinProvider());
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        Capability<IPlayerSkin> KNOWLEDGE = IPlayerSkin.INSTANCE;
        event.getOriginal().reviveCaps();
        event.getEntity().getCapability(KNOWLEDGE).ifPresent((k) -> event.getOriginal().getCapability(KNOWLEDGE).ifPresent((o) ->
                ((INBTSerializable<CompoundTag>) k).deserializeNBT(((INBTSerializable<CompoundTag>) o).serializeNBT())));
        if (!event.getEntity().level().isClientSide) {
            BlatApiPacketHandler.sendTo((ServerPlayer) event.getEntity(), new PlayerSkinUpdatePacket(event.getEntity()));
        }
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player && !event.getLevel().isClientSide()) {
            BlatApiPacketHandler.sendTo((ServerPlayer) event.getEntity(), new PlayerSkinUpdatePacket(player));
            playerSkinUpdate.add(player);
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level().isClientSide()) {
            for (Player player : playerSkinUpdate) {
                for (ServerPlayer serverPlayer : player.getServer().getPlayerList().getPlayers()) {
                    BlatApiPacketHandler.sendTo(serverPlayer, new PlayerSkinUpdatePacket(player));
                    if (player != serverPlayer) {
                        BlatApiPacketHandler.sendTo(player, new PlayerSkinUpdatePacket(serverPlayer));
                    }
                }
            }
            playerSkinUpdate.clear();
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (!level.isClientSide()) {
            Vec3 pos = entity.position().add(0, entity.getBbHeight() / 2f, 0);
            BlatApiPacketHandler.sendToTracking(level, BlockPos.containing(pos), new BloodPacket(pos, event.getAmount()));
        }
    }
}
