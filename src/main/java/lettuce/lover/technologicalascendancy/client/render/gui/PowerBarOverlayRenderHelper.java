package lettuce.lover.technologicalascendancy.client.render.gui;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

/**
 * A helper class to define how to render energy bars on top of <code>ItemStacks</code>.
 *
 * @author LettuceLover
 */
public class PowerBarOverlayRenderHelper {
    /**
     * Vanilla renders bars 1 pixel too wide (13px).
     * <p>
     * If set to true, mod power bars will mimic this behavior.
     * </p>
     */
    private static final boolean MIMIC_VANILLA = false;
    /**
     * If true, will paint over vanilla durability bars to make them the correct size.
     */
    private static final boolean HIDE_VANILLA_RENDERBUG = false;
    /**
     * Show energy bar when energy storage is empty.
     */
    private static final boolean SHOW_ON_EMPTY = true;
    /**
     * Show energy bar when energy storage is full.
     */
    private static final boolean SHOW_ON_FULL = true;
    /**
     * Add shadow to energy bar
     */
    private static final boolean SHADOW = true;

    /**
     * The width of all energy bars. 13 if <code>MIMIC_VANILLA</code> is true; 12 otherwise.
     */
    private static final int BAR_WIDTH = MIMIC_VANILLA ? 13 : 12;

    /**
     * The X offset of all energy bars.
     */
    private static final int BAR_X_OFFSET = 2;
    /**
     * The Y offset of all energy bars. Currently unused.
     */
    private static final int BAR_Y_OFFSET = 0;

    // ARGB format
    /**
     * The color of the shadow drawn underneath the energy bar.
     */
    protected int colorShadow = FastColor.ARGB32.color(255, 0, 0, 0);
    /**
     * The color of the left side of the energy bar.
     */
    protected int colorBarLeft = FastColor.ARGB32.color(255, 255, 0, 0);
    /**
     * The color of the right side of the energy bar.
     */
    protected int colorBarRight = FastColor.ARGB32.color(255, 0, 255, 0);
    /**
     * The color of the background of the energy bar.
     */
    protected int colorBG = FastColor.ARGB32.color(255, 0, 0, 0);

    /**
     * A static instance of <code>PowerBarOverlayRenderHelper</code> for <code>Item</code> subclasses to call.
     */
    public static final @NotNull PowerBarOverlayRenderHelper instance = new PowerBarOverlayRenderHelper();

    protected PowerBarOverlayRenderHelper() {
    }

    /**
     * @param maxEnergy the max capacity of the <code>ItemStack's</code> <code>{@linkplain IEnergyStorage IEnergyStorage}</code>.
     * @param energy the current energy stored in the <code>ItemStack</code>
     * @return <code>boolean</code> of whether the <code>ItemStack</code> should display an energy bar.
     */
    protected boolean shouldShowBar(int maxEnergy, int energy) {
        if (energy < 0 || energy > maxEnergy) {
            return false;
        }
        if (energy == 0) {
            return SHOW_ON_EMPTY;
        }
        if (energy == maxEnergy) {
            return SHOW_ON_FULL;
        }
        return true;
    }

    /**
     * A variant of render that doesn't take an <code>{@linkplain IEnergyStorage IEnergyStorage}</code> as a parameter
     * and instead queries it itself.
     *
     * @param guiGraphics the <code>{@linkplain GuiGraphics GuiGraphics}</code> of the scene the <code>ItemStack</code> is in
     * @param stack the <code>ItemStack</code> to render an energy bar for
     * @param xPos the <code>ItemStack's</code> relative X position
     * @param yPos the <code>ItemStack's</code> relative Y position
     */
    public void render(GuiGraphics guiGraphics, @NotNull ItemStack stack, int xPos, int yPos) {
        IEnergyStorage capability = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        render(guiGraphics, stack, capability, xPos, yPos);
    }

