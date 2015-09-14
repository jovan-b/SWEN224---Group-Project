package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * The main frame of the game
 * Deals with the canvas internally
 * 
 * @author Jah Seng Lee
 *
 */

public class GUI extends JFrame{
	
	//width and height of main panel
	public static final int HEIGHT = 400;
	public static final int WIDTH = 400;
	
	Controller controller;
	Canvas canvas;
	
	public GUI(Controller controller){
		super("ECS Wars");
		canvas = new Canvas();
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(false);
		setVisible(true);
		addKeyListener(controller);
	}
	
	public void draw(){
		canvas.repaint();
	}
	
	/**
	 * The main canvas inside the GUI frame which the game is drawn
	 * 
	 * @author Jah Seng Lee
	 *
	 */
	private class Canvas extends JComponent{
		
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
		}
		
		@Override
		public Dimension getPreferredSize(){
			return new Dimension(GUI.WIDTH, GUI.HEIGHT);
		}
		
		/**
		 * Draws all the graphics on the screen
		 * Mainly calls each objects own draw method
		 */
		@Override
		public void paint(Graphics g){
			//paint background
			g.setColor(Color.BLACK);	//black for now
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}
}
