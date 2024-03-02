package lettuce.lover.technologicalascendancy.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataGeneration {
    public static void generate(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new TechAscendBlockStates(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new TechAscendItemModels(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new TechAscendLanguageProvider(packOutput, "en_us"));

        TechAscendBlockTags blockTags = new TechAscendBlockTags(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new TechAscendItemTags(packOutput, lookupProvider, blockTags, existingFileHelper));
        generator.addProvider(event.includeServer(), new TechAscendRecipes(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(TechAscendLootTables::new, LootContextParamSets.BLOCK))));
    }
}
