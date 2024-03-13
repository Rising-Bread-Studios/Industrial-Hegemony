package lettuce.lover.technologicalascendancy.items;

import lettuce.lover.technologicalascendancy.client.render.gui.PowerBarOverlayRenderHelper;
import lettuce.lover.technologicalascendancy.interfaces.IEnergyBarOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class Tablet extends Item implements IEnergyBarOverlay {
    public static final String ENERGY_TAG = "Energy";
    public static int CAPACITY = 1000;
    public static int TRANSFER_RATE = 20;
    public Tablet(Properties props) {
        super(new Item.Properties());
    }
    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Energy", 0);
    }

    public void renderItemOverlayIntoGUI(GuiGraphics guiGraphics, @NotNull ItemStack stack, IEnergyStorage energyStorage, int xPos, int yPos) {
        PowerBarOverlayRenderHelper.instance.render(guiGraphics, stack, energyStorage, xPos, yPos);
    }

}
