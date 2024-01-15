package com.Obstacle;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.GameManager.ImageLoader;

/* HardClay Class:
 * - representing the harder to clean clay splatter which must be clicked three times to be removed
 */
public class HardClay extends DirtyClay {
	
	public HardClay(float x, float y) {
		super(x, y);
		this.health = 3;
		img = ImageLoader.loadImage("assets/hardclay.png");
	}

	public void drawObj(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(xPos, yPos);

		g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);

		g2.setTransform(transform);
	}
	
}
