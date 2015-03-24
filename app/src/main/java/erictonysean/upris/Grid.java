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
 * 'Created' by Eric and Tony on 3/19/2015.
 */

/* Create a new grid object of size width x height */
public class Grid extends View {
    private static final String TAG = "Log_Grid"; // For logs
    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private Paint blackPaint = new Paint();
    private boolean[][] cellChecked;
    private Random Rand = new Random();
    private boolean canMake = true;
    private int leftBound, rightBound;
    private int currentRow, currentColumn;

    private int r, g, b;
    private int shape;
    private int[][] redArray = new int[15][15];
    private int[][] greenArray = new int[15][15];
    private int[][] blueArray = new int[15][15];

    public Grid(Context context) {
        this(context, null);
    }

    public Grid(Context context, AttributeSet attrs) {
        super(context, attrs);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    /* Check if ready for next shape */
    //@return ready state
    public boolean checkReady() {
        return canMake;
    }

    /* Create a shape on the x coordinate return true when ready for next shape */
    //@param x bottom coordinate to generate shape
    //@return true when done
    public void makeShape(int x) {
        r = Rand.nextInt(255);
        g = Rand.nextInt(255);
        b = Rand.nextInt(255);
        shape = 0;/*Rand.nextInt(2);*/
        //Log.d(TAG, "Starting Column: " + x);

        if (shape == 1) {
            cellChecked[x][14] = true;
            currentRow = 14;
            currentColumn = x;
            updateBounds(-1);
        } else //IT'S A LINE BRO
        {
            cellChecked[x][14] = true;
            cellChecked[x][13] = true;
            cellChecked[x][12] = true;
            cellChecked[x][11] = true;
            currentColumn = x;
            currentRow = 11;
            updateBounds(-1);
        }
        Log.d(TAG, "Shape = " + shape);
        invalidate();
        moveShape();
        //     Log.d(TAG, "Exiting makeShape");
    }

    /* Update rightBound and leftBound based on moved direction*/
    //@param moveType direction moved 0 left 1 right -1 default
    void updateBounds(int moveType) {
        if (moveType == -1) {
            if (shape == 1) {// Dot
                leftBound = currentColumn;
                rightBound = currentColumn;
                Log.d(TAG, "Object Created with bounds " + leftBound);
            } else { // Line
                leftBound = currentColumn;
                rightBound = currentColumn;
            }
        } else if (moveType == 1) { // moved right
            if (shape == 1) { // Dot
                leftBound++;
                rightBound++;
                Log.d(TAG, "Bound updated " + leftBound);
            } else { // Line
                leftBound++;
                rightBound++;
                Log.d(TAG, "Bound updated " + leftBound);
            }
        } else { // moved left
            if (shape == 1) {
                leftBound--;
                rightBound--;
                Log.d(TAG, "Bound updated " + leftBound);
            } else {
                leftBound--;
                rightBound--;
                Log.d(TAG, "Bound updated " + leftBound);
            }
        }
    }

    /* Fill a row except the last column*/
    //@param x row to be filled
    void fillRow(int x) {
        Log.d(TAG, "Filling Row " + x);
        for (int i = 0; i < numColumns - 1; i++) {
            cellChecked[i][x] = true;
        }
        invalidate();
    }

    /* Move Shape every second until collision */
    //cellChecked[Col][Row]
    public void moveShape() {
        new CountDownTimer(300, 1000) { // Change first para x'000 seconds
            @Override
            public void onTick(long miliseconds) {
            }

            @Override
            public void onFinish() {
                canMake = true;
                if (shape == 1) // Dot
                {
                    for (int i = currentColumn; i == currentColumn; i++) // Move UpRis
                        for (int j = currentRow; j == currentRow; j++)
                            if (cellChecked[i][j] && j != 0 && !cellChecked[leftBound][j - 1] && !cellChecked[rightBound][j - 1]) // Collision detection
                            {
                                canMake = false;//ready = false;
                                cellChecked[i][j] = false;
                                cellChecked[i][--j] = true;
                                currentRow--;
                                //Log.d(TAG, "Current Row AND COLUMN: ROW: " + currentRow + ", COL: " + currentColumn);
                                invalidate();
                                start();
                                checkFullRow();
                            }
                } else // Line
                {
                    for (int i = currentColumn; i == currentColumn; i++)
                        for (int j = currentRow; j == currentRow; j++)
                            if (cellChecked[i][j] && j != 0 && !cellChecked[leftBound][j - 1] && !cellChecked[rightBound][j - 1]) // Collision detection
                            {
                                canMake = false;//canMake = false;
                                cellChecked[i][j + 3] = false;
                                cellChecked[i][--j] = true;
                                currentRow--;
                                invalidate();
                                start();
                                checkFullRow();
                                Log.d(TAG, "Current Row AND COLUMN: ROW: " + currentRow + ", COL: " + currentColumn);
                            }
                }
                if (canMake == true) {
                    if (shape == 1) { // Dot
                        Log.d(TAG, "Finished moving -- Blocks been placed " + currentColumn + " " + currentRow + " " + r + " " + g + " " + b);
                        redArray[currentColumn][currentRow] = r;
                        greenArray[currentColumn][currentRow] = g;
                        blueArray[currentColumn][currentRow] = b;
                        Log.d(TAG, "Done storying colors");
                    } else { // Line
                        for (int i = 0; i < 4; i++) {
                            redArray[currentColumn][currentRow+i] = r;
                            greenArray[currentColumn][currentRow+i] = g;
                            blueArray[currentColumn][currentRow+i] = b;
                        }
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //     Log.d(TAG, "Size Changed");
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    /* Check if an entire row is filled */
    private void checkFullRow() {
        boolean rowFull = true;

        for (int i = 0; i < numColumns; i++)
            if (!cellChecked[i][0]) {
                rowFull = false;
                break;
            }

        if (rowFull == true) {
            boolean[][] tempArray = new boolean[15][15];

            for (int i = 0; i < numColumns; i++)
                for (int j = 0; j < numRows; j++)
                    tempArray[i][j] = false;

            Log.d(TAG, "You filled a row. Oh thank god");

            String makeStuff;
            Log.d(TAG, "BEFORE");
            for (int i = 0; i < numColumns; i++) {
                makeStuff = "";
                for (int j = 0; j < numRows; j++) {
                    if (cellChecked[i][j] == true)
                        makeStuff = (makeStuff + "T ");
                    else
                        makeStuff = (makeStuff + "F ");
                }
                Log.d(TAG, makeStuff);
            }

            for (int i = 0; i < numColumns; i++) {
                for (int j = 1; j < numRows; j++) {
                    if (cellChecked[i][j] == true)
                        tempArray[i][j - 1] = true;
                    else
                        tempArray[i][j - 1] = false;
                }
            }

            for (int i = 0; i < numColumns; i++)
                for (int j = 1; j < numRows; j++)
                    redArray[i][j - 1] = redArray[i][j];

            for (int i = 0; i < numColumns; i++)
                for (int j = 1; j < numRows; j++) {
                    greenArray[i][j - 1] = greenArray[i][j];
                }

            for (int i = 0; i < numColumns; i++)
                for (int j = 1; j < numRows; j++) {
                    blueArray[i][j - 1] = blueArray[i][j];
                }

            for (int i = 0; i < numColumns; i++)
                for (int j = 0; j < numRows; j++)
                    cellChecked[i][j] = tempArray[i][j];

            invalidate();

            Log.d(TAG, "AFTER!");

            for(int i = 0; i < numColumns; i++)
            {
                makeStuff = "";
                for (int j = 0; j < numRows; j++)
                {
                    if (cellChecked[i][j] == true)
                        makeStuff = (makeStuff + "T ");
                    else
                        makeStuff = (makeStuff + "F ");
                }
                Log.d(TAG, makeStuff);
            }

            Log.d(TAG, "NEW ARRAY");

            /*for(int i = 0; i < numColumns; i++)
            {
                makeStuff = "";
                for (int j = 0; j < numRows; j++)
                {
                    if (tempArray[i][j] == true)
                        makeStuff = (makeStuff + "T ");
                    else
                        makeStuff = (makeStuff + "F ");
                }
                Log.d(TAG, makeStuff);
            }*/
        }
    }

    private void calculateDimensions() {
        if (numColumns == 0 || numRows == 0)
            return;

        cellWidth = getWidth() / numColumns;
        cellHeight = getHeight() / numRows;

        cellChecked = new boolean[numColumns][numRows];
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                cellChecked[i][j] = false;
            }
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        if (numColumns == 0 || numRows == 0)
            return;

        int width = getWidth();
        int height = getHeight();

        Paint blockColor = new Paint();

        for (int i = 0; i < numColumns; i++)
            for (int j = 0; j < numRows; j++) {
                if (cellChecked[i][j]) {
                    /*int r = Rand.nextInt(255);
                    int g = Rand.nextInt(255);
                    int b = Rand.nextInt(255);*/
                    blockColor.setARGB(255, redArray[i][j], greenArray[i][j], blueArray[i][j]);
                    blockColor.setStyle(Paint.Style.FILL_AND_STROKE);
                    canvas.drawRect(i * cellWidth, j * cellHeight, (i + 1) * cellWidth, (j + 1) * cellHeight, blockColor);
                }
            }

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return true;

        int clickedColumn = (int) (event.getX() / cellWidth);
        int clickedRow = (int) (event.getY() / cellHeight);

        if (currentRow != 0) {
            if (clickedColumn < currentColumn && !cellChecked[leftBound - 1][currentRow]) // Move left
            {
                if (shape == 0) { // Line
                    cellChecked[currentColumn][currentRow] = false;
                    cellChecked[currentColumn][currentRow + 1] = false;
                    cellChecked[currentColumn][currentRow + 2] = false;
                    cellChecked[currentColumn][currentRow + 3] = false;

                    cellChecked[--currentColumn][currentRow] = true;
                    cellChecked[currentColumn][currentRow + 1] = true;
                    cellChecked[currentColumn][currentRow + 2] = true;
                    cellChecked[currentColumn][currentRow + 3] = true;

                    updateBounds(0);
                } else { // Single unit
                    cellChecked[currentColumn][currentRow] = false;
                    cellChecked[--currentColumn][currentRow] = true;
                    updateBounds(0);
                }
            }
            if (clickedColumn > currentColumn && !cellChecked[leftBound + 1][currentRow]) // Move right
            {
                if(shape == 0) { // LINE
                    cellChecked[currentColumn][currentRow] = false;
                    cellChecked[currentColumn][currentRow + 1] = false;
                    cellChecked[currentColumn][currentRow + 2] = false;
                    cellChecked[currentColumn][currentRow + 3] = false;

                    cellChecked[++currentColumn][currentRow] = true;
                    cellChecked[currentColumn][currentRow + 1] = true;
                    cellChecked[currentColumn][currentRow + 2] = true;
                    cellChecked[currentColumn][currentRow + 3] = true;
                    updateBounds(1);
                }
                else { // Dot
                    cellChecked[currentColumn][currentRow] = false;
                    cellChecked[++currentColumn][currentRow] = true;
                    updateBounds(1);
                }
            }
            invalidate();
        }
        return true;
    }

}
