package gameObjects;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Compass {
	
	BufferedImage compassImage;
	BufferedImage rotated;
	double rot;
	double targetRot;

	public Compass() {
		rot = 0;
		targetRot = 0;
		try {
			compassImage = ImageIO.read(new File("Resources"+File.separator+"Compass.png"));
			rotated = compassImage;
		} catch (IOException e) {
			System.out.println("Error reading Compass image: " + e.getMessage());
		}
	}
	
	public void rotate(double angle){
		targetRot += angle;
	}
	
	private void rotateImage(double angle) {
		double rotate = Math.toRadians(angle);
		double locationX = compassImage.getWidth(null) / 2;
		double locationY = compassImage.getHeight(null) / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotate, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		rotated = op.filter(compassImage, null);
	}
	
	public Image getImage(){
		return rotated;
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
