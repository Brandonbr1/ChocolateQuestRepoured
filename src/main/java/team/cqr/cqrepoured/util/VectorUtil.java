package team.cqr.cqrepoured.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/**
 * Copyright (c) 29.04.2019 Developed by DerToaster98 GitHub: https://github.com/DerToaster98
 */
public class VectorUtil {

	public enum EAxis {
		AXIS_Y, AXIS_X, AXIS_Z;
	}

	public static Vec3d rotateVectorAroundY(Vec3d vector, double degrees) {
		return vector.rotateYaw((float) Math.toRadians(degrees));
	}

	public static Vec3i rotateVector(EAxis axis, Vec3i vector, Double degrees) {
		double rad = Math.toRadians(degrees);

		double currentX = vector.getX();
		double currentZ = vector.getZ();
		double currentY = vector.getY();

		double cosine = Math.cos(rad);
		double sine = Math.sin(rad);

		switch (axis) {
		case AXIS_X:
			return new Vec3i(vector.getX(), (currentY * cosine - currentZ * sine), (currentY * sine - currentZ * cosine));
		case AXIS_Y:
			return new Vec3i((cosine * currentX - sine * currentZ), vector.getY(), (sine * currentX + cosine * currentZ));
		case AXIS_Z:
			return new Vec3i((currentX * cosine - currentY * sine), (currentX * sine - currentY * cosine), vector.getZ());
		default:
			break;

		}
		return null;
	}

	public static Vec3i rotateVectorAroundY(Vec3i newPos, double degrees) {
		Vec3d res = rotateVectorAroundY(new Vec3d(newPos.getX(), newPos.getY(), newPos.getZ()), degrees);
		return new Vec3i(Math.floor(res.x), Math.floor(res.y), Math.floor(res.z));
	}

	public static Vec3i vectorAdd(Vec3i start, int x, int y, int z) {
		return new Vec3i(start.getX() + x, start.getY() + y, start.getZ() + z);
	}

	public static NBTTagCompound createVectorNBTTag(Vec3d vector) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setDouble("X", vector.x);
		nbttagcompound.setDouble("Y", vector.y);
		nbttagcompound.setDouble("Z", vector.z);
		return nbttagcompound;
	}

	public static Vec3d getVectorFromTag(NBTTagCompound tag) {
		return new Vec3d(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"));
	}

	public static Vec3d rotateAroundAnyAxis(Vec3d axis, Vec3d toBeRotated, double rotationDegrees) {
		// Mathematical theory => Rotation matrices
		// https://de.wikipedia.org/wiki/Drehmatrix
		final double alpha = Math.toRadians(rotationDegrees);
		final Vec3d normaledAxis = axis.normalize();
		final Vec3d axisCrossPoint = normaledAxis.crossProduct(toBeRotated);
		Vec3d rotated = normaledAxis.scale(normaledAxis.dotProduct(toBeRotated));
		rotated = rotated.add(axisCrossPoint.scale(Math.cos(alpha)).crossProduct(normaledAxis));
		rotated = rotated.add(axisCrossPoint.scale(Math.sin(alpha)));

		return rotated;
	}

}
