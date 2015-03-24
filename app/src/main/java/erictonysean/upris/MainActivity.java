package erictonysean.upris;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import java.util.Random;


public class MainActivity extends Activity implements Runnable {
    private static final String TAG = "Log_Main"; // For logs
    Grid grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Create Grid */
        Log.d(TAG, "Creating Grid");
        grid = new Grid(this);
        grid.setNumColumns(15);
        grid.setNumRows(15);

        setContentView(grid);
        createShape();
    }
    /* Wohoo thread */
    public void run(){
//        while(true) {
//            while (!grid.checkReady())
//                grid.moveShape();
//        }
    }
    public void createShape()
    {
      //  (new Thread(new MainActivity())).start();
        /* Wait x / 1000 seconds */
        new CountDownTimer(300, 1000)
        {
            boolean done = false;
            @Override
            public void onTick(long miliseconds)
            { }

            @Override
            public void onFinish()
            {
                if(done == false)
                {
                    grid.fillRow(0);
                    done = true;
                }

                Random rand = new Random();
                int x = rand.nextInt(15);
         //       Log.d(TAG, "Hi lady");
                if(grid.checkReady() == true)
                {
                    grid.makeShape(x);

                }
                start(); // Loop back
            }
        }.start();
    }
}