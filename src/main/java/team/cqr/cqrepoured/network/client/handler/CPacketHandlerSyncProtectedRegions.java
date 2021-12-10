package team.cqr.cqrepoured.network.client.handler;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.network.server.packet.SPacketSyncProtectedRegions;
import team.cqr.cqrepoured.world.structure.protection.IProtectedRegionManager;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegion;
import team.cqr.cqrepoured.world.structure.protection.ProtectedRegionManager;

public class CPacketHandlerSyncProtectedRegions implements IMessageHandler<SPacketSyncProtectedRegions, IMessage> {

	@Override
	public IMessage onMessage(SPacketSyncProtectedRegions message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				World world = CQRMain.proxy.getWorld(ctx);
				IProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(world);
				ByteBuf buf = message.getBuffer();

				if (buf.readBoolean()) {
					protectedRegionManager.clearProtectedRegions();
				}

				int protectedRegionsCount = buf.readShort();
				for (int i = 0; i < protectedRegionsCount; i++) {
					protectedRegionManager.addProtectedRegion(new ProtectedRegion(world, buf));
				}
			});
		}
		return null;
	}

}
