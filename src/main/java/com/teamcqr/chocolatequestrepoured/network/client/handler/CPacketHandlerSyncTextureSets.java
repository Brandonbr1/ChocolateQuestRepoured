package com.teamcqr.chocolatequestrepoured.network.client.handler;

import com.teamcqr.chocolatequestrepoured.customtextures.ClientPacketHandler;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketCustomTextures;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketHandlerSyncTextureSets implements IMessageHandler<SPacketCustomTextures, IMessage> {

	@Override
	public IMessage onMessage(SPacketCustomTextures message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				ClientPacketHandler.handleCTPacketClientside(message);
			});
		}
		return null;
	}

}
