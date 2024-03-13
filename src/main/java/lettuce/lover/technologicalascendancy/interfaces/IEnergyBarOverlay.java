package lettuce.lover.technologicalascendancy.interfaces;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

/**
 * An interface that all <code>Items</code> that should render an energy bar must implement.
 *
 * @author LettuceLover
 */
public interface IEnergyBarOverlay {
    void renderItemOverlayIntoGUI(GuiGraphics guiGraphics, @NotNull ItemStack stack, IEnergyStorage energyStorage, int xPos, int yPos);
}
