package com.talons.playershops.events;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.talons.playershops.block.ModBlocks.*;

@Mod.EventBusSubscriber(modid = "playershops", bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreativeModeTabEvent {
    // Registered on the MOD event bus
    // Assume we have RegistryObject<Item> and RegistryObject<Block> called ITEM and BLOCK
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Add to ingredients tab
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ACACIA_PLAYER_SHOP_BLOCK);
            event.accept(BIRCH_PLAYER_SHOP_BLOCK);
            event.accept(CRIMSON_PLAYER_SHOP_BLOCK);
            event.accept(JUNGLE_PLAYER_SHOP_BLOCK);
            event.accept(DARK_OAK_PLAYER_SHOP_BLOCK);
            event.accept(OAK_PLAYER_SHOP_BLOCK);
            event.accept(SPRUCE_PLAYER_SHOP_BLOCK);
            event.accept(WARPED_PLAYER_SHOP_BLOCK);
            event.accept(CHERRY_PLAYER_SHOP_BLOCK);
            event.accept(MANGROVE_PLAYER_SHOP_BLOCK);
            event.accept(BAMBOO_PLAYER_SHOP_BLOCK);
        }
    }
}
