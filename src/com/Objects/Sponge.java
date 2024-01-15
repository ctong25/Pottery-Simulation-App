package com.Objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.GameManager.ImageLoader;
import com.UnmovableObjects.Water;

/* Sponge Class:
 * - represents a sponge object used to hold water and wet the clay by dragging it
 */
public class Sponge extends SimulationObject {
	private int state = 0;
	
	public Sponge(float x, float y) {
		super(x, y);
		img = ImageLoader.loadImage("assets/sponge.png");
	}
	
	@Override
	public void drawObj(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);

		g2.setTransform(transform);
	}
	
	//check if the sponge hits the clay
	public boolean hit(Clay clay) {
		if(clay.getXPos() > (xPos - ((double) img.getWidth())/2) 
				&& clay.getXPos() < (xPos + ((double) img.getWidth())/2) 
				&& clay.getYPos() > (yPos - ((double) img.getHeight())) 
				&& clay.getYPos() < (yPos + ((double) img.getHeight()))) 
			return true;
		else {
			return false;
		}
	}
	
	//check if the sponge hits the water bucket
	public boolean hit(Water bucket) {
		if(bucket.getXPos() > (xPos - ((double) img.getWidth())/2) 
				&& bucket.getXPos() < (xPos + ((double) img.getWidth())/2) 
				&& bucket.getYPos() > (yPos - ((double) img.getHeight())+120) 
				&& bucket.getYPos() < (yPos + ((double) img.getHeight()))) 
			return true;
		else {
			return false;
		}
	}
	
	public void setSpongeImg(int spongeState) {
		if (spongeState == 0) {
			img = ImageLoader.loadImage("assets/sponge.png"); //lump of clay
			state = 0;
		}
		else if (spongeState == 1) {
			img = ImageLoader.loadImage("assets/wetsponge.png"); //finished pot
			state = 1;
		}
	}
	
	public int getSpongeState() {
		return state;
	}
}