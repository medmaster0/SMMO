package com.example.smmo;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;

import com.example.smmo.Platform;

public class Level implements Shiftable{

	public Platform[] platforms;
	public int numPlats = 6;
	public int width, height; //width and height of the screen
	private float dy; //a unit of height (for resolution portability
	
	int upcounter;
	int downcounter;
	
	/**
	 * 
	 * @param width of screen
	 * @param height of screen
	 */
	public Level(Resources r, int width, int height){
		
		this.width = width;
		this.height = height;
		this.dy = height / 850;
		
		int y;
		
		/*Generate Platforms*/
		platforms = new Platform[numPlats];
		for(int i = 0; i < numPlats; i++ ){
			//calculate initial y pos of platforms*/
			y = (i+1)*(int)((float)(height) /(float)numPlats); 
			platforms[i] = new Platform(r, 0, y, width, height);
			/*Skew the platforms*/
			if(i%2 == 0) //if is even
			{
				platforms[i].x = (width - platforms[i].length); //move it all the way to the right
			}
		}
	}
	
	public void draw(Canvas canvas){
		for(Platform plat: platforms){
			plat.drawPlatform(canvas);
			plat.tickTock();
		}
	}
	
	/**
	 * Causes the entire level
	 * to shift y units up
	 */
	public void shiftUp(int y) {
		//move up platforms
		for(Platform plat: platforms){
			plat.y = plat.y-(int)(y * this.dy);
			if(plat.y < 0){
				plat.y = height + (plat.y); //correct the reverse over compensation
				upcounter++;
			}
			if(plat.y>height){
				plat.y = plat.y - height;  //correct the over compesation
				downcounter++;
			}
		}
	}
}
