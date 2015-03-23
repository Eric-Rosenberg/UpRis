package erictonysean.upris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * 'Created' by Eric on 3/19/2015.
 */

/* Create a new grid object of size width x height */
public class Grid extends View
{
    private static final String TAG = "Log_Grid"; // For logs
    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint blackPaint = new Paint();
    private boolean[][] cellChecked;
    private boolean collision;
    private Random Rand = new Random();
    private boolean ready = true;
    private boolean canMake = true;
    private int furthestLeft,furthestRight; //TODO implement better
    private int currentRow, currentColumn;

    public Grid(Context context)
    {
        this(context, null);
    }

    public Grid(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void setNumColumns(int numColumns)
    {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumColumns()
    {
        return numColumns;
    }

    public void setNumRows(int numRows)
    {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;

    }
    /* Check if ready for next shape */
    //@return ready state
    public boolean checkReady(){
        return canMake;
    }
    /* Create a shape on the x coordiante return true when ready for next shape */
    //@param x bottom coordinate to generate shape
    //@return true when done
    public void makeShape(int x)
    {
        Log.d(TAG, "Filling Point " + x);
        ready = false;
        cellChecked[x][14] = true;
        furthestLeft = x; // TODO methods for each shape
        currentRow = 14;
        currentColumn = x;
        furthestRight = x;
        invalidate();
        moveShape();
        Log.d(TAG, "Exiting makeShape");
    }
    /* Move Shape every second until collision */
    public void moveShape()
    {
        new CountDownTimer(1000, 1000) { // Change first para x'000 seconds
            @Override
            public void onTick(long miliseconds) {  }

            @Override
            public void onFinish()
            {
                canMake = true;
                for (int i = 0; i < numColumns; i++) // Move UpRis
                {
                    for (int j = 0; j < numRows; j++)
                    {
                        if (cellChecked[i][j] && j != 0)
                        {
                            canMake = false;
                            //ready = false;
                            cellChecked[i][j] = false;
                            cellChecked[i][--j] = true;
                            currentRow--;
                            Log.d(TAG, "Current Row AND COLUMN: ROW: " + currentRow + ", COL: " + currentColumn);
                            invalidate();
                            start();
                        }
                    }
                }
            }
        }.start();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        Log.d(TAG, "Size Changed");
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions()
    {
        if (numColumns == 0 || numRows == 0)
            return;

        cellWidth = getWidth() / numColumns;
        cellHeight = getHeight() / numRows;

        cellChecked = new boolean[numColumns][numRows];

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        if (numColumns == 0 || numRows == 0)
            return;

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < numColumns; i++)
        {
            for (int j = 0; j < numRows; j++)
            {
                if (cellChecked[i][j])
                {
                    canvas.drawRect(i * cellWidth, j * cellHeight, (i + 1) * cellWidth, (j + 1) * cellHeight, blackPaint);
                }
            }
        }

        for (int i = 1; i < numColumns; i++)
        {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
        }

        for (int i = 1; i < numRows; i++)
        {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return true;

        int clickedColumn = (int)(event.getX() / cellWidth);
        int clickedRow = (int)(event.getY() / cellHeight);

        Log.d(TAG, "clickedColumn: " + clickedColumn);
        Log.d(TAG, "currentRow, currentColumn: " + currentRow + " " + currentColumn);
        if(currentRow != 0) {
            if (clickedColumn < currentColumn) {
                cellChecked[currentColumn][currentRow] = false;
                cellChecked[--currentColumn][currentRow] = true;
            }
            if (clickedColumn > currentColumn) {
                cellChecked[currentColumn][currentRow] = false;
                cellChecked[++currentColumn][currentRow] = true;
            }
            invalidate();
        }
        return true;
    }

}
