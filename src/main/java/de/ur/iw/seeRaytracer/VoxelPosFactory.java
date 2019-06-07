package de.ur.iw.seeRaytracer;

import com.google.common.base.Preconditions;
import jdk.jshell.spi.ExecutionControl;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class VoxelPosFactory {

    private VoxelPosFactory(){
    }

    public static VoxelPos createFromIndices(int x, int y, int z){
        return new VoxelPos(x, y, z);
    }

    public static VoxelPos createFromIndexArray(int[] array){
        assert(array != null);
        Preconditions.checkArgument(array.length == 3);
        return new VoxelPos(array[0], array[1], array[2]);
    }

    public static VoxelPos createFromRealSpaceVector(Vector3D v){
        int x = (int)Math.floor(v.getX()/Voxel.VOXEL_WIDTH);
        int y = (int)Math.floor(v.getY()/Voxel.VOXEL_WIDTH);
        int z = (int)Math.floor(v.getZ()/Voxel.VOXEL_WIDTH);
        return new VoxelPos(x, y, z);
    }



}
