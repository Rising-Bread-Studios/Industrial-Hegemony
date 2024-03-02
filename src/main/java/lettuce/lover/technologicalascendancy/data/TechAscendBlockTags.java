package lettuce.lover.technologicalascendancy.data;

import lettuce.lover.technologicalascendancy.Registration;
import lettuce.lover.technologicalascendancy.TechnologicalAscendancy;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

public class TechAscendBlockTags extends BlockTagsProvider {
    public TechAscendBlockTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, TechnologicalAscendancy.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.COMPLEX_BLOCK.get(), Registration.SIMPLE_BLOCK.get(), Registration.CHARGER_BLOCK.get(), Registration.PROCESSOR_BLOCK.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(Registration.COMPLEX_BLOCK.get(), Registration.SIMPLE_BLOCK.get(), Registration.CHARGER_BLOCK.get(), Registration.PROCESSOR_BLOCK.get());
    }
}
