package team.cqr.cqrepoured.world.structure.generation.generators;

import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;

public class SuspensionBridgeHelper {

	private float tension = 5.0F;
	private int width = 5;

	private final BlockPos startPos, endPos;

	private BlockState pathBlock, fenceBlock, railingBlock, anchorBlock;

	public interface IBridgeDataSupplier {
		float getBridgeTension();

		int getBridgeWidth();

		BlockState getBridgePathBlock();

		BlockState getBridgeFenceBlock();

		BlockState getBridgeRailingBlock();

		BlockState getBridgeAnchorBlock();
	}

	public SuspensionBridgeHelper(IBridgeDataSupplier data, BlockPos start, BlockPos end) {
		this(data.getBridgeTension(), data.getBridgeWidth(), start, end, data.getBridgePathBlock(), data.getBridgeFenceBlock(), data.getBridgeRailingBlock(), data.getBridgeAnchorBlock());
	}

	/*
	 * @param bridgePoints needs to contain at least two elements
	 */
	public SuspensionBridgeHelper(float tension, int width, BlockPos start, BlockPos end, BlockState pathBlock, BlockState fenceBlock, BlockState railingBlock, BlockState anchorBlock) {
		this.tension = tension;
		this.width = width;
		this.pathBlock = pathBlock;
		this.fenceBlock = fenceBlock;
		this.anchorBlock = anchorBlock;
		this.railingBlock = railingBlock;

		this.startPos = start;
		this.endPos = end;
	}

	// DONE: Return stateMap or add parameter for statemap

	public boolean generate(Map<BlockPos, BlockState> stateMap) {

		this.saggyPath(this.startPos, this.endPos, stateMap);

		return true;
	}

	private void saggyPath(BlockPos currentPos, BlockPos nextPos, Map<BlockPos, BlockState> stateMap) {
		double dx = nextPos.getX() - currentPos.getX();
		double dy = nextPos.getY() - currentPos.getY();
		double dz = nextPos.getZ() - currentPos.getZ();

		double distHorizontal = dx * dx + dz * dz;
		double distance = Math.sqrt(dy * dy + distHorizontal);

		double phi = Math.atan2(dy, Math.sqrt(distHorizontal));
		double theta = Math.atan2(dz, dx);
		double toTheRight = theta + Math.PI / 2;
		double toTheLeft = theta - Math.PI / 2;

		double lx = (currentPos.getX() + (this.width / 2) * Math.cos(toTheLeft));
		double lz = (currentPos.getZ() + (this.width / 2) * Math.sin(toTheLeft));
		double rx = (currentPos.getX() + (this.width / 2) * Math.cos(toTheRight));
		double rz = (currentPos.getZ() + (this.width / 2) * Math.sin(toTheRight));

		double ddx = rx - lx;
		double ddz = rz - lz;

		double dist = Math.ceil(Math.sqrt(ddx * ddx + ddz * ddz));

		double dthetax = 1, dthetaz = 1;

		double i = 0;
		while (i <= dist) {
			dthetax = Math.cos(toTheRight);
			dthetaz = Math.sin(toTheRight);

			double startX = lx + dthetax * i;
			double startZ = lz + dthetaz * i;

			this.drawSaggyArc(this.pathBlock, new BlockPos(startX, currentPos.getY(), startZ), theta, phi, distance, stateMap);

			i += 0.5;
		}

		for (int iterY = 1; iterY <= 2; iterY++) {
			this.drawSaggyArc(this.fenceBlock, new BlockPos(lx, currentPos.getY() + iterY, lz), theta, phi, distance, stateMap);
			this.drawSaggyArc(this.fenceBlock, new BlockPos(lx + dthetax * dist, currentPos.getY() + iterY, lz + dthetaz * dist), theta, phi, distance, stateMap);
		}
		for (int iterY = 2; iterY <= 3; iterY++) {
			this.drawSaggyArc(this.railingBlock, new BlockPos(lx, currentPos.getY() + iterY, lz), theta, phi, distance, stateMap);
			this.drawSaggyArc(this.railingBlock, new BlockPos(lx + dthetax * dist, currentPos.getY() + iterY, lz + dthetaz * dist), theta, phi, distance, stateMap);
		}

		BlockPos lxPos = new BlockPos(lx, currentPos.getY(), lz);

		this.drawLine(this.anchorBlock, lxPos, lxPos.offset(0, 2, 0), stateMap);
		this.drawLine(this.anchorBlock, lxPos.offset(dthetax * dist, 0, dthetax * dist), lxPos.offset(dthetax * dist, 2, dthetax * dist), stateMap);
		this.drawLine(this.anchorBlock, lxPos.offset(distance * Math.cos(theta) * Math.cos(phi), distance * Math.sin(phi), distance * Math.sin(theta) * Math.cos(phi)), lxPos.offset(distance * Math.cos(theta) * Math.cos(phi), 3 + distance * Math.sin(phi), distance * Math.sin(theta) * Math.cos(phi)), stateMap);
		this.drawLine(this.anchorBlock, lxPos.offset(dthetax * dist + distance * Math.cos(theta) * Math.cos(phi), distance * Math.sin(phi), dthetaz * dist + distance * Math.sin(theta) * Math.cos(phi)),
				lxPos.offset(distance * Math.cos(theta) * Math.cos(phi), 3 + distance * Math.sin(phi), distance * Math.sin(theta) * Math.cos(phi)), stateMap);
	}

	private void drawSaggyArc(BlockState material, BlockPos pos, double theta, double phi, double distance, Map<BlockPos, BlockState> stateMap) {
		double midPoint = distance / 2;
		double scale = distance / this.tension;

		BlockPos p = new BlockPos(0, 0, 0);
		double iterator = 0;
		while (iterator <= distance) {
			double xx = (iterator - midPoint) / midPoint;
			double ddy = xx * xx * scale;
			BlockPos n = new BlockPos((int) (pos.getX() + iterator * Math.cos(theta) * Math.cos(phi)), (int) (pos.getY() + iterator * Math.sin(phi) + ddy - scale), (int) (pos.getZ() + iterator * Math.sin(theta) * Math.cos(phi)));
			if (p.distSqr(Vector3i.ZERO) != 0.0) {
				this.drawLine(material, p, n, stateMap);
			}

			p = n;
			iterator += 0.5;
		}
	}

	private void drawLine(BlockState material, BlockPos p, BlockPos n, Map<BlockPos, BlockState> stateMap) {
		this.drawLineConstrained(material, p, n, stateMap, 0);
	}

	private void drawLineConstrained(BlockState material, BlockPos p, BlockPos n, Map<BlockPos, BlockState> stateMap, int maxLength) {
		int dx = n.getX() - p.getX();
		int dy = n.getY() - p.getY();
		int dz = n.getZ() - p.getZ();

		double distHoriz = dx * dx + dz * dz;
		double distance = Math.sqrt(dy * dy + distHoriz);

		if (distance < maxLength || maxLength < 1) {
			double phi = Math.atan2(dy, Math.sqrt(distHoriz));
			double theta = Math.atan2(dz, dx);

			double iterator = 0;
			while (iterator <= distance) {
				stateMap.put(p.offset(iterator * Math.cos(theta) * Math.cos(phi), iterator * Math.sin(phi), iterator * Math.sin(theta) * Math.cos(phi)), material);
				iterator += 0.5D;
			}
		}
	}

}
