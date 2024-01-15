package com.Obstacle;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.GameManager.ImageLoader;

/* WetClay Class:
 * - representing the easier to clean clay splatter
 */
public class WetClay extends DirtyClay {
	
	public WetClay(float x, float y) {
		super(x, y);
		this.health = 1;
		img = ImageLoader.loadImage("assets/softclay.png");
	}

	public void drawObj(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);

		g2.setTransform(transform);
	}
	
}
