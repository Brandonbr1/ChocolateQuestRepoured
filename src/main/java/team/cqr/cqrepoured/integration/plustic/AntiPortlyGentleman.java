package team.cqr.cqrepoured.integration.plustic;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQRBoss;

@EventBusSubscriber
public class AntiPortlyGentleman {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void blockPortlyRelocatorOnBosses(PlayerInteractEvent.EntityInteract event) {
		ItemStack tool = event.getItemStack();
		if (tool.isEmpty() || !tool.hasTagCompound() || !event.getEntityPlayer().isSneaking()) {
			return;
		}

		if (event.getEntityLiving() instanceof AbstractEntityCQRBoss) {
			CompoundNBT itemNBT = tool.getTagCompound();
			if (itemNBT.hasKey("Traits", Constants.NBT.TAG_STRING)) {
				ListNBT tagList = itemNBT.getTagList("Traits", Constants.NBT.TAG_STRING);
				for (int i = 0; i < tagList.tagCount(); i++) {
					if (tagList.getStringTagAt(i).equals("portly")) {
						event.setCanceled(true);

						event.getEntityPlayer().sendMessage(new StringTextComponent("Hmm... this doesn't seem to work for bosses..."));

						return;
					}
				}
			}
		}

	}
}
