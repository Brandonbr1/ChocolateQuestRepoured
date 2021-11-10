package team.cqr.cqrepoured.network.server.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import team.cqr.cqrepoured.config.CQRConfig;

public class SPacketSyncProtectionConfig implements IMessage {

	private CQRConfig.DungeonProtection protectionConfig;

	public SPacketSyncProtectionConfig() {

	}

	public SPacketSyncProtectionConfig(CQRConfig.DungeonProtection protectionConfig) {
		this.protectionConfig = protectionConfig;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.protectionConfig = new CQRConfig.DungeonProtection();
		this.protectionConfig.protectionSystemEnabled = buf.readBoolean();
		this.protectionConfig.enablePreventBlockBreaking = buf.readBoolean();
		this.protectionConfig.enablePreventBlockPlacing = buf.readBoolean();
		this.protectionConfig.enablePreventEntitySpawning = buf.readBoolean();
		this.protectionConfig.enablePreventExplosionOther = buf.readBoolean();
		this.protectionConfig.enablePreventExplosionTNT = buf.readBoolean();
		this.protectionConfig.enablePreventFireSpreading = buf.readBoolean();
		this.protectionConfig.protectionSystemBreakableBlockWhitelist = new String[buf.readInt()];
		for (int i = 0; i < this.protectionConfig.protectionSystemBreakableBlockWhitelist.length; i++) {
			this.protectionConfig.protectionSystemBreakableBlockWhitelist[i] = ByteBufUtils.readUTF8String(buf);
		}
		this.protectionConfig.protectionSystemBreakableMaterialWhitelist = new String[buf.readInt()];
		for (int i = 0; i < this.protectionConfig.protectionSystemBreakableMaterialWhitelist.length; i++) {
			this.protectionConfig.protectionSystemBreakableMaterialWhitelist[i] = ByteBufUtils.readUTF8String(buf);
		}
		this.protectionConfig.protectionSystemPlaceableBlockWhitelist = new String[buf.readInt()];
		for (int i = 0; i < this.protectionConfig.protectionSystemPlaceableBlockWhitelist.length; i++) {
			this.protectionConfig.protectionSystemPlaceableBlockWhitelist[i] = ByteBufUtils.readUTF8String(buf);
		}
		this.protectionConfig.protectionSystemPlaceableMaterialWhitelist = new String[buf.readInt()];
		for (int i = 0; i < this.protectionConfig.protectionSystemPlaceableMaterialWhitelist.length; i++) {
			this.protectionConfig.protectionSystemPlaceableMaterialWhitelist[i] = ByteBufUtils.readUTF8String(buf);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(this.protectionConfig.protectionSystemEnabled);
		buf.writeBoolean(this.protectionConfig.enablePreventBlockBreaking);
		buf.writeBoolean(this.protectionConfig.enablePreventBlockPlacing);
		buf.writeBoolean(this.protectionConfig.enablePreventEntitySpawning);
		buf.writeBoolean(this.protectionConfig.enablePreventExplosionOther);
		buf.writeBoolean(this.protectionConfig.enablePreventExplosionTNT);
		buf.writeBoolean(this.protectionConfig.enablePreventFireSpreading);
		buf.writeInt(this.protectionConfig.protectionSystemBreakableBlockWhitelist.length);
		for (String s : this.protectionConfig.protectionSystemBreakableBlockWhitelist) {
			ByteBufUtils.writeUTF8String(buf, s);
		}
		buf.writeInt(this.protectionConfig.protectionSystemBreakableMaterialWhitelist.length);
		for (String s : this.protectionConfig.protectionSystemBreakableMaterialWhitelist) {
			ByteBufUtils.writeUTF8String(buf, s);
		}
		buf.writeInt(this.protectionConfig.protectionSystemPlaceableBlockWhitelist.length);
		for (String s : this.protectionConfig.protectionSystemPlaceableBlockWhitelist) {
			ByteBufUtils.writeUTF8String(buf, s);
		}
		buf.writeInt(this.protectionConfig.protectionSystemPlaceableMaterialWhitelist.length);
		for (String s : this.protectionConfig.protectionSystemPlaceableMaterialWhitelist) {
			ByteBufUtils.writeUTF8String(buf, s);
		}
	}

	public CQRConfig.DungeonProtection getProtectionConfig() {
		return this.protectionConfig;
	}

}
