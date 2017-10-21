package com.droidkings.game;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nazmul on 4/15/17.
 */


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 856;
    public static final int HIGHT = 480;
    public static final int MOVESPEED = -5;
    private long smokeStartTime;
    private long missileStartTime;

    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private ArrayList<TopBorder> topborder;
    private ArrayList<BotBorder> botborder;
    private Random rand = new Random();
    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean topDown = true;
    private boolean botDown = true;
    private boolean newGameCreated = true;

    //increase to slow down difficulty progress, decrease to speed up difficulty
    private int progressDenom = 20;

    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;

    private int best ;


    public GamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 65, 25, 3);
        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();

        topborder = new ArrayList<TopBorder>();
        botborder = new ArrayList<BotBorder>();

        smokeStartTime = System.nanoTime();
        missileStartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);
        //Safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!player.getPlaying() && newGameCreated && reset) {
                player.setPlaying(true);
            }
            if(player.getPlaying()){
                if(!started)
                    started = true;

                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setUp(false);
            return true;

        }

        return super.onTouchEvent(event);
    }

    public void update() {

        if (player.getPlaying()) {
            if(botborder.isEmpty())
            {
                player.setPlaying(false);
            }
            if(topborder.isEmpty())
            {
                player.setPlaying(false);
                return;
            }

            bg.update();
            player.update();
            //calculate the threas hold of the height the border can have based on the screen
            //max and min border are update
            //min is met
            maxBorderHeight = 30 + player.getScore() / progressDenom;
            //cap max border
            if (maxBorderHeight > HIGHT / 4) maxBorderHeight = HIGHT / 4;
            minBorderHeight = 5 + player.getScore() / progressDenom;


            //ceck top border collision
            for(int i = 0 ; i < botborder.size(); i++)
            {
                if(collision(botborder.get(i),player))
                    player.setPlaying(false);
            }
            //check bottom border collision
            for(int i = 0; i < topborder.size(); i++)
            {
                if(collision(topborder.get(i),player))
                    player.setPlaying(false);
            }
            //update top border
            this.updateTopBorder();



            //update bottom border
            this.updateBottomBorder();
            //add missiles on timer
            long missileElapsed = (System.nanoTime() - missileStartTime) / 1000000;
            if (missileElapsed > (2000 - player.getScore() / 4)) {
                System.out.println("making missile");
                //first Missile always goes down the middle

                if (missiles.size() == 0) {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, HIGHT / 2, 45, 15, player.getScore(), 13));

                } else {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                            WIDTH + 10, (int) (rand.nextDouble() * (HIGHT-(maxBorderHeight * 2))+maxBorderHeight), 45, 15, player.getScore(), 13));
                }
                //Reset Timer
                missileStartTime = System.nanoTime();

            }

            //Loopthrough Every missile
            for (int i = 0; i < missiles.size(); i++) {
                //Update missile
                missiles.get(i).update();
                if (collision(missiles.get(i), player)) {
                    missiles.remove(i);
                    player.setPlaying(false);
                    break;

                }
                //remove missile if it is way off the screen
                if (missiles.get(i).getX() < -100) {
                    missiles.remove(i);
                    break;
                }
            }

            //add smoke puff
            long elapsed = (System.nanoTime() - smokeStartTime) / 1000000;
            if (elapsed > 120) {
                smoke.add(new Smokepuff(player.getX(), player.getY() + 10));
                smokeStartTime = System.nanoTime();
            }

            for (int i = 0; i < smoke.size(); i++) {
                smoke.get(i).update();
                if (smoke.get(i).getX() < -10) {
                    smoke.remove(i);
                }
            }
        }else
        {
            player.resetDY();
            if(!reset)
            {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset= true;
                dissapear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),player.getX(),
                        player.getY()-30,100,100,25);
            }

            explosion.update();
            long resetElapsed = ( System.nanoTime() - startReset)/1000000;
            if(resetElapsed > 2500 && !newGameCreated)
            {
                newGame();
            }

        }
    }

    public boolean collision(GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        //Scale
        final float scaleFactorX =  getWidth() / WIDTH * 1.f;
        final float scaleFactorY =  getHeight() / HIGHT * 1.f;

        if (canvas != null) {
            final int savedState = canvas.save();
           canvas.scale(scaleFactorX, scaleFactorY);

            bg.draw(canvas);
            if(!dissapear)
            {
                player.draw(canvas);
            }
            //draw topborder
            for (TopBorder tb : topborder) {
                tb.draw(canvas);
            }
            //draw botborder
            for (BotBorder bb : botborder) {
                bb.draw(canvas);
            }

            //Draw Smoke
            for (Smokepuff sp : smoke) {
                sp.draw(canvas);
            }

            //Draw Missile
            for (Missile m : missiles) {
                m.draw(canvas);
            }


            if(started)
            {
                explosion.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void updateTopBorder() {
        //every 50 points , insert randomly placed top blocks that break the pattern
        if (player.getScore() % 50 == 0) {
            topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources()
                    , R.drawable.brick), topborder.get(topborder.size() - 1).getX() + 20, 0, (int) ((rand.nextDouble() * (maxBorderHeight)) + 1)));
        }
        for (int i = 0; i < topborder.size(); i++) {
            topborder.get(i).update();
            if (topborder.get(i).getX() < -20) {
                topborder.remove(i);
                if (topborder.get(topborder.size() - 1).getHeight() >= maxBorderHeight) {
                    topDown = false;
                }
                if (topborder.get(topborder.size() - 1).getHeight() <= minBorderHeight) {
                    topDown = true;
                }
                //new border added will have larger height
                if (topDown) {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick), topborder.get(topborder.size() - 1).getX() + 20,
                            0, topborder.get(topborder.size() - 1).getHeight() + 1));
                } else {
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.brick), topborder.get(topborder.size() - 1).getX() + 20,
                            0, topborder.get(topborder.size() - 1).getHeight() - 1));

                }
            }
        }
    }

    public void updateBottomBorder() {

        //every 40 points, insert randomly placed bottom blocks that break pattern
        if (player.getScore() % 40 == 0) {
            botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                    botborder.get(botborder.size() - 1).getX() + 20, (int) (rand.nextDouble()
                    * maxBorderHeight) + (HIGHT - maxBorderHeight)));
        }
        //Update Bottom Border
        for (int i = 0; i < botborder.size(); i++) {
            botborder.get(i).update();

            //if border is moving off screen remove it and add corresponding new one
            if (botborder.get(i).getX() < -20) {
                botborder.remove(i);
                if (botborder.get(botborder.size() - 1).getHeight() >= maxBorderHeight) {
                    botDown = false;
                }
                if (botborder.get(botborder.size() - 1).getHeight() <= minBorderHeight) {
                    botDown = true;
                }
                //new border added will have larger height

                if (botDown) {
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                    ), botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1).getY() + 1));
                } else {
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick
                    ), botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1).getY() - 1));

                }
            }

        }

    }
    public void newGame()
    {
        dissapear = false;

        botborder.clear();
        topborder.clear();
        missiles.clear();
        smoke.clear();

        minBorderHeight = 5;
        maxBorderHeight = 30;

        player.resetDY();
        player.resetScore();
        player.setY(HIGHT/2);

        if(player.getScore() >best)
        {
            best = player.getScore();
        }
        //create initial borders

        //initial top border
        for(int i = 0; i*20<WIDTH+40;i++)
        {
            //first top border create
            if(i==0)
            {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick
                ),i*20,0, 10));
            }
            else
            {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick
                ),i*20,0, topborder.get(i-1).getHeight()+1));
            }
        }
        //initial bottom border
        for(int i = 0; i*20<WIDTH+40; i++)
        {
            //first border ever created
            if(i==0)
            {
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick)
                        ,i*20,HIGHT-minBorderHeight));
            }
            //adding borders until the initial screen is filed
            else
            {
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                        i * 20, botborder.get(i - 1).getY() - 1));
            }
        }

        newGameCreated = true;

    }
    public void drawText(Canvas  canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        canvas.drawText("Distance: "+ (player.getScore()*3),10, HIGHT -10, paint);
        canvas.drawText("BEST " + best, WIDTH -215, HIGHT -10, paint);

        if(!player.getPlaying() && newGameCreated)
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH / 2 -50 , HIGHT/2, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH/2-50, HIGHT/2+20, paint1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2-50, HIGHT/2+40, paint1);


        }
    }
}
