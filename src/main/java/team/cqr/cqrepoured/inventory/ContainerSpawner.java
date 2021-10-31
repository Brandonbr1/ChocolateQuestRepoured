package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.cqr.cqrepoured.objects.items.ItemSoulBottle;
import team.cqr.cqrepoured.tileentity.TileEntitySpawner;

public class ContainerSpawner extends Container {

	private final TileEntitySpawner tileEntity;

	public ContainerSpawner(InventoryPlayer playerInv, TileEntitySpawner tileentity) {
		this.tileEntity = tileentity;
		IItemHandler inventory = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
		}

		for (int l = 0; l < 3; l++) {
			for (int m = 0; m < 3; m++) {
				this.addSlotToContainer(new SlotItemHandler(inventory, m + l * 3, 62 + m * 18, 17 + l * 18) {
					@Override
					public void onSlotChanged() {
						tileentity.markDirty();
					}

					@Override
					public boolean isItemValid(ItemStack stack) {
						return stack.getItem() instanceof ItemSoulBottle && stack.hasTagCompound() && stack.getTagCompound().hasKey("EntityIn");
					}
				});
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		if (!playerIn.isCreative()) {
			return false;
		}
		if (playerIn.world.getTileEntity(this.tileEntity.getPos()) != this.tileEntity) {
			return false;
		}
		return playerIn.getDistanceSqToCenter(this.tileEntity.getPos()) <= 64.0D;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			ItemStack itemstack = itemstack1.copy();

			if (index > 35) {
				if (this.mergeItemStack(itemstack1, 0, 36, false)) {
					return itemstack;
				}
			} else {
				if (this.mergeItemStack(itemstack1, 36, this.inventorySlots.size(), false)) {
					return itemstack;
				}
			}
		}

		return ItemStack.EMPTY;
	}

}
