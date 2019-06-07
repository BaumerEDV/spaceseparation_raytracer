package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VoxelsContainerTest {

    private VoxelsContainer vc;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        Triangle[] triangles = new Triangle[8];
        int i = 0;
        for (int x = -1; x < 1; x++) {
            for (int y = -1; y < 1; y++) {
                for (int z = -1; z < 1; z++) {
                    Vector3D v =
                            new Vector3D(x * Voxel.VOXEL_WIDTH + Voxel.VOXEL_WIDTH / 2,
                            y * Voxel.VOXEL_WIDTH + Voxel.VOXEL_WIDTH / 2,
                            z * Voxel.VOXEL_WIDTH + Voxel.VOXEL_WIDTH / 2);
                    triangles[i] = new Triangle(v, v, v);
                    i++;
                }
            }
        }
        vc = new VoxelsContainer(Arrays.asList(triangles));

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {

    }

    @Test
    void boundingBoxIsCorrect() {
        assertEquals(new VoxelPos(-1, -1, -1), vc.boundingBoxForTestsOnly().min());
        assertEquals(new VoxelPos(0, 0, 0), vc.boundingBoxForTestsOnly().max());
    }

    @Test
    void correctNumberOfTrianglesIsInVoxels() {
        assertEquals(8, vc.voxelsForTestsOnly().keySet().size());
        int[] c = new int[]{-1, 0};
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    assertTrue(vc.voxelsForTestsOnly().containsKey(new VoxelPos(c[x], c[y], c[z])));
                    assertEquals(1, vc.voxelsForTestsOnly().get(new VoxelPos(c[x], c[y], c[z])).containedTriangles().size());
                }
            }
        }
    }


    @Test
    void anArbitraryRayHitsItsSupposedTargets(){

        Vector3D rayOrigin = new Vector3D(-Voxel.VOXEL_WIDTH/2, -Voxel.VOXEL_WIDTH/2, 2);
        Vector3D rayDirection = new Vector3D(0, 0, -1);
        Ray ray = new Ray(rayOrigin, rayDirection.normalize());

        List<Voxel> voxels = vc.getOrderedListOfVoxelsThatRayIntersects(ray);

        assertEquals(2, voxels.size());
        assertEquals(new VoxelPos(-1, -1, -1), voxels.get(0).position());
        assertEquals(new VoxelPos(-1, -1, 0), voxels.get(1).position());



    }


}
