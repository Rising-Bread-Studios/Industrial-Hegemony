package lettuce.lover.technologicalascendancy.client;

import lettuce.lover.technologicalascendancy.Registration;
import lettuce.lover.technologicalascendancy.client.rendering.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static lettuce.lover.technologicalascendancy.TechnologicalAscendancy.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(Registration.PROCESSOR_CONTAINER.get(), ProcessorScreen::new);
        });
    }

    @SubscribeEvent
    public static void initClient(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registration.COMPLEX_BLOCK_ENTITY.get(), ComplexBlockRenderer::new);
        event.registerBlockEntityRenderer(Registration.CHARGER_BLOCK_ENTITY.get(), ChargerBlockRenderer::new);
    }
}
