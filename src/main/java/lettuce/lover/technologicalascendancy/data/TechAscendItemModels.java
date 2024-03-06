package lettuce.lover.technologicalascendancy.data;

import lettuce.lover.technologicalascendancy.Registration;
import lettuce.lover.technologicalascendancy.TechnologicalAscendancy;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TechAscendItemModels extends ItemModelProvider {
    public TechAscendItemModels(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, TechnologicalAscendancy.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Registration.SIMPLE_BLOCK.getId().getPath(), modLoc("block/simple_block"));
        withExistingParent(Registration.COMPLEX_BLOCK.getId().getPath(), modLoc("block/complex_block"));
        withExistingParent(Registration.CHARGER_BLOCK.getId().getPath(), modLoc("block/charger_block"));
        withExistingParent(Registration.PROCESSOR_BLOCK.getId().getPath(), modLoc("block/processor_main"));
        basicItem(Registration.TABLET_ITEM.getId());

    }
}
