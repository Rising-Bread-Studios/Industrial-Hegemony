package lettuce.lover.technologicalascendancy.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.IItemHandler;
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

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ChargerBlockEntity) {
                IItemHandler itemHandler = ((ChargerBlockEntity) blockEntity).getItemHandler();
                if (itemHandler.getStackInSlot(ChargerBlockEntity.SLOT) == ItemStack.EMPTY) {
                    ItemStack stack = player.getItemInHand(hand);
                    ItemStack insertItem = stack.copyWithCount(1);
                    itemHandler.insertItem(ChargerBlockEntity.SLOT, insertItem, false);
                    stack = stack.copyWithCount(stack.getCount() - 1);
                    player.setItemInHand(hand, stack);
                } else {
                    ejectItem((ChargerBlockEntity) blockEntity);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void ejectItem(ChargerBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos().relative(Direction.UP);
        Block.popResource(blockEntity.getLevel(), pos, blockEntity.getItemHandler().extractItem(ChargerBlockEntity.SLOT, 1, false));
    }
}
