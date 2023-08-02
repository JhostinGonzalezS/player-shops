package com.talons.playershops.block.custom;

import com.talons.playershops.block.ModBlocks;
import com.talons.playershops.block.entity.ModBlockEntities;
import com.talons.playershops.block.entity.custom.PlayerShopBlockEntity;
import com.talons.playershops.block.entity.custom.varients.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class PlayerShopBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public PlayerShopBlock(Properties properties) {
        super(properties);
    }
    private static final VoxelShape SHAPE = Shapes.or(Block.box(1, 0, 1, 15, 3, 15), Block.box(3, 3, 3, 13, 13, 13));



    public boolean isSignalSource(BlockState p_55213_) {
        return true;
    }

    public int getSignal(BlockState p_55208_, BlockGetter p_55209_, BlockPos p_55210_, Direction p_55211_) {
        BlockEntity entity = p_55209_.getBlockEntity(p_55210_);
        if(entity instanceof PlayerShopBlockEntity) {
            return ((PlayerShopBlockEntity) entity).currentPowerLevel;
        } else {
            throw new IllegalStateException("Our Container provider is missing!");
        }
    }

    public int getDirectSignal(BlockState p_51109_, BlockGetter p_51110_, BlockPos p_51111_, Direction p_51112_) {
        BlockEntity entity = p_51110_.getBlockEntity(p_51111_);
        if(entity instanceof PlayerShopBlockEntity) {
            return Direction.DOWN == p_51112_ ? ((PlayerShopBlockEntity) entity).currentPowerLevel : 0;
        } else {
            throw new IllegalStateException("Our Container provider is missing!");
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        if (world.isClientSide)
            return;
        BlockEntity _te = world.getBlockEntity(pos);
        if (!(_te instanceof PlayerShopBlockEntity))
            return;
        PlayerShopBlockEntity te = (PlayerShopBlockEntity) _te;
        te.setOwner(entity);
    }

    /* FACING */

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    /* BLOCK ENTITY */

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof PlayerShopBlockEntity) {
                if(((PlayerShopBlockEntity) entity).isOwner(this, pPlayer)) {
                    if(pHand == InteractionHand.MAIN_HAND) {
                        if(((PlayerShopBlockEntity) entity).getSellingItemStack().isEmpty()) {
                            ((PlayerShopBlockEntity) entity).setSellingItemStack(pPlayer.getItemInHand(InteractionHand.MAIN_HAND).copy());
                            ((PlayerShopBlockEntity) entity).itemHandler.setStackInSlot(0, pPlayer.getItemInHand(InteractionHand.MAIN_HAND));
                            pPlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        } else if(((PlayerShopBlockEntity) entity).getBuyingItemStack().isEmpty()) {
                            ((PlayerShopBlockEntity) entity).setBuyingItemStack(pPlayer.getItemInHand(InteractionHand.MAIN_HAND).copy());
                        } else {
                            NetworkHooks.openGui(((ServerPlayer) pPlayer), (PlayerShopBlockEntity) entity, pPos);
                        }
                    }
                }
                else {
                    if(((PlayerShopBlockEntity) entity).getSellingItemStack().getCount() <=
                            ((PlayerShopBlockEntity) entity).itemHandler.getStackInSlot(0).getCount() &&
                            ((PlayerShopBlockEntity) entity).itemHandler.getStackInSlot(0).getItem().equals(
                                    ((PlayerShopBlockEntity) entity).getSellingItemStack().getItem())) {

                        if(((PlayerShopBlockEntity) entity).itemHandler.getStackInSlot(1).getCount() <=
                                Math.max(((PlayerShopBlockEntity) entity).getBuyingItemStack().getMaxStackSize() * 10, 64) - ((PlayerShopBlockEntity) entity).getBuyingItemStack().getCount() &&
                                (((PlayerShopBlockEntity) entity).itemHandler.getStackInSlot(1).getItem().equals(
                                        ((PlayerShopBlockEntity) entity).getBuyingItemStack().getItem()) ||
                                        ((PlayerShopBlockEntity) entity).itemHandler.getStackInSlot(1).isEmpty())) {

                            if(pHand == InteractionHand.MAIN_HAND) {
                                if(pPlayer.getItemInHand(InteractionHand.MAIN_HAND)
                                        .sameItem(((PlayerShopBlockEntity) entity).getBuyingItemStack()) &&
                                        pPlayer.getItemInHand(InteractionHand.MAIN_HAND).getCount() >=
                                                ((PlayerShopBlockEntity) entity).getBuyingItemStack().getCount()) {

                                    if(pPlayer.addItem(((PlayerShopBlockEntity) entity).getSellingItemStack().copy())) {
                                        pPlayer.getItemInHand(InteractionHand.MAIN_HAND).shrink(((PlayerShopBlockEntity) entity).getBuyingItemStack().getCount());
                                        ((PlayerShopBlockEntity) entity).itemHandler.getStackInSlot(0).shrink(((PlayerShopBlockEntity) entity).getSellingItemStack().getCount());
                                        if(((PlayerShopBlockEntity) entity).itemHandler.getStackInSlot(1).isEmpty()) {
                                            ((PlayerShopBlockEntity) entity).itemHandler.setStackInSlot(1, ((PlayerShopBlockEntity) entity).getBuyingItemStack().copy());
                                        }
                                        else {
                                            ((PlayerShopBlockEntity) entity).itemHandler.getStackInSlot(1).grow(((PlayerShopBlockEntity) entity).getBuyingItemStack().getCount());
                                        }
                                        ((PlayerShopBlockEntity) entity).onTicks = ((PlayerShopBlockEntity) entity).statingOnTicks;
                                        if(((PlayerShopBlockEntity) entity).currentPowerLevel < 15) { ((PlayerShopBlockEntity) entity).currentPowerLevel++; }
                                        entity.setChanged();
                                        ((PlayerShopBlockEntity) entity).sendUpdate();
                                        entity.saveWithoutMetadata();
                                    }
                                    else {
                                        pPlayer.displayClientMessage(new TextComponent("Your inventory is full, please clear space"), true);
                                    }

                                }
                                else {
                                    pPlayer.displayClientMessage(new TextComponent("Insufficient funds, please hold the payment item"), true);
                                }
                            }
                        }
                        else {
                            pPlayer.displayClientMessage(new TextComponent("Shop profit slot is full"), true);
                        }
                    }
                    else {
                        pPlayer.displayClientMessage(new TextComponent("Shop is out of stock"), true);
                    }
                }
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        if (world.isClientSide)
            return;
        BlockEntity _te = world.getBlockEntity(pos);
        if (!(_te instanceof PlayerShopBlockEntity))
            return;
        PlayerShopBlockEntity te = (PlayerShopBlockEntity) _te;
        if (te.isOwner(this, player)) {
            if (player.isShiftKeyDown()) {
                te.dropShop(player, pos);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if(this.asBlock() == ModBlocks.OAK_PLAYER_SHOP_BLOCK.get()) { return new OakShopBlockEntity(pPos, pState); }
        if(this.asBlock() == ModBlocks.ACACIA_PLAYER_SHOP_BLOCK.get()) { return new AcaciaShopBlockEntity(pPos, pState); }
        if(this.asBlock() == ModBlocks.BIRCH_PLAYER_SHOP_BLOCK.get()) { return new BirchShopBlockEntity(pPos, pState); }
        if(this.asBlock() == ModBlocks.CRIMSON_PLAYER_SHOP_BLOCK.get()) { return new CrimsonShopBlockEntity(pPos, pState); }
        if(this.asBlock() == ModBlocks.DARK_OAK_PLAYER_SHOP_BLOCK.get()) { return new DarkOakShopBlockEntity(pPos, pState); }
        if(this.asBlock() == ModBlocks.JUNGLE_PLAYER_SHOP_BLOCK.get()) { return new JungleShopBlockEntity(pPos, pState); }
        if(this.asBlock() == ModBlocks.SPRUCE_PLAYER_SHOP_BLOCK.get()) { return new SpruceShopBlockEntity(pPos, pState); }
        if(this.asBlock() == ModBlocks.WARPED_PLAYER_SHOP_BLOCK.get()) { return new WarpedShopBlockEntity(pPos, pState); }
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        if(this.asBlock() == ModBlocks.OAK_PLAYER_SHOP_BLOCK.get()) { return type == ModBlockEntities.OAK_PLAYER_SHOP_BLOCK_ENTITY.get() ? PlayerShopBlockEntity::tick : null; }
        if(this.asBlock() == ModBlocks.ACACIA_PLAYER_SHOP_BLOCK.get()) { return type == ModBlockEntities.ACACIA_PLAYER_SHOP_BLOCK_ENTITY.get() ? PlayerShopBlockEntity::tick : null; }
        if(this.asBlock() == ModBlocks.BIRCH_PLAYER_SHOP_BLOCK.get()) { return type == ModBlockEntities.BIRCH_PLAYER_SHOP_BLOCK_ENTITY.get() ? PlayerShopBlockEntity::tick : null; }
        if(this.asBlock() == ModBlocks.CRIMSON_PLAYER_SHOP_BLOCK.get()) { return type == ModBlockEntities.CRIMSON_PLAYER_SHOP_BLOCK_ENTITY.get() ? PlayerShopBlockEntity::tick : null; }
        if(this.asBlock() == ModBlocks.DARK_OAK_PLAYER_SHOP_BLOCK.get()) { return type == ModBlockEntities.DARK_OAK_PLAYER_SHOP_BLOCK_ENTITY.get() ? PlayerShopBlockEntity::tick : null; }
        if(this.asBlock() == ModBlocks.JUNGLE_PLAYER_SHOP_BLOCK.get()) { return type == ModBlockEntities.JUNGLE_PLAYER_SHOP_BLOCK_ENTITY.get() ? PlayerShopBlockEntity::tick : null; }
        if(this.asBlock() == ModBlocks.SPRUCE_PLAYER_SHOP_BLOCK.get()) { return type == ModBlockEntities.SPRUCE_PLAYER_SHOP_BLOCK_ENTITY.get() ? PlayerShopBlockEntity::tick : null; }
        if(this.asBlock() == ModBlocks.WARPED_PLAYER_SHOP_BLOCK.get()) { return type == ModBlockEntities.WARPED_PLAYER_SHOP_BLOCK_ENTITY.get() ? PlayerShopBlockEntity::tick : null; }
        return null;
    }
}
