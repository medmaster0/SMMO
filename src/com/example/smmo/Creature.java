package com.example.smmo;

import java.util.Random;

import com.example.smmo.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

public class Creature {
	/*take care of animation*/
	private int bmpRows = 1;
	private int bmpCols = 2;
	private int currentFrame = 1; //collumn in sprite sheet
	private int currentDir = 0; //row in sprite sheet
	private int xStretch, yStretch = 0; //how much to scale the sprite by
	
	/*general fields*/
	private int width; //width of individual sprite (frame)
	private int height; //height of individual sprite (frame)
	private int screenWidth; //width of the screen
	private int screenHeight; //height of the screen
	private float r = 32; //half of what is actually drawn on screen
	public float x = 100;
	public float y = 100;
	public float oldy = 0;
	public float vx;
	public float vy;
	public float termVel; //terminal velocity, a widely used constant
	private float gravity = (float)0.2;
	private Bitmap bmp;
	private int prim, seco;
	private int oldp, olds; //keeps track of the creature's color's
	
	public Creature(Resources r, int screenWidth, int screenHeight){
		/*Always Add the opt so we get a mutable bitmap that wwe can scale*/
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
		bmp = BitmapFactory.decodeResource(r, R.drawable.creature, opt);
		this.oldp = Color.BLACK;
		this.olds = Color.WHITE;
		this.width = bmp.getWidth() / bmpCols;
		this.height = bmp.getHeight() / bmpRows;
		//Become aware of screen dimensions/resolution
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.r = (float) ((32.0/850.0)*screenHeight);
		this.xStretch = (int) ((64.0/540.0)*screenWidth);
		this.yStretch = (int) ((64.0/850.0)*screenHeight);
		this.vx = (float) ( ((-5.0 / 540.0) * screenWidth) + Math.random()*(10.0/540.0)*screenWidth);
		this.vx = (float) ( ((-5.0 / 850.0) * screenHeight) + Math.random()*(10.0/850.0)*screenHeight);
		//this.gravity = (float) ((0.2/850.0)*screenHeight);
		this.gravity = (float)((0.2/850.0)*screenHeight);
		this.termVel = (float) ((10.0/850.0)*screenHeight);

		this.x = 100 + (int)(Math.random()*100);
		this.y = 100 + (int)(Math.random()*100);
		randomizeBmp();
	}
	
	//todo: maybe add a class containing this function
	public void randomizeBmp(){
		
		/*Make some random colors*/
		Random rnd = new Random();
		prim = Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
		seco = Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
		
		/*We don't want the skin and dooRag to be the same*/
		while(prim == seco) {
			prim = Color.argb(255, rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256));
		}
		
		/*Setup pixel buffer*/
		int[] pixels = new int[this.bmp.getHeight()*this.bmp.getWidth()];
		this.bmp.getPixels(pixels, 0, this.bmp.getWidth(), 0, 0, this.bmp.getWidth(), this.bmp.getHeight());
			
		/*Go through each pixel and change accordingly*/
		for (int i=0; i<pixels.length; i++){
			if(pixels[i] == this.oldp){
				pixels[i] = prim;
			} else if (pixels[i] == this.olds){
				pixels[i] = seco;
			}
		    
		}
		this.bmp.setPixels(pixels, 0, this.bmp.getWidth(), 0, 0, this.bmp.getWidth(), 
				this.bmp.getHeight());
		this.olds = seco;
		this.oldp = prim;
	
	}
	
	//todo: maybe add a class containing this function
	public void setColors(int p, int s){
		prim = p;
		seco = s;
		
		/*Setup pixel buffer*/
		int[] pixels = new int[this.bmp.getHeight()*this.bmp.getWidth()];
		this.bmp.getPixels(pixels, 0, this.bmp.getWidth(), 0, 0, this.bmp.getWidth(), this.bmp.getHeight());
			
		/*Go through each pixel and change accordingly*/
		for (int i=0; i<pixels.length; i++){
			if(pixels[i] == this.oldp){
				pixels[i] = prim;
			} else if (pixels[i] == this.olds){
				pixels[i] = seco;
			}
		    
		}
		this.bmp.setPixels(pixels, 0, this.bmp.getWidth(), 0, 0, this.bmp.getWidth(), 
				this.bmp.getHeight());
		this.olds = seco;
		this.oldp = prim;
	}

	public void draw(Canvas canvas){
		
		int srcX = currentFrame * width;
		int srcY = currentDir * height;
		Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
		Rect dst = new Rect((int)(x),(int)(y), 
				(int)(x + xStretch), (int)(y + yStretch));
				//the scalar coeffeiciet of width and height SCALE the sprite by that much
		canvas.drawBitmap(bmp, src, dst, null);
	}
	
	public void move(){
		
		//effects of gravitah!
	    vy += gravity;
	    //attempt at time factor....
		//float dt = (y - oldy) / vy; 
		//Log.w("test", Float.toString(dt));
		//vy = vy + gravity*dt;
		if(vy > termVel)vy = termVel;
	    if(vy < -termVel)vy = -termVel;
	    
	    //actually move the position;
	    x += vx;
	    oldy = y;
	    y += vy;
	    
	    if((x + r) > screenWidth){
	        x -= 2*((x + r) - screenWidth); //correct for (perfect) bouncing
	        vx = -vx;
	      }else if(x < 0){
	        x += 2*(Math.abs(x));
	        vx = -vx;
	      }
	      
	      if((y + r) > screenHeight){
	        y -= 2*((y + r) - screenHeight); //correct for (perfect) bouncing
	        //vy = -vy;
	        vy = -termVel;
	      }else if(y < 0){
	        y += 2*(Math.abs(y));
	        vy = -vy;
	      }
	      
//	      if(((y + r) > height) | y - r < 0){
//	        vy = -vy;
//	        
//	      }
//	      
	      //determine correct animation
	      if(y - oldy > 0){
	    	  currentFrame = 1;
	      }else{
	    	  currentFrame = 0;
	      }
	       	
	      //Log.w("vel", Float.toString(vy));
	}
	
	public void move(long time){
		
		time = time/2000;
		
		//effects of gravitah!
	    vy += gravity*time;
		if(vy > termVel)vy = termVel;
	    if(vy < -termVel)vy = -termVel;
	    
	    //actually move the position;
	    x += vx*time;
	    oldy = y;
	    y += vy*time;
	    
	    if((x + r) > screenWidth){
	        x -= 2*((x + r) - screenWidth); //correct for (perfect) bouncing
	        vx = -vx;
	      }else if(x < 0){
	        x += 2*(Math.abs(x));
	        vx = -vx;
	      }
	      
	      if((y + r) > screenHeight){
	        y -= 2*((y + r) - screenHeight); //correct for (perfect) bouncing
	        //vy = -vy;
	        vy = -termVel;
	      }else if(y < 0){
	        y += 2*(Math.abs(y));
	        vy = -vy;
	      }
	      
//	      if(((y + r) > height) | y - r < 0){
//	        vy = -vy;
//	        
//	      }
//	      
	      //determine correct animation
	      if(y - oldy > 0){
	    	  currentFrame = 1;
	      }else{
	    	  currentFrame = 0;
	      }
	       	
	      //Log.w("test", Float.toString(y));
	      
	}
	
	public void jump(){
		vy = -10 + (int)Math.random()*10;
	}
	
	//TODO
	//setHat
	//setBow
	//setFlag
	//setHorn
	
}
