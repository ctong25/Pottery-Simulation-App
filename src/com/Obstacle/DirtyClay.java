package com.Obstacle;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.GameManager.ImageLoader;

/* DirtyClay Class:
 * - abstract superclass for the different types of dirty clay that must be cleaned up
 */
public abstract class DirtyClay {
	protected BufferedImage img;
	protected float xPos;
	protected float yPos;
	protected int health;
	
	public DirtyClay(float x, float y) {
		xPos = x;
		yPos = y;
	}

	public boolean clicked(double x, double y){
		boolean clicked = false;
		
		if (x > (xPos - ((double) img.getWidth()) / 2) && x < (xPos + ((double) img.getWidth())/2) && y > (yPos - ((double) img.getHeight())/2) && y < (yPos + ((double) img.getHeight())/2)) 
			clicked = true;
		
		return clicked;
	}
	
	public void decreaseHealth() {
		health--;
	}
	
	public int getHealth() {
		return health;
	}
	
	public double getXPos(){
		return xPos;
	}
	
	public double getYPos(){
		return yPos;
	}
	
	public abstract void drawObj(Graphics2D g2);
}


