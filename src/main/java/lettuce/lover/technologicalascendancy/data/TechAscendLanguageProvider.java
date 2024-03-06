package lettuce.lover.technologicalascendancy.data;

import lettuce.lover.technologicalascendancy.Registration;
import lettuce.lover.technologicalascendancy.TechnologicalAscendancy;
import lettuce.lover.technologicalascendancy.blocks.ProcessorBlock;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class TechAscendLanguageProvider extends LanguageProvider {
    public TechAscendLanguageProvider(PackOutput packOutput, String locale) {
        super(packOutput, TechnologicalAscendancy.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add(Registration.SIMPLE_BLOCK.get(), "Simple Block");
        add(Registration.COMPLEX_BLOCK.get(), "Complex Block");
        add(Registration.CHARGER_BLOCK.get(), "Charger Block");
        add(Registration.PROCESSOR_BLOCK.get(), "Processor");
        add(Registration.TABLET_ITEM.get(), "Tablet");
        add(ProcessorBlock.SCREEN_TUTORIAL_PROCESSOR, "Processor");
    }
}
