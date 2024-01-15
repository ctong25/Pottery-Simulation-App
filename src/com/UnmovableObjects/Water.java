package com.UnmovableObjects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.GameManager.ImageLoader;
import com.Objects.SimulationObject;

/* Water class:
 * - represents the bucket of water that the sponge interacts with
 */
public class Water extends SimulationObject {
	private BufferedImage img;

	public Water(float x, float y) {
		super(x, y);
		img = ImageLoader.loadImage("assets/water.png");
	}

	public void drawObj(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);

		g2.setTransform(transform);
	}
}

