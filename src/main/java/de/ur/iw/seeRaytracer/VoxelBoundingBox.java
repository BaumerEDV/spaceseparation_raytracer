package de.ur.iw.seeRaytracer;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class VoxelBoundingBox {

    private VoxelPosition min;
    private VoxelPosition max;

    public VoxelBoundingBox(VoxelPosition[] positions) {
        assert (positions != null);
        Preconditions.checkArgument(positions.length > 0);

        min = VoxelPosition.minForEachDimension(positions);
        max = VoxelPosition.maxForEachDimension(positions);
    }


    public double realSpaceEuclideanDiagonalLength() {
        Vector3D min = this.min.toRealSpaceVector3D();
        @SuppressWarnings("SuspiciousNameCombination")
        Vector3D max = this.max.toRealSpaceVector3D()
                .add(new Vector3D(Voxel.VOXEL_WIDTH, Voxel.VOXEL_WIDTH, Voxel.VOXEL_WIDTH));
        Vector3D diagonal = max.subtract(min);
        return diagonal.getNorm();
    }

    public VoxelPosition min() {
        return min;
    }

    public VoxelPosition max() {
        return max;
    }


}
