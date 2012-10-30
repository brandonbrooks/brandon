package brandon.gui;

import java.awt.*;
import java.awt.image.*;
import java.awt.color.ColorSpace;
import java.util.HashMap;
import java.awt.geom.*;
import brandon.utils.Log;
import brandon.utils.Problem;
import java.net.URL;
import com.sun.image.codec.jpeg.ImageFormatException;

public class ImageResource extends MediaResource {
	Image image = null;
	Image inactiveImage = null;
    Image highlightedImage = null;
	HashMap<Integer, Image> rotatedImage = new HashMap<Integer, Image>();

	public enum ImageType { DEFAULT_IMAGE, PLAYER_IMAGE };

    
	public ImageType type = ImageType.DEFAULT_IMAGE;

    
	private Toolkit toolkit = null;

	public ImageResource(String name, String URL) {
		super(name, URL);
		toolkit = Toolkit.getDefaultToolkit();
	}

	public ImageResource(String name, String URL, ImageType type) {
		super(name, URL);
		this.type = type;
		toolkit = Toolkit.getDefaultToolkit();
	}

	public void load() {
		// Load an image from inside the jar file
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        switch(media.getImagePackaging()) {
            case FILE_SYSTEM:
                image = toolkit.getImage(location);
                break;
            case WEBSTART_JAR:
                ClassLoader classLoader = media.getClassLoader();
                if (classLoader == null) {
                    Log.error(this, "load", "ClassLoader is NULL! " + 
                            "Cannot load image [" + location + "]");
                } else {
                    URL url = classLoader.getResource(location);
                    image = toolkit.getImage(url);
                }
                break;
            case JAR_FILE:
                break;
        }

		if (image == null) {
			Problem.warning("Could not retrieve the image: " + location);
		}

		if (name.startsWith("Player")) {
			Log.comment(this, "load", name + " added as a Player Image");
			type = ImageType.PLAYER_IMAGE;
		}
        
        //Log.comment(this, "load", "loaded.");
	}

	/** Returns the Regular Version of this Image */
	public Object get() {
		return image;
	}

	/** Returns the Regular Version of this Image */
	public Image regular() {
		return image;
	}

	/** Returns the Inactive Version of this Image */
	public Image inactive() {
		if (inactiveImage == null) {
			createInactiveImage();
		}
		return inactiveImage;
	}

	public void modify() {
	}

	private void createInactiveImage() {
		// Copy the image to a BufferedImage, so we can modify it
		BufferedImage bufferedImage = new BufferedImage(
				image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bufferedImage.getGraphics();
		g.drawImage(image, 0, 0, null);

		// Create the Inactive Version of this Image (B&W)
		bufferedImage = convertToGrayscale(bufferedImage);

		// Create an image out of this BufferedImage
		inactiveImage = Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
	}

	/** Returns this image rotated the specified number of degrees. */
	private void createRotatedImage(int degrees) {
        
		// Create a Buffered Image from the main image
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		//Log.info(this, "createRotatedImage(" + degrees + ")", 
        //       "Width of starting Image is " + width + ", height is " + height);
		BufferedImage bufferedImage = new BufferedImage(width, height,
                                            BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bufferedImage.createGraphics();

		// Clear image with transparent alpha by drawing a rectangle
		// TODO: Setting an AlphaComposite to CLEAR seems to make the whole thing NOT WORK!
		//g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
		//Rectangle2D.Double rect = new Rectangle2D.Double(0,0,width,height);
		//g.setColor(Color.GREEN);
		///g.fill(rect);

        //g.setColor(Color.RED);
        //g.drawString("Hello", 10, 10);
		// Draw the Image onto the BufferedImage
		//g.drawImage(image, 0, 0, null);
        //AffineTransform aft = new AffineTransform(); 

		
        AffineTransform tx = g.getTransform();
        //tx.setToTranslation(20, 20);
        
		// Rotate the number of degrees specified
		tx.rotate(Math.toRadians(degrees), bufferedImage.getWidth() / 2,
               bufferedImage.getHeight() / 2);
		g.setTransform(tx);
        
        g.drawImage(image, 0, 0, null);
       
		//AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		//bufferedImage = op.filter(bufferedImage, null);

        //g.drawImage(image, width, height, null);
          
        
		// Create an image out of this BufferedImage
		Image createdImage = Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
		
        rotatedImage.put(new Integer(degrees), createdImage);
        
	}

	public Image rotated(int degrees) {
		if (degrees == 0) { return image; }
		Integer degreesInteger = new Integer(degrees);
		Image newImage = rotatedImage.get(degreesInteger);
		if (newImage == null) {
			createRotatedImage(degrees);
			newImage = rotatedImage.get(degreesInteger);
		}
		return newImage;
	}

	public Image highlighted() {
        if (highlightedImage == null) {
            // Copy the image to a BufferedImage, so we can modify it
            BufferedImage bufferedImage = new BufferedImage(
                    image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            //Graphics g = bufferedImage.getGraphics();
            //g.drawImage(image, 0, 0, null);
            
            // Create the Inactive Version of this Image (B&W)
            bufferedImage = convertToHighlighted(bufferedImage);

            // Create an image out of this BufferedImage
            highlightedImage = Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
        }
        return highlightedImage;
	}

	public Image highlightedAndRotated(int degrees) {
		return null;
	}

	private BufferedImage convertToGrayscale(BufferedImage source) {
		BufferedImageOp op = new ColorConvertOp(
				ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		return op.filter(source, null);
	}
    
    private BufferedImage convertToHighlighted(BufferedImage source) {
        AffineTransform atImageSpace = new AffineTransform();
        
        AlphaComposite myAlpha = AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER,0.75f);
        Graphics2D g = (Graphics2D) source.getGraphics();
        g.setColor(Color.WHITE);
        Polygon p = new Polygon();
            p.addPoint(source.getWidth() / 2, 0);
            p.addPoint(source.getWidth(), source.getHeight() / 5);
            p.addPoint(source.getWidth(), 4 * (source.getHeight() / 5));
            p.addPoint(source.getWidth() / 2, source.getHeight());
            p.addPoint(0, 4 * (source.getHeight() / 5));
            p.addPoint(0, source.getHeight() / 5);
            
        g.fillPolygon(p);
        g.setComposite(myAlpha);
        g.drawImage(image, atImageSpace, null);
        
        return source;
    }

}
