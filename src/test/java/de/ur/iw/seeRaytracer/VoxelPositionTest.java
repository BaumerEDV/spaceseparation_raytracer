package de.ur.iw.seeRaytracer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoxelPositionTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {

    }

    @org.junit.jupiter.api.RepeatedTest(50)
    void voxelPosIsAtGivenIndex() {
        int x = (int) (Math.random() * 100) - 50;
        int y = (int) (Math.random() * 100) - 50;
        int z = (int) (Math.random() * 100) - 50;


        VoxelPosition vp = new VoxelPosition(x, y, z);

        assertEquals(x, vp.getX());
        assertEquals(y, vp.getY());
        assertEquals(z, vp.getZ());

    }

    @org.junit.jupiter.api.RepeatedTest(50)
    void voxelPosAddsCorrectly() {
        int x = (int) (Math.random() * 100) - 50;
        int y = (int) (Math.random() * 100) - 50;
        int z = (int) (Math.random() * 100) - 50;

        int xAdd = (int) (Math.random() * 100) - 50;
        int yAdd = (int) (Math.random() * 100) - 50;
        int zAdd = (int) (Math.random() * 100) - 50;

        var vp = new VoxelPosition(x, y, z);
        var adder = new VoxelPosition(xAdd, yAdd, zAdd);
        var result = vp.add(adder);

        assertEquals(x + xAdd, result.getX());
        assertEquals(y + yAdd, result.getY());
        assertEquals(z + zAdd, result.getZ());

        //addition is symmetrical
        var result2 = adder.add(vp);

        assertEquals(result, result2);


        // a - a = 0
        var vpInverse = new VoxelPosition(-x, -y, -z);
        var result3 = vpInverse.add(vp);

        assertEquals(new VoxelPosition(0, 0, 0), result3);
    }

    @Test
    void findsMinimumComponents() {
        VoxelPosition p1 = new VoxelPosition(-1, 5, 10);
        VoxelPosition p2 = new VoxelPosition(5, -1, 10);
        VoxelPosition p3 = new VoxelPosition(10, 5, -1);
        VoxelPosition[] args = new VoxelPosition[]{p1, p2, p3};
        VoxelPosition expected = new VoxelPosition(-1, -1, -1);

        assertEquals(expected, VoxelPosition.minForEachDimension(args));
    }


    @Test
    void findsMaximumComponents() {
        VoxelPosition p1 = new VoxelPosition(-1, 5, 10);
        VoxelPosition p2 = new VoxelPosition(5, -1, 10);
        VoxelPosition p3 = new VoxelPosition(10, 5, -1);
        VoxelPosition[] args = new VoxelPosition[]{p1, p2, p3};
        VoxelPosition expected = new VoxelPosition(10, 5, 10);

        assertEquals(expected, VoxelPosition.maxForEachDimension(args));
    }


}
