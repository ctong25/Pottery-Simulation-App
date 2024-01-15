package com.Objects;

import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;

import com.GameManager.ImageLoader;

import processing.core.PVector;

/* SimulationObject abstract superclass:
 * - represents the objects used in this simulation
 */
public abstract class SimulationObject {
	protected float xPos;
	protected float yPos;
	protected BufferedImage img;
	
	public SimulationObject(float x, float y) {
		this.xPos = x;
		this.yPos = y;
	}
	
	public double getXPos(){
		return xPos;
	}
	
	public double getYPos(){
		return yPos;
	}
	
	public void setXPos(float x){
		xPos = x;
	}
	
	public void setYPos(float y){
		yPos = y;
	}
	
	public abstract void drawObj(Graphics2D g);
}
