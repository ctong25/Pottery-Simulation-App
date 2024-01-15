package com.Objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.GameManager.ImageLoader;

/* Pedal Class:
 * - represents the foot pedal that starts and stops the wheel which is what spins the clay, it can
 * be clicked on and off based on what the instructions state
 */
public class Pedal extends SimulationObject {
	private BufferedImage img;
	private int status;
	
	public Pedal(float x, float y) {
		super(x, y);
		img = ImageLoader.loadImage("assets/pedal-open.png");
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

	public void setPedalImg(int pedalState) {
		if (pedalState == 0) {
			img = ImageLoader.loadImage("assets/pedal-open.png"); //open pedal
		}
		else if (pedalState == 1) {
			img = ImageLoader.loadImage("assets/pedal-closed.png"); //closed pedal
		}
		status = pedalState;
	}
	
	public int getPedalState(){
		return status;
	}
}

