package com.Background;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.GameManager.ImageLoader;
import com.GameManager.PotteryPanel;

/* Screen Class:
 * - class to display different types of screens like the intro, end, & background
 */
public class Screen {
	private BufferedImage img;
	private int alpha = 1;
	private double wWidth = 445;
	private double wHeight = 190;

	public Screen(String file) {
		img = ImageLoader.loadImage(file);
	}

	public void drawBackground(Graphics2D g2, int state) {
		AffineTransform at = g2.getTransform();
		g2.drawImage(img, 0, 0, PotteryPanel.BACKGROUND_W, PotteryPanel.BACKGROUND_H, null);

		if(state == PotteryPanel.FIRING) {
			drawNightShift(g2);
		}
		
		g2.setTransform(at);
	}
	
	public void drawNightShift(Graphics2D g2) {
		AffineTransform transform = g2.getTransform();
		g2.translate(355, 143);
		
		Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, wWidth, wHeight);
		g2.setColor(new Color(0, 0, 0, alpha));
		g2.fill(rect);
		
		g2.setTransform(transform);
	}
	
	public void incAlpha() {
		alpha++;
	}
	
	public void decAlpha() {
		alpha--;
	}
	
	public int getAlpha() {
		return alpha;
	}
	
	public void setAlpha(int a) {
		alpha = a;
	}
}
