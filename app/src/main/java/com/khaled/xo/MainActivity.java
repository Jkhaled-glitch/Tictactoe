package com.khaled.xo;


import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    boolean gameActive = true;
    //    player representation
//    0=x
//    1=o
    int activePlayer = new Random().nextInt(2);
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    int tapCount = 0;
    int [] score ={0,0};

    String  resetText ;
    String statusText;


//    state meaning
//    0=x
//    1=o
//    2=null

    ImageView back,image0, image1, image2, image3, image4, image5, image6, image7, image8;

    Button reset;
    TextView status,x_score,o_score;

    Bitmap bitmap ;


    Canvas canvas ;

    Paint paint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        image0 = findViewById(R.id.image0);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        image6 = findViewById(R.id.image6);
        image7 = findViewById(R.id.image7);
        image8 = findViewById(R.id.image8);
        reset = findViewById(R.id.reset);
        x_score= findViewById(R.id.x_score);
        o_score = findViewById(R.id.o_score);
        status = findViewById(R.id.status);


         bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
         canvas = new Canvas(bitmap);
         paint = new Paint();
         back();
        if (activePlayer == 0) {
            status.setText("Next Player : X");
        } else {
            status.setText("Next Player : O");
        }

        reset.setVisibility(View.GONE);


        setClickListenerForImage(0, image0);
        setClickListenerForImage(1, image1);
        setClickListenerForImage(2, image2);
        setClickListenerForImage(3, image3);
        setClickListenerForImage(4, image4);
        setClickListenerForImage(5, image5);
        setClickListenerForImage(6, image6);
        setClickListenerForImage(7, image7);
        setClickListenerForImage(8, image8);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset.setVisibility(View.GONE);
                gameReset();
            }
        });

        if (savedInstanceState != null) {

            //recuperer mes information
            gameActive = savedInstanceState.getBoolean("gameActive");
            activePlayer = savedInstanceState.getInt("activePlayer");
            gameState = savedInstanceState.getIntArray("gameState");
            tapCount = savedInstanceState.getInt("tapCount");
            score = savedInstanceState.getIntArray("score");
            resetText = savedInstanceState.getString("resetText");
            statusText = savedInstanceState.getString("statusText");


            int activePlayerHistory;
            //recuperer la vue
            for(int index = 8 ; index >= 0 ;index--){
                if(gameState[index] != 2) {
                    activePlayerHistory = gameState[index];
                    switch (index) {
                        case 0:
                            drawCanvas(activePlayerHistory == 0 ? "x" : "o", image0);
                            break;
                        case 1:
                            drawCanvas(activePlayerHistory == 0 ? "x" : "o", image1);
                            break;
                        case 2:
                            drawCanvas(activePlayerHistory == 0 ? "x" : "o", image2);

                            break;
                        case 3:
                            drawCanvas(activePlayerHistory == 0 ? "x" : "o", image3);
                            break;
                        case 4:
                            drawCanvas(activePlayerHistory == 0 ? "x" : "o", image4);
                            break;
                        case 5:
                            drawCanvas(activePlayerHistory == 0 ? "x" : "o", image5);
                            break;
                        case 6:
                            drawCanvas(activePlayerHistory == 0 ? "x" : "o", image6);
                            break;
                        case 7:
                            drawCanvas(activePlayerHistory == 0 ? "x" : "o", image7);
                            break;
                        case 8:
                            drawCanvas(activePlayerHistory == 0 ? "x" : "o", image8);
                            break;
                    }
                }
            }
            if(tapCount>=1){
                reset.setVisibility(View.VISIBLE);
                reset.setText(resetText);
            }
            status.setText(statusText);
            x_score.setText("X "+score[0]);
            o_score.setText(score[1]+" O");
        }
    }


    public void playerTap(int position, ImageView image) {
        int tappedImage = position;

        if (gameActive) {

            if (gameState[tappedImage] == 2) {
                tapCount++;
                if(tapCount==1) {
                    reset.setVisibility(View.VISIBLE);
                    reset.setText("Reset");
                }
                gameState[tappedImage] = activePlayer;
                image.setTranslationY(-1000f);

                if (activePlayer == 0) {
                    drawCanvas("x", image);
                    activePlayer = 1;
                    status.setText("Next Player : O");
                } else {
                    drawCanvas("o", image);
                    activePlayer = 0;
                    status.setText("Next Player : X");
                }
                image.animate().translationYBy(1000f).setDuration(500);
            }
            //check if any player has won
            if(!anyPlayerWon()){
                if (tapCount == 9 && gameActive) {
//
                    String gameOver = "Game Over Try again!";
                    gameActive = false;

//                update the status bar for gameOver announcement
                    status.setText(gameOver);
                    reset.setText("Replay");
                }
            }
        }
    }

    private boolean  anyPlayerWon(){
        boolean won = false;
        int[][] winPositions = { {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6} };
        //check if any player has won

        for (int i = 0;i<winPositions.length;i++)
                //int[] winPosition : winPositions)
        {
            int[] line = winPositions[i];

            if (gameState[line[0]] != 2 &&
                    gameState[line[0]] == gameState[line[1]] && gameState[line[1]] == gameState[line[2]]) {
//                then somebody has won
                won = true;

                String winnerStr;
                gameActive = false;
                if (gameState[line[0]] == 0) {
                    winnerStr = "X has won";
                    score[0]++;
                } else {
                    winnerStr = "O has won";
                    score[1]++;
                }
//                update the status bar for winner announcement

                status.setText(winnerStr);
                reset.setText("Replay");

                animateLineWinner(line);

                x_score.setText("X "+score[0]);
                o_score.setText(score[1]+" O");

                //quitter la boucle for
                break;
            }

        }
        return  won;
    }
    public void gameReset() {

        image0.animate().cancel();
        image1.animate().cancel();
        image2.animate().cancel();
        image3.animate().cancel();
        image4.animate().cancel();
        image5.animate().cancel();
        image6.animate().cancel();
        image7.animate().cancel();
        image8.animate().cancel();

        bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        back();

        gameActive = true;
        activePlayer =activePlayer == 0 ? 1 : 0;
        tapCount = 0;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }

        ((ImageView) findViewById(R.id.image0)).setImageResource(0);
        ((ImageView) findViewById(R.id.image1)).setImageResource(0);
        ((ImageView) findViewById(R.id.image2)).setImageResource(0);
        ((ImageView) findViewById(R.id.image3)).setImageResource(0);
        ((ImageView) findViewById(R.id.image4)).setImageResource(0);
        ((ImageView) findViewById(R.id.image5)).setImageResource(0);
        ((ImageView) findViewById(R.id.image6)).setImageResource(0);
        ((ImageView) findViewById(R.id.image7)).setImageResource(0);
        ((ImageView) findViewById(R.id.image8)).setImageResource(0);



        if (activePlayer == 0)
            status.setText("Next Player : X");
        else
            status.setText("Next Player : O");

    }
    private void drawCanvas(String type, ImageView displayer) {

        // Create a Bitmap with the same dimensions as the canvas
        Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        // Create a Canvas with the bitmap
        Canvas bitmapCanvas = new Canvas(bitmap);

        // Draw on the bitmapCanvas using your paint

        Paint paint = new Paint();
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);

        if (type == "x") {

            paint.setColor(Color.rgb(141, 69, 94));
            // Draw the X on the bitmapCanvas
            bitmapCanvas.drawLine(10, 10, bitmapCanvas.getWidth() - 10, bitmapCanvas.getWidth() - 10, paint);
            bitmapCanvas.drawLine(10, bitmapCanvas.getWidth() - 10, bitmapCanvas.getWidth() - 10, 10, paint);
        } else {

            paint.setColor(Color.rgb(20, 123, 170));

            RectF circleDrawingRect = new RectF(10, 10, 90, 90);
            float centerX = circleDrawingRect.centerX();
            float centerY = circleDrawingRect.centerY();
            float radius = Math.min(circleDrawingRect.width(), circleDrawingRect.height()) / 2;
            bitmapCanvas.drawCircle(centerX, centerY, radius, paint);

        }

        // Set the bitmap as the image resource

        displayer.setImageBitmap(bitmap);
    }
    private void drawLineWinner(int x1, int y1, int x2, int y2) {

        paint.setColor(Color.rgb(255,165,100));
        paint.setStrokeWidth(5);
        paint.setShadowLayer(1, 0, 0, Color.WHITE);

        final float[] from = {x1, y1, x1, y1};
        final float[] to = {x1, y1, x2, y2};

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(2000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();

                from[0] = x1 + (x2 - x1) * fraction;
                from[1] = y1 + (y2 - y1) * fraction;

                for (float i = 0; i < 1; i += 0.1) {
                    float x = from[0] + i * (to[0] - from[0]);
                    float y = from[1] + i * (to[1] - from[1]);
                    float x2 = from[0] + (i + 0.1f) * (to[0] - from[0]);
                    float y2 = from[1] + (i + 0.1f) * (to[1] - from[1]);
                    canvas.drawLine(x, y, x2, y2, paint);
                }
                back.setImageBitmap(bitmap);
            }
        });

        animator.start();
    }
    private void animateLineWinner(int[] winPosition) {

        String StringWinPosition = Integer.toString(winPosition[0]) + Integer.toString(winPosition[1]) + Integer.toString(winPosition[2]);
        switch (StringWinPosition) {
            case "012":

                drawLineWinner(20,35,180,35);


                break;
            case "345":
                drawLineWinner(20, 105, 180, 105);
                break;

            case "678":
                drawLineWinner(20, 175, 180, 175);
                break;
            case "036":
                drawLineWinner(35, 20, 35, 190);
                break;

            case "147":
                drawLineWinner(100, 20, 100, 190);
                break;
            case "258":
                drawLineWinner(165, 20, 165, 190);
                break;
            case "048":
                drawLineWinner(20, 20, 180, 190);
                break;
            case "246":
                drawLineWinner(180, 20,20, 190);

                break;
            default:
                break;

        }

    }
    private void back(){


        paint.setColor(Color.rgb(69,67 ,68 ));

        paint.setStrokeWidth(5);
        paint.setShadowLayer(10, 0, 0, Color.BLACK);

        canvas.drawLine(20, 70, 180, 70, paint);

        canvas.drawLine(20, 140, 180, 140, paint);

        canvas.drawLine(70, 20, 70, 190, paint);

        canvas.drawLine(130, 20, 130, 190, paint);

        back = findViewById(R.id.back);
        back.setImageBitmap(bitmap);

    }


    private void setClickListenerForImage(int index,ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerTap(index, imageView);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("gameActive", gameActive);
        outState.putInt("activePlayer", activePlayer);
        outState.putIntArray("gameState",gameState);
        outState.putInt("tapCount", tapCount);
        outState.putIntArray("score",score);
        outState.putString("resetText",reset.getText().toString());
        outState.putString("statusText",status.getText().toString());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gameActive = savedInstanceState.getBoolean("gameActive");
        activePlayer = savedInstanceState.getInt("activePlayer");
        gameState = savedInstanceState.getIntArray("gameState");
        tapCount = savedInstanceState.getInt("tapCount");
        score = savedInstanceState.getIntArray("score");
        resetText = savedInstanceState.getString("resetText");
        statusText = savedInstanceState.getString("statusText");

    }
}