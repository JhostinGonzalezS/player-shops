package com.talons.playershops.block.entity.custom;

import com.talons.playershops.block.ModBlocks;
import com.talons.playershops.block.custom.PlayerShopBlock;
import com.talons.playershops.block.entity.ModBlockEntities;
import com.talons.playershops.item.ModItems;
import com.talons.playershops.screen.PlayerShopMenu;
import com.talons.utils.stacks.UtilItemStack;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.function.Function;

public class PlayerShopBlockEntity extends BlockEntity implements MenuProvider {

    private UUID owner = new UUID(0, 0);
    private ItemStack selling_item_stack = ItemStack.EMPTY;
    private ItemStack buying_item_stack = ItemStack.EMPTY;

    public int onTicks = 0;
    public int statingOnTicks = 30;
    public int currentPowerLevel = 0;

    public void setOwner(LivingEntity entity) {
        this.owner = entity.getUUID();
        sendUpdate();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbtTag = new CompoundTag();
        CompoundTag sellingItemTag = new CompoundTag();
        CompoundTag buyingItemTag = new CompoundTag();
        int saveStockItemCount;
        int saveCoinItemCount;
        nbtTag.put("inventory", itemHandler.serializeNBT());
        selling_item_stack.save(sellingItemTag);
        nbtTag.put(SELLING_ITEM_STACK, sellingItemTag);
        buying_item_stack.save(buyingItemTag);
        nbtTag.put(BUYING_ITEM_STACK, buyingItemTag);
        if (!itemHandler.getStackInSlot(0).isEmpty()) {
            saveStockItemCount = itemHandler.getStackInSlot(0).getCount();
        } else {
            saveStockItemCount = 0;
        }
        if (!itemHandler.getStackInSlot(1).isEmpty()) {
            saveCoinItemCount = itemHandler.getStackInSlot(1).getCount();
        } else {
            saveCoinItemCount = 0;
        }
        nbtTag.putInt(STOCK_ITEM_COUNT, saveStockItemCount);
        nbtTag.putInt(COIN_ITEM_COUNT, saveCoinItemCount);
        return ClientboundBlockEntityDataPacket.create(this, new Function<BlockEntity, CompoundTag>() {
            @Override
            public CompoundTag apply(BlockEntity blockEntity) {
                return nbtTag;
            }
        });
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag();
        int stock_item_count = tag.getInt(STOCK_ITEM_COUNT);
        int coin_item_count = tag.getInt(COIN_ITEM_COUNT);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        selling_item_stack = ItemStack.of(tag.getCompound(SELLING_ITEM_STACK));
        buying_item_stack = ItemStack.of(tag.getCompound(BUYING_ITEM_STACK));
        itemHandler.getStackInSlot(0).setCount(stock_item_count);
        itemHandler.getStackInSlot(1).setCount(coin_item_count);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void onChunkUnloaded() {
        this.saveWithoutMetadata();
        super.onChunkUnloaded();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    public void sendUpdate() {
        setChanged();
        BlockState defaultBlockState = ModBlocks.OAK_PLAYER_SHOP_BLOCK.get().defaultBlockState();
        level.sendBlockUpdated(worldPosition, defaultBlockState, defaultBlockState, 3);
    }

    public boolean isOwner(Block _block, LivingEntity entity) {

        if (!(_block instanceof PlayerShopBlock))
            return false;
        return owner.equals(entity.getUUID());
    }

    public final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            sendUpdate();
            if (!level.isClientSide) {
                saveAdditional(new CompoundTag());
            }
        }

        @Override
        public boolean isItemValid(int slot, @NonNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getItem() == selling_item_stack.getItem();
                case 1 -> false;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public PlayerShopBlockEntity(BlockEntityType<?> entityType, BlockPos p_155229_, BlockState p_155230_) {
        super(entityType, p_155229_, p_155230_);
    }

    public void dropShop(Player player, BlockPos pos) {
        dropInv(player.position());

        player.level.removeBlock(pos, true);
        spawn(level, player.position(), new ItemStack(ModBlocks.OAK_PLAYER_SHOP_BLOCK.get()));
    }

    private void dropInv(Vec3 pos) {
        ItemStack stockStack = itemHandler.getStackInSlot(0);
        ItemStack profitStack = itemHandler.getStackInSlot(1);

        int stockStackRemaining = stockStack.getCount();
        int profitStackRemaining = profitStack.getCount();

        while (stockStackRemaining > 0) {
            int drop = Math.min(stockStack.getMaxStackSize(), stockStack.getCount());
            spawn(level, pos, stockStack, drop);
            stockStackRemaining -= drop;
        }

        while (profitStackRemaining > 0) {
            int drop = Math.min(profitStack.getMaxStackSize(), profitStack.getCount());
            spawn(level, pos, profitStack, drop);
            profitStackRemaining -= drop;
        }
    }

    public void spawn(Level world, Vec3 pos, ItemStack stack, int amount) {
        spawn(world, pos, UtilItemStack.setCount(stack, amount));
    }

    public void spawn(Level world, Vec3 pos, ItemStack stack) {
        world.addFreshEntity(new ItemEntity(world, pos.x, pos.y, pos.z, stack.copy()));
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Your Shop");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new PlayerShopMenu(p_39954_, p_39955_, this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void setSellingItemStack(ItemStack itemStack) {
        selling_item_stack = itemStack;
        sendUpdate();
    }

    public void setBuyingItemStack(ItemStack itemStack) {
        buying_item_stack = itemStack;
        sendUpdate();
    }

    public ItemStack getSellingItemStack() {
        return selling_item_stack.copy();
    }

    public ItemStack getBuyingItemStack() {
        return buying_item_stack.copy();
    }

    private static final String NBT_OWNER_MSB = "owner_msb";
    private static final String NBT_OWNER_LSB = "owner_lsb";
    private static final String SELLING_ITEM_STACK = "selling";
    private static final String BUYING_ITEM_STACK = "buying";
    private static final String STOCK_ITEM_COUNT = "stock_count";
    private static final String COIN_ITEM_COUNT = "coin_count";

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        CompoundTag sellingItemTag = new CompoundTag();
        CompoundTag buyingItemTag = new CompoundTag();
        int saveStockItemCount;
        int saveCoinItemCount;
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putLong(NBT_OWNER_MSB, owner.getMostSignificantBits());
        tag.putLong(NBT_OWNER_LSB, owner.getLeastSignificantBits());
        selling_item_stack.save(sellingItemTag);
        tag.put(SELLING_ITEM_STACK, sellingItemTag);
        buying_item_stack.save(buyingItemTag);
        tag.put(BUYING_ITEM_STACK, buyingItemTag);
        if (!itemHandler.getStackInSlot(0).isEmpty()) {
            saveStockItemCount = itemHandler.getStackInSlot(0).getCount();
        } else {
            saveStockItemCount = 0;
        }
        if (!itemHandler.getStackInSlot(1).isEmpty()) {
            saveCoinItemCount = itemHandler.getStackInSlot(1).getCount();
        } else {
            saveCoinItemCount = 0;
        }
        tag.putInt(STOCK_ITEM_COUNT, saveStockItemCount);
        tag.putInt(COIN_ITEM_COUNT, saveCoinItemCount);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        int stock_item_count = nbt.getInt(STOCK_ITEM_COUNT);
        int coin_item_count = nbt.getInt(COIN_ITEM_COUNT);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.owner = new UUID(nbt.getLong(NBT_OWNER_MSB), nbt.getLong(NBT_OWNER_LSB));
        selling_item_stack = ItemStack.of(nbt.getCompound(SELLING_ITEM_STACK));
        buying_item_stack = ItemStack.of(nbt.getCompound(BUYING_ITEM_STACK));
        itemHandler.getStackInSlot(0).setCount(stock_item_count);
        itemHandler.getStackInSlot(1).setCount(coin_item_count);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        PlayerShopBlockEntity tile = (PlayerShopBlockEntity) be;

        if(tile.onTicks > 0) {
            tile.onTicks--;
        }
        else if(tile.currentPowerLevel != 0) {
            tile.currentPowerLevel = 0;
            tile.setChanged();
        }
    }
}
