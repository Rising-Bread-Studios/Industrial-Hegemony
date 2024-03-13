package lettuce.lover.technologicalascendancy.client.render.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lettuce.lover.technologicalascendancy.TechnologicalAscendancy;
import lettuce.lover.technologicalascendancy.interfaces.IEnergyBarOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.gui.overlay.ExtendedGui;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.joml.Matrix4f;

/**
 * A class to organize the rendering of energy bars on <code>ItemStacks</code>.
 *
 * @author LettuceLover
 */
public class EnergyBarRenderer {

    public EnergyBarRenderer() {
    }

    /**
     * Render the energy bars of <code>{@linkplain ItemStack ItemStacks}</code> that implement <code>{@linkplain IEnergyBarOverlay IEnergyBarOverlay}</code> being carried by the cursor.
     * <p>
     * Check the <b>carried</b> <code>ItemStack</code> in the <code>{@linkplain Player Player}'s</code> <code>{@linkplain Inventory Inventory}</code>
     * (using <code>{@linkplain InventoryMenu#getCarried() InventoryMenu.getCarried}</code>) for an
     * <code>{@linkplain IEnergyStorage IEnergyStorage}</code> capability and if the <code>ItemStack</code> implements <code>IEnergyBarOverlay</code>.
     * </p>
     * <p>
     * Call the parent <code>Item's</code>
     * {@linkplain IEnergyBarOverlay#renderItemOverlayIntoGUI(GuiGraphics, ItemStack, IEnergyStorage, int, int) <code>renderItemOverlayIntoGUI</code>}
     * method if the <code>ItemStack</code> has both an <code>IEnergyStorage</code> capability and implements <code>IEnergyBarOverlay</code>.
     * </p>
     *
     * @param guiGraphics the <code>{@linkplain GuiGraphics GuiGraphics}</code> of the event that called this method through
     * {@linkplain lettuce.lover.technologicalascendancy.EventHandlers.ClientNeoForgeEvents <code>EventHandlers.ClientNeoForgeEvents</code>}.
     * @see lettuce.lover.technologicalascendancy.items.Tablet#renderItemOverlayIntoGUI(GuiGraphics, ItemStack, IEnergyStorage, int, int) Tablet.renderItemOverlayIntoGUI
     */
    public static void renderCarried(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?> inventoryScreen &&
                inventoryScreen.getMenu().getCarried().getItem() instanceof IEnergyBarOverlay) {
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
            // Draws a white square to the screen. For debug purposes.
            //guiGraphics.fill(0, 0, 50, 50, FastColor.ARGB32.color(255, 255, 255, 255));

            double guiScale = Minecraft.getInstance().getWindow().getGuiScale();
            MouseHandler mouseHandler = Minecraft.getInstance().mouseHandler;
            ItemStack stack = inventoryScreen.getMenu().getCarried();
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (!stack.isEmpty() && energyStorage != null) {
                ((IEnergyBarOverlay) stack.getItem()).renderItemOverlayIntoGUI(guiGraphics, stack, energyStorage,
                        (int) (mouseHandler.xpos() / guiScale) - 8, (int) (mouseHandler.ypos() / guiScale) - 8);
            }
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
        }
        guiGraphics.flush();
        guiGraphics.pose().popPose();
    }

    /**
     * Render the energy bars of <code>{@linkplain ItemStack ItemStacks}</code> that implement <code>{@linkplain IEnergyBarOverlay IEnergyBarOverlay}</code> in the hotbar.
     * <p>
     * Check each <code>ItemStack</code> in the <code>{@linkplain Player Player}'s</code> <code>{@linkplain Inventory Inventory}</code> for an
     * <code>{@linkplain IEnergyStorage IEnergyStorage}</code> capability and if the <code>ItemStack</code> implements <code>IEnergyBarOverlay</code>.
     * </p>
     * <p>
     * Call the parent <code>Item's</code>
     * {@linkplain IEnergyBarOverlay#renderItemOverlayIntoGUI(GuiGraphics, ItemStack, IEnergyStorage, int, int) <code>renderItemOverlayIntoGUI</code>}
     * method if the <code>ItemStack</code> has both an <code>IEnergyStorage</code> capability and implements <code>IEnergyBarOverlay</code>.
     * </p>
     *
     * @param guiGraphics the <code>{@linkplain GuiGraphics GuiGraphics}</code> of the event that called this method through
     * {@linkplain lettuce.lover.technologicalascendancy.EventHandlers.ClientNeoForgeEvents <code>EventHandlers.ClientNeoForgeEvents</code>}.
     * @see lettuce.lover.technologicalascendancy.items.Tablet#renderItemOverlayIntoGUI(GuiGraphics, ItemStack, IEnergyStorage, int, int) Tablet.renderItemOverlayIntoGUI
     */
    public static void renderHotbar(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        Player player = (Player) Minecraft.getInstance().getCameraEntity();
        Inventory inventory = player.getInventory();

        // If the player's inventory has any ItemStacks that implement IEnergyBarOverlay
        if (inventory.hasAnyMatching(itemStack -> itemStack.getItem() instanceof IEnergyBarOverlay)) {
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
            // Draws a cyan square to the screen. For debug purposes.
            //guiGraphics.fill(100, 100, 150, 150, FastColor.ARGB32.color(255, 0, 255, 255));

            // Hotbar's width is 182. Subtract half of hotbar width from middle of screen to get left side of hotbar.
            int x = (Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2) - 182/2;
            // Hotbar's height from the bottom of the screen is 22.
            int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() - 22;
            int j = 182;
            int k = 91;

            // For each hotbar slot if stack in slot has capability then render
            for (int hotbarIndex = 0; hotbarIndex < Inventory.getSelectionSize(); hotbarIndex++) {
                int leftPos = x + (20 * hotbarIndex);
                int topPos = y;
                ItemStack stack = inventory.getItem(hotbarIndex);
                IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                if (!stack.isEmpty() && energyStorage != null && stack.getItem() instanceof IEnergyBarOverlay) {
                    ((IEnergyBarOverlay) stack.getItem()).renderItemOverlayIntoGUI(guiGraphics, stack, energyStorage, leftPos + 3, topPos + 2);
                }
            }
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
        }

        guiGraphics.flush();
        guiGraphics.pose().popPose();
    }

    /**
     * Render the energy bars of <code>{@linkplain ItemStack ItemStacks}</code> within the current
     * <code>{@linkplain AbstractContainerScreen AbstractContainerScreen}</code> that implement
     * <code>{@linkplain IEnergyBarOverlay IEnergyBarOverlay}</code>.
     * <p>
     * Check each <code>ItemStack</code> in the player's <code>AbstractContainerScreen</code>
     * for an <code>{@linkplain IEnergyStorage IEnergyStorage}</code> capability and
     * if the <code>ItemStack</code> implements <code>IEnergyBarOverlay</code>.
     * </p>
     * <p>
     * Call the parent <code>Item's</code>
     * {@linkplain IEnergyBarOverlay#renderItemOverlayIntoGUI(GuiGraphics, ItemStack, IEnergyStorage, int, int) <code>renderItemOverlayIntoGUI</code>}
     * method if the <code>ItemStack</code> has both an <code>IEnergyStorage</code> capability and implements <code>IEnergyBarOverlay</code>.
     * </p>
     *
     * @param guiGraphics the <code>{@linkplain GuiGraphics GuiGraphics}</code> of the event that called this method through
     * {@linkplain lettuce.lover.technologicalascendancy.EventHandlers.ClientNeoForgeEvents <code>EventHandlers.ClientNeoForgeEvents</code>}.
     * @see lettuce.lover.technologicalascendancy.items.Tablet#renderItemOverlayIntoGUI(GuiGraphics, ItemStack, IEnergyStorage, int, int) Tablet.renderItemOverlayIntoGUI
     */
    public static void render(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        // If the current screen is a container && current container has an item that can render an energy bar
        if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?> containerScreen &&
                containerScreen.getMenu().getItems().stream().anyMatch(itemStack -> itemStack.getItem() instanceof IEnergyBarOverlay)) {
            AbstractContainerMenu containerMenu = containerScreen.getMenu();
            int leftPos = containerScreen.getGuiLeft();
            int topPos = containerScreen.getGuiTop();
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
            // Draws a red square to the screen. For debug purposes.
            //guiGraphics.fill(0, 0, 50, 50, FastColor.ARGB32.color(255, 255, 0, 0));

            // For slot in container: check if ItemStack in slot meets all conditions for rendering an energy bar.
            // If ItemStack meets all conditions, call the parent Item's renderItemOverlayIntoGUI method.
            for (int i = 0; i < containerMenu.slots.size(); i++) {
                Slot slot = containerScreen.getMenu().getSlot(i);
                ItemStack stack = slot.getItem();
                IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                if (!stack.isEmpty() && energyStorage != null && stack.getItem() instanceof IEnergyBarOverlay) {
                    ((IEnergyBarOverlay) stack.getItem()).renderItemOverlayIntoGUI(guiGraphics, stack, energyStorage, leftPos + slot.x, topPos + slot.y);
                }
            }
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
        }
        guiGraphics.flush();
        guiGraphics.pose().popPose();
    }
}
