package main;


import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

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

	private Controller controller;
	private GUICanvas canvas;

	/**
	 * Constructor for class GUIFrame.
	 * @param controller The game controller for this frame
	 * @param player The current client's player
	 */
	public GUIFrame(Controller controller, Player player, KeyListener key, MouseListener mouse, MouseMotionListener mouse2){
		super("ECS Wars");
		canvas = new GUICanvas(this, controller, player);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse2);
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(true);
		setVisible(true);
		addKeyListener(key);
	}
	
	public GUICanvas getCanvas(){
		return canvas;
	}
	
	/**
	 * Draws everything within this window.
	 */
	public void draw(){
		canvas.repaint();
	}

}
