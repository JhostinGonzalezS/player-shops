package com.talons.playershops.events;

import com.mojang.brigadier.LiteralMessage;
import com.talons.playershops.PlayerShopsMain;
import com.talons.playershops.config.PlayerShopsCommonConfigs;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "playershops")
public class ModEvents {
    private static long oldTime;
    private static final boolean UseAllowance = PlayerShopsCommonConfigs.UseAllowance.get();
    private static final Long AllowanceTimeOfDay = PlayerShopsCommonConfigs.AllowanceTimeOfDay.get();
    @SubscribeEvent
    public static void onWorldTickEvent(TickEvent.WorldTickEvent event) {
        if(!event.world.isClientSide) {
            if(UseAllowance) {
                if (event.world.getDayTime() != oldTime) {
                    oldTime = event.world.getDayTime();
                    if (event.world.getDayTime() % AllowanceTimeOfDay == 0) {
                        for (int i = 0; i < event.world.players().size(); i++) {
                            ItemStack giveItem = Registry.ITEM.get(new ResourceLocation(PlayerShopsCommonConfigs.AllowanceItem.get())).getDefaultInstance();
                            giveItem.setCount(PlayerShopsCommonConfigs.AllowanceItemCount.get());
                            if(event.world.players().get(i).addItem(giveItem)) {
                                event.world.players().get(i).displayClientMessage(new TextComponent(PlayerShopsCommonConfigs.AllowanceMessage.get()), true);
                            }
                            else  {
                                event.world.players().get(i).displayClientMessage(new TextComponent(PlayerShopsCommonConfigs.AllowanceFailMessage.get()), true);
                            }
                        }
                    }
                }
            }
        }
    }
}
