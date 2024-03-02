package lettuce.lover.technologicalascendancy.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ComplexBlock extends Block implements EntityBlock {

    public ComplexBlock() {
        super(Properties.of()
                .strength(3.5f)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ComplexBlockEntity(pos, state);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        } else {
            return (lvl, pos, blockState, blockEntity) -> {
                if (blockEntity instanceof ComplexBlockEntity be) {
                    be.tickServer();
                }
            };
        }
    }
}
