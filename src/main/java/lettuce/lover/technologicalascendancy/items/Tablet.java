package lettuce.lover.technologicalascendancy.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class Tablet extends Item {
    public static final String ENERGY_TAG = "Energy";
    public static int CAPACITY = 1000;
    public static int TRANSFER_RATE = 20;
    public Tablet(Properties props) {
        super(new Item.Properties().stacksTo(1));
    }
    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Energy", 0);
    }
}
