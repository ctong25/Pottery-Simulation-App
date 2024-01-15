package com.GameManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.Background.Screen;
import com.Background.Studio;
import com.Objects.Clay;
import com.Objects.GlazeBrush;
import com.Objects.Kiln;
import com.Objects.Pedal;
import com.Objects.SimulationObject;
import com.Objects.Sponge;
import com.Objects.WaxBrush;
import com.Obstacle.DirtyClay;
import com.Obstacle.HardClay;
import com.Obstacle.WetClay;
import com.UnmovableObjects.Button;
import com.UnmovableObjects.Water;
import com.UnmovableObjects.WaterSplatter;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

/*
 * References: https://pixabay.com/ for sound effects
 * (all images drawn by me)
 */

/* ECO Features:
 * - 2pts: Shift from day to night
 * - 2pts: All images drawn by myself
 * - 2pts: FSM with at least 4 states
 * - 2pts: Both a Simulation & Video Game all at once
 * - 2pts: Contains a timer for the video game part where an action must be done in a certain amount of time
 * - 2pts: Micro-animation of dirty clay splatters appearing randomly over time while wheel is spinning
 */

/* PotteryPanel Class:
 * - panel class for the pottery simulation game
 */
public class PotteryPanel extends JPanel implements ActionListener{
	//constants
	public final static int BACKGROUND_W = 1200;
	public final static int BACKGROUND_H = 800;
	
	//different states
	private int state = 0; 
	public final static int INTRO = 0;
	public final static int SPONGE = 8;
	public final static int START = 1;
	public final static int DONE_SPINNING = 11;
	public final static int WAX = 3;
	public final static int CLEAN = 9;
	public final static int GLAZE = 2;
	public final static int DONE_GLAZE = 10;
	public final static int MOVE_TO_KILN = 4;
	public final static int FIRING = 5;
	public final static int DONE = 7;
	public final static int RESTART = 6;
	public final static int FAIL = 12;
	
	//fields for screens & their associated button
	private Screen introScreen;
	private Screen endScreen;
	private Screen failScreen;
	private Screen background;
	private Button startButton;
	private Button restartButton;
	
	// variables for holding mouse position
	private double mouseX;
	private double mouseY;
	
	//other
	private Dimension pnlSize;
	private Timer t;

	//boolean fields for shift between day and night
	private boolean isDay = true;
	private boolean isNight = false;
	
	//other booleans
	private boolean halfDone = false; //boolean for water splatter being halfway done
	private boolean isGlazed = false;
	private boolean slowingDown = false; //boolean for water splatter disappearing
	private boolean isSpinning = false;
	
	//fields for timers
	private int waterTimer = 150; //timer for water splatter
	private int timer;
	
	//fields for objects
	private Clay clay;
	private WaterSplatter splatter;
	private Studio studio;
	private Kiln kiln = null;
	private Sponge sponge = null;
	private GlazeBrush gBrush = null;
	private WaxBrush wBrush = null;
	private Water bucket = null;
	private Pedal pedal = null;
	
	//ArrayLists for objects
	private ArrayList<SimulationObject> objList;
	private ArrayList<DirtyClay> dirtyList;
	private ArrayList<DirtyClay> removeList = new ArrayList<>();
	
	//fields to play sounds
	private Minim minim;
	private AudioPlayer bkmusic, click, drag, squeeze, absorb, nextStep, nature;
	
	// variable for snowflake depth for recursion
    private int snowflakeDepth = 5; 
	
	public PotteryPanel() {
		super();
		pnlSize = new Dimension(BACKGROUND_W, BACKGROUND_H);
		this.setPreferredSize(pnlSize);
		
		introScreen = new Screen("assets/intro-screen.png");
		endScreen = new Screen("assets/end-screen.png");
		failScreen = new Screen("assets/fail-screen.png");
		
		startButton = new Button(BACKGROUND_W / 2, BACKGROUND_H * 2/ 3 + 100, "assets/start.png");
		restartButton = new Button(BACKGROUND_W / 2, BACKGROUND_H * 2/ 3 + 100, "assets/restart.png");
		
		//CRITERIA: First Inclusion Polymorphism: subclass objects being added to ArrayList typed with the superclass
		this.objList = new ArrayList<>();
		objList.add(new Water(975, 525));
		objList.add(new Kiln((float)139.5, (float)400));
		objList.add(new Pedal(975, (float)730.5));
		objList.add(new GlazeBrush(1130, 285));
		objList.add(new WaxBrush(1115, 450));
		objList.add(new Sponge(1100, 700));
		
		this.dirtyList = new ArrayList<>();
			
		background = new Screen("assets/background.png");
		clay = new Clay(BACKGROUND_W/2-15, 550);
		splatter = new WaterSplatter((float) clay.getXPos()-100, (float) clay.getYPos() + 50, 250, 25);
		studio = new Studio(BACKGROUND_W/2-15, 636);
		
		minim = new Minim(new MinimHelper());
		bkmusic = minim.loadFile("pottery-studio.mp3");
		click = minim.loadFile("tap.mp3");
		drag = minim.loadFile("drag.mp3");
		squeeze = minim.loadFile("squeeze.mp3");
		absorb = minim.loadFile("absorb.mp3");
		nextStep = minim.loadFile("ding.mp3");
		nature = minim.loadFile("nature.mp3");
		
		bkmusic.loop();
		
		MyMouseListener ml = new MyMouseListener();
		addMouseListener(ml);
		
		MyMouseMotionListener mm = new MyMouseMotionListener();
		addMouseMotionListener(mm);
		
		t = new Timer(33, this);
		t.start();
	}
	
