package com.GameManager;

import javax.swing.JFrame;

/*
 * PotteryApp Class:
 * - to create a window for the simulation that contains all the graphics
 */
public class PotteryApp extends JFrame {

	public PotteryApp(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		PotteryPanel panel = new PotteryPanel();
		
		this.add(panel);
		this.pack();
		
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new PotteryApp("Pottery Simulation App");
	}

}

