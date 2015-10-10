package gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;

/**
 * The game winndow.
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 *
 */
@SuppressWarnings("serial")
public class GUIFrame extends JFrame {
	//width and height of main panel
	public static final int INIT_WIDTH = 800;
	public static final int INIT_HEIGHT = 600;
	public static final int MIN_WIDTH = 400;
	public static final int MIN_HEIGHT = 300;

	private GUICanvas canvas; // the canvas this contains

	/**
	 * Constructor for class GUIFrame.
	 * @param controller The game controller for this frame
	 * @param player The current client's player
	 */
	public GUIFrame(){
		super("ECS Wars"); // set window title

		// initialise the canvas
		canvas = new GUICanvas(this);
		
		// set up layout
		setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		// change settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setResizable(true);
		setVisible(true);
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
	
	public static void main(String[] args){
		new GUIFrame();
	}
}
