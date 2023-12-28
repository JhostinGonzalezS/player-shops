package com.talons.playershops.block.entity;

import com.talons.playershops.block.ModBlocks;
import com.talons.playershops.block.entity.custom.varients.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "playershops");

    public static final RegistryObject<BlockEntityType<OakShopBlockEntity>> OAK_PLAYER_SHOP_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("oak_player_shop_block_entity", () ->
                    BlockEntityType.Builder.of(OakShopBlockEntity::new,
                            ModBlocks.OAK_PLAYER_SHOP_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<AcaciaShopBlockEntity>> ACACIA_PLAYER_SHOP_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("acacia_player_shop_block_entity", () ->
                    BlockEntityType.Builder.of(AcaciaShopBlockEntity::new,
                            ModBlocks.ACACIA_PLAYER_SHOP_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<BirchShopBlockEntity>> BIRCH_PLAYER_SHOP_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("birch_player_shop_block_entity", () ->
                    BlockEntityType.Builder.of(BirchShopBlockEntity::new,
                            ModBlocks.BIRCH_PLAYER_SHOP_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<CrimsonShopBlockEntity>> CRIMSON_PLAYER_SHOP_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("crimson_player_shop_block_entity", () ->
                    BlockEntityType.Builder.of(CrimsonShopBlockEntity::new,
                            ModBlocks.CRIMSON_PLAYER_SHOP_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<DarkOakShopBlockEntity>> DARK_OAK_PLAYER_SHOP_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("dark_oak_player_shop_block_entity", () ->
                    BlockEntityType.Builder.of(DarkOakShopBlockEntity::new,
                            ModBlocks.DARK_OAK_PLAYER_SHOP_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<JungleShopBlockEntity>> JUNGLE_PLAYER_SHOP_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("jungle_player_shop_block_entity", () ->
                    BlockEntityType.Builder.of(JungleShopBlockEntity::new,
                            ModBlocks.JUNGLE_PLAYER_SHOP_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<SpruceShopBlockEntity>> SPRUCE_PLAYER_SHOP_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("spruce_player_shop_block_entity", () ->
                    BlockEntityType.Builder.of(SpruceShopBlockEntity::new,
                            ModBlocks.SPRUCE_PLAYER_SHOP_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<WarpedShopBlockEntity>> WARPED_PLAYER_SHOP_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("warped_player_shop_block_entity", () ->
                    BlockEntityType.Builder.of(WarpedShopBlockEntity::new,
                            ModBlocks.WARPED_PLAYER_SHOP_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
