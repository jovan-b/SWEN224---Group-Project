package gameObjects;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GUICanvas;

/**
 * A UI component which displays the player's current view direction.
 * 
 * @author Chris Read 300254724
 * @author Sarah Dobie 300315033
 */
public class Compass {
	
	BufferedImage compassImage;
	BufferedImage scaledCompass;
	BufferedImage rotated;
	double rot; // the current rotation angle
	double targetRot; // the target rotation angle

	/**
	 * Constructor for class Compass.
	 */
	public Compass() {
		rot = 0;
		targetRot = 0;
		loadImages();
	}

	/**
	 * Parses and stores all required images.
	 */
	private void loadImages() {
		try {
			compassImage = ImageIO.read(new File("Resources"+File.separator+"Compass.png"));
			scaledCompass = compassImage;
			rotated = scaledCompass;
		} catch (IOException e) {
			System.out.println("Error reading Compass image: " + e.getMessage());
		}
	}
	
	/**
	 * Rotates the compass clockwise by the given angle.
	 * @param angle The angle to rotate by
	 */
	public void rotate(double angle){
		targetRot += angle;
	}
	
	/**
	 * Rotates the compass image clockwise by the given angle.
	 * @param angle The angle to rotate by
	 */
	private void rotateImage(double angle) {
		double rotate = Math.toRadians(angle);
		double locationX = scaledCompass.getWidth(null) / 2;
		double locationY = scaledCompass.getHeight(null) / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotate, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		rotated = op.filter(scaledCompass, null);
	}
	
	/**
	 * Returns the current compass image.
	 * @return The current compass image
	 */
	public Image getImage(){
		return rotated;
	}
	
	/**
	 * Scales the compass image according to the given scale.
	 * @param scale The scale (1 or 2) to use
	 * @param c The canvas the compass will be drawn on
	 */
	public void scaleImage(int scale, GUICanvas c){
		// if the new scale is 1, use the original image size
		if (scale == 1){
			scaledCompass = compassImage;
			rotated = scaledCompass;
			return;
		}
		// scale is 2, scale the image
		BufferedImage before = compassImage; // original image
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage(w*2, h*2, BufferedImage.TYPE_INT_ARGB); // image to scale
		AffineTransform at = new AffineTransform();
		// perform scale
		at.scale(2.0, 2.0);
		AffineTransformOp scaleOp = 
		   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		// update fields
		scaledCompass = scaleOp.filter(before, after);
		rotated = scaledCompass;
	}

	/**
	 * Updates the image to the next frame in its animation.
	 */
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
