package gameObjects;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GUICanvas;

public class Compass {
	
	BufferedImage compassImage;
	BufferedImage scaledCompass;
	BufferedImage rotated;
	double rot;
	double targetRot;

	public Compass() {
		rot = 0;
		targetRot = 0;
		try {
			compassImage = ImageIO.read(new File("Resources"+File.separator+"Compass.png"));
			scaledCompass = compassImage;
			rotated = scaledCompass;
		} catch (IOException e) {
			System.out.println("Error reading Compass image: " + e.getMessage());
		}
	}
	
	public void rotate(double angle){
		targetRot += angle;
	}
	
	private void rotateImage(double angle) {
		double rotate = Math.toRadians(angle);
		double locationX = scaledCompass.getWidth(null) / 2;
		double locationY = scaledCompass.getHeight(null) / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotate, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		rotated = op.filter(scaledCompass, null);
	}
	
	public Image getImage(){
		return rotated;
	}
	
	public void scaleImage(int scale, GUICanvas c){
		if (scale == 1){
			scaledCompass = compassImage;
			rotated = scaledCompass;
			return;
		}
		BufferedImage before = compassImage;
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage(w*2, h*2, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(2.0, 2.0);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		scaledCompass = scaleOp.filter(before, after);
		rotated = scaledCompass;
	}

	public void update() {
		//double rotateAngle = ((targetRot+(Math.random()*20)-10)-rot)*0.1; // adds "wiggle" to compass
		if (rot == targetRot){ // dont need to rotate image if the angles are the same
			return;
		}
		double rotateAngle = (targetRot-rot)*0.1;
		rot += rotateAngle;
		rotateImage(rot);
	}

}
