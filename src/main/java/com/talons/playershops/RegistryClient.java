package com.talons.playershops;

import com.talons.playershops.block.entity.ModBlockEntities;
import com.talons.playershops.block.entity.custom.PlayerShopTER;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RegistryClient {

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.OAK_PLAYER_SHOP_BLOCK_ENTITY.get(), PlayerShopTER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ACACIA_PLAYER_SHOP_BLOCK_ENTITY.get(), PlayerShopTER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BIRCH_PLAYER_SHOP_BLOCK_ENTITY.get(), PlayerShopTER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CRIMSON_PLAYER_SHOP_BLOCK_ENTITY.get(), PlayerShopTER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DARK_OAK_PLAYER_SHOP_BLOCK_ENTITY.get(), PlayerShopTER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.JUNGLE_PLAYER_SHOP_BLOCK_ENTITY.get(), PlayerShopTER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SPRUCE_PLAYER_SHOP_BLOCK_ENTITY.get(), PlayerShopTER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WARPED_PLAYER_SHOP_BLOCK_ENTITY.get(), PlayerShopTER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CHERRY_PLAYER_SHOP_BLOCK_ENTITY.get(), PlayerShopTER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MANGROVE_PLAYER_SHOP_BLOCK_ENTITY.get(), PlayerShopTER::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BAMBOO_PLAYER_SHOP_BLOCK_ENTITY.get(), PlayerShopTER::new);
    }

}
