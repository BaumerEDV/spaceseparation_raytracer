package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VoxelPosTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp(){

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown(){

    }

    @org.junit.jupiter.api.RepeatedTest(50)
    void voxelPosIsAtGivenIndex(){
        int x = (int)(Math.random()*100)-50;
        int y = (int)(Math.random()*100)-50;
        int z = (int)(Math.random()*100)-50;


        VoxelPos vp = new VoxelPos(x, y, z);

        assertEquals(x, vp.getX());
        assertEquals(y, vp.getY());
        assertEquals(z, vp.getZ());

    }

    @org.junit.jupiter.api.RepeatedTest(50)
    void voxelPosAddsCorrectly(){
        int x = (int)(Math.random()*100)-50;
        int y = (int)(Math.random()*100)-50;
        int z = (int)(Math.random()*100)-50;

        int xAdd = (int)(Math.random()*100)-50;
        int yAdd = (int)(Math.random()*100)-50;
        int zAdd = (int)(Math.random()*100)-50;

        var vp = new VoxelPos(x, y, z);
        var adder = new VoxelPos(xAdd, yAdd, zAdd);
        var result = vp.add(adder);

        assertEquals(x + xAdd, result.getX());
        assertEquals(y + yAdd, result.getY());
        assertEquals(z + zAdd, result.getZ());

        //addition is symmetrical
        var result2 = adder.add(vp);

        assertEquals(result, result2);


        // a - a = 0
        var vpInverse = new VoxelPos(-x, -y, -z);
        var result3 = vpInverse.add(vp);

        assertEquals(new VoxelPos(0, 0, 0), result3);
    }

    @Test
    void findsMinimumComponents(){
        VoxelPos p1 = new VoxelPos(-1, 5, 10);
        VoxelPos p2 = new VoxelPos(5, -1, 10);
        VoxelPos p3 = new VoxelPos(10, 5, -1);
        VoxelPos[] args = new VoxelPos[]{p1, p2, p3};
        VoxelPos expected = new VoxelPos(-1, -1, -1);

        assertEquals(expected, VoxelPos.minForEachDimension(args));
    }


    @Test
    void findsMaximumComponents(){
        VoxelPos p1 = new VoxelPos(-1, 5, 10);
        VoxelPos p2 = new VoxelPos(5, -1, 10);
        VoxelPos p3 = new VoxelPos(10, 5, -1);
        VoxelPos[] args = new VoxelPos[]{p1, p2, p3};
        VoxelPos expected = new VoxelPos(10, 5, 10);

        assertEquals(expected, VoxelPos.maxForEachDimension(args));
    }


}
