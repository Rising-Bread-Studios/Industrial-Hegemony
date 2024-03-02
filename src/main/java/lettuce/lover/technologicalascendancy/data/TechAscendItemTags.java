package lettuce.lover.technologicalascendancy.data;

import lettuce.lover.technologicalascendancy.TechnologicalAscendancy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class TechAscendItemTags extends ItemTagsProvider {
    public TechAscendItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, blockTags.contentsGetter(), TechnologicalAscendancy.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

    }
}
