package com.Background;

import com.GameManager.ImageLoader;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/*
 * Studio Class:
 * - to draw the pottery wheel that remains stationary and doesn't interact with anything
 */
public class Studio {
	private double xPos; 
	private double yPos;
	private BufferedImage img;
	
	public Studio(double x, double y) {
		xPos = x;
		yPos = y;
		img = ImageLoader.loadImage("assets/wheel.png");
	}
	
	public void drawBackground(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);

		g2.setTransform(transform);
	}
}
	
	
