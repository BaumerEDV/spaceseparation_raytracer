package de.ur.iw.seeRaytracer;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class VoxelBoundingBox {

    private VoxelPos min;
    private VoxelPos max;

    public VoxelBoundingBox(VoxelPos[] positions){
        assert (positions != null);
        Preconditions.checkArgument(positions.length > 0);

        min = VoxelPos.minForEachDimension(positions);
        max = VoxelPos.maxForEachDimension(positions);
    }


    public double realSpaceEuclidianDiagonalLength(){
        Vector3D min = this.min.toRealSpaceVector3D();
        Vector3D max = this.max.toRealSpaceVector3D().add(new Vector3D(Voxel.VOXEL_WIDTH, Voxel.VOXEL_WIDTH, Voxel.VOXEL_WIDTH));
        Vector3D diagonal = max.subtract(min);
        return diagonal.getNorm();
    }

    public VoxelPos min(){
        return min;
    }

    public VoxelPos max(){
        return max;
    }




}
