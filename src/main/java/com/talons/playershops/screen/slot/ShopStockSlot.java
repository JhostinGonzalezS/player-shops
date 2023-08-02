package com.talons.playershops.screen.slot;

import com.talons.playershops.block.entity.custom.PlayerShopBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ShopStockSlot extends SlotItemHandler {

    private PlayerShopBlockEntity parentEntity;

    public ShopStockSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, PlayerShopBlockEntity pEntity) {
        super(itemHandler, index, xPosition, yPosition);
        parentEntity = pEntity;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return parentEntity.getSellingItemStack().getItem().equals(stack.getItem());
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return Math.max(stack.getMaxStackSize() * 10, 64);
    }
}
