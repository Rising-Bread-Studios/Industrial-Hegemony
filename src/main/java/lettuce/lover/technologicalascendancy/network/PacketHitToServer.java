package lettuce.lover.technologicalascendancy.network;

import lettuce.lover.technologicalascendancy.TechnologicalAscendancy;
import lettuce.lover.technologicalascendancy.blocks.ProcessorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record PacketHitToServer(BlockPos pos, int button) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(TechnologicalAscendancy.MODID, "hit");

    public static PacketHitToServer create(FriendlyByteBuf buffer) {
        return new PacketHitToServer(buffer.readBlockPos(), buffer.readByte());
    }

    public static PacketHitToServer create(BlockPos pos, int button) {
        return new PacketHitToServer(pos, button);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeByte(button);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public void handle(PlayPayloadContext context) {
        context.workHandler().submitAsync(() -> {
            context.player().ifPresent(player -> {
                if (player.level().getBlockEntity(pos) instanceof ProcessorBlockEntity processor) {
                    processor.hit(player, button);
                }
            });
        });
    }
}
