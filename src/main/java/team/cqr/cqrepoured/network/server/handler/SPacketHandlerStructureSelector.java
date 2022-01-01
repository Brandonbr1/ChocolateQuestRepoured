package team.cqr.cqrepoured.network.server.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.item.ItemStructureSelector;
import team.cqr.cqrepoured.network.client.packet.CPacketStructureSelector;

public class SPacketHandlerStructureSelector implements IMessageHandler<CPacketStructureSelector, IMessage> {

	@Override
	public IMessage onMessage(CPacketStructureSelector message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
				PlayerEntity player = CQRMain.proxy.getPlayer(ctx);
				ItemStack stack = player.getHeldItem(message.getHand());

				if (stack.getItem() instanceof ItemStructureSelector) {
					((ItemStructureSelector) stack.getItem()).setFirstPos(stack, new BlockPos(player));
				}
			});
		}
		return null;
	}

}
