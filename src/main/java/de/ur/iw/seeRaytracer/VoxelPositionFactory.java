package de.ur.iw.seeRaytracer;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class VoxelPositionFactory {

    private VoxelPositionFactory() {
    }

    public static VoxelPosition createFromIndices(int x, int y, int z) {
        return new VoxelPosition(x, y, z);
    }

    public static VoxelPosition createFromIndexArray(int[] array) {
        assert (array != null);
        Preconditions.checkArgument(array.length == 3);
        return new VoxelPosition(array[0], array[1], array[2]);
    }

    public static VoxelPosition createFromRealSpaceVector(Vector3D v) {
        int x = (int) Math.floor(v.getX() / Voxel.VOXEL_WIDTH);
        int y = (int) Math.floor(v.getY() / Voxel.VOXEL_WIDTH);
        int z = (int) Math.floor(v.getZ() / Voxel.VOXEL_WIDTH);
        return new VoxelPosition(x, y, z);
    }


}
