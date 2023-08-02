package com.talons.playershops.block.entity.custom;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import com.talons.playershops.block.custom.PlayerShopBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import com.talons.utils.client.UtilRenderItem;

public class PlayerShopTER implements BlockEntityRenderer<PlayerShopBlockEntity> {

    public PlayerShopTER(BlockEntityRendererProvider.Context ctx) {
    }

    @SuppressWarnings("resource")
    @Override
    public void render(PlayerShopBlockEntity tile, float partialTickTime, PoseStack matrix, MultiBufferSource buffer,
                       int combinedLight, int combinedOverlay) {
        if (tile == null)
            return;

        ClientLevel world;
        if (tile.getLevel() != null & tile.getLevel() instanceof ClientLevel)
            world = (ClientLevel) tile.getLevel();
        else
            return;

        BlockState state = world.getBlockState(tile.getBlockPos());
        if (!(state.getBlock() instanceof PlayerShopBlock))
            return;

        matrix.pushPose();

        switch (state.getValue(PlayerShopBlock.FACING)) {
            case SOUTH:
                matrix.translate(0.5, 0, 0.5);
                matrix.mulPose(new Quaternion(Vector3f.YN, 90, true));
                matrix.translate(-0.5, 0, -0.5);
                break;
            case WEST:
                matrix.translate(0.5, 0, 0.5);
                matrix.mulPose(new Quaternion(Vector3f.YN, 180, true));
                matrix.translate(-0.5, 0, -0.5);
                break;
            case NORTH:
                matrix.translate(0.5, 0, 0.5);
                matrix.mulPose(new Quaternion(Vector3f.YN, 270, true));
                matrix.translate(-0.5, 0, -0.5);
                break;
            default:
        }

        Font font = Minecraft.getInstance().font;
        ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();

        //
        // BOTTOM PART: COST

        ItemStack renderStack = tile.getBuyingItemStack();
        if (!renderStack.isEmpty()) {
            BakedModel itemModel = renderItem.getModel(renderStack, null, null, 0);
            boolean render3D = itemModel.isGui3d();

            if (render3D)
                Lighting.setupFor3DItems();
            else
                Lighting.setupForFlatItems();


            float xTranslate = 1.001f;
            float yTranslate = -.2f;
            float zTranslate = .665f;
            matrix.pushPose(); // START RENDER ITEM
            matrix.translate(xTranslate, yTranslate, zTranslate);
            matrix.mulPose(new Quaternion(Vector3f.YN, 180, true));
            matrix.scale(0, render3D ? .666f : .5f, render3D ? .666f : .5f);
            if (render3D) {
                matrix.mulPose(new Quaternion(Vector3f.ZP, 30f, true));
                matrix.translate(0, -.1, 0);
            }
            matrix.mulPose(new Quaternion(Vector3f.YN, render3D ? 45f : 90, true));

            renderItem.render(renderStack, ItemTransforms.TransformType.GROUND, false, matrix, buffer, combinedLight,
                    combinedOverlay, itemModel);
            matrix.popPose(); // FINNISH RENDER ITEM

            matrix.pushPose(); // START RENDER TEXT
            matrix.translate(xTranslate, yTranslate, zTranslate);
            matrix.mulPose(new Quaternion(Vector3f.YN, 90, true));
            matrix.mulPose(new Quaternion(Vector3f.ZN, 180, true));
            matrix.scale(.028f, .028f, .028f);
            matrix.translate(4.65f, -5.8f, 0);
            if (renderStack.getCount() > 9) {
                matrix.scale(.65f, .65f, .65f);
                matrix.translate(0, 1.75, 0);
            }
            font.draw(matrix, new TextComponent("x" + renderStack.getCount()), 0, 0, 0);
            matrix.popPose(); // END RENDER TEXT

        }

        //
        // TOP PART: VALUE

        ItemStack renderStack2 = tile.getSellingItemStack();
        if (!renderStack2.isEmpty()) {

            matrix.pushPose();

            UtilRenderItem.init(renderStack2) //
                    .pose(matrix) //
                    .buffer(buffer) //
                    .combined(combinedLight, combinedOverlay) //
                    .pos(.5, .25, .5) //
                    .rotate() //
                    .render();

            matrix.popPose();

            matrix.pushPose();
            matrix.scale(.0125f, .0125f, .0125f);
            matrix.mulPose(new Quaternion(Vector3f.ZN, 180, true));
            matrix.mulPose(new Quaternion(Vector3f.YN, 270, true));
            matrix.mulPose(new Quaternion(Vector3f.XN, 0, true));
            matrix.translate(renderStack2.getCount() > 9 ? -48.5 : -45.5, -18.5, -80.001);
            font.draw(matrix, new TextComponent("x" + renderStack2.getCount()), 0, 0, 0);
            matrix.popPose();
        }

        //
        // MIDDLE PART: STOCK

        int stock;
        if (tile.itemHandler.getStackInSlot(0).isEmpty()) {
            stock = 0;
        } else {
            stock = (int)Math.floor((float)tile.itemHandler.getStackInSlot(0).getCount() / (float)renderStack2.getCount());
        }
        matrix.pushPose();
        matrix.scale(.0125f, .0125f, .0125f);
        matrix.mulPose(new Quaternion(Vector3f.ZN, 180, true));
        matrix.mulPose(new Quaternion(Vector3f.YN, 270, true));
        matrix.mulPose(new Quaternion(Vector3f.XN, 0, true));
        matrix.translate(stock > 99 ? -59.5 : stock > 9 ? -56.5 : -53.5, -8.5, -80.001);
        font.draw(matrix, new TextComponent("Qty: " + stock), 0, 0, 0);
        matrix.popPose();

        matrix.popPose();

    }

}