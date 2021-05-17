package de.ur.iw.seeRaytracer;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainClass {

  public static void main(String[] args) throws IOException {

    var bunnyOBJ = ClassLoader.getSystemResourceAsStream("bunny.obj");
    var triangles = DataFileReader.parseTrianglesFromOBJ(bunnyOBJ);

    int imageWidth = 1920;
    //int imageWidth = 160;

    Camera camera = createCameraThatLooksAtBunnyTriangles(triangles);

    var scene = new Scene();
    scene.addAll(triangles);

    int imageHeight = (int) (imageWidth / camera.getAspectRatio());
    var image = renderImage(scene, camera, imageWidth, imageHeight);
    ImageIO.write(image, "PNG", new File("bunny.png"));

  }

  /**
   * Creates a camera that keeps a set of triangles in view. The camera's distance to the triangles
   * is relative to the size of their bounding box. The camera's orientation is chosen specifically
   * to look good with the data in the file bunny.obj.
   */
  private static Camera createCameraThatLooksAtBunnyTriangles(List<Triangle> triangles) {
    var boundingBox = AxisAlignedBoundingBox.createFrom(triangles);
    var distanceFromCameraToTriangles =
        0.8 * boundingBox.getMaxDiameter(); // somewhat arbitrary value
    var lookAt = boundingBox.getCenter();
    var lookDirection = new Vector3D(0, 0,
        -1); // chosen so that the bunny is viewed from its front side
    var cameraPosition = lookAt.subtract(distanceFromCameraToTriangles, lookDirection);
    var up = new Vector3D(0, 1, 0); // chosen so that the bunny is viewed with its ears upwards

    return Camera.buildFromEyeForwardUp(
        cameraPosition, lookDirection, up,
        70,
        16, 9
    );
  }

  private static BufferedImage renderImage(Scene scene, Camera camera, int imageWidth,
      int imageHeight) {
    long startTime = System.nanoTime();
    BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    ExecutorService service = Executors.newCachedThreadPool();
    var cameraRays = camera.createRayIteratorForImage(imageWidth, imageHeight);
    CameraRay[] rays;
    for (int k = 0; k < imageHeight; k++) {
      rays = new CameraRay[imageWidth];
      for (int i = 0; i < imageWidth; i++) {
        rays[i] = cameraRays.next();
      }
      service.execute(new PixelRowRayTrace(rays, scene, image));
    }
    try {
      service.shutdown();
      boolean finished = service.awaitTermination(100, TimeUnit.SECONDS);
      System.out.println("completed in: " + (System.nanoTime() - startTime));
      return image;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("completed in: " + (System.nanoTime() - startTime));

    return null;
  }
}
