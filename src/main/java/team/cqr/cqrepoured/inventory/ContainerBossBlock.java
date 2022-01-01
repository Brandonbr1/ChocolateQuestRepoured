package team.cqr.cqrepoured.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import team.cqr.cqrepoured.item.ItemSoulBottle;
import team.cqr.cqrepoured.tileentity.TileEntityBoss;

public class ContainerBossBlock extends Container {

	private final TileEntityBoss tileEntity;

	public ContainerBossBlock(PlayerInventory playerInv, TileEntityBoss tileentity) {
		this.tileEntity = tileentity;
		IItemHandler inventory = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 50 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 108));
		}

		this.addSlotToContainer(new SlotItemHandler(inventory, 0, 80, 18) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof ItemSoulBottle && stack.hasTagCompound() && stack.getTagCompound().hasKey("EntityIn");
			}
		});
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		if (!playerIn.isCreative()) {
			return false;
		}
		if (playerIn.world.getTileEntity(this.tileEntity.getPos()) != this.tileEntity) {
			return false;
		}
		return playerIn.getDistanceSqToCenter(this.tileEntity.getPos()) <= 64.0D;
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			ItemStack itemstack = itemstack1.copy();

			if (index > 35) {
				if (this.mergeItemStack(itemstack1, 0, 36, false)) {
					return itemstack;
				}
			} else if (this.mergeItemStack(itemstack1, 36, this.inventorySlots.size(), false)) {
				return itemstack;
			} else if (index > 26) {
				if (this.mergeItemStack(itemstack1, 0, 27, false)) {
					return itemstack;
				}
			} else {
				if (this.mergeItemStack(itemstack1, 27, 36, false)) {
					return itemstack;
				}
			}
		}

		return ItemStack.EMPTY;
	}

}
