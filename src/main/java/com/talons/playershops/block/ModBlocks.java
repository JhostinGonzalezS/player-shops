package com.talons.playershops.block;

import com.talons.playershops.block.custom.PlayerShopBlock;
import com.talons.playershops.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "playershops");

    public static final RegistryObject<Block> OAK_PLAYER_SHOP_BLOCK = registerBlock("oak_player_shop",
            () -> new PlayerShopBlock(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.WOOD).mapColor(MapColor.WOOD).strength(-1.0F, 3600000.8F).noOcclusion()));
    public static final RegistryObject<Block> ACACIA_PLAYER_SHOP_BLOCK = registerBlock("acacia_player_shop",
            () -> new PlayerShopBlock(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.WOOD).mapColor(MapColor.WOOD).strength(-1.0F, 3600000.8F).noOcclusion()));
    public static final RegistryObject<Block> BIRCH_PLAYER_SHOP_BLOCK = registerBlock("birch_player_shop",
            () -> new PlayerShopBlock(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.WOOD).mapColor(MapColor.WOOD).strength(-1.0F, 3600000.8F).noOcclusion()));
    public static final RegistryObject<Block> CRIMSON_PLAYER_SHOP_BLOCK = registerBlock("crimson_player_shop",
            () -> new PlayerShopBlock(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.WOOD).mapColor(MapColor.WOOD).strength(-1.0F, 3600000.8F).noOcclusion()));
    public static final RegistryObject<Block> DARK_OAK_PLAYER_SHOP_BLOCK = registerBlock("dark_oak_player_shop",
            () -> new PlayerShopBlock(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.WOOD).mapColor(MapColor.WOOD).strength(-1.0F, 3600000.8F).noOcclusion()));
    public static final RegistryObject<Block> JUNGLE_PLAYER_SHOP_BLOCK = registerBlock("jungle_player_shop",
            () -> new PlayerShopBlock(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.WOOD).mapColor(MapColor.WOOD).strength(-1.0F, 3600000.8F).noOcclusion()));
    public static final RegistryObject<Block> SPRUCE_PLAYER_SHOP_BLOCK = registerBlock("spruce_player_shop",
            () -> new PlayerShopBlock(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.WOOD).mapColor(MapColor.WOOD).strength(-1.0F, 3600000.8F).noOcclusion()));
    public static final RegistryObject<Block> WARPED_PLAYER_SHOP_BLOCK = registerBlock("warped_player_shop",
            () -> new PlayerShopBlock(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.WOOD).mapColor(MapColor.WOOD).strength(-1.0F, 3600000.8F).noOcclusion()));
    public static final RegistryObject<Block> CHERRY_PLAYER_SHOP_BLOCK = registerBlock("cherry_player_shop",
            () -> new PlayerShopBlock(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.WOOD).mapColor(MapColor.WOOD).strength(-1.0F, 3600000.8F).noOcclusion()));
    public static final RegistryObject<Block> MANGROVE_PLAYER_SHOP_BLOCK = registerBlock("mangrove_player_shop",
            () -> new PlayerShopBlock(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.WOOD).mapColor(MapColor.WOOD).strength(-1.0F, 3600000.8F).noOcclusion()));
    public static final RegistryObject<Block> BAMBOO_PLAYER_SHOP_BLOCK = registerBlock("bamboo_player_shop",
            () -> new PlayerShopBlock(BlockBehaviour.Properties.copy(Blocks.BARRIER).sound(SoundType.WOOD).mapColor(MapColor.WOOD).strength(-1.0F, 3600000.8F).noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    public static void register(IEventBus eventBus) {
        BLOCKS.register((eventBus));
    }
}
