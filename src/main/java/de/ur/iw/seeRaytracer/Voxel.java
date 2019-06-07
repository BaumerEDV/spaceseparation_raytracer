package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.Collection;

import static jdk.jshell.spi.ExecutionControl.NotImplementedException;

public class Voxel {

    //longest triangle edge is 0.028something
    public static final double VOXEL_WIDTH = 0.03;

    private ArrayList<Triangle> containedTriangles;
    private VoxelPos coordinate;

    public Voxel(VoxelPos coordinate) {
        assert (coordinate != null);
        this.coordinate = coordinate;
        containedTriangles = new ArrayList<>();
    }

    public boolean isVectorPointInVoxel(Vector3D v) {
        boolean result = true;
        double voxelOriginX = coordinate.getX() * VOXEL_WIDTH;
        double voxelOriginY = coordinate.getY() * VOXEL_WIDTH;
        double voxelOriginZ = coordinate.getZ() * VOXEL_WIDTH;
        result &= voxelOriginX <= v.getX();
        result &= voxelOriginY <= v.getY();
        result &= voxelOriginZ <= v.getZ();

        double voxelOriginOppositeX = voxelOriginX + VOXEL_WIDTH;
        double voxelOriginOppositeY = voxelOriginY + VOXEL_WIDTH;
        double voxelOriginOppositeZ = voxelOriginZ + VOXEL_WIDTH;

        result &= v.getX() < voxelOriginOppositeX;
        result &= v.getY() < voxelOriginOppositeY;
        result &= v.getZ() < voxelOriginOppositeZ;
        return result;
    }

    public void addTriangle(Triangle triangle) {
        assert (triangle != null);
        containedTriangles.add(triangle);
    }

    public Collection containedTriangles() {
        return containedTriangles;
    }

    public String toString(){
        return "Voxel (" + coordinate.getX() + ", " + coordinate.getY() + ", " + coordinate.getZ() + ")";
    }


}
