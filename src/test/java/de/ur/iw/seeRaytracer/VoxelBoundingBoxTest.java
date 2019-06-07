package de.ur.iw.seeRaytracer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoxelBoundingBoxTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp(){

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown(){

    }

    @Test
    void boundingBoxDimensionsIsCorrect(){
        VoxelPos vp1 = new VoxelPos(-4, -2, 1);
        VoxelPos vp2 = new VoxelPos(3, -5, 0);
        VoxelPos vp3 = new VoxelPos(2, -1, 4);
        VoxelPos[] args = new VoxelPos[]{vp1, vp2, vp3};
        VoxelBoundingBox box = new VoxelBoundingBox(args);
        assertEquals(new VoxelPos(-4, -5, 0), box.min());
        assertEquals(new VoxelPos(3, -1, 4), box.max());
    }

    void diagonalIsCorrect(){

        VoxelBoundingBox box = new VoxelBoundingBox(new VoxelPos[]{new VoxelPos(0, 0, 0)});
        double expectedDiagonal = Math.sqrt((Math.pow(Voxel.VOXEL_WIDTH, 2))*3);
        assertEquals(expectedDiagonal, box.realSpaceEuclidianDiagonalLength(), 0.001);
    }



}
