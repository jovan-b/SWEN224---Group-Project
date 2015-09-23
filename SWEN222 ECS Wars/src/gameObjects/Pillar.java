package gameObjects;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GUICanvas;

public class Pillar implements Item {

	private Image image;
	private Image scaledImage;
	private int yOffset = 3;
	
	public Pillar() {
		try {
			image = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"Pillar.png"));
			scaledImage = image;
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
	public Image getImage(int viewDirection) {
		return image;
	}

	@Override
	public boolean canWalk() {
		return false;
	}

	@Override
	public int xOffset(int viewDirection) {
		return 0;
	}

	@Override
	public int yOffset(int viewDirection) {
		return yOffset;
	}

	@Override
	public void setScaledImage(int viewDirection, Image scaledImage) {
		this.scaledImage = scaledImage;
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		return scaledImage;
	}

}