	@Override
	//where you draw something
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		if(state != INTRO && state != RESTART) { //studio background
			background.drawBackground(g2, state);
			studio.drawBackground(g2);
		}
		
		if (state == INTRO) { //intro pg
			introScreen.drawBackground(g2, state);
			startButton.drawButton(g2);
		}
		
		else if (state == SPONGE) { //interacting with sponge
			drawText(g2, "Wet the clay by dragging the sponge to the water bucket and then dragging it to the clay.");
			clay.drawObj(g2);
		}
		
		else if (state == DONE_SPINNING) {//done spinning the wheel
			if(clay.getState() == 1) {
				drawText(g2, "You have formed a vase. Click the pedal to stop spinning the wheel.");
			clay.drawObj(g2);
			splatter.drawWater(g2);
			}
		}
		
		else if (state == START) { //start spinning the wheel
			clay.drawObj(g2);
			
			if(isSpinning) {
				splatter.drawWater(g2);
				if(waterTimer % 50 == 0 && waterTimer != 400) {
					
						dirtyList.add(new WetClay(Util.random(270, BACKGROUND_W-270), Util.random(375, BACKGROUND_H-53)));
				}
				if(waterTimer % 100 == 0 && waterTimer != 400) { {
						dirtyList.add(new HardClay(Util.random(270, BACKGROUND_W-270), Util.random(375, BACKGROUND_H)-53));
					}	
				}
			} 
			
			if(clay.getState() == 0) {
				drawText(g2, "Click the pedal to start spinning the wheel until the clay forms a shape.");
			}
			
			if(slowingDown) {
				splatter.drawWater(g2);
			}
		}
		
		else if (state == WAX) { //waxing a design stage
			drawText(g2, "Drag the wax brush to the clay so that a pretty pattern will appear after firing. Right now it is invisible since the wax is transparent.");
			clay.drawObj(g2);
		}
		
		else if (state == CLEAN) { //cleaning up the studio
			drawText(g2, "Oh no, clean up the dirty clay splatters by clicking on them repeatedly in 10 seconds before the supervisor catches you.");
			clay.drawObj(g2);
			
		}
		
		else if(state == DONE_GLAZE) { //finished glazing
			drawText(g2, "Click on the clay to start moving it from the wheel.");
			clay.drawObj(g2);
		}
		
		else if (state == GLAZE) { //glazing the vase
			drawText(g2, "Drag the glaze brush to the clay to give it a pretty color.");
			clay.drawObj(g2);
		}
		
		else if(state == MOVE_TO_KILN) { //move vase to kiln
			drawText(g2, "Drag the clay to the kiln to start firing.");
		}
		
		else if(state == FIRING) {//firing the vase in the kiln
			drawText(g2, "Waiting until the next day for the vase to be done firing...");
			//switch from day to night
			if(isDay) {
				if(background.getAlpha() != 200) {
					background.incAlpha();
				} else {
					isDay = false;
					isNight = true;		
				}
			}
			if(isNight) {
				if(background.getAlpha() != 0) {
					background.decAlpha();
				} else {
					background.setAlpha(0);
					isNight = false;
					nextStep.play(0);
					state = DONE;
				}
			}
		}
			
		else if(state == DONE) { //vase done firing
			clay.setScale((float)1.5);
			drawText(g2, "Click the kiln to open it and reveal your finished piece!");
		}
		
		//loop through and draw all the objects in the studio
		if(state != INTRO && state != RESTART) {
			for(SimulationObject obj : objList) {
				obj.drawObj(g2);
				if(state == MOVE_TO_KILN) {
					clay.drawObj(g2);
				}
			}
		}
		
