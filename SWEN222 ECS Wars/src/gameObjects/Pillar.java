package gameObjects;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GUICanvas;

public class Pillar implements Item {

	private Image image;
	private int yOffset = 2;
	
	public Pillar() {
		try {
			image = ImageIO.read(new File("Resources"+File.separator+"Pillar.png"));
		} catch (IOException e) {
			System.out.println("Failed to read Pillar image file: " + e.getMessage());
		}
	}

	@Override
	public void draw(Graphics g, GUICanvas c) {
	}

	@Override
	public void use() {
		return;
	}

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public boolean canWalk() {
		return false;
	}

	@Override
	public int yOffset() {
		return yOffset;
	}

}
