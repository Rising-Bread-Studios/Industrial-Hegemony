package lettuce.lover.technologicalascendancy;

import lettuce.lover.technologicalascendancy.blocks.ProcessorBlock;
import lettuce.lover.technologicalascendancy.network.Channel;
import lettuce.lover.technologicalascendancy.network.PacketHitToServer;
import lettuce.lover.technologicalascendancy.tools.SafeClientTools;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class EventHandlers {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {
        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);
        if (state.is(Registration.PROCESSOR_BLOCK.get())) {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            if (facing == event.getFace()) {
                event.setCanceled(true);
                if (level.isClientSide) {
                    HitResult hit = SafeClientTools.getClientMouseOver();
                    if (hit.getType() == HitResult.Type.BLOCK) {
                        Vec3 relative = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
                        int quadrant = ProcessorBlock.getQuadrant(facing, relative);
                        Channel.sendToServer(new PacketHitToServer(pos, quadrant));
                    }
                }
            }
        }
    }
}
