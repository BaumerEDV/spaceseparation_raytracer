package de.ur.iw.seeRaytracer;

import com.google.common.collect.UnmodifiableIterator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.checkerframework.checker.nullness.compatqual.NonNullType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class VoxelsContainer {

    private HashMap<VoxelPos, Voxel> voxels;
    private VoxelBoundingBox boundingBox;

    public VoxelsContainer(Collection<Triangle> trianglesToBeDisplayed) {
        voxels = new HashMap<>();
        createVoxelsAndPutContainedTrianglesIntoThem(trianglesToBeDisplayed);
        createBoundingBoxFromTriangles(trianglesToBeDisplayed);
    }

    private void createVoxelsAndPutContainedTrianglesIntoThem(Collection<Triangle> triangles) {
        for (Triangle triangle : triangles) {
            var voxelsThisBelongsTo = createCollectionOfPositionsThisBelongsTo(triangle);
            for (VoxelPos pos : voxelsThisBelongsTo) {
                voxels.putIfAbsent(pos, new Voxel(pos));
                voxels.get(pos).addTriangle(triangle);
            }
        }
    }

    private void createBoundingBoxFromTriangles(Collection<Triangle> triangles) {
        ArrayList<VoxelPos> positions = new ArrayList<>();

        for (Triangle triangle : triangles) {
            for (Vector3D vertex : triangle) {
                positions.add(VoxelPosFactory.createFromRealSpaceVector(vertex));
            }
        }

        VoxelPos[] args = new VoxelPos[positions.size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = positions.get(i);
        }

        boundingBox = new VoxelBoundingBox(args);


    }

    private Collection<VoxelPos> createCollectionOfPositionsThisBelongsTo(Triangle triangle) {
        ArrayList<VoxelPos> result = new ArrayList<>();
        final int SPACE_DIMENSIONS = 3;
        VoxelPos[] vertexBounds = new VoxelPos[SPACE_DIMENSIONS];
        int i = 0;
        for (Vector3D vertex : triangle) {
            vertexBounds[i] = VoxelPosFactory.createFromRealSpaceVector(vertex);
            i++;
        }
        VoxelBoundingBox box = new VoxelBoundingBox(vertexBounds);
        var minX = box.min().getX();
        var maxX = box.max().getX();
        var minY = box.min().getY();
        var maxY = box.max().getY();
        var minZ = box.min().getZ();
        var maxZ = box.max().getZ();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    result.add(new VoxelPos(x, y, z));
                }
            }
        }

        return result;
    }

    public VoxelBoundingBox boundingBoxForTestsOnly() {
        return boundingBox;
    }

    public HashMap<VoxelPos, Voxel> voxelsForTestsOnly() {
        return voxels;
    }

    /*public List<Triangle> getOrderedListOfVoxelsThatRayIntersects(){
        throw new ExecutionControl.NotImplementedException("");
    }*/


}
