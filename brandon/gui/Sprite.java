package brandon.gui;

import java.awt.Image;

/** Defines a Sprite */
public class Sprite {
	public int x;
	public int y;
	public int width;
	public int height;
	public int number;
	public Image image;

	public Sprite(int x, int y, Image image) {
		this.x = x;
		this.y = y;
		this.image = image;
		width = image.getWidth(null);
		height = image.getHeight(null);
	}

	public Sprite(int x, int y, Image image, int width, int height) {
		this.x = x;
		this.y = y;
		this.image = image;
		this.width = width;
		this.height = height;
	}
}
