package com.example.smmo;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * @author Anthony
 *
 */
public class ImageStuff {

	//nearest neighbor scales the img to the specified w and h
	public static void nearestNeighbor(Bitmap img, int w, int h){
		/*Setup pixel buffer*/
		int[] pixels = new int[img.getHeight()*img.getWidth()];
		img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
		
		int[] out = new int[w * h];
		
		float x_ratio = (float)img.getWidth() / (float)w;
	    float y_ratio = (float)img.getHeight() / (float)h;
	    float px, py;
	    float i, j;
	    for ( i =0 ;i<h;i++) {
	      for ( j = 0;j<w;j++) {
	        px = (float)Math.floor(j * x_ratio);
	        py = (float)Math.floor(i * y_ratio);
	        out[(int)(i*w)+(int)j] = pixels[(int)(Math.floor((py*img.getWidth())+px))] ;
	      }
	    }
	    img.setPixels(out, 0, 0, 0, 0, 0, 0);
	    
	 }
	
	
	/**
	 * @param oldp first color in image to change
	 * @param olds second color in image to change
	 */
	public static int[] randomizeBmp(Bitmap img, int oldp, int olds){

		/*Make some random colors*/
		Random rnd = new Random();
		int prim = Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
		int seco = Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));

		/*We don't want the skin and dooRag to be the same*/
		while(prim == seco) {
			prim = Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
		}

		/*Setup pixel buffer*/
		int[] pixels = new int[img.getHeight()*img.getWidth()];
		img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

		/*Go through each pixel and change accordingly*/
		for (int i=0; i<pixels.length; i++){
			if(pixels[i] == oldp){
				pixels[i] = prim;
			} else if (pixels[i] == olds){
				pixels[i] = seco;
			}

		}
		img.setPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), 
				img.getHeight());
		
		int[] newColors = new int[2];
		newColors[0] = prim;
		newColors[1] = seco;
		return newColors;
	}
	
}
