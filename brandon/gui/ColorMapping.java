package brandon.gui;

import java.awt.Color;
import java.io.Serializable;

/** Mapping of color to a name and number */
public class ColorMapping implements Serializable {
  public String name;
  public Color color;
  public Color mutedColor;
  public Color highlightedColor;
  public Color textColor;
  
  /** Define a new color mapping */
  public ColorMapping(String name) {
	  this.name = name;
  }
  
  /** Define a new color mapping */  
  public ColorMapping(String name, Color color, Color mutedColor, Color highlightedColor ) {
      this.name = name;
      this.color = color;
      this.mutedColor = mutedColor;
      this.highlightedColor = highlightedColor;
  }

}
