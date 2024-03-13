package lettuce.lover.technologicalascendancy;

import lettuce.lover.technologicalascendancy.blocks.ProcessorBlock;
import lettuce.lover.technologicalascendancy.client.render.gui.EnergyBarRenderer;
import lettuce.lover.technologicalascendancy.network.Channel;
import lettuce.lover.technologicalascendancy.network.PacketHitToServer;
import lettuce.lover.technologicalascendancy.tools.SafeClientTools;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ContainerScreenEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod.EventBusSubscriber(modid = TechnologicalAscendancy.MODID)
public class EventHandlers {

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {
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
    @Mod.EventBusSubscriber(modid = TechnologicalAscendancy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
    public static class ServerModEvents {
        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event)
        {
            // Do something when the server starts
            TechnologicalAscendancy.LOGGER.info("HELLO from server starting");
        }
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = TechnologicalAscendancy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            TechnologicalAscendancy.LOGGER.info("HELLO FROM CLIENT SETUP");
            TechnologicalAscendancy.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void addCreative(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
                event.accept(Registration.SIMPLE_BLOCK_ITEM);
                event.accept(Registration.COMPLEX_BLOCK_ITEM);
                event.accept(Registration.CHARGER_BLOCK_ITEM);
                event.accept(Registration.PROCESSOR_BLOCK_ITEM);
            }
            if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                event.accept(Registration.TABLET_ITEM);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = TechnologicalAscendancy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ClientNeoForgeEvents {
        @SubscribeEvent
        public static void addEnergyBarsInventory(ContainerScreenEvent.Render.Foreground event) {
            EnergyBarRenderer.render(event.getGuiGraphics());
        }
        @SubscribeEvent
        public static void addEnergyBarCarried(ScreenEvent.Render.Post event) {
            EnergyBarRenderer.renderCarried(event.getGuiGraphics());
        }
        @SubscribeEvent
        public static void addEnergyBarHotbar(RenderGuiOverlayEvent.Post event) {
            if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
                EnergyBarRenderer.renderHotbar(event.getGuiGraphics());
            }
        }
    }
}
