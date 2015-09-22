package main;


import java.awt.BorderLayout;

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

	/**
	 * Constructor for class GUIFrame.
	 * @param controller The game controller for this frame
	 * @param player The current client's player
	 */
	public GUIFrame(Controller controller, Player player){
		super("ECS Wars");
		canvas = new GUICanvas(this, player);
		canvas.addMouseListener(controller);
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(true);
		setVisible(true);
		addKeyListener(controller);
	}

	/**
	 * Draws everything within this window.
	 */
	public void draw(){
		canvas.repaint();
	}

}
