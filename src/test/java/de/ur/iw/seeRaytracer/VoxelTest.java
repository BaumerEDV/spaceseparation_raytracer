package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import static org.junit.jupiter.api.Assertions.*;

public class VoxelTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {

    }

    @org.junit.jupiter.api.Test
    void addingTriangleIncreasesTriangleCount() {

        Voxel v = new Voxel(new VoxelPos(0, 0, 0));
        for (int i = 0; i < 6; i++) {
            int initialTriangleCount = v.containedTriangles().size();
            v.addTriangle(new Triangle(new Vector3D[]{Vector3D.ZERO, Vector3D.PLUS_I, Vector3D.MINUS_I}));
            int postTriangleCount = v.containedTriangles().size();
            assertEquals(initialTriangleCount + 1, postTriangleCount);
        }
    }

    @org.junit.jupiter.api.Test
    void pointFullyInsideVoxelIsInsideVoxel() {
        final double WIDTH = Voxel.VOXEL_WIDTH;
        final double X_OFFSET = WIDTH / 2;
        final double Y_OFFSET = WIDTH / 3;
        final double Z_OFFSET = WIDTH / 4;

        for (int x = -3; x < 4; x++) {
            for (int y = -3; y < 4; y++) {
                for (int z = -3; z < 4; z++) {
                    Voxel v = new Voxel(new VoxelPos(x, y, z));
                    double pointX = x * WIDTH + X_OFFSET;
                    double pointY = y * WIDTH + Y_OFFSET;
                    double pointZ = z * WIDTH + Z_OFFSET;
                    Vector3D testPoint = new Vector3D(pointX, pointY, pointZ);
                    assertTrue(v.isVectorPointInVoxel(testPoint));
                }
            }
        }
    }

    @org.junit.jupiter.api.Test
    void pointInOriginIsInVoxel() {
        final double WIDTH = Voxel.VOXEL_WIDTH;

        for (int x = -3; x < 4; x++) {
            for (int y = -3; y < 4; y++) {
                for (int z = -3; z < 4; z++) {
                    Voxel v = new Voxel(new VoxelPos(x, y, z));
                    double pointX = x * WIDTH;
                    double pointY = y * WIDTH;
                    double pointZ = z * WIDTH;
                    Vector3D testPoint = new Vector3D(pointX, pointY, pointZ);
                    assertTrue(v.isVectorPointInVoxel(testPoint));
                }
            }
        }
    }

    @org.junit.jupiter.api.Test
    void pointOppositeOriginIsOutsideVoxel(){
        final double WIDTH = Voxel.VOXEL_WIDTH;
        for (int x = -3; x < 4; x++) {
            for (int y = -3; y < 4; y++) {
                for (int z = -3; z < 4; z++) {
                    Voxel v = new Voxel(new VoxelPos(x, y, z));
                    double pointX = (x + 1) * WIDTH;
                    double pointY = (y + 1) * WIDTH;
                    double pointZ = (z + 1) * WIDTH;
                    Vector3D testPoint = new Vector3D(pointX, pointY, pointZ);
                    assertFalse(v.isVectorPointInVoxel(testPoint));
                }
            }
        }
    }

    @org.junit.jupiter.api.Test
    void pointOutsideVoxelIsOutsideVoxel(){
        final double WIDTH = Voxel.VOXEL_WIDTH;
        for (int x = -3; x < 4; x++) {
            for (int y = -3; y < 4; y++) {
                for (int z = -3; z < 4; z++) {
                    Voxel v = new Voxel(new VoxelPos(x, y, z));
                    double pointX = (x + 2) * WIDTH;
                    double pointY = (y + 2) * WIDTH;
                    double pointZ = (z + 2) * WIDTH;
                    Vector3D testPoint = new Vector3D(pointX, pointY, pointZ);
                    assertFalse(v.isVectorPointInVoxel(testPoint));
                }
            }
        }
    }


    @org.junit.jupiter.api.Test
    void voxelWidthIsValid() {
        assertTrue(Voxel.VOXEL_WIDTH > 0);
        //longest triangle edge: 0.028717610399887527
        //assertTrue(Voxel.VOXEL_WIDTH > 0.028717610399887527
    }


}
