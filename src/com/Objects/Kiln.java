package com.Objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.GameManager.ImageLoader;

/* Kiln Class:
 * - represents the kiln that fires the pottery which switches between an open and closed state
 */
public class Kiln extends SimulationObject {
	private BufferedImage img;

	public Kiln(float x, float y) {
		super(x, y);
		img = ImageLoader.loadImage("assets/kiln-open.png");
	}

	public void drawObj(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);

		g2.setTransform(transform);
	}
	
	public boolean clicked(double x, double y){
		boolean clicked = false;
		
		if (x > (xPos - ((double) img.getWidth()) / 2) && x < (xPos + ((double) img.getWidth())/2) && y > (yPos - ((double) img.getHeight())/2) && y < (yPos + ((double) img.getHeight())/2)) 
			clicked = true;
		
		return clicked;
	}

	public void setKilnImg(int kilnState) {
		if (kilnState == 0)
			img = ImageLoader.loadImage("assets/kiln-open.png"); //open kiln
		else if (kilnState == 1)
			img = ImageLoader.loadImage("assets/kiln-closed.png"); //closed kiln
	}
}
