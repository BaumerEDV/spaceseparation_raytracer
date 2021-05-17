package de.ur.iw.seeRaytracer;

import java.awt.image.BufferedImage;

public class PixelRowRayTrace implements Runnable {

  private CameraRay[] cameraRays;
  private Scene scene;
  private BufferedImage image;

  public PixelRowRayTrace(CameraRay[] cameraRays, Scene scene, BufferedImage image) {
    this.cameraRays = cameraRays;
    this.scene = scene;
    this.image = image;
  }


  @Override
  public void run() {
    for (CameraRay cameraRay : cameraRays) {
      var pixelColor = scene.computeLightThatFlowsBackAlongRay(cameraRay);
      image.setRGB(cameraRay.getPixelCoordinateX(), cameraRay.getPixelCoordinateY(),
          pixelColor.getRGB());
    }
  }
}