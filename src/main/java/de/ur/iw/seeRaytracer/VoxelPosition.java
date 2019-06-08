package de.ur.iw.seeRaytracer;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class VoxelPosition {

    private int x;
    private int y;
    private int z;

    public VoxelPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public VoxelPosition add(VoxelPosition vp) {
        return this.addX(vp.x).addY(vp.y).addZ(vp.z);
    }

    @SuppressWarnings("WeakerAccess")
    public VoxelPosition addX(int x) {
        return new VoxelPosition(this.x + x, this.y, this.z);
    }

    @SuppressWarnings("WeakerAccess")
    public VoxelPosition addY(int y) {
        return new VoxelPosition(this.x, this.y + y, this.z);
    }

    @SuppressWarnings("WeakerAccess")
    public VoxelPosition addZ(int z) {
        return new VoxelPosition(this.x, this.y, this.z + z);
    }

    public static VoxelPosition minForEachDimension(VoxelPosition[] positions) {
        assert (positions != null);
        Preconditions.checkArgument(positions.length > 0);
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        for (VoxelPosition p : positions) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            minZ = Math.min(minZ, p.getZ());
        }

        return new VoxelPosition(minX, minY, minZ);
    }

    public static VoxelPosition maxForEachDimension(VoxelPosition[] positions) {
        assert (positions != null);
        Preconditions.checkArgument(positions.length > 0);
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (VoxelPosition p : positions) {
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
            maxZ = Math.max(maxZ, p.getZ());
        }

        return new VoxelPosition(maxX, maxY, maxZ);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        VoxelPosition v = (VoxelPosition) o;
        boolean result = x == v.x;
        result &= y == v.y;
        result &= z == v.z;
        return result;
    }

    @Override
    public int hashCode() {
        return 137 * x + 149 * y + 163 * z;
    }


    public Vector3D toRealSpaceVector3D() {
        return new Vector3D(x * Voxel.VOXEL_WIDTH, y * Voxel.VOXEL_WIDTH, z * Voxel.VOXEL_WIDTH);
    }


}