    /**
     * Defaults <code>boolean alwaysShow</code> to true.
     *
     * @param guiGraphics the <code>{@linkplain GuiGraphics GuiGraphics}</code> of the scene the <code>ItemStack</code> is in
     * @param stack the <code>ItemStack</code> to render an energy bar for
     * @param capability the <code>{@linkplain IEnergyStorage IEnergyStorage}</code> of the <code>ItemStack</code>
     * @param xPos the <code>ItemStack's</code> relative X position
     * @param yPos the <code>ItemStack's</code> relative Y position
     */
    public void render(GuiGraphics guiGraphics, @NotNull ItemStack stack, IEnergyStorage capability, int xPos, int yPos) {
        render(guiGraphics, stack, capability, xPos, yPos, true);
    }

    /**
     * The main render method.
     * <p>
     * Determine the <code>level</code> of the <code>ItemStack's</code> energy and
     * add a vertical offset if the <code>ItemStack</code> has a durability bar as well.
     * </p>
     *
     * @param guiGraphics the <code>{@linkplain GuiGraphics GuiGraphics}</code> of the scene the <code>ItemStack</code> is in
     * @param stack the <code>ItemStack</code> to render an energy bar for
     * @param capability the <code>{@linkplain IEnergyStorage IEnergyStorage}</code> of the <code>ItemStack</code>
     * @param xPos the <code>ItemStack's</code> relative X position
     * @param yPos the <code>ItemStack's</code> relative Y position
     * @param alwaysShow whether the <code>ItemStack's</code> energy bar should always be shown, even if <code>ItemStack</code> has 0 energy or is full
     */
    public void render(GuiGraphics guiGraphics, @NotNull ItemStack stack, IEnergyStorage capability, int xPos, int yPos, boolean alwaysShow) {
        int maxEnergy = capability.getMaxEnergyStored();
        if (maxEnergy > 0) {
            int energy = capability.getEnergyStored();
            if (alwaysShow || shouldShowBar(maxEnergy, energy)) {
                double level = (double) energy / (double) maxEnergy;
                boolean up = stack.getItem().isBarVisible(stack);
                boolean top = stack.getCount() != 1;
                render(guiGraphics, level, xPos, yPos, top ? 12 : up ? 2 : 0, SHADOW);
            }
        }
        if (HIDE_VANILLA_RENDERBUG && stack.getItem().isBarVisible(stack)) {
            overpaintVanillaRenderBug((BufferBuilder) guiGraphics.bufferSource().getBuffer(RenderType.guiOverlay()), xPos, yPos);
        }
    }

    /**
     * Where the actual drawing begins.
     * <p>
     * Get the <code><b>RenderType.guiOverlay</b></code> <code>{@linkplain BufferBuilder BufferBuilder}</code> and call
     * drawPlain, drawGrad, drawRight, and overPaintVanillaRenderBug with it.
     * </p>
     * <p>
     * <b>NOT</b> the <code>RenderType.gui</code> <code>BufferBuilder</code>. This will result in the energy bar being drawn
     * underneath the <code>ItemStack</code>. Learn from my mistakes.
     * </p>
     *
     * @param guiGraphics the <code>{@linkplain GuiGraphics GuiGraphics}</code> of the scene the <code>ItemStack</code> is in
     * @param level the energy level of the <code>ItemStack</code> (energy/maxEnergy)
     * @param xPos the <code>ItemStack's</code> relative X position
     * @param yPos the <code>ItemStack's</code> relative Y position
     * @param offset the vertical offset of the bar depending on whether the <code>ItemStack</code> has a durability bar
     * @param shadow whether the energy bar should have a shadow
     */
    public void render(GuiGraphics guiGraphics, double level, int xPos, int yPos, int offset, boolean shadow) {
        BufferBuilder worldRenderer = (BufferBuilder) guiGraphics.bufferSource().getBuffer(RenderType.guiOverlay());
        double barWidth = level * BAR_WIDTH;
        this.drawPlain(worldRenderer, xPos + BAR_X_OFFSET, yPos + 13 - offset, BAR_WIDTH, shadow ? 2 : 1, colorShadow);
        this.drawGrad(worldRenderer, xPos + BAR_X_OFFSET, yPos + 13 - offset, ((double)BAR_WIDTH + barWidth) / 2, 1, colorBarLeft, colorBarRight);
        this.drawRight(worldRenderer, xPos + BAR_X_OFFSET + BAR_WIDTH, yPos + 13 - offset, BAR_WIDTH - barWidth, 1, colorBG);
        if (HIDE_VANILLA_RENDERBUG && offset == 2) {
            overpaintVanillaRenderBug(worldRenderer, xPos, yPos);
        }
    }

