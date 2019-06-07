package de.ur.iw.seeRaytracer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Scene {
    private VoxelsContainer voxelsContainer;

    /**
     * Adds triangles to this scene.
     */
    public void addAll(Collection<Triangle> triangles) {
        //this.triangles.addAll(triangles);
        this.voxelsContainer = new VoxelsContainer(triangles);
    }

    /**
     * Computes the color of the light that is seen when looking at the scene along the given ray.
     */
    public Color computeLightThatFlowsBackAlongRay(Ray ray) {
        // determine which part of the scene gets hit by the given ray, if any
        SurfaceInformation closestSurface = findFirstIntersection(ray);

        if (closestSurface != null) {
            // return surface color at intersection point
            return closestSurface.computeEmittedLightInGivenDirection(ray.getNormalizedDirection().negate());
        } else {
            // nothing hit -> return transparent
            return new Color(0, 0, 0, 0);
        }
    }

    /**
     * Traces the given ray through the scene until it intersects anything.
     *
     * @param ray describes the exact path through the scene that will be searched.
     * @return information on the surface point where the first intersection of the ray with any scene object occurs - or null for no intersection.
     */
    private SurfaceInformation findFirstIntersection(Ray ray) {
        SurfaceInformation closestSurface = null;
        double distanceToClosestSurface = Double.POSITIVE_INFINITY;

        java.util.List<Voxel> voxelsRayVisits = voxelsContainer.getOrderedListOfVoxelsThatRayIntersects(ray);

        for (Voxel voxel : voxelsRayVisits){
            for(Triangle triangle : voxel.containedTriangles()){
                var surface = triangle.intersectWith(ray);
                if(surface != null){
                    if(voxel.isVectorPointInVoxel(surface.getPosition())){
                        double distanceToSurface = surface.getPosition().distance(ray.getOrigin());
                        if(distanceToSurface < distanceToClosestSurface){
                            distanceToClosestSurface = distanceToSurface;
                            closestSurface = surface;
                        }
                    }
                }
            }
        }

        /*
        for (var triangle : triangles) {
            var surface = triangle.intersectWith(ray);
            if (surface != null) {
                double distanceToSurface = surface.getPosition().distance(ray.getOrigin());
                if (distanceToSurface < distanceToClosestSurface) {
                    distanceToClosestSurface = distanceToSurface;
                    closestSurface = surface;
                }
            }
        }*/
        return closestSurface;
    }
}
