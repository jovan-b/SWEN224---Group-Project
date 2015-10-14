package gui;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A UI component which displays the progress of the day/night cycle.
 * 
 * @author Chris Read 300254724
 * @author Sarah Dobie 300315033
 */
public class Sundial {
	
	GUICanvas canvas;
	BufferedImage sundialImage;
	BufferedImage largeImage;
	BufferedImage scaledSundial;
	BufferedImage rotated;
	double rot; // the current rotation angle
	double targetRot; // the target rotation angle
	double rotateAngle;

	/**
	 * Constructor for class Sundial.
	 */
	public Sundial(GUICanvas canvas) {
		this.canvas = canvas;
		rot = 0;
		targetRot = 0;
		rotateAngle = 0;
		loadImages();
	}

	/**
	 * Parses and stores all required images.
	 */
	private void loadImages() {
		try {
			sundialImage = ImageIO.read(Sundial.class.getResource("/Sundial.png"));
			largeImage = ImageIO.read(Sundial.class.getResource("/SundialLarge.png"));
			scaledSundial = sundialImage;
			rotated = scaledSundial;
		} catch (IOException e) {
			System.out.println("Error reading Sundial image: " + e.getMessage());
		}
	}
	
	/**
	 * Updates the image to the next frame in its animation.
	 */
	public void update() {
		if (rot > targetRot && targetRot > rotateAngle){
			return;
		}
		rot += rotateAngle;
		rotateImage(rot);
		
		float alpha = (float) ((-Math.cos(Math.toRadians(rot)))*0.5+0.5);
		canvas.getController().setNightAlpha(alpha);
	}

	/**
	 * Rotates the sundial clockwise by the given angle.
	 * @param angle The angle to rotate by
	 */
	public void rotate(double angle){
		targetRot += angle;
		rotateAngle = (targetRot-rot)*0.02;
	}
	
	/**
	 * Rotates the sundial image clockwise by the given angle.
	 * @param angle The angle to rotate by
	 */
	private void rotateImage(double angle) {
		double rotate = Math.toRadians(angle);
		double locationX = scaledSundial.getWidth(null) / 2;
		double locationY = scaledSundial.getHeight(null) / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotate, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		rotated = op.filter(scaledSundial, null);
	}
	
	/**
	 * Returns the current sundial image.
	 * @return The current sundial image
	 */
	public Image getImage(){
		return rotated;
	}
	
	/**
	 * Scales the sundial image according to the given scale.
	 * @param scale The scale (1 or 2) to use
	 * @param c The canvas the sundial will be drawn on
	 */
	public void scaleImage(int scale, GUICanvas c){
		// if the new scale is 1, use the original image size
		if (scale == 1){
			scaledSundial = sundialImage;
			rotated = scaledSundial;
			return;
		} else {
			scaledSundial = largeImage;
			rotated = scaledSundial;
		}
	}

}
