package com.example.minigame2d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread gameThread;

    private ChibiCharacter chibi5;

    private final List<ChibiCharacter> chibiList = new ArrayList<ChibiCharacter>();
    private final List<Explosion> explosionsList = new ArrayList<Explosion>();

    public GameSurface(Context context) {
        super(context);

        this.setFocusable(true);

        this.getHolder().addCallback(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            int movingVectorX = x - this.chibi5.getX();
            int movingVectorY = y - this.chibi5.getY();


            this.chibi5.setMovingVector(movingVectorX, movingVectorY);
/*
            Iterator<ChibiCharacter> iteratorChibi = this.chibiList.iterator();
            //kiem tra nv
            while (iteratorChibi.hasNext()) {
                ChibiCharacter chibi = iteratorChibi.next();
                if (chibi.getX() < this.chibi5.getX() && this.chibi5.getX() < chibi.getX() + chibi.getWidth() && chibi.getY() < this.chibi5.getY() && this.chibi5.getY() < chibi.getY() + chibi.getHeight()) {

                    iteratorChibi.remove();

                    //tao doi tuong moi(lua)
                    Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion);
                    Explosion explosion = new Explosion(this, bitmap, chibi.getX(), chibi.getY());

                    this.explosionsList.add(explosion);
                }
            }
*/

            return true;


        }
        return false;

    }


    public void update() {
        this.chibi5.update();
        for (ChibiCharacter chibi : chibiList) {
            chibi.update();

        }

        for (Explosion explosion : this.explosionsList) {
            explosion.update();
        }

        Iterator<Explosion> iterator = this.explosionsList.iterator();
        while (iterator.hasNext()) {
            Explosion explosion = iterator.next();

            if (explosion.isFinish()) {
                iterator.remove();
                continue;
            }
        }

        Iterator<ChibiCharacter> iteratorChibi = this.chibiList.iterator();
        //kiem tra nv
        while (iteratorChibi.hasNext()) {
            ChibiCharacter chibi = iteratorChibi.next();
            if (chibi.getX() < this.chibi5.getX() && this.chibi5.getX() < chibi.getX() + chibi.getWidth() && chibi.getY() < this.chibi5.getY() && this.chibi5.getY() < chibi.getY() + chibi.getHeight()) {

                iteratorChibi.remove();

                //tao doi tuong moi(lua)
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.explosion);
                Explosion explosion = new Explosion(this, bitmap, chibi.getX(), chibi.getY());

                this.explosionsList.add(explosion);
            }
        }

    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        this.chibi5.draw(canvas);

        for (ChibiCharacter chibi : chibiList) {
            chibi.draw(canvas);
        }
        for (Explosion explosion : this.explosionsList) {
            explosion.draw(canvas);
        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //nhan vat nam
        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        ChibiCharacter chibi1 = new ChibiCharacter(this, chibiBitmap1, 100, 100);

        Bitmap chibiBitmap2 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        ChibiCharacter chibi2 = new ChibiCharacter(this, chibiBitmap2, 200, 150);

        Bitmap chibiBitmap3 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        ChibiCharacter chibi3 = new ChibiCharacter(this, chibiBitmap3, 400, 200);

        Bitmap chibiBitmap4 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        ChibiCharacter chibi4 = new ChibiCharacter(this, chibiBitmap4, 30, 150);

        Bitmap chibiBitmap6 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi1);
        ChibiCharacter chibi6 = new ChibiCharacter(this, chibiBitmap6, 100, 400);
        //nha vat nu
        Bitmap chibiBitmap5 = BitmapFactory.decodeResource(this.getResources(), R.drawable.chibi2);
        this.chibi5 = new ChibiCharacter(this, chibiBitmap5, 300, 50);


        this.chibiList.add(chibi1);
        this.chibiList.add(chibi2);
        this.chibiList.add(chibi3);
        this.chibiList.add(chibi4);
        this.chibiList.add(chibi6);

        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                this.gameThread.setRunning(false);

                // Luồng cha, cần phải tạm dừng chờ GameThread kết thúc.
                this.gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = true;
        }
    }


}

