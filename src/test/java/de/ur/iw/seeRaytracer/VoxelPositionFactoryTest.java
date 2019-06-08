package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VoxelPositionFactoryTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {

    }

    @org.junit.jupiter.api.RepeatedTest(50)
    void voxelPosIsAtGivenIntIndex() {
        int x = (int) (Math.random() * 100 - 50);
        int y = (int) (Math.random() * 100 - 50);
        int z = (int) (Math.random() * 100 - 50);

        VoxelPosition vp = VoxelPositionFactory.createFromIndices(x, y, z);
        assertEquals(x, vp.getX());
        assertEquals(y, vp.getY());
        assertEquals(z, vp.getZ());

        int[] array = new int[]{x, y, z};

        vp = VoxelPositionFactory.createFromIndexArray(array);
        assertEquals(x, vp.getX());
        assertEquals(y, vp.getY());
        assertEquals(z, vp.getZ());
    }

    @org.junit.jupiter.api.RepeatedTest(50)
    void realWorldPointToVoxelIndexIsCorrect() {

        int x = (int) (Math.random() * 100 - 50);
        int y = (int) (Math.random() * 100 - 50);
        int z = (int) (Math.random() * 100 - 50);

        double px = x * Voxel.VOXEL_WIDTH + Voxel.VOXEL_WIDTH / (1 / Math.random());
        double py = y * Voxel.VOXEL_WIDTH + Voxel.VOXEL_WIDTH / (1 / Math.random());
        double pz = z * Voxel.VOXEL_WIDTH + Voxel.VOXEL_WIDTH / (1 / Math.random());

        Vector3D v = new Vector3D(px, py, pz);

        VoxelPosition vp = VoxelPositionFactory.createFromRealSpaceVector(v);

        assertEquals(x, vp.getX());
        assertEquals(y, vp.getY());
        assertEquals(z, vp.getZ());
    }

}
