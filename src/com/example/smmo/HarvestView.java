package com.example.smmo;

import com.example.smmo.Background;
import com.example.smmo.UpdateThread;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HarvestView extends SurfaceView 
	implements SurfaceHolder.Callback {

	//Variables/Fields
	int width, height; //dimensions of entire SurfaceView
    UpdateThread updateThread; //thread to keep engine running
	Background back;
	Creature llc;
	float followRes;
	long oldTime = 0;
    
	private SurfaceHolder surfaceHolder; //later updateThread will take care of this
	
	//METHODS
	public HarvestView(Context context) {
		super(context);
		getHolder().addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        Rect surfaceFrame = holder.getSurfaceFrame();
        this.width = surfaceFrame.width();
        this.height = surfaceFrame.height();
        this.back = new Background(getResources(),this.width,this.height);
        this.llc = new Creature(getResources(), this.width, this.height);
        
        this.followRes = (float) ((20.0 / 850.0)*height);
        
        oldTime = System.currentTimeMillis();
        updateThread = new UpdateThread(this);
        updateThread.setRunning(true);
        updateThread.start();
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.updateThread.setRunning(false);
		
	}

	public void updatePhysics() {
		long newTime = System.currentTimeMillis();
		long dt = newTime - oldTime;
		oldTime = newTime;
		Log.w("time", Float.toString(dt));
		llc.move();
		//back.level.shiftUp((int)((llc.y - (height/3.3))/followRes));
		
	}

	public void onDraw(Canvas c) {
		this.back.drawBackground(c);
		this.llc.draw(c);
	}
}
