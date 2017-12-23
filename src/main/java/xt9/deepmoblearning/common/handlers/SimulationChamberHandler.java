package xt9.deepmoblearning.common.handlers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import xt9.deepmoblearning.DeepConstants;
import xt9.deepmoblearning.common.Registry;
import xt9.deepmoblearning.common.items.ItemLivingMatter;
import xt9.deepmoblearning.common.items.ItemPolymerClay;
import xt9.deepmoblearning.common.items.ItemMobChip;
import xt9.deepmoblearning.common.items.ItemPristineMatter;

import javax.annotation.Nonnull;

/**
 * Created by xt9 on 2017-06-19.
 */
public class SimulationChamberHandler extends ItemStackHandler {
    public SimulationChamberHandler() {
        super(DeepConstants.SIMULATION_CHAMBER_INTERNAL_SLOTS);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if(!canInsertOnSlot(stack, slot)) {
            return stack;
        } else {
            return super.insertItem(slot, stack, simulate);
        }
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(!canExtractFromSlot(slot)) {
            return ItemStack.EMPTY;
        } else {
            return super.extractItem(slot, amount, simulate);
        }
    }

    @Nonnull
    public ItemStack extractItemAsPlayer(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    public boolean canExtractFromSlot(int slot) {
        ItemStack stack = getStackInSlot(slot);
        return canDoSlotOperation(stack, slot, "extract");
    }

    public boolean canInsertOnSlot(ItemStack stack, int slot) {
        return canDoSlotOperation(stack, slot, "insert");
    }

    public boolean canDoSlotOperation(ItemStack stack, int slot, String type) {
        switch(slot) {
            case DeepConstants.SIMULATION_CHAMBER_CHIP_SLOT:
                return type.equals("insert") && stack.getItem() instanceof ItemMobChip;
            case DeepConstants.SIMULATION_CHAMBER_INPUT_SLOT:
                return type.equals("insert") && stack.getItem() instanceof ItemPolymerClay;
            case DeepConstants.SIMULATION_CHAMBER_OUTPUT_SLOT:
                return type.equals("extract") && stack.getItem() instanceof ItemLivingMatter;
            case DeepConstants.SIMULATION_CHAMBER_PRISTINE_SLOT:
                return type.equals("extract") && stack.getItem() instanceof ItemPristineMatter;
            default:
                return false;
        }
    }

    @Override
    public int getSlotLimit(int slot)
    {
        if(slot == DeepConstants.SIMULATION_CHAMBER_CHIP_SLOT) {
            return 1;
        }
        return 64;
    }

    public ItemStack getChip() {
        return this.getStackInSlot(DeepConstants.SIMULATION_CHAMBER_CHIP_SLOT);
    }

    public ItemStack getInput() {
        return this.getStackInSlot(DeepConstants.SIMULATION_CHAMBER_INPUT_SLOT);
    }

    public ItemStack getOutput() {
        return this.getStackInSlot(DeepConstants.SIMULATION_CHAMBER_OUTPUT_SLOT);
    }

    public ItemStack getPristine() {
        return this.getStackInSlot(DeepConstants.SIMULATION_CHAMBER_PRISTINE_SLOT);
    }

    public boolean hasPolymerClay() {
        ItemStack stack = this.getStackInSlot(DeepConstants.SIMULATION_CHAMBER_INPUT_SLOT);
        return stack.getItem() instanceof ItemPolymerClay && stack.getCount() > 0;
    }

    public boolean hasChip() {
        return this.getStackInSlot(DeepConstants.SIMULATION_CHAMBER_CHIP_SLOT).getItem() instanceof ItemMobChip;
    }

    public boolean outputIsFull() {
        ItemStack stack = this.getOutput();
        if(stack.isEmpty()) {
            return false;
        }

        boolean stackLimitReached = stack.getCount() == this.getSlotLimit(DeepConstants.SIMULATION_CHAMBER_OUTPUT_SLOT);
        boolean outputMatches = chipMatchesOutput(this.getChip(), this.getOutput());

        return stackLimitReached || !outputMatches;
    }

    public boolean pristineIsFull() {
        ItemStack stack = this.getPristine();
        if(stack.isEmpty()) {
            return false;
        }

        boolean stackLimitReached = stack.getCount() == this.getSlotLimit(DeepConstants.SIMULATION_CHAMBER_PRISTINE_SLOT);
        boolean outputMatches = chipMatchesPristine(this.getChip(), this.getPristine());

        return stackLimitReached || !outputMatches;
    }

    public ItemStack createMatterFromMobChip(ItemStack chip, int amount) {
        ItemStack matter = new ItemStack(Registry.livingMatter, amount);
        matter.setItemDamage(ItemMobChip.getMatterType(chip));
        return matter;
    }

    public static boolean chipMatchesOutput(ItemStack chip, ItemStack stack) {
        int chipMatterType = ItemMobChip.getMatterType(chip);
        return chipMatterType == stack.getItemDamage();
    }

    public static boolean chipMatchesPristine(ItemStack chip, ItemStack stack) {
        String chipType = ItemMobChip.getSubName(chip);
        String pristineType = ItemPristineMatter.getSubName(stack);
        return chipType.equals(pristineType);
    }
}
