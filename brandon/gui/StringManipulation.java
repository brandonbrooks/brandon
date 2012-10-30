package brandon.gui;

import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import brandon.utils.Log;

/** StringManipulation is a utility class which provides helper
 *  functions to manipulate and display text
 */
public class StringManipulation {
  public StringManipulation() {
  }

  /** Returns the largest font size < 20 which fits in the specified box */
  public static Font getFontThatFits(Graphics g, String s, int width, int height) {
    int pointSize = 20;
    Font f = new Font("Arial", Font.PLAIN, pointSize);
    FontMetrics fm = g.getFontMetrics(f);

    // Scale the font to fit within width / height
    while((fm.getHeight() >=height) || (fm.stringWidth(s) >= width)) {
      f = new Font(f.getName(), f.getStyle(), (f.getSize() -1));
      fm = g.getFontMetrics(f);
    }

    Log.info(StringManipulation.class,
    		 "getFontThatFits(Graphics, String, int, int)", " Found font that fits is size " + f);
    return f;
  }


  /** Get the size that the text will render at in this
   *  Graphics context
   */
  public static Dimension getTextSize(Graphics g, String s) {
     Dimension d = new Dimension();

     FontMetrics fm = g.getFontMetrics();
     d.width = fm.stringWidth(s);
     d.height = fm.getHeight();
     fm.getAscent();
     fm.getDescent();
     return d;
  }

  private static int shiftNorth(int p, int distance) {
    return (p - distance);
  }

  private static int shiftSouth(int p, int distance) {
    return (p + distance);
  }

  private static int shiftEast(int p, int distance) {
    return (p + distance);
  }

  private static int shiftWest(int p, int distance) {
    return (p - distance);
  }

  /** Draw the specified string in 2 colors, innerColor is the
   *  solid color, and the outlineColor is the outer color
   */
  public static void drawOutlined(String string, Color innerColor,
                                  Color outlineColor, Graphics g, int x, int y) {
    g.setColor(outlineColor);
    g.drawString(string, shiftWest(x, 1), shiftNorth(y, 1));
    g.drawString(string, shiftWest(x, 1), shiftSouth(y, 1));
    g.drawString(string, shiftEast(x, 1), shiftNorth(y, 1));
    g.drawString(string, shiftEast(x, 1), shiftSouth(y, 1));
    g.setColor(innerColor);
    g.drawString(string, x, y);
  }

  /** Draw the specified string with a shadow */
  public static void drawShadow(String string, Color textColor, Color shadowColor,
                                Graphics g, int x, int y) {
    g.setColor(shadowColor);
    g.drawString(string, shiftEast(x, 2), shiftSouth(y, 2));
    g.setColor(textColor);
    g.drawString(string, x, y);
  }

  public static void draw3D(String string, Color textColor,
                            Color shadowColor1, Color shadowColor2,
                            int length, Graphics g, int x, int y) {
    for (int i = 0; i < length; i++) {
      g.setColor(shadowColor1);
      g.drawString(string, shiftEast(x, i), shiftNorth(shiftSouth(y, i), 1));
      g.setColor(shadowColor2);
      g.drawString(string, shiftWest(shiftEast(x, i), 1), shiftSouth(y, i));
    }
    g.setColor(textColor);
    g.drawString(string, shiftEast(x, 5), shiftSouth(y, 5));
  }

  public static void addAntiAliasing(Graphics2D g) {
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                       RenderingHints.VALUE_ANTIALIAS_ON);
  }

  public static GraphicsConfiguration getDefaultConfiguration() {

    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

    GraphicsDevice gd = ge.getDefaultScreenDevice();

    return gd.getDefaultConfiguration();

}


public static BufferedImage toCompatibleImage(BufferedImage image, GraphicsConfiguration gc) {

    if (gc == null)

        gc = getDefaultConfiguration();

    int w = image.getWidth();

    int h = image.getHeight();

    int transparency = image.getColorModel().getTransparency();

    BufferedImage result = gc.createCompatibleImage(w, h, transparency);

    Graphics2D g2 = result.createGraphics();

    g2.drawRenderedImage(image, null);

    g2.dispose();

    return result;

}

