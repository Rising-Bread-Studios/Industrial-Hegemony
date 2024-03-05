package lettuce.lover.technologicalascendancy.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import lettuce.lover.technologicalascendancy.TechnologicalAscendancy;
import lettuce.lover.technologicalascendancy.blocks.ChargerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import org.joml.Quaternionf;

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
                Quaternionf rotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
                poseStack.mulPose(rotation);
                //poseStack.mulPose(Axis.YP.rotationDegrees(angle));
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, packedOverlay, poseStack, buffer, Minecraft.getInstance().level, 0);
                poseStack.popPose();
            }
        }
    }
}
