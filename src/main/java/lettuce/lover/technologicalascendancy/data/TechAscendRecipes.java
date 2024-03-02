package lettuce.lover.technologicalascendancy.data;

import lettuce.lover.technologicalascendancy.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class TechAscendRecipes extends RecipeProvider {
    public TechAscendRecipes(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Registration.SIMPLE_BLOCK.get())
                .requires(ItemTags.DIRT)
                .requires(Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(Tags.Items.GEMS_DIAMOND).build()
                ))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.COMPLEX_BLOCK.get())
                .pattern("dsd")
                .pattern("dxd")
                .pattern("ddd")
                .define('d', ItemTags.DIRT)
                .define('x', Tags.Items.GEMS_DIAMOND)
                .define('s', Items.STICK)
                .group("technologicalascendancy")
                .unlockedBy("has_diamond", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(Tags.Items.GEMS_DIAMOND).build()
                ))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.CHARGER_BLOCK.get())
                .pattern("iii")
                .pattern("ici")
                .pattern("iii")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('c', Tags.Items.INGOTS_COPPER)
                .group("technologicalascendancy")
                .unlockedBy("has_copper", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(Tags.Items.INGOTS_COPPER).build()
                ))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Registration.PROCESSOR_BLOCK.get())
                .pattern("iii")
                .pattern("rgr")
                .pattern("iii")
                .define('i', Tags.Items.INGOTS_IRON)
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('g', Tags.Items.GLASS)
                .group("technologicalascendancy")
                .unlockedBy("has_redstone", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(Tags.Items.DUSTS_REDSTONE).build()))
                .save(recipeOutput);
    }
}
