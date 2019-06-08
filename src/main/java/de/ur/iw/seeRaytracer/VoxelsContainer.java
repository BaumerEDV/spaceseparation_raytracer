package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class VoxelsContainer {

    private HashMap<VoxelPosition, Voxel> voxels;
    private VoxelBoundingBox boundingBox;

    public VoxelsContainer(Collection<Triangle> trianglesToBeDisplayed) {
        voxels = new HashMap<>();
        createVoxelsAndPutContainedTrianglesIntoThem(trianglesToBeDisplayed);
        createBoundingBoxFromTriangles(trianglesToBeDisplayed);
    }

    private void createVoxelsAndPutContainedTrianglesIntoThem(Collection<Triangle> triangles) {
        for (Triangle triangle : triangles) {
            var voxelsThisBelongsTo = createCollectionOfPositionsThisBelongsTo(triangle);
            for (VoxelPosition pos : voxelsThisBelongsTo) {
                voxels.putIfAbsent(pos, new Voxel(pos));
                voxels.get(pos).addTriangle(triangle);
            }
        }
    }

    private void createBoundingBoxFromTriangles(Collection<Triangle> triangles) {
        ArrayList<VoxelPosition> positions = new ArrayList<>();

        for (Triangle triangle : triangles) {
            for (Vector3D vertex : triangle) {
                positions.add(VoxelPositionFactory.createFromRealSpaceVector(vertex));
            }
        }

        VoxelPosition[] args = new VoxelPosition[positions.size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = positions.get(i);
        }

        boundingBox = new VoxelBoundingBox(args);


    }

    private Collection<VoxelPosition> createCollectionOfPositionsThisBelongsTo(Triangle triangle) {
        ArrayList<VoxelPosition> result = new ArrayList<>();
        final int SPACE_DIMENSIONS = 3;
        VoxelPosition[] vertexBounds = new VoxelPosition[SPACE_DIMENSIONS];
        int i = 0;
        for (Vector3D vertex : triangle) {
            vertexBounds[i] = VoxelPositionFactory.createFromRealSpaceVector(vertex);
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
                    result.add(new VoxelPosition(x, y, z));
                }
            }
        }

        return result;
    }

    public VoxelBoundingBox boundingBoxForTestsOnly() {
        return boundingBox;
    }

    public HashMap<VoxelPosition, Voxel> voxelsForTestsOnly() {
        return voxels;
    }

    public List<Voxel> getOrderedListOfVoxelsThatRayIntersects(Ray ray) {
        ArrayList<Voxel> visited = new ArrayList<>();

        //good part of this taken from: https://github.com/sketchpunk/FunWithWebGL2/blob/master/lesson_074_voxel_ray_intersection/test.html
        //but with lots of bug fixing
        //scientific basis: http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.42.3443&rep=rep1&type=pdf
        // A fast Voxel Traversal Algorithm for Ray Tracing by Amanatides and Wo
        // basically a 3D version of Bresenham's algorithm for lines in pixel grids, but the paper doesn't quote that at all

        VoxelBoundingBox cameraObjectBoundingBox = new VoxelBoundingBox(
                new VoxelPosition[]{
                        VoxelPositionFactory.createFromRealSpaceVector(ray.getOrigin()),
                        boundingBox.max(),
                        boundingBox.min(),
                }
        );

        Vector3D rStart = ray.getOrigin();

        int ix = (int) Math.min(Math.floor(rStart.getX() / Voxel.VOXEL_WIDTH), cameraObjectBoundingBox.max().getX());
        int iy = (int) Math.min(Math.floor(rStart.getY() / Voxel.VOXEL_WIDTH), cameraObjectBoundingBox.max().getY());
        int iz = (int) Math.min(Math.floor(rStart.getZ() / Voxel.VOXEL_WIDTH), cameraObjectBoundingBox.max().getZ());

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

        int xOut = (xDir < 0) ? cameraObjectBoundingBox.min().getX() - 1 : cameraObjectBoundingBox.max().getX() + 1; //TODO: -1 is probably wrong? min.getX for -1 and max.getX for len?
        int yOut = (yDir < 0) ? cameraObjectBoundingBox.min().getY() - 1 : cameraObjectBoundingBox.max().getY() + 1; //maybe these last ones need a +1
        int zOut = (zDir < 0) ? cameraObjectBoundingBox.min().getZ() - 1 : cameraObjectBoundingBox.max().getZ() + 1;

        while (true) {
            if (xt < yt && xt < zt) {
                addVoxelToListIfVoxelContainsTriangle(visited, new VoxelPosition(ix, iy, iz));
                ix += xDir;
                if (ix == xOut) {
                    return visited;
                }
                xt += xDelta;
            } else if (yt < zt) {
                addVoxelToListIfVoxelContainsTriangle(visited, new VoxelPosition(ix, iy, iz));
                iy += yDir;
                if (iy == yOut) {
                    return visited;
                }
                yt += yDelta;
            } else {
                addVoxelToListIfVoxelContainsTriangle(visited, new VoxelPosition(ix, iy, iz));
                iz += zDir;
                if (iz == zOut) {
                    return visited;
                }
                zt += zDelta;
            }
        }
    }

    private void addVoxelToListIfVoxelContainsTriangle(List<Voxel> list, VoxelPosition position) {
        if (voxels.containsKey(position)) {
            list.add(voxels.get(position));
        }
    }


}
