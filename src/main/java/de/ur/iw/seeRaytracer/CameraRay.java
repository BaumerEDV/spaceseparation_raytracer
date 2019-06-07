package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Objects;

/**
 * This is a special ray that was generated by a Camera. It represents a path starting at the camera's origin - or
 * "eye point" - and going through the center of one of the camera sensor's pixels.
 */
public class CameraRay extends Ray {
    private final int pixelCoordinateX;
    private final int pixelCoordinateY;

    public CameraRay(Vector3D start, Vector3D normalizedDirection, int pixelCoordinateX, int pixelCoordinateY) {
        super(start, normalizedDirection);
        this.pixelCoordinateX = pixelCoordinateX;
        this.pixelCoordinateY = pixelCoordinateY;
    }

    public int getPixelCoordinateX() {
        return pixelCoordinateX;
    }

    public int getPixelCoordinateY() {
        return pixelCoordinateY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CameraRay cameraRay = (CameraRay) o;
        return pixelCoordinateX == cameraRay.pixelCoordinateX &&
                pixelCoordinateY == cameraRay.pixelCoordinateY;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), pixelCoordinateX, pixelCoordinateY);
    }

    @Override
    public String toString() {
        return "CameraRay{" +
                "pixelCoordinateX=" + pixelCoordinateX +
                ", pixelCoordinateY=" + pixelCoordinateY +
                "} " + super.toString();
    }
}