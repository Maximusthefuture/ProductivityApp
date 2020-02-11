package com.maximus.productivityappfinalproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.maximus.productivityappfinalproject.domain.model.AppsModel;


public class MyStatisticView extends View {
    private static final String TAG = "MyStatisticView";
    private Paint mPaint;
    private Context mContext;
    private AppsModel[] mDataArray;
    private float mMaxValueOfData;
    private final int mStrokeWidth = 2;
    private int mAxisFontSize = 14;
    private int mMaxValueCountOnYAxis = 9;
    private int mDistanceAxisAndValue;
    private int mMaxWidthOfYAxisText;
    private int mMaxHeightOfXAxisText;



    public MyStatisticView(Context context) {
        super(context);
    }

    public MyStatisticView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint = new Paint();
        init();
    }

    public MyStatisticView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mDistanceAxisAndValue = (int) dpToPixels(mContext, 14);
    }

    public void setYAxisData(AppsModel[] barData) {

        mDataArray = barData;
        mMaxValueOfData = Float.MIN_VALUE;
        for (int index = 0; index < mDataArray.length; index++) {

            if (mMaxValueOfData < mDataArray[index].getAppUsageTime())
                mMaxValueOfData = mDataArray[index].getAppUsageTime();
        }
        findMaxWidthOfText(barData);
        invalidate();
    }

    /**
     * Returns the maximum value in the data set.
     *
     * @return Maximum value in the data set.
     */
    public float getMaxValueOfData() {

        return mMaxValueOfData;
    }

    /**
     * Returns maximum width occupied by any of the Y axis values.
     *
     * @return maximum width occupied by any of the Y axis values
     */
    private int getMaxWidthOfYAxisText() {

        return mMaxWidthOfYAxisText;
    }

    /**
     * Calculate the maximum width occupied by any of given bar chart data. Width is calculated
     * based on default font used and size specified in {@link #mAxisFontSize}.
     *
     * @param appData data to be used in bar chart
     */
    private void findMaxWidthOfText(AppsModel[] appData) {

        mMaxWidthOfYAxisText = Integer.MIN_VALUE;
        mMaxHeightOfXAxisText = Integer.MIN_VALUE;

        Paint paint = new Paint();
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(dpToPixels(mContext, mAxisFontSize));

        Rect bounds = new Rect();

        for (int index = 0; index < mDataArray.length; index++) {
            int currentTextWidth =
                    (int) paint.measureText(Float.toString(appData[index].getAppUsageTime()));
            if (mMaxWidthOfYAxisText < currentTextWidth)
                mMaxWidthOfYAxisText = currentTextWidth;


            mPaint.getTextBounds(appData[index].getAppName(), 0,
                    appData[index].getAppName().length(), bounds);

            if (mMaxHeightOfXAxisText < bounds.height())
                mMaxHeightOfXAxisText = bounds.height();
        }
    }

    /**
     * Returns the maximum height of X Axis text.
     *
     * @return the maximum height of X Axis text
     */
    public int getMaxHeightOfXAxisText() {

        return mMaxHeightOfXAxisText;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int usableViewHeight = getHeight() - getPaddingBottom() - getPaddingTop();
        int usableViewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        Point origin = getOrigin();
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        mPaint.setStrokeWidth(mStrokeWidth);
        //draw y axis
        canvas.drawLine(origin.x, origin.y, origin.x,
                origin.y - (usableViewHeight - getXAxisLabelAndMargin()), mPaint);
        //draw x axis
        mPaint.setStrokeWidth(mStrokeWidth + 1);
        canvas.drawLine(origin.x, origin.y,
                origin.x + usableViewWidth -
                        (getMaxWidthOfYAxisText() +
                                mDistanceAxisAndValue), origin.y, mPaint);

        if (mDataArray == null || mDataArray.length == 0)
            return;
        //draw bar chart
        int barAndVacantSpaceCount = (mDataArray.length << 1) + 1;
        int widthFactor = (usableViewWidth - getMaxWidthOfYAxisText()) / barAndVacantSpaceCount;
        int x1, x2, y1, y2;
        float maxValue = getMaxValueOfData();
        for (int index = 0; index < mDataArray.length; index++) {
            x1 = origin.x + ((index << 1) + 1) * widthFactor;
            x2 = origin.x + ((index << 1) + 2) * widthFactor;
            int barHeight = (int) ((usableViewHeight - getXAxisLabelAndMargin()) *
                    mDataArray[index].getAppUsageTime() / maxValue);
            y1 = origin.y - barHeight;
            y2 = origin.y;
            canvas.drawRect(x1, y1, x2, y2, mPaint);
            showXAxisLabel(origin, mDataArray[index].getAppName(), x1 + (x2 - x1) / 2, canvas);
        }
        showYAxisLabels(origin, (usableViewHeight - getXAxisLabelAndMargin()), canvas);
    }

    /**
     * Formats the given float value up to one decimal precision point.
     *
     * @param value float which needs to be formatted
     *
     * @return String in the format "0.0" e.g. 100.0, 11.1
     *

     *
     */
    private String getFormattedValue(float value) {

        return Utils.formatMillisToSeconds((long)value);
    }

    /**
     * Draws Y axis labels and marker points along Y axis.
     *
     * @param origin           coordinates of origin on canvas
     * @param usableViewHeight view height after removing the padding
     * @param canvas           canvas to draw the chart
     */
    public void showYAxisLabels(Point origin, int usableViewHeight, Canvas canvas) {

        float maxValueOfData = (int) getMaxValueOfData();
        float yAxisValueInterval = usableViewHeight / mMaxValueCountOnYAxis;
        float dataInterval = maxValueOfData / mMaxValueCountOnYAxis;
        float valueToBeShown = maxValueOfData;
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(dpToPixels(mContext, mAxisFontSize));

        //draw all texts from top to bottom
        for (int index = 0; index < mMaxValueCountOnYAxis; index++) {
            String string = getFormattedValue(valueToBeShown);

            Rect bounds = new Rect();
            mPaint.getTextBounds(string, 0, string.length(), bounds);
            int y = (int) ((origin.y - usableViewHeight) + yAxisValueInterval * index);
            canvas.drawLine(origin.x - (mDistanceAxisAndValue >> 1), y, origin.x, y, mPaint);
            y = y + (bounds.height() >> 1);
            canvas.drawText(string, origin.x - bounds.width() - mDistanceAxisAndValue, y, mPaint);
            valueToBeShown = valueToBeShown - dataInterval;
        }
    }

    /**
     * Draws X axis labels.
     *
     * @param origin  coordinates of origin on canvas
     * @param label   label to be drawn below a bar along X axis
     * @param centerX center x coordinate of the given bar
     * @param canvas  canvas to draw the chart
     */
    public void showXAxisLabel(Point origin, String label, int centerX, Canvas canvas) {

        Rect bounds = new Rect();
        mPaint.getTextBounds(label, 0, label.length(), bounds);
        int y = origin.y + mDistanceAxisAndValue + getMaxHeightOfXAxisText();
        int x = centerX - bounds.width() / 2;
        mPaint.setTextSize(dpToPixels(mContext, 9));
        mPaint.setTypeface(Typeface.DEFAULT);
        canvas.drawText(label, x, y, mPaint);
    }

    /**
     * Returns the X axis' maximum label height and margin between label and the X axis.
     *
     * @return the X axis' maximum label height and margin between label and the X axis
     */
    private int getXAxisLabelAndMargin() {

        return getMaxHeightOfXAxisText() + mDistanceAxisAndValue;
    }

    /**
     * Returns the origin coordinates in canvas' coordinates.
     *
     * @return origin's coordinates
     */
    public Point getOrigin() {

        if (mDataArray != null) {

            return new Point(getPaddingLeft() + getMaxWidthOfYAxisText() + mDistanceAxisAndValue,
                    getHeight() - getPaddingBottom() - getXAxisLabelAndMargin());
        } else {

            return new Point(getPaddingLeft() + getMaxWidthOfYAxisText() + mDistanceAxisAndValue,
                    getHeight() - getPaddingBottom());
        }
    }

    /**
     * Конвертируем dp в пиксели
     *
     * @param context Context
     * @param dpValue значение в dp
     *
     * @return значение в пикселях
     */
    public static float dpToPixels(Context context, float dpValue) {

        if (context != null) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, metrics);
        }
        return 0;
    }



}
