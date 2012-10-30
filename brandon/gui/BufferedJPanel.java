package brandon.gui;

import java.awt.Image;
import javax.swing.JPanel;

public abstract class BufferedJPanel extends JPanel {
  public abstract Image getBackgroundImage();
  public abstract Image getBuffer();
}
