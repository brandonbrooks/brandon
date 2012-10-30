package brandon.gui;

import java.awt.*;
import java.awt.image.*;
import brandon.utils.Log;
import javax.swing.JApplet;

public class MultiImageResource extends ImageResource {
  private Image[] imageArray = null;
  private int numFrames = 1;

  public MultiImageResource(String name, String URL) {
    super(name, URL);
  }

  public MultiImageResource(String name, String URL, int numFrames) {
    super(name, URL);
    this.numFrames = numFrames;
    imageArray = new Image[numFrames];
  }

  /** Chops up the image into the specified number of pieces */
  public void modify(JApplet applet) {
	  String methodName = "modify(JApplet)";
	  int imageWidth = (image.getWidth(null) / numFrames);
	  if ((imageWidth * numFrames) != image.getWidth(null)) {
		  Log.error(this, methodName, "image width of " + image.getWidth(null) +
                " is not divisible evenly by " + numFrames);
		  return;
	  }

      int imageHeight = image.getHeight(null);
      int x = 0, y = 0;

      // Slice up the image, and each sliced image is it's own image
      for(int i=0; (i < numFrames); i++) {
          x = x + (i * imageWidth);
          // Copy the image to a BufferedImage, so we can modify it
          CropImageFilter crop = new CropImageFilter(x,y,imageWidth,imageHeight);
          imageArray[i] = applet.createImage(new FilteredImageSource(image.getSource(), crop));
      }
  }

  public Image regular(int numImage) {
	  String methodName = "regular(" + numImage +")";
      if (numImage >= numFrames) {
          Log.error(this, methodName, " image too large for " +
                    "the array size of " + numFrames);
          return null;
      }
      return imageArray[numImage];
  }

  public int numFrames() {
    return imageArray.length;
  }

}
