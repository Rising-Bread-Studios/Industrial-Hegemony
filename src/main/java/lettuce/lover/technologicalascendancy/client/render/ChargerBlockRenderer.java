package lettuce.lover.technologicalascendancy.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import lettuce.lover.technologicalascendancy.blocks.ChargerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;

public class ChargerBlockRenderer implements BlockEntityRenderer<ChargerBlockEntity> {

    public ChargerBlockRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(ChargerBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        IItemHandler itemHandler = blockEntity.getItemHandler();
        if (itemHandler != null) {
            ItemStack stack = itemHandler.getStackInSlot(ChargerBlockEntity.SLOT);
            if (!stack.isEmpty()) {
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                long millis = System.currentTimeMillis();

                poseStack.pushPose();
                poseStack.scale(0.5f, 0.5f, 0.5f);
                poseStack.translate(1f, 3f, 1f);

                Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
                Vec3 blockPos = blockEntity.getBlockPos().getCenter();
                blockPos.add(0, 30, 0);
                Vec3 direction = blockPos.vectorTo(cameraPos);
                Vec3 xzPlaneNormal = new Vec3(0, 1, 0);
                double anglexz = Math.toDegrees(Math.atan2(direction.z, direction.x));
                double angley = Math.toDegrees(
                        Math.acos(
                                (direction.dot(xzPlaneNormal))/(direction.length()*xzPlaneNormal.length())
                        )
                ) - 90;
                poseStack.mulPose(Axis.YN.rotationDegrees((float) anglexz + 90));
                poseStack.mulPose(Axis.XN.rotationDegrees((float) angley));
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, packedOverlay, poseStack, buffer, Minecraft.getInstance().level, 0);
                poseStack.popPose();
            }
        }
    }
}