		//draw the dirty clay 
		if(state == CLEAN || state == WAX || state == START || state == DONE_SPINNING) {
			for(DirtyClay d : dirtyList) {
				d.drawObj(g2);
			}
		}
		
		if (state == RESTART) { //end pg
			endScreen.drawBackground(g2, RESTART);
			
			clay.setClayImg(2);
			clay.setXPos(BACKGROUND_W/2);
			clay.setYPos(BACKGROUND_H/2 + 10);
			clay.drawObj(g2);
			
			restartButton.drawButton(g2);
			
			//draw the recursive fractal on the finished pottery
			int centerX = BACKGROUND_W / 2 -5;
			int centerY = BACKGROUND_H / 2 + 40;
			int radius = 70; 
			drawSnowflake(g2, centerX, centerY, radius, snowflakeDepth);		
		}
		
		if(state == FAIL) {//failing the game page
			failScreen.drawBackground(g2, RESTART);
			restartButton.drawButton(g2);
		}
	}
	
	//reset everything to restart the game from the beginning
	private void clearGame() {
		clay.setClayImg(0);
		clay.setXPos(BACKGROUND_W/2-15);
		clay.setYPos(550);
		clay.setScale(1);
		slowingDown = false;
		isDay = true;
		isGlazed = false;
		dirtyList.clear();
		bkmusic.loop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer++;
		splatter.setWidth(waterTimer / 2);
		splatter.setHeight(waterTimer / 10);

		if(state == GLAZE && isGlazed) {
			nextStep.play(0);
			state = DONE_GLAZE;
		}
		
		//increase or decrease the water timer for Perlin noise
		if (isSpinning && !halfDone) {
			waterTimer++;
			if (waterTimer >= 400) { 
				waterTimer = 400;
				clay.setClayImg(1);
				nextStep.play(0);
				halfDone = true;
				state = DONE_SPINNING;
			}
		} 
			if(slowingDown) {
				waterTimer -= 10;
				if (waterTimer <= 0) {
					waterTimer = 0;
					halfDone = false;
					slowingDown = false;
					nextStep.play(0);
					state = WAX;
				}
			}
			System.out.println(dirtyList.size());
			
			//remove the clicked dirty clay
			if(removeList.size() != 0) {
				dirtyList.removeAll(removeList);
			}
			
			//30 frames per second so 300 = 10 seconds
			if(state == CLEAN && timer < 300) {
				//change state if all the dirty clay is cleaned
				if(dirtyList.size()== 0 && state == CLEAN) {
					nextStep.play(0);
					state = GLAZE;
				}
			//fail screen if all the dirty clay is not cleaned
			} else if(state == CLEAN && timer > 300){
				nextStep.play(0);
				state = FAIL;
			}		
		repaint();
	}
	
	public class MyMouseListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			
			for(SimulationObject obj : objList) {
				findInstanceOf(obj);
			}
			
			//clean up the dirty clay splatters by clicking on them
			if(state == CLEAN) {
				for(DirtyClay d : dirtyList) {
					if(d.clicked(mouseX, mouseY)) {
						d.decreaseHealth();
						if(d.getHealth() == 0) {
							removeList.add(d);
						}
					}
				}
			}
			
			//start button clicked
			if(state == INTRO && startButton.clicked(mouseX, mouseY)) {
				click.play(0);
				state = SPONGE;
			}
			
			//click kiln to open it and reveal pottery
			if(state == DONE && kiln.clicked(mouseX, mouseY)) {
				nextStep.play(0);
//				nature.pause();
				kiln.setKilnImg(0);
				state = RESTART;
			}
			
			//restart button clicked
			if(state == RESTART || state == FAIL) {
					if(restartButton.clicked(mouseX, mouseY)) {
						click.play(0);
						clearGame();
						state = SPONGE;
						nature.pause();
					}
			}
			
			//click glazed pottery
			if(state == DONE_GLAZE && clay.clicked(mouseX, mouseY)) {
				nextStep.play(0);
				state = MOVE_TO_KILN;
			}

			//pedal clicked to turn on wheel
			if(state == START && pedal.clicked(mouseX, mouseY)) {
				if(pedal.getPedalState() == 0) {
					click.play(0);
					waterTimer = 0;
					isSpinning = true;
					halfDone = false;
					slowingDown = false;
					pedal.setPedalImg(1);
				}
			}
			
			//by clicking the wheel, it turns off the wheel
			if(state == DONE_SPINNING && pedal.clicked(mouseX, mouseY)) {
				if(pedal.getPedalState() == 1) {
					click.play(0);
					isSpinning = false;
					halfDone = true;
					pedal.setPedalImg(0);
					slowingDown = true;
				} 
			}
		} 
	}
	
	public class MyMouseMotionListener extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
			for(SimulationObject obj : objList) {
				findInstanceOf(obj);
			}
			
			//drag sponge
			if(state == SPONGE) {
				sponge.setXPos(e.getX());  
				sponge.setYPos(e.getY());
				
				if(sponge.hit(bucket)) {
					absorb.play(0);
					sponge.setSpongeImg(1);
				}
				
				if(sponge.hit(clay)){
					squeeze.play(0);
					sponge.setSpongeImg(0);
					sponge.setXPos(1100);
					sponge.setYPos(700);
					nextStep.play(0);
					state = START;
				}
			}
			
			//drag clay
			if(state == MOVE_TO_KILN) {
				bkmusic.pause();
				clay.setXPos(e.getX());  
				clay.setYPos(e.getY());
				
				if(clay.hit(kiln)) {
					kiln.setKilnImg(1);
					nextStep.play(0);
					state = FIRING;
					nature.loop();
				}
			}
			
			//draw wax brush
			if(state == WAX) {
				wBrush.setXPos(e.getX());  
				wBrush.setYPos(e.getY());
				
				if(wBrush.hit(clay)) {
					clay.setClayImg(1);
					nextStep.play(0);
					timer = 0;
					state = CLEAN;
					gBrush.setXPos(1130);
					gBrush.setYPos(285);
				}
			}
			
			//draw glaze brush
			if(state == GLAZE) {
				
				gBrush.setXPos(e.getX());  
				gBrush.setYPos(e.getY());
				
				if(gBrush.hit(clay)) {
					nextStep.play(0);
					clay.setClayImg(2);
					state = DONE_GLAZE;
					isGlazed = true;
				}
			}
		}
	}
	
	//CRITERIA: Recursion drawing a circular fractal
	private void drawSnowflake(Graphics2D g, int centerX, int centerY, int radius, int depth) {
        if (depth == 0) {
            return;
        }

        int numCircles = 6; // number of circles around the center

        for (int i = 0; i < numCircles; i++) {
            int x = (int) (centerX + radius * Math.cos(2 * Math.PI * i / numCircles));
            int y = (int) (centerY + radius * Math.sin(2 * Math.PI * i / numCircles));
            g.setColor(new Color(255, 255, 255, 150));
            g.drawOval(x - radius / 2, y - radius / 2, radius, radius);

            drawSnowflake(g, x, y, radius / 3, depth - 1);
        }
    }
	
	//wraps instruction text to fit a certain width
	public void drawWrappedText(Graphics2D g, String text, int maxWidth) {
	    Font f = new Font("Arial", Font.BOLD, 20);
	    FontMetrics metrics = g.getFontMetrics(f);

	    String[] words = text.split("\\s+");
	    StringBuilder wrappedText = new StringBuilder();
	    String line = "";

	    for (String word : words) {
	        String testLine = line.isEmpty() ? word : line + " " + word;
	        int lineWidth = metrics.stringWidth(testLine);

	        if (line.isEmpty() || lineWidth <= maxWidth) {
	            line = testLine;
	        } else {
	            wrappedText.append(line).append("\n");
	            line = word;
	        }
	    }
	    wrappedText.append(line);

	    g.setFont(f);
	    g.setColor(new Color(255, 255, 255, 175));
	    g.fillRect(0, 0, maxWidth + 25, wrappedText.toString().split("\n").length * metrics.getHeight() + 2 * 12);
	    g.setColor(Color.black);

	    String[] lines = wrappedText.toString().split("\n");
	    for (int i = 0; i < lines.length; i++) {
	        g.drawString(lines[i], 14, 10 + (i + 1) * (metrics.getHeight()));
	    }
	}

	//draws the text
	public void drawText(Graphics2D g, String st) {
	    AffineTransform at = g.getTransform();
	    g.translate(860, 100);

	    int maxWidth = 275; 

	    drawWrappedText(g, st, maxWidth);

	    g.setTransform(at);
	}
	
	//CRITERIA: Second Inclusion Polymorphism: superclass as a parameter for a method
	private void findInstanceOf(SimulationObject obj) {
		if(obj instanceof Sponge) {
			sponge = (Sponge) obj;
		}
		if(obj instanceof GlazeBrush) {
			gBrush = (GlazeBrush) obj;
		}
		if(obj instanceof WaxBrush) {
			wBrush = (WaxBrush) obj;
		}
		if(obj instanceof Water) {
			bucket = (Water) obj;
		}
		if(obj instanceof Kiln) {
			kiln = (Kiln) obj;
		}
		if(obj instanceof Pedal) {
			pedal = (Pedal) obj;
		}
	}

}