    /**
     * Draw the gradient/charged part of the energy bar.
     *
     * @param renderer the <code>{@linkplain BufferBuilder BufferBuilder}</code> to be used for drawing the bar's vertices
     * @param xPos the X position of the gradient bar's top left corner
     * @param yPos the Y position of the gradient bar's top left corner
     * @param width the width of the gradient bar
     * @param height the height of the gradient bar
     * @param pColorLeft the color of the left side of the gradient
     * @param pColorRight the color of the right side of the gradient
     */
    protected void drawGrad(BufferBuilder renderer, int xPos, int yPos, double width, double height, int pColorLeft, int pColorRight) {
        renderer.vertex(xPos + 0, yPos + 0, 0).color(pColorLeft).endVertex();
        renderer.vertex(xPos + 0, yPos + height, 0).color(pColorLeft).endVertex();
        renderer.vertex(xPos + width, yPos + height, 0).color(pColorRight).endVertex();
        renderer.vertex(xPos + width, yPos + 0, 0).color(pColorRight).endVertex();
    }

    /**
     * Draw the background/shadow of the energy bar. Also used to correct the vanilla render bug.
     *
     * @param renderer the <code>{@linkplain BufferBuilder BufferBuilder}</code> to be used for drawing the bar's vertices
     * @param xPos the X position of the background bar's top left corner
     * @param yPos the Y position of the background bar's top left corner
     * @param width the width of the background bar
     * @param height the height of the background bar
     * @param pColor the color of the background bar
     */
    protected void drawPlain(BufferBuilder renderer, int xPos, int yPos, double width, double height, int pColor) {
        renderer.vertex(xPos + 0, yPos + 0, 0).color(pColor).endVertex();
        renderer.vertex(xPos + 0, yPos + height, 0).color(pColor).endVertex();
        renderer.vertex(xPos + width, yPos + height, 0).color(pColor).endVertex();
        renderer.vertex(xPos + width, yPos + 0, 0).color(pColor).endVertex();
    }

    /**
     * Draw the right/empty part of the energy bar
     *
     * @param renderer the <code>{@linkplain BufferBuilder BufferBuilder}</code> to be used for drawing the bar's vertices
     * @param xPos the X position of the right bar's top left corner
     * @param yPos the Y position of the right bar's top left corner
     * @param width the width of the right bar
     * @param height the height of the right bar
     * @param pColor the color of the right bar
     */
    protected void drawRight(BufferBuilder renderer, int xPos, int yPos, double width, double height, int pColor) {
        renderer.vertex(xPos - width, yPos + 0, 0).color(pColor).endVertex();
        renderer.vertex(xPos - width, yPos + height, 0).color(pColor).endVertex();
        renderer.vertex(xPos + 0, yPos + height, 0).color(pColor).endVertex();
        renderer.vertex(xPos + 0, yPos + 0, 0).color(pColor).endVertex();
    }

    /**
     * Draw over the vanilla durability bar and make it 12px. (I believe?)
     *
     * @param renderer the <code>{@linkplain BufferBuilder BufferBuilder}</code> to be used for drawing the bar's vertices
     * @param xPos the X position of the vanilla durability bar's top left corner
     * @param yPos the Y position of the vanilla durability bar's top left corner
     */
    protected void overpaintVanillaRenderBug(BufferBuilder renderer, int xPos, int yPos) {
        this.drawPlain(renderer, xPos + 2 + 12, yPos + 13, 1, 1, colorShadow);
    }
}