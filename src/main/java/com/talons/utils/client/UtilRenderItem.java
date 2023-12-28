package com.talons.utils.client;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import com.talons.utils.client.CustomVertexConsumer.ColorAction;
import com.talons.utils.stacks.UtilItemStack;
import org.joml.Quaternionf;

public class UtilRenderItem {

    public static Minecraft MC = Minecraft.getInstance();
    public static ItemRenderer renderItem = MC.getItemRenderer();
    public static LocalPlayer player = Minecraft.getInstance().player;

    //

    private final ItemStack stack;
    private PoseStack poseStack;
    private Vec3 position = null;
    private ColorAction colorAction = null;
    private MultiBufferSource buffer = null;
    private Integer combinedLight = null;
    private Integer combinedOverlay = null;
    private boolean rotate = false;

    private static final Vec3 DEFAULT_POSITION = new Vec3(0, 0, 0);
    private static final int DEFAULT_COMBINED_LIGHT = 15728880;
    private static final int DEFAULT_COMBINED_OVERLAY = OverlayTexture.NO_OVERLAY;

    private static MultiBufferSource DEFAULT_BUFFER() {
        return MC.renderBuffers().bufferSource();
    }

    public static UtilRenderItem init(ItemStack stack) {
        return new UtilRenderItem(stack);
    }

    private UtilRenderItem(ItemStack stack) {
        this.stack = stack;
    }

    public UtilRenderItem pose(PoseStack poseStack) {
        this.poseStack = poseStack;
        return this;
    }

    public UtilRenderItem pos(double x, double y, double z) {
        this.position = new Vec3(x, y, z);
        return this;
    }

    public UtilRenderItem rotate() {
        this.rotate = true;
        return this;
    }

    public UtilRenderItem buffer(MultiBufferSource buffer) {
        this.buffer = buffer;
        return this;
    }

    public UtilRenderItem combined(int combinedLight, int combinedOverlay) {
        this.combinedLight = combinedLight;
        this.combinedOverlay = combinedOverlay;
        return this;
    }

    public void render() {

        if (!UtilItemStack.isValid(stack))
            return;

        MultiBufferSource buffer = this.buffer != null ? this.buffer : DEFAULT_BUFFER();
        Vec3 position = this.position != null ? this.position : DEFAULT_POSITION;
        int combinedLight = this.combinedLight != null ? this.combinedLight : DEFAULT_COMBINED_LIGHT;
        int combinedOverlay = this.combinedOverlay != null ? this.combinedOverlay : DEFAULT_COMBINED_OVERLAY;

        BakedModel model = renderItem.getModel(stack, null, player, 0);

        poseStack.pushPose();

        boolean render3D = model.isGui3d();
        if (render3D)
            Lighting.setupFor3DItems();
        else
            Lighting.setupForFlatItems();

        if (position != null)
            poseStack.translate(position.x, position.y, position.z);
        if (rotate) {
            long time = System.currentTimeMillis() / -40 % 360;
            poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(0, 1, 0, time));
        }

        if (!render3D) {
            poseStack.scale(.8f, .8f, .8f);
            poseStack.translate(0, .1, 0);
        }

        if (colorAction != null) {
            System.out.println("ERROR: COLOR ACTION SHOULD BE NULL");
        } else {
            renderItem.render(stack, ItemDisplayContext.GROUND, false, poseStack, buffer, combinedLight, combinedOverlay, model);
        }

        poseStack.popPose();

    }

}