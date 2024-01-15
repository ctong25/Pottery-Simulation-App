package com.Objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.GameManager.ImageLoader;

/* GlazeBrush Class:
 * - representing the glaze brush object that can be dragged to the clay to paint it a certain color
 */
public class GlazeBrush extends SimulationObject {
	public GlazeBrush(float x, float y) {
		super(x, y);
		img = ImageLoader.loadImage("assets/glazebrush.png");
	}
	
	@Override
	public void drawObj(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);

		g2.setTransform(transform);
	}
	
	//check to see if the brush hits the clay
	public boolean hit(Clay clay) {
		if(clay.getXPos() > (xPos - ((double) img.getWidth())/2) && clay.getXPos() < (xPos + ((double) img.getWidth())/2) && clay.getYPos() > (yPos - ((double) img.getHeight())/2) && clay.getYPos() < (yPos + ((double) img.getHeight())/2)) { 
			xPos = 1130;
			yPos = 285;
			return true;
	}
		else {
			return false;
		}
	}
}