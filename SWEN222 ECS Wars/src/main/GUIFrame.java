package main;


import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.*;

import characters.Player;

/**
 * The game winndow.
 * @author Sarah Dobie, Chris Read
 *
 */
@SuppressWarnings("serial")
public class GUIFrame extends JFrame {
	//width and height of main panel
		public static final int INIT_WIDTH = 800;
		public static final int INIT_HEIGHT = 600;
		
		Controller controller;
		GUICanvas canvas;
		
		public GUIFrame(Controller controller, Player player, KeyListener key, MouseListener mouse){
			super("ECS Wars");
			canvas = new GUICanvas(this, player);
			canvas.addMouseListener(mouse);
			setLayout(new BorderLayout());
			add(canvas, BorderLayout.CENTER);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			pack();
			setResizable(true);
			setVisible(true);
			addKeyListener(key);
		}
		
		public void draw(){
			canvas.repaint();
		}
	
}
