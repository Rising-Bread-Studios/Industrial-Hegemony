package lettuce.lover.technologicalascendancy.blocks;

import lettuce.lover.technologicalascendancy.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ComplexBlockEntity extends BlockEntity {
    public static final String ITEMS_TAG = "Inventory";

    // Defines how many slots the ItemStackHandler capability will have
    public static int SLOT_COUNT = 1;
    public static int SLOT = 0;

    // ItemStackHandler that is used in the registration of the block entity's capability.
    private final ItemStackHandler items = createItemHandler();
    // Creates a Lazy reference to the capability. This delays the creation of the capability instance until it is actually needed.
    private final Lazy<IItemHandler> itemHandler = Lazy.of(() -> items);

    // Constructor of the Complex Block Entity.
    public ComplexBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.COMPLEX_BLOCK_ENTITY.get(), pos, state);
    }

    // Creates the ItemStackHandler used in registration of the capability with a slot count = SLOT_COUNT.
    @NotNull
    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(SLOT_COUNT) {
            // Overrides the default ItemStackHandler's onContentsChanged function because it is an empty function by default.
            @Override
            protected void onContentsChanged(int slot) {
                // MUST call setChanged() every time block entity data is changed or else it will get skipped over during saving and loading.
                setChanged();
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            }
        };
    }

    // Method to return the block entities IItemHandler; used during registration of the capability
    public IItemHandler getItemHandler() {
        return itemHandler.get();
    }

    /*
    The four methods below this comment (saveAdditional, load, saveClientData, loadClientData)
    all deal with the saving and loading of NBT data on the client's side.
     */

    // Saves additional data to the entity's NBT tags/data
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveClientData(tag);
    }
    // Loads the entity's NBT tags/data
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadClientData(tag);
    }

    // Serializes the NBT data of this entity and puts it into the NBT tags under ITEMS_TAG ("Inventory")
    private void saveClientData(CompoundTag tag) {
        tag.put(ITEMS_TAG, items.serializeNBT());
    }
    // If the entity's NBT data includes the ITEMS_TAG this method deserializes that NBT data
    private void loadClientData(CompoundTag tag) {
        if (tag.contains(ITEMS_TAG)) {
            items.deserializeNBT(tag.getCompound(ITEMS_TAG));
        }
    }

    /*
    The methods below this comment deal with server and client synchronization.
     */

    /*
    The getUpdateTag()/handleUpdateTag() pair of methods is called whenever the client receives a new chunk;
    i.e. a chunk is loaded.
     */

    // Would normally just return tag, but we override it in order to
    // save data client side (saveClientData()) before the tag is returned.
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveClientData(tag);
        return tag;
    }
    // If the update tag is not null, we load data from the tag.
    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if (tag != null) {
            loadClientData(tag);
        }
    }

    /*
    The getUpdatePacket()/onDataPacket() pair of methods is called when a block update happens on the client
    (a blockstate changes or server sends an explicit notification of a block update). It is easiest to implement
    these based on getUpdateTag()/handleUpdateTag()
     */


    @NotNull
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        // This is called on the client side
        CompoundTag tag = pkt.getTag();
        // This will call loadClientData()
        if (tag != null) {
            handleUpdateTag(tag);
        }
    }

    public void tickServer() {
        if (level.getGameTime() % 20 == 0) {
            ItemStack stack = items.getStackInSlot(SLOT);
            if (!stack.isEmpty()) {
                if (stack.isDamageableItem()) {
                    int value = stack.getDamageValue();
                    if (value > 0) {
                        stack.setDamageValue(value - 1);
                    } else {
                        ejectItem();
                    }
                } else {
                    ejectItem();
                }
            }
        }
    }

    private void ejectItem() {
        BlockPos pos = worldPosition.relative(Direction.UP);
        Block.popResource(level, pos, items.extractItem(SLOT, 1, false));
    }
}
