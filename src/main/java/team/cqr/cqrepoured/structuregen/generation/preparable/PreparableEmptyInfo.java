package team.cqr.cqrepoured.structuregen.generation.preparable;

import java.util.function.Supplier;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.cqr.cqrepoured.init.CQRBlocks;
import team.cqr.cqrepoured.structuregen.generation.DungeonPlacement;
import team.cqr.cqrepoured.structuregen.generation.generatable.GeneratableBlockInfo;
import team.cqr.cqrepoured.structuregen.generation.generatable.GeneratablePosInfo;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparablePosInfo.Registry.IFactory;
import team.cqr.cqrepoured.structuregen.generation.preparable.PreparablePosInfo.Registry.ISerializer;
import team.cqr.cqrepoured.structuregen.structurefile.BlockStatePalette;

public class PreparableEmptyInfo extends PreparablePosInfo {

	public PreparableEmptyInfo(BlockPos pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
	}

	public PreparableEmptyInfo(int x, int y, int z) {
		super(x, y, z);
	}

	@Override
	protected GeneratablePosInfo prepare(World world, DungeonPlacement placement, BlockPos pos) {
		return null;
	}

	@Override
	protected GeneratablePosInfo prepareDebug(World world, DungeonPlacement placement, BlockPos pos) {
		return new GeneratableBlockInfo(pos, CQRBlocks.NULL_BLOCK.getDefaultState(), null);
	}

	public static class Factory implements IFactory<TileEntity> {

		@Override
		public PreparablePosInfo create(World world, int x, int y, int z, IBlockState state, Supplier<TileEntity> tileEntitySupplier) {
			return new PreparableEmptyInfo(x, y, z);
		}

	}

	public static class Serializer implements ISerializer<PreparableEmptyInfo> {

		@Override
		public void write(PreparableEmptyInfo preparable, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			// nothing to write
		}

		@Override
		public PreparableEmptyInfo read(int x, int y, int z, ByteBuf buf, BlockStatePalette palette, NBTTagList nbtList) {
			return new PreparableEmptyInfo(x, y, z);
		}

		@Override
		@Deprecated
		public PreparableEmptyInfo read(int x, int y, int z, NBTTagIntArray nbtIntArray, BlockStatePalette palette, NBTTagList nbtList) {
			return new PreparableEmptyInfo(x, y, z);
		}

	}

}
