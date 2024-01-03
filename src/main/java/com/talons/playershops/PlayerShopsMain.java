package com.talons.playershops;

import com.mojang.logging.LogUtils;
import com.talons.playershops.block.ModBlocks;
import com.talons.playershops.block.entity.ModBlockEntities;
import com.talons.playershops.config.PlayerShopsCommonConfigs;
import com.talons.playershops.item.ModItems;
import com.talons.playershops.screen.ModMenuTypes;
import com.talons.playershops.screen.PlayerShopScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("playershops")
public class PlayerShopsMain
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static boolean ftbTeamsCompat = false;

    public PlayerShopsMain()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::clientSetup);
        // Register the setup method for modloading
        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PlayerShopsCommonConfigs.SPEC, "PlayerShops-Common.toml");
        // Register ourselves for server and other game events we are interested in
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModBlockEntities.register(eventBus);
        ModMenuTypes.register(eventBus);


        MinecraftForge.EVENT_BUS.register(this);
    }


    private void clientSetup(final FMLClientSetupEvent event)
    {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.OAK_PLAYER_SHOP_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ACACIA_PLAYER_SHOP_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BIRCH_PLAYER_SHOP_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRIMSON_PLAYER_SHOP_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DARK_OAK_PLAYER_SHOP_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.JUNGLE_PLAYER_SHOP_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPRUCE_PLAYER_SHOP_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WARPED_PLAYER_SHOP_BLOCK.get(), RenderType.cutout());

        MenuScreens.register(ModMenuTypes.PLAYER_SHOP_MENU.get(), PlayerShopScreen::new);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        ftbTeamsCompat = ModList.get().isLoaded("ftbteams");
    }
}