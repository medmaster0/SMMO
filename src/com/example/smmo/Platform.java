package com.example.smmo;

import com.example.smmo.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Platform{

	public int x,y; //posiiton on screen
	public int width;  //width of bitmap frame
	public int height; //width of bitmap frame
	public int screenResX; //screeen resolution X
	public int screenResY; //screen resolution Y
	
	int thickness;
	int length;
	
	public Bitmap bmp;
	
	/*take care of animation*/
	private int bmpRows = 1;
	private int bmpCols = 2;
	public int currentFrame = 0; //collumn in sprite sheet
	private int currentDir = 0; //row in sprite sheet
	public int count; //for animation "clock"
	
	/**Constructor that creates a platform 
	 * 
	 * @param r //just the resources we've been passing along
	 * @param x //x position on screen
	 * @param y //y position on screen
	 * @param resX //x resolution of screen
	 * @param resY //y resolution of screen
	 */
	public Platform(Resources r, int x, int y, int resX, int resY){
		
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
		bmp = BitmapFactory.decodeResource(r, R.drawable.platform, opt);
		
		this.x = x;
		this.y = y;
		this.width = bmp.getWidth() / bmpCols;
		this.height = bmp.getHeight() / bmpRows;
		this.screenResX = resX;
		this.screenResY = resY;
		
		this.thickness= (int) Math.round(0.0216 * this.screenResY);
		this.length = (int) Math.round(0.667 * this.screenResX);
	}
	
	public void drawPlatform(Canvas canvas){
		
		int srcX = currentFrame * width;
		int srcY = currentDir * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect((int)(x),(int)(y), 
				(int)x + length, (int)y + thickness);
				//the scalar coeffeiciet of width and height SCALE the sprite by that much
		canvas.drawBitmap(bmp, src, dst, null);
		
	}
	
	public void tickTock(){
		if(count > 30){
			currentFrame = (++currentFrame % bmpCols); //iterate to next frame
			count = 0;
		}else{
			count++;
		}
	}
}
