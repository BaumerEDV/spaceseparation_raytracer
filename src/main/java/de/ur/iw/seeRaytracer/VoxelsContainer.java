package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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

    public List<Voxel> getOrderedListOfVoxelsThatRayIntersects(Ray ray) {
        //throw new ExecutionControl.NotImplementedException("");

        ArrayList<Voxel> visited = new ArrayList<>();

        //https://github.com/sketchpunk/FunWithWebGL2/blob/master/lesson_074_voxel_ray_intersection/test.html

        VoxelBoundingBox cameraObjectBoundingBox = new VoxelBoundingBox(
                new VoxelPos[]{
                        VoxelPosFactory.createFromRealSpaceVector(ray.getOrigin()),
                        boundingBox.max(),
                        boundingBox.min(),
                }
        );

        Vector3D rStart = ray.getOrigin();
        Vector3D rEnd = ray.getOrigin().add(cameraObjectBoundingBox.realSpaceEuclidianDiagonalLength(), ray.getNormalizedDirection());

        int ix = (int) Math.min(Math.floor(rStart.getX() / Voxel.VOXEL_WIDTH), cameraObjectBoundingBox.max().getX());
        int iy = (int) Math.min(Math.floor(rStart.getY() / Voxel.VOXEL_WIDTH), cameraObjectBoundingBox.max().getY());
        int iz = (int) Math.min(Math.floor(rStart.getZ() / Voxel.VOXEL_WIDTH), cameraObjectBoundingBox.max().getZ());

        int iix = (int) Math.min(Math.floor(rEnd.getX() / Voxel.VOXEL_WIDTH), cameraObjectBoundingBox.max().getX());
        int iiy = (int) Math.min(Math.floor(rEnd.getY() / Voxel.VOXEL_WIDTH), cameraObjectBoundingBox.max().getY());
        int iiz = (int) Math.min(Math.floor(rEnd.getZ() / Voxel.VOXEL_WIDTH), cameraObjectBoundingBox.max().getZ());

        int xDir = (ray.getNormalizedDirection().getX() > 0) ? 1 : (ray.getNormalizedDirection().getX() < 0) ? -1 : 0;
        int yDir = (ray.getNormalizedDirection().getY() > 0) ? 1 : (ray.getNormalizedDirection().getY() < 0) ? -1 : 0;
        int zDir = (ray.getNormalizedDirection().getZ() > 0) ? 1 : (ray.getNormalizedDirection().getZ() < 0) ? -1 : 0;

        double cellSize = Voxel.VOXEL_WIDTH;

        double xBound = ((xDir > 0) ? ix + 1 : ix) * cellSize;
        double yBound = ((yDir > 0) ? iy + 1 : iy) * cellSize;
        double zBound = ((zDir > 0) ? iz + 1 : iz) * cellSize;

        double xt = (xBound - rStart.getX()) / ray.getNormalizedDirection().getX();
        double yt = (yBound - rStart.getY()) / ray.getNormalizedDirection().getY();
        double zt = (zBound - rStart.getZ()) / ray.getNormalizedDirection().getZ();

        double xDelta = cellSize * xDir / ray.getNormalizedDirection().getX();
        double yDelta = cellSize * yDir / ray.getNormalizedDirection().getY();
        double zDelta = cellSize * zDir / ray.getNormalizedDirection().getZ();

        int xOut = (xDir < 0) ? -1 : cameraObjectBoundingBox.max().getX() - cameraObjectBoundingBox.min().getX(); //TODO: -1 is probably wrong? min.getX for -1 and max.getX for len?
        int yOut = (yDir < 0) ? -1 : cameraObjectBoundingBox.max().getY() - cameraObjectBoundingBox.min().getY(); //maybe these last ones need a +1
        int zOut = (zDir < 0) ? -1 : cameraObjectBoundingBox.max().getZ() - cameraObjectBoundingBox.min().getZ();

        for(;;){//for(int i = 0; i < 30; i++) {//should probably be while(true)
            if(xt < yt && xt < zt){
                if(voxels.containsKey(new VoxelPos(ix, iy, iz))){
                    visited.add(voxels.get(new VoxelPos(ix, iy, iz)));
                }

                ix += xDir;
                if(ix == xOut){
                    return visited;
                }
                xt += xDelta;
            } else if(yt < zt){
                if(voxels.containsKey(new VoxelPos(ix, iy, iz))){
                    visited.add(voxels.get(new VoxelPos(ix, iy, iz)));
                }
                iy += yDir;
                if(iy == yOut){
                    return visited;
                }
                yt += yDelta;
            } else{
                if(voxels.containsKey(new VoxelPos(ix, iy, iz))){
                    visited.add(voxels.get(new VoxelPos(ix, iy, iz)));
                }

                iz += zDir;
                if(iz == zOut){
                    return visited;
                }
                zt += zDelta;
            }


        }
    }


}
