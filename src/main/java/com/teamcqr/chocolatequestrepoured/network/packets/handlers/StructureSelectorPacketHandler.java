package com.teamcqr.chocolatequestrepoured.network.packets.handlers;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toServer.StructureSelectorPacket;
import com.teamcqr.chocolatequestrepoured.objects.items.ItemStructureSelector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StructureSelectorPacketHandler implements IMessageHandler<StructureSelectorPacket, IMessage> {

	@Override
	public IMessage onMessage(StructureSelectorPacket message, MessageContext ctx) {
		FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
			EntityPlayer player = CQRMain.proxy.getPlayer(ctx);
			ItemStack stack = player.getHeldItem(message.getHand());
			if (stack.getItem() instanceof ItemStructureSelector) {
				ItemStructureSelector.setFirstPos(stack, player.getPosition(), player);
			}
		});
		return null;
	}

}
