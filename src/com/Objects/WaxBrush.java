package com.Objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.GameManager.ImageLoader;

/* WaxBrush Class:
 * - representing the wax brush object that can be dragged to the clay to paint a hidden pattern that will
 * be revealed after firing
 */
public class WaxBrush extends SimulationObject {
	public WaxBrush(float x, float y) {
		super(x, y);
		img = ImageLoader.loadImage("assets/waxbrush.png");
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
			xPos = 1110;
			yPos = 450;
			return true;
	}
		else {
			return false;
		}
	}
}
