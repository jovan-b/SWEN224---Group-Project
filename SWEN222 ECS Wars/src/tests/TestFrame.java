package tests;

import gui.GUIFrame;

/**
 * A GUIFrame which does not display itself.
 * Exists solely for testing purposes.
 * @author Sarah Dobie 300315033
 *
 */
public class TestFrame extends GUIFrame {
	public TestFrame(){
		super();
		setVisible(false);
	}
}
