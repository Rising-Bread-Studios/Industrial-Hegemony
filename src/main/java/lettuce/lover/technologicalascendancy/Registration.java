package lettuce.lover.technologicalascendancy;

import lettuce.lover.technologicalascendancy.blocks.*;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static lettuce.lover.technologicalascendancy.TechnologicalAscendancy.MODID;

public class Registration {

    // Create Deferred Registers for Blocks, Items, Block Entities, Menus, and Creative Mode Tabs
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Create Deferred Block and Item for SimpleBlock
    public static final DeferredBlock<SimpleBlock> SIMPLE_BLOCK = BLOCKS.register("simple_block", SimpleBlock::new);
    public static final DeferredItem<Item> SIMPLE_BLOCK_ITEM = ITEMS.register("simple_block", () -> new BlockItem(SIMPLE_BLOCK.get(), new Item.Properties()));
    // Create Deferred Block, Item, and Block Entity for ComplexBlock
    public static final DeferredBlock<ComplexBlock> COMPLEX_BLOCK = BLOCKS.register("complex_block", ComplexBlock::new);
    public static final DeferredItem<Item> COMPLEX_BLOCK_ITEM = ITEMS.register("complex_block", () -> new BlockItem(COMPLEX_BLOCK.get(), new Item.Properties()));
    public static final Supplier<BlockEntityType<ComplexBlockEntity>> COMPLEX_BLOCK_ENTITY = BLOCK_ENTITIES.register("complex_block",
            () -> BlockEntityType.Builder.of(ComplexBlockEntity::new, COMPLEX_BLOCK.get()).build(null)
    );
    // Create Deferred Block, Item, and Block Entity for ChargerBlock
    public static final DeferredBlock<ChargerBlock> CHARGER_BLOCK = BLOCKS.register("charger_block", ChargerBlock::new);
    public static final DeferredItem<Item> CHARGER_BLOCK_ITEM = ITEMS.register("charger_block", () -> new BlockItem(CHARGER_BLOCK.get(), new Item.Properties()));
    public static final Supplier<BlockEntityType<ChargerBlockEntity>> CHARGER_BLOCK_ENTITY = BLOCK_ENTITIES.register("charger_block",
            () -> BlockEntityType.Builder.of(ChargerBlockEntity::new, CHARGER_BLOCK.get()).build(null)
    );
    // Create Deferred Block, Item, and Block Entity for ProcessorBlock
    public static final DeferredBlock<ProcessorBlock> PROCESSOR_BLOCK = BLOCKS.register("processor_block", ProcessorBlock::new);
    public static final DeferredItem<Item> PROCESSOR_BLOCK_ITEM = ITEMS.register("processor_block", () -> new BlockItem(PROCESSOR_BLOCK.get(), new Item.Properties()));
    public static final Supplier<BlockEntityType<ProcessorBlockEntity>> PROCESSOR_BLOCK_ENTITY = BLOCK_ENTITIES.register("processor_block",
            () -> BlockEntityType.Builder.of(ProcessorBlockEntity::new, PROCESSOR_BLOCK.get()).build(null)
    );
    public static final Supplier<MenuType<ProcessorContainer>> PROCESSOR_CONTAINER = MENU_TYPES.register("processor_block",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new ProcessorContainer(windowId, inv.player, data.readBlockPos()))
    );

    // Register mod items to custom creative mode tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.technologicalascendancy")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> SIMPLE_BLOCK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(SIMPLE_BLOCK_ITEM.get()); // Add the simple block item to the tab. For your own tabs, this method is preferred over the event
                output.accept(COMPLEX_BLOCK_ITEM.get());
                output.accept(CHARGER_BLOCK_ITEM.get());
                output.accept(PROCESSOR_BLOCK_ITEM.get());
            }).build());

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(SIMPLE_BLOCK_ITEM);
            event.accept(COMPLEX_BLOCK_ITEM);
            event.accept(CHARGER_BLOCK_ITEM);
            event.accept(PROCESSOR_BLOCK_ITEM);
        }
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                COMPLEX_BLOCK_ENTITY.get(),
                (entity, direction) -> {
                    return entity.getItemHandler();
                }
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                CHARGER_BLOCK_ENTITY.get(),
                (entity, direction) -> {
                    return entity.getItemHandler();
                }
        );
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                CHARGER_BLOCK_ENTITY.get(),
                (entity, direction) -> {
                    return entity.getEnergyStorage();
                }
        );
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                PROCESSOR_BLOCK_ENTITY.get(),
                (entity, direction) -> {
                    if (direction == null) {
                        return entity.getItemHandler().get();
                    }
                    if (direction == Direction.DOWN) {
                        return entity.getOutputItemHandler().get();
                    }
                    return entity.getInputItemHandler().get();
                });
    }
}
