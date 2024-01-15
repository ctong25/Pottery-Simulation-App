package com.Objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.GameManager.ImageLoader;

/* Clay Class:
 * - the object that you will be working on the create a successful piece of pottery
 */
public class Clay extends SimulationObject {
	private float scale = 1;
	private int state = 0;
	
	public Clay(float x, float y) {
		super(x, y);
		img = ImageLoader.loadImage("assets/clay0.png");
	}
	
	@Override
	public void drawObj(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);
		g2.scale(scale, scale);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);

		g2.setTransform(transform);
	}
	
	//checks to see if clay has hit the kiln
	public boolean hit(Kiln kiln) {
		if(kiln.getXPos() > (xPos - ((double) img.getWidth())/2) && kiln.getXPos() < (xPos + ((double) img.getWidth())/2) && kiln.getYPos() > (yPos - ((double) img.getHeight())/2) && kiln.getYPos() < (yPos + ((double) img.getHeight())/2)) 
			return true;
		else {
			return false;
		}
	}
	
	public void setClayImg(int clayState) {
		if (clayState == 0)
			img = ImageLoader.loadImage("assets/clay0.png"); //lump of clay
		else if (clayState == 1)
			img = ImageLoader.loadImage("assets/clay1.png"); //shaped vase
		else if (clayState == 2)
			img = ImageLoader.loadImage("assets/clay2.png"); //finished vase
		
		state = clayState;
	}
	
	public boolean clicked(double x, double y){
		boolean clicked = false;
		
		if (x > (xPos - ((double) img.getWidth()) / 2 * scale) && x < (xPos + ((double) img.getWidth())/2*scale) && y > (yPos - ((double) img.getHeight())/2*scale) && y < (yPos + ((double) img.getHeight())/2*scale)) 
			clicked = true;
		
		return clicked;
	}
	
	public void setScale(float sca) {
		scale = sca;
	}
	
	public int getState() {
		return state;
	}
}
