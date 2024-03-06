package lettuce.lover.technologicalascendancy.client.rendering.gui;

import com.mojang.blaze3d.vertex.VertexConsumer;
import lettuce.lover.technologicalascendancy.TechnologicalAscendancy;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.earlydisplay.ElementShader;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.NeoForgeRenderTypes;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.joml.Matrix4f;

public class EnergyBarRenderer implements IGuiOverlay {

    public EnergyBarRenderer() {
    }

    @Override
    public void render(ExtendedGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        guiGraphics.pose().pushPose();
        guiGraphics.fill(0, 0, 50, 50, FastColor.ARGB32.color(255, 255, 255, 255));
        Player player = (Player) gui.getMinecraft().getCameraEntity();
        if (player != null) {
            boolean hasEnergyItem = player.getInventory().hasAnyMatching((itemStack -> itemStack.getCapability(Capabilities.EnergyStorage.ITEM) != null));
            if (hasEnergyItem) {
                guiGraphics.pose().translate(0, 0, 20);
                Inventory inventory = player.getInventory();
                drawHotbarEnergyBar(guiGraphics, inventory, screenWidth, screenHeight);
                /*for (ItemStack item : inventory.items) {
                    if (item.getCapability(Capabilities.EnergyStorage.ITEM) != null) {
                        int slotIndex = inventory.findSlotMatchingItem(item);
                        if (slotIndex < 9) {
                            drawHotbarEnergyBar(guiGraphics, inventory, screenWidth, screenHeight);
                        }
                        if (player.inventoryMenu.active) {

                        }
                    }
                }*/
            }
        }
        guiGraphics.pose().popPose();
    }

    private void drawHotbarEnergyBar(GuiGraphics guiGraphics, Inventory inventory, int screenWidth, int screenHeight) {
        // Hotbar
        for (int hotbarIndex = 0; hotbarIndex < Inventory.getSelectionSize(); hotbarIndex++) {
            ItemStack item = inventory.getItem(hotbarIndex);
            if (!item.isEmpty()) {
                IEnergyStorage energyStorage = item.getCapability(Capabilities.EnergyStorage.ITEM);
                if (energyStorage != null) {
                    int energy = energyStorage.getEnergyStored();
                    int max = energyStorage.getMaxEnergyStored();
                    float percent = (float) energy / max;
                    int j1 = (screenWidth / 2) - 90 + hotbarIndex * 20;
                    int k1 = screenHeight - 7;
                    guiGraphics.fill(j1 + 4, k1, j1 + 17, k1 + 2, FastColor.ARGB32.color(255, 0, 0, 0));
                    int colorFrom = FastColor.ARGB32.color(255, 255, 0, 0);
                    int colorTo = FastColor.ARGB32.color(255, (int) Math.ceil(255*(1 - percent)), (int) Math.floor(255*percent), 0);
                    TechnologicalAscendancy.LOGGER.debug("RED: " + FastColor.ARGB32.red(colorTo));
                    TechnologicalAscendancy.LOGGER.debug("GREEN: " + FastColor.ARGB32.green(colorTo));
                    colorTo = FastColor.ARGB32.color(255, 0, FastColor.ARGB32.green(colorTo), 0);
                    renderGradient(guiGraphics, j1+4, k1, (int) (j1+(17*percent)), k1+2, 0, colorFrom, colorTo);
                }
            }
        }
    }

    private void renderGradient(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int z, int colorFrom, int colorTo) {
        VertexConsumer vertexConsumer = guiGraphics.bufferSource().getBuffer(RenderType.gui());
        float f = (float)FastColor.ARGB32.alpha(colorFrom) / 255.0F;
        float f1 = (float)FastColor.ARGB32.red(colorFrom) / 255.0F;
        float f2 = (float)FastColor.ARGB32.green(colorFrom) / 255.0F;
        float f3 = (float)FastColor.ARGB32.blue(colorFrom) / 255.0F;
        float f4 = (float)FastColor.ARGB32.alpha(colorTo) / 255.0F;
        float f5 = (float)FastColor.ARGB32.red(colorTo) / 255.0F;
        float f6 = (float)FastColor.ARGB32.green(colorTo) / 255.0F;
        float f7 = (float)FastColor.ARGB32.blue(colorTo) / 255.0F;
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        vertexConsumer.vertex(matrix4f, (float)x1, (float)y1, (float)z).color(f1, f2, f3, f).endVertex();
        vertexConsumer.vertex(matrix4f, (float)x1, (float)y2, (float)z).color(f1, f2, f3, f).endVertex();
        vertexConsumer.vertex(matrix4f, (float)x2, (float)y2, (float)z).color(f5, f6, f7, f4).endVertex();
        vertexConsumer.vertex(matrix4f, (float)x2, (float)y1, (float)z).color(f5, f6, f7, f4).endVertex();
    }
}
