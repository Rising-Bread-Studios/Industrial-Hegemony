package lettuce.lover.technologicalascendancy;

import com.mojang.logging.LogUtils;
import lettuce.lover.technologicalascendancy.data.DataGeneration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TechnologicalAscendancy.MODID)
public class TechnologicalAscendancy {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "technologicalascendancy";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public TechnologicalAscendancy(IEventBus modEventBus) {
        // Register the data generation
        modEventBus.addListener(DataGeneration::generate);
        Registration.init(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register capabilities to mod items
        modEventBus.addListener(Registration::registerCapabilities);


        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (TechnologicalAscendancy) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        //NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

    }




}