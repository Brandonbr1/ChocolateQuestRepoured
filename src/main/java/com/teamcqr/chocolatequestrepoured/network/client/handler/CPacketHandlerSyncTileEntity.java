package com.teamcqr.chocolatequestrepoured.network.client.handler;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.datasync.DataEntry;
import com.teamcqr.chocolatequestrepoured.network.datasync.TileEntityDataManager;
import com.teamcqr.chocolatequestrepoured.network.server.packet.SPacketSyncTileEntity;
import com.teamcqr.chocolatequestrepoured.tileentity.ITileEntitySyncable;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketHandlerSyncTileEntity implements IMessageHandler<SPacketSyncTileEntity, IMessage> {

	@Override
	public IMessage onMessage(SPacketSyncTileEntity message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				TileEntity tileEntity = world.getTileEntity(message.getPos());

				if (tileEntity instanceof ITileEntitySyncable) {
					TileEntityDataManager dataManager = ((ITileEntitySyncable) tileEntity).getDataManager();
					ByteBuf buf = message.getBuffer();

					int size = ByteBufUtils.readVarInt(buf, 5);
					for (int i = 0; i < size; i++) {
						int id = ByteBufUtils.readVarInt(buf, 5);
						DataEntry<?> entry = dataManager.getById(id);
						if (entry == null) {
							throw new IllegalArgumentException(String.format("No tile entity data manager entry found for id %s.", id));
						} else {
							entry.readChanges(buf);
						}
					}
				}

				CQRMain.proxy.updateGui();
			});
		}
		return null;
	}

}