//returns target

public static BufferedImage copy(BufferedImage source, BufferedImage target) {

    Graphics2D g2 = target.createGraphics();

    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

    double scalex = (double) target.getWidth()/ source.getWidth();

    double scaley = (double) target.getHeight()/ source.getHeight();

    AffineTransform xform = AffineTransform.getScaleInstance(scalex, scaley);

    g2.drawRenderedImage(source, xform);

    g2.dispose();

    return target;

}

// For smaller images, use this:
//Image image = sourceImage.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);

// For larger images (than the source) use this:
public static BufferedImage getScaledInstance(BufferedImage image,
                                              int width, int height,
                                              GraphicsConfiguration gc) {

    if (gc == null)

        gc = getDefaultConfiguration();

    int transparency = image.getColorModel().getTransparency();

    return copy(image, gc.createCompatibleImage(width, height, transparency));

}

  public static ColorModel getColorModel(Image image) {

      try {

          PixelGrabber grabby = new PixelGrabber(image, 0, 0, 1, 1, false);

          if (!grabby.grabPixels())

              throw new RuntimeException("pixel grab fails");

          return grabby.getColorModel();

      } catch (InterruptedException e) {

          throw new RuntimeException("interrupted", e);

      }

  }


  public static BufferedImage toBufferedImage(Image image, GraphicsConfiguration gc) {

      if (image instanceof BufferedImage)

          return (BufferedImage) image;

      // loadImage(image, new Label());

      int w = image.getWidth(null);

      int h = image.getHeight(null);

      int transparency = getColorModel(image).getTransparency();

      if (gc == null)

          gc = getDefaultConfiguration();

      BufferedImage result = gc.createCompatibleImage(w, h, transparency);

      Graphics2D g = result.createGraphics();

      g.drawImage(image, 0, 0, null);

      g.dispose();

      return result;

  }

 /* public static Font findFont(String[] text, Shape constraint, double cx, double cy) {
    Font lo = getFont(), hi = lo.deriveFont(256f), med=lo;
    FONT_LOOP:
        while (hi.getSize2D() - lo.getSize2D() > 3f) {
          med = lo.deriveFont((hi.getSize2D() + lo.getSize2D())/2);
          int fontHeight = getFontMetrics(med).getHeight();
          for(int i=0; i<text.length; ++i) {
            Rectangle bounds = med.getStringBounds(text[i], frc).getBounds();
            double x = cx-bounds.getWidth()/2;
            double y = cy-text.length*fontHeight/2 + i*fontHeight;
            bounds.setRect(x, y, bounds.getWidth(), bounds.getHeight());
            if (constraint.intersects(bounds)) {
              hi = med;
              continue FONT_LOOP;
            }
          }
          lo = med;
        }
        return med;
  }
*/
//
  private AlphaComposite makeComposite(float alpha) {
    int type = AlphaComposite.SRC_OVER;
    return(AlphaComposite.getInstance(type, alpha));
  }



  private void drawSquares(Graphics2D g2d, float alpha) {
    Composite originalComposite = g2d.getComposite();
    g2d.setPaint(Color.blue);
    g2d.fill(new Rectangle(0,0,100,100));
    g2d.setComposite(makeComposite(alpha));
    g2d.setPaint(Color.red);
    g2d.fill(new Rectangle(50,50,100,100));
    g2d.setComposite(originalComposite);
  }




  /*public class ListFonts {
    public static void main(String[] args) {
      GraphicsEnvironment env =
        GraphicsEnvironment.getLocalGraphicsEnvironment();
      String[] fontNames = env.getAvailableFontFamilyNames();
      System.out.println("Available Fonts:");
      for(int i=0; i<fontNames.length; i++)
        System.out.println("  " + fontNames[i]);
    }
  }
*/



}
