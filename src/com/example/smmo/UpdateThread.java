package com.example.smmo;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.example.smmo.HarvestView;;

public class UpdateThread extends Thread {
    private long time;
    private final int fps = 20;
    private boolean toRun = false;
    private HarvestView harvestView;
    private SurfaceHolder surfaceHolder;
    
    public UpdateThread(HarvestView rHarvestView) {
        harvestView = rHarvestView;
        surfaceHolder = harvestView.getHolder();
    }
    public void setRunning(boolean run) {
        toRun = run;
    }
    @Override
    public void run() {
        Canvas c;
        while (toRun) {
            long cTime = System.currentTimeMillis();
            if ((cTime - time) <= (1000 / fps)) {
                c = null;
                try {
                    c = surfaceHolder.lockCanvas();
                    harvestView.updatePhysics();
                    harvestView.onDraw(c);
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
            time = cTime;
        }
    }
}
