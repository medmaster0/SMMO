package com.example.smmo;

import com.example.smmo.ImageStuff;
import com.example.smmo.Level;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

public class Background {

	private Bitmap back;
	public Bitmap fore;
	public int backX; //position designator for scrolling back ground
	public int width; //width of the display
	public int height; //height of the display
	public int prim, seco;
	public int oldp, olds;
	public Level level; 
	
	/**
	 * 
	 * @param r = resources from view
	 * @param width = width of screen (used for scaling)
	 * @param height = height of screen (used for scaling)
	 */
	public Background(Resources r, int width, int height){
		
		//So we can make a mutable (changeable) bitmap
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
        
		back = BitmapFactory.decodeResource(r, R.drawable.cityset, opt);
		fore = BitmapFactory.decodeResource(r, R.drawable.tree, opt);
		ImageStuff.nearestNeighbor(back, width, height);
		ImageStuff.nearestNeighbor(fore, width, height);
		this.width = width;
		this.height = height;
		this.oldp = Color.BLACK;
		this.olds = Color.WHITE;
		this.level = new Level(r, this.width, this.height);
		
	}
	
	public void drawBackground(Canvas canvas){
		//We're going to draw two of the same bitmap side by side
		Rect src = new Rect(0, 0, back.getWidth(), back.getHeight());
		Rect dst = new Rect(backX, 0, backX + width, height);
		Rect dst2 = new Rect(backX + width, 0 , backX + (2 * width), height);
		canvas.drawBitmap(back, src, dst, null);
		canvas.drawBitmap(back, src, dst2, null);
		//Update the backX values and check when you have to reset to create scrolling motion
		backX--;
		if(backX == -width){
			int[] capture = ImageStuff.randomizeBmp(this.back, this.oldp, this.olds);
			this.oldp = capture[0];
			this.olds = capture[1];
			backX = 0; //reset
		}
		
		//Draw the foreground
		Rect fsrc = new Rect(0,0, fore.getWidth(), fore.getHeight());
		Rect fdst = new Rect(0,0, width, height);
		canvas.drawBitmap(fore, fsrc, fdst, null);
		
		level.draw(canvas); //draw the level
		
	}
	
}
