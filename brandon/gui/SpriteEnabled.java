package brandon.gui;

import java.util.HashMap;
import java.awt.Graphics;

/** BufferedJPanel is a JPanel with two additional routines for working
 *  with it's buffers
 */

public interface SpriteEnabled {
  public HashMap getSprites(); // A vector of sprite objects
  public void removeSprite(int number);
  public int addSprite(Sprite sprite);
  public void drawSprites(Graphics g);
}
