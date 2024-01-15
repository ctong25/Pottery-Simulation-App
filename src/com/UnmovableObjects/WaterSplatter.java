package com.UnmovableObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import processing.core.PApplet;
import com.GameManager.Util;

/* The WaterSplatter Class:
 * - using perlin noise, it represents the water and clay that is being flung away from the clay while the wheel
 * is spinning
 */
public class WaterSplatter {
	private float xPos, yPos;
	private int width, height;

	private float xstart;
	private float xnoise;
	private float ynoise;
	private PApplet pa;

	public WaterSplatter(float x , float y, int w, int h) {
		xPos = x;
		yPos = y;
		width = w;
		height = h;
		xstart = Util.random(10);
		xnoise = xstart;
		ynoise = Util.random(10);
		pa = new PApplet();
	}
	
	public void drawWater(Graphics2D g2) {
		float noiseFactor;
		AffineTransform at = g2.getTransform();
		g2.translate(xPos, yPos);

		for(int y=0; y <=height; y += 5) {
			ynoise += 0.1;
			xnoise = xstart;
			for(int x= 0; x<=width; x+=5) {
				xnoise+= 0.1;
				noiseFactor = pa.noise(xnoise,ynoise);

				AffineTransform at1 = g2.getTransform();
				g2.translate(x, y);
				g2.rotate(noiseFactor*Util.radians(540));
				float edgeSize = noiseFactor * 35;
	            int blue = (int) (100 + (noiseFactor * 55));
				int alph = (int) (20 +(noiseFactor*105));
				g2.setColor(new Color(100, 130, blue, alph));
				g2.fill(new Ellipse2D.Float(-edgeSize, -edgeSize / 2, edgeSize, edgeSize));
				g2.setTransform(at1);
			}
		}
		g2.setTransform(at);
	}

	public void setWidth(int w) {
		width = w;
	}

	public void setHeight(int h) {
		height = h;
	}
}
