package brandon.utils;

import javax.swing.JRootPane;

public interface ExitableApplet {
  public void exitGracefully();
  public void exitImmediately();
  public JRootPane getRootPane();
}
