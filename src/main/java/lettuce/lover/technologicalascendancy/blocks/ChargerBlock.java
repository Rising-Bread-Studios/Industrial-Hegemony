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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ChargerBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE_DOWN = Shapes.box(0, 2.0/16, 0, 1, 1, 1);


    // Creates a new Block with the id "technologicalascendancy:charger_thingy"
    public ChargerBlock() {
        super(Properties.of()
                .strength(3.5f)
                .sound(SoundType.METAL)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ChargerBlockEntity(pPos, pState);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        } else {
            return (lvl, pos, blockState, blockEntity) -> {
                if (blockEntity instanceof ChargerBlockEntity be) {
                    be.tickServer();
                }
            };
        }
    }
}
