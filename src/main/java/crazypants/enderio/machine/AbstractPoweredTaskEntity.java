package crazypants.enderio.machine;

import java.util.Random;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractPoweredTaskEntity extends AbstractMachineEntity implements ISidedInventory {

  protected PoweredTask currentTask = null;
  protected IMachineRecipe lastCompletedRecipe;

  private final Random random = new Random();

  protected int ticksSinceCheckedRecipe = 0;
  protected boolean startFailed = false;

  public AbstractPoweredTaskEntity(SlotDefinition slotDefinition) {
    super(slotDefinition);
  }

  @Override
  public int[] getAccessibleSlotsFromSide(int var1) {
    int[] res = new int[inventory.length - slotDefinition.getNumUpgradeSlots()];
    int index = 0;
    for (int i = 0; i < inventory.length; i++) {
      if(!slotDefinition.isUpgradeSlot(i)) {
        res[index] = i;
        index++;
      }
    }
    return res;
  }

  @Override
  public boolean canInsertItem(int i, ItemStack itemstack, int j) {

    if(!slotDefinition.isInputSlot(i)) {
      return false;
    }
    if(!isItemValidForSlot(i, itemstack)) {
      return false;
    }
    if(inventory[i] == null) {
      return true;
    }
    return inventory[i].isItemEqual(itemstack);
  }

  @Override
  public boolean canExtractItem(int i, ItemStack itemstack, int j) {
    if(!slotDefinition.isOutputSlot(i)) {
      return false;
    }
    if(inventory[i] == null || inventory[i].stackSize < itemstack.stackSize) {
      return false;
    }
    return itemstack.itemID == inventory[i].itemID;
  }

  @Override
  public boolean isActive() {
    return currentTask == null ? false : currentTask.getProgress() > 0 && hasPower() && redstoneCheckPassed;
  }

  @Override
  public float getProgress() {
    return currentTask == null ? 0 : currentTask.getProgress();
  }

  public float getExperienceForOutput(ItemStack output) {
    if(lastCompletedRecipe == null) {
      return 0;
    }
    return lastCompletedRecipe.getExperianceForOutput(output);
  }

  @Override
  protected boolean processTasks(boolean redstoneChecksPassed) {

    if(!redstoneChecksPassed) {
      return false;
    }

    boolean requiresClientSync = false;
    // Process any current items
    requiresClientSync |= checkProgress();

    if(currentTask != null || !hasPower() || !hasInputStacks()) {
      return requiresClientSync;
    }

    if(startFailed) {
      ticksSinceCheckedRecipe++;
      if(ticksSinceCheckedRecipe < 20) {
        return false;
      }
    }
    ticksSinceCheckedRecipe = 0;

    float chance = random.nextFloat();
    // Then see if we need to start a new one
    IMachineRecipe nextRecipe = canStartNextTask(chance);
    if(nextRecipe != null) {
      boolean started = startNextTask(nextRecipe, chance);
      startFailed = !started;
      requiresClientSync |= started;
    } else {
      startFailed = true;
    }
    return requiresClientSync;
  }

  protected boolean checkProgress() {
    if(currentTask == null || !hasPower()) {
      return false;
    }

    float used = Math.min(powerHandler.getEnergyStored(), getPowerUsePerTick());
    powerHandler.setEnergy(powerHandler.getEnergyStored() - used);
    currentTask.update(used);

    // then check if we are done
    if(currentTask.isComplete()) {
      taskComplete();
      return true;
    }
    return worldObj.getWorldTime() % 10 == 0;
  }

  protected void taskComplete() {

    if(currentTask != null) {
      lastCompletedRecipe = currentTask.getRecipe();
      ItemStack[] output = currentTask.getCompletedResult();
      if(output != null && output.length > 0) {
        ItemStack[] results = currentTask.getCompletedResult();
        for (ItemStack result : results) {
          if(result != null) {
            int toMerge = result.stackSize;
            for (int i = slotDefinition.getMinOutputSlot(); i <= slotDefinition.getMaxOutputSlot() && toMerge > 0; i++) {
              int outputIndex = i;
              if(inventory[outputIndex] == null) {
                inventory[outputIndex] = result.copy();
                toMerge = 0;
              } else {
                int newStackSize = Math.min(inventory[outputIndex].stackSize + getNumCanMerge(inventory[outputIndex], result),
                    inventory[outputIndex].getMaxStackSize());
                int merged = newStackSize - inventory[outputIndex].stackSize;
                toMerge -= merged;
                if(merged > 0) {
                  inventory[outputIndex] = result.copy();
                  inventory[outputIndex].stackSize = newStackSize;
                }
              }
            }
          }
        }
      }
    }
    currentTask = null;
  }

  protected MachineRecipeInput[] getInputs() {
    MachineRecipeInput[] res = new MachineRecipeInput[slotDefinition.getNumInputSlots()];
    int fromSlot = slotDefinition.minInputSlot;
    for (int i = 0; i < res.length; i++) {
      res[i] = new MachineRecipeInput(fromSlot, inventory[fromSlot]);
      fromSlot++;
    }

    return res;
  }

  protected IMachineRecipe canStartNextTask(float chance) {
    IMachineRecipe nextRecipe = MachineRecipeRegistry.instance.getRecipeForInputs(getMachineName(), getInputs());
    if(nextRecipe == null) {
      return null; // no template
    }
    // make sure we have room for the next output
    return canInsertResult(chance, nextRecipe) ? nextRecipe : null;
  }

  protected boolean canInsertResult(float chance, IMachineRecipe nextRecipe) {

    // if we have an empty output, all good
    for (int i = slotDefinition.minOutputSlot; i <= slotDefinition.maxOutputSlot; i++) {
      if(inventory[i] == null) {
        return true;
      }
    }

    ItemStack[] nextResults = nextRecipe.getCompletedResult(chance, getInputs());
    ItemStack[] outputStacks = new ItemStack[slotDefinition.getNumOutputSlots()];
    int copyIndex = 0;
    for (int i = slotDefinition.minOutputSlot; i <= slotDefinition.maxOutputSlot; i++) {
      ItemStack inv = inventory[i];
      if(inv != null) {
        outputStacks[copyIndex] = inv.copy();
      }
      copyIndex++;
    }

    for (ItemStack result : nextResults) {
      int canMerge = 0;
      for (ItemStack outStack : outputStacks) {
        canMerge += getNumCanMerge(outStack, result);
      }
      if(canMerge < result.stackSize) {
        return false;
      }
    }

    return true;
  }

  protected boolean hasInputStacks() {
    int fromSlot = slotDefinition.minInputSlot;
    for (int i = 0; i < slotDefinition.getNumInputSlots(); i++) {
      if(inventory[fromSlot] != null) {
        return true;
      }
      fromSlot++;
    }
    return false;
  }

  protected int getNumCanMerge(ItemStack itemStack, ItemStack result) {
    if(!itemStack.isItemEqual(result)) {
      return 0;
    }
    return Math.min(itemStack.getMaxStackSize() - itemStack.stackSize, result.stackSize);
  }

  protected boolean startNextTask(IMachineRecipe nextRecipe, float chance) {
    if(hasPower() && nextRecipe.isRecipe(getInputs())) {
      // then get our recipe and take away the source items
      currentTask = new PoweredTask(nextRecipe, chance, getInputs());

      MachineRecipeInput[] consumed = nextRecipe.getQuantitiesConsumed(getInputs());
      for (MachineRecipeInput item : consumed) {
        if(item != null && item.item != null && item.item.stackSize > 0) {
          decrStackSize(item.slotNumber, item.item.stackSize);
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbtRoot) {
    super.readFromNBT(nbtRoot);
    currentTask = PoweredTask.readFromNBT(nbtRoot.getCompoundTag("currentTask"));
    String uid = nbtRoot.getString("lastCompletedRecipe");
    lastCompletedRecipe = MachineRecipeRegistry.instance.getRecipeForUid(uid);
  }

  @Override
  public void writeToNBT(NBTTagCompound nbtRoot) {
    super.writeToNBT(nbtRoot);
    if(currentTask != null) {
      NBTTagCompound currentTaskNBT = new NBTTagCompound();
      currentTask.writeToNBT(currentTaskNBT);
      nbtRoot.setCompoundTag("currentTask", currentTaskNBT);
    }
    if(lastCompletedRecipe != null) {
      nbtRoot.setString("lastCompletedRecipe", lastCompletedRecipe.getUid());
    }
  }

}
