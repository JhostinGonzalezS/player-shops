package com.talons.playershops.screen;

import com.talons.playershops.block.entity.custom.PlayerShopBlockEntity;
import com.talons.playershops.screen.slot.ShopCoinSlot;
import com.talons.playershops.screen.slot.ShopStockSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PlayerShopMenu extends AbstractContainerMenu {

    private final PlayerShopBlockEntity blockEntity;
    private final Level level;

    public PlayerShopMenu(int p_38852_, Inventory inv, FriendlyByteBuf extraData) {
        this(p_38852_, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public PlayerShopMenu(int p_38852_, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.PLAYER_SHOP_MENU.get(), p_38852_);
        checkContainerSize(inv, 2);
        blockEntity = ((PlayerShopBlockEntity) entity);
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getStockOptional().ifPresent(handler -> {
            this.addSlot(new ShopStockSlot(handler, 0, 80, 12, blockEntity));
        });

        this.blockEntity.getCoinOptional().ifPresent(handler -> {
            this.addSlot(new ShopCoinSlot(handler, 0, 80, 60));
        });

    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 2;  // must be the number of slots you have!

    protected boolean moveItemStackToModifiedIn(ItemStack p_38904_, int p_38905_, int p_38906_, boolean p_38907_) {
        boolean flag = false;
        int i = p_38905_;
        if (p_38907_) {
            i = p_38906_ - 1;
        }
        while (!p_38904_.isEmpty()) {
            if (p_38907_) {
                if (i < p_38905_) {
                    break;
                }
            } else if (i >= p_38906_) {
                break;
            }

            Slot slot = this.slots.get(i);
            ItemStack itemstack = slot.getItem();
            if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(p_38904_, itemstack)) {
                int j = itemstack.getCount() + p_38904_.getCount();
                int maxSize = Math.max(p_38904_.getMaxStackSize() * 10, 64);
                if (j <= maxSize) {
                    p_38904_.setCount(0);
                    itemstack.setCount(j);
                    slot.setChanged();
                    flag = true;
                } else if (itemstack.getCount() < maxSize) {
                    p_38904_.shrink(maxSize - itemstack.getCount());
                    itemstack.setCount(maxSize);
                    slot.setChanged();
                    flag = true;
                }
            }

            if (p_38907_) {
                --i;
            } else {
                ++i;
            }
        }

        if (!p_38904_.isEmpty()) {
            if (p_38907_) {
                i = p_38906_ - 1;
            } else {
                i = p_38905_;
            }

            while (true) {
                if (p_38907_) {
                    if (i < p_38905_) {
                        break;
                    }
                } else if (i >= p_38906_) {
                    break;
                }

                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(p_38904_)) {
                    if (p_38904_.getCount() > slot1.getMaxStackSize()) {
                        slot1.set(p_38904_.split(Math.max(p_38904_.getMaxStackSize() * 10, 64)));
                    } else {
                        slot1.set(p_38904_.split(p_38904_.getCount()));
                    }

                    slot1.setChanged();
                    flag = true;
                    break;
                }

                if (p_38907_) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }
    protected boolean moveItemStackToModifiedOut(ItemStack p_38904_, int p_38905_, int p_38906_, boolean p_38907_) {
        boolean flag = false;
        if (!p_38904_.isEmpty()) {
            int availableSpaceNotEmpty = lookForAvailableSpace(p_38905_, p_38906_, p_38907_, p_38904_, false);
            int availableSpaceEmpty = lookForAvailableSpace(p_38905_, p_38906_, p_38907_, p_38904_, true);
            if(availableSpaceNotEmpty > -1) {
                Slot slot = this.slots.get(availableSpaceNotEmpty);
                ItemStack itemstack = slot.getItem();
                int j = itemstack.getCount() + Math.min(p_38904_.getCount(), p_38904_.getMaxStackSize());
                int maxSize = Math.min(slot.getMaxStackSize(), p_38904_.getMaxStackSize());
                if (j <= maxSize) {
                    p_38904_.shrink(Math.max(p_38904_.getMaxStackSize(), p_38904_.getCount()));
                    itemstack.setCount(j);
                    slot.setChanged();
                    flag = true;
                } else {
                    int shiftClickAmount = Math.min(p_38904_.getCount(), p_38904_.getMaxStackSize());
                    int shiftClickAmountC = shiftClickAmount;
                    int remove;
                    int nextAvailableSpaceEmpty;
                    int nextAvailableSpaceNotEmpty;
                    while (shiftClickAmount > 0) {
                        nextAvailableSpaceEmpty = lookForAvailableSpace(p_38905_, p_38906_, p_38907_, p_38904_, true);
                        nextAvailableSpaceNotEmpty = lookForAvailableSpace(p_38905_, p_38906_, p_38907_, p_38904_, false);
                        if(nextAvailableSpaceNotEmpty > -1) {
                            Slot slot1 = this.slots.get(nextAvailableSpaceNotEmpty);
                            ItemStack itemstack1 = slot1.getItem();
                            remove = itemstack1.getMaxStackSize() - itemstack1.getCount();
                            if(remove <= shiftClickAmount) {
                                itemstack1.setCount(maxSize);
                                shiftClickAmount -= remove;
                            }
                            else {
                                itemstack1.grow(shiftClickAmount);
                                shiftClickAmount = 0;
                            }
                            slot1.setChanged();
                            flag = true;
                        }
                        else if(nextAvailableSpaceEmpty > -1) {
                            Slot slot2 = this.slots.get(nextAvailableSpaceEmpty);
                            remove = Math.min(shiftClickAmount, p_38904_.getMaxStackSize());
                            slot2.set(p_38904_.copy());
                            ItemStack itemstack2 = slot2.getItem();
                            itemstack2.setCount(remove);
                            shiftClickAmount -= remove;
                            slot2.setChanged();
                            flag = true;
                        }
                        else {
                            shiftClickAmountC -= shiftClickAmount;
                            shiftClickAmount = 0;
                        }
                    }
                    p_38904_.shrink(shiftClickAmountC);
                }
            } else if(availableSpaceEmpty > -1) {
                Slot slot = this.slots.get(availableSpaceEmpty);
                ItemStack out = p_38904_.copy();
                out.setCount(Math.min(p_38904_.getCount(), p_38904_.getMaxStackSize()));
                p_38904_.setCount(p_38904_.getCount() - Math.min(p_38904_.getCount(), p_38904_.getMaxStackSize()));
                slot.set(out);
            }
        }

        return flag;
    }
    private int lookForAvailableSpace(int startPoint, int endPoint, boolean backwards, ItemStack item, boolean findEmpty) {
        int i = startPoint;
        if (backwards) {
            i = endPoint - 1;
        }

        while(true) {
            if (backwards) {
                if (i < startPoint) {
                    return -1;
                }
            } else if (i >= endPoint) {
                return -1;
            }

            Slot slot = this.slots.get(i);
            ItemStack itemstack = slot.getItem();
            if (findEmpty) {
                if (itemstack.isEmpty() && slot.mayPlace(item)) {
                    return i;
                }
            } else {
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(item, itemstack)) {
                    if(itemstack.getCount() < itemstack.getMaxStackSize()) { return i; }
                }
            }

            if (backwards) {
                --i;
            } else {
                ++i;
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        blockEntity.saveWithoutMetadata();
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackToModifiedIn(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackToModifiedOut(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, blockEntity.getBlockState().getBlock());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
