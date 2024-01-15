package com.UnmovableObjects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.GameManager.ImageLoader;

/* Button class"
 * - for the buttons the user can click to start or restart the game
 */
public class Button  {
	private double xPos;
	private double yPos;
	private BufferedImage img;
	
	public Button(double x, double y, String file) {
		xPos = x;
		yPos = y;
		img = ImageLoader.loadImage(file);
	}

	public void drawButton(Graphics2D g2) {
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
}
