package lettuce.lover.technologicalascendancy;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ChargerBE extends BlockEntity {
    IEnergyStorage capability = level.getCapability(Capabilities.EnergyStorage.BLOCK, this.getBlockPos(), this.getBlockState(), this, null);
    public ChargerBE(BlockPos pos, BlockState state) {
        super(TechnologicalAscendancy.CHARGER_THINGY_BE.get(), pos, state);
    }
    @Override
    public void saveAdditional(CompoundTag tag) {
        if (capability != null) {
            int energy = capability.getEnergyStored();
            tag.putInt("techascend:energy", energy);
        }
        super.saveAdditional(tag);
    }
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
    }
}
