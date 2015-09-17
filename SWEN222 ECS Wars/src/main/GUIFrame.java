package main;

import gameObjects.Room;

import java.awt.BorderLayout;

import javax.swing.*;

import characters.Player;

/**
 * The game winndow.
 * @author Sarah Dobie, Chris Read
 *
 */
public class GUIFrame extends JFrame {
	//width and height of main panel
		public static final int INIT_WIDTH = 400;
		public static final int INIT_HEIGHT = 400;
		
		Controller controller;
		GUICanvas canvas;
		
		public GUIFrame(Controller controller, Player player){
			super("ECS Wars");
			canvas = new GUICanvas(this, player);
			setLayout(new BorderLayout());
			add(canvas, BorderLayout.CENTER);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			pack();
			setResizable(true);
			setVisible(true);
			addKeyListener(controller);
		}
		
		public void draw(){
			canvas.repaint();
		}
	
}
