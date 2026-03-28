package ru.blatfan.blatapi.common.block.sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ru.blatfan.blatapi.common.BARegistry;

public class CustomSignBlockEntity extends SignBlockEntity {

    public CustomSignBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return BARegistry.BlockEntities.SIGN.get();
    }
}
