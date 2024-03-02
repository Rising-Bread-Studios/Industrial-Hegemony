package lettuce.lover.technologicalascendancy.data;

import lettuce.lover.technologicalascendancy.Registration;
import lettuce.lover.technologicalascendancy.TechnologicalAscendancy;
import lettuce.lover.technologicalascendancy.blocks.ChargerBlockEntity;
import lettuce.lover.technologicalascendancy.blocks.ComplexBlockEntity;
import lettuce.lover.technologicalascendancy.blocks.ProcessorBlockEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.stream.Collectors;

public class TechAscendLootTables extends VanillaBlockLoot {
    @Override
    protected void generate() {
        dropSelf(Registration.SIMPLE_BLOCK.get());
        createStandardTable(Registration.COMPLEX_BLOCK.get(), Registration.COMPLEX_BLOCK_ENTITY.get(), ComplexBlockEntity.ITEMS_TAG);
        createStandardTable(Registration.CHARGER_BLOCK.get(), Registration.CHARGER_BLOCK_ENTITY.get(), ChargerBlockEntity.ITEMS_TAG, ChargerBlockEntity.ENERGY_TAG);
        createStandardTable(Registration.PROCESSOR_BLOCK.get(), Registration.PROCESSOR_BLOCK_ENTITY.get(), ProcessorBlockEntity.ITEMS_INPUT_TAG, ProcessorBlockEntity.ITEMS_OUTPUT_TAG);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.holders()
                .filter(e -> e.key().location().getNamespace().equals(TechnologicalAscendancy.MODID))
                .map(Holder.Reference::value)
                .collect(Collectors.toList());
    }

    public void createStandardTable(Block block, BlockEntityType<?> type, String... tags) {
        LootPoolSingletonContainer.Builder<?> lootTableItem = LootItem.lootTableItem(block);
        lootTableItem.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
        for (String tag : tags) {
            lootTableItem.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy(tag, "BlockEntityTag." + tag, CopyNbtFunction.MergeStrategy.REPLACE));
        }
        lootTableItem.apply(SetContainerContents.setContents(type).withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))));

        LootPool.Builder builder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(lootTableItem);
        add(block, LootTable.lootTable().withPool(builder));
    }
}
