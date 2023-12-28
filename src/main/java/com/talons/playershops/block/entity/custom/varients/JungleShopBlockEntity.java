package com.talons.playershops.block.entity.custom.varients;

import com.talons.playershops.block.entity.ModBlockEntities;
import com.talons.playershops.block.entity.custom.PlayerShopBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class JungleShopBlockEntity extends PlayerShopBlockEntity {
    public JungleShopBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModBlockEntities.JUNGLE_PLAYER_SHOP_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }
}
