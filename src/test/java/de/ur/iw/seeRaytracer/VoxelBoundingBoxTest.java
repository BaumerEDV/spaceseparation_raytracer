package de.ur.iw.seeRaytracer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoxelBoundingBoxTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {

    }

    @Test
    void boundingBoxDimensionsIsCorrect() {
        VoxelPosition vp1 = new VoxelPosition(-4, -2, 1);
        VoxelPosition vp2 = new VoxelPosition(3, -5, 0);
        VoxelPosition vp3 = new VoxelPosition(2, -1, 4);
        VoxelPosition[] args = new VoxelPosition[]{vp1, vp2, vp3};
        VoxelBoundingBox box = new VoxelBoundingBox(args);
        assertEquals(new VoxelPosition(-4, -5, 0), box.min());
        assertEquals(new VoxelPosition(3, -1, 4), box.max());
    }

    @Test
    void diagonalIsCorrect() {

        VoxelBoundingBox box = new VoxelBoundingBox(new VoxelPosition[]{new VoxelPosition(0, 0, 0)});
        double expectedDiagonal = Math.sqrt((Math.pow(Voxel.VOXEL_WIDTH, 2)) * 3);
        assertEquals(expectedDiagonal, box.realSpaceEuclideanDiagonalLength(), 0.001);
    }


}
