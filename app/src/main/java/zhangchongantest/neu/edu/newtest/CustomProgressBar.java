package zhangchongantest.neu.edu.newtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.PrimitiveIterator;

/**
 * Author: Cheung SzeOn
 * Date: 2019/8/3 17:32
 * Description: 自定义progressBar
 * History:
 */
public class CustomProgressBar extends View {
    private Paint mProgressValuePaint;
    private Paint mSliderPaint;
    private Paint mBitmapPaint;
    private Paint mBackGroundPaint;

    private Point mLeftTopPoint;
    private Point mRightButtonPoint;

    private int progressColor;
    private int restColor;
    private int maxValue;
    private int presentValue;
    private float sliderWidth;

    private Bitmap fgSrc;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mProgressValuePaint = new Paint();

        mBackGroundPaint= new Paint();

        mSliderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //只描边
        mSliderPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSliderPaint.setStrokeWidth(sliderWidth);
        mSliderPaint.setColor(getResources().getColor(R.color.sliderColor, null));

        mLeftTopPoint = new Point(0, 0);

        initAttrs(context, attrs);
        invalidate();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.custom_progress_bar);
        maxValue = array.getInteger(R.styleable.custom_progress_bar_maxValue, 100);
        progressColor = array.getColor(R.styleable.custom_progress_bar_progressColor, context.getColor(R.color.progressColorDefault));
        restColor = array.getColor(R.styleable.custom_progress_bar_restColor, context.getColor(R.color.restColorDefault));
        sliderWidth = array.getInt(R.styleable.custom_progress_bar_silderWidth, 3);
        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRightButtonPoint = new Point(getWidth(), getHeight());
        float rate = (float) presentValue / (float) maxValue;
        drawSlider(canvas, mRightButtonPoint);
        drawProgressBar(canvas, mLeftTopPoint, mRightButtonPoint, rate);
    }

    private void drawProgressBar(Canvas canvas, Point leftTopPoint, Point rightButtomPoint, float rate) {

        RectF buttonRect = new RectF(leftTopPoint.x,leftTopPoint.y,rightButtomPoint.x,rightButtomPoint.y);
        mBackGroundPaint.setColor(restColor);
        canvas.drawRoundRect(buttonRect,30,30,mBackGroundPaint);
//        if (mBitmapPaint == null) {
//            mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        }
        RectF topRect = new RectF(leftTopPoint.x + 3, leftTopPoint.y + 3, (int) (getWidth() * rate) + 3, getHeight() - 3);
        mProgressValuePaint.setColor(progressColor);
        canvas.drawRoundRect(topRect,30,30,mProgressValuePaint);

    }

    private void drawSlider(Canvas canvas, Point viewSize) {

        RectF rectF = new RectF(0, 0, viewSize.x, viewSize.y);

        canvas.drawRoundRect(rectF, 30, 30, mSliderPaint);
    }

    public void updateValue(int value) {
        if (value > maxValue || value < 0) {
            new Exception();
            return;
        }
        presentValue = value;
        invalidate();
    }
}
