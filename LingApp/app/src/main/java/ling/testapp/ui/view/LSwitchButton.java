package ling.testapp.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import ling.testapp.R;

//自定義Switch(仿iOS)
public class LSwitchButton extends View {

    public  static final int        SHAPE_RECT              = 1;
    public  static final int        SHAPE_CIRCLE            = 2;
    private static final int        RIM_SIZE                = 1;

    private static final String BUNDLE_TAG_IS_OPEN          = "isOpen";
    private static final String BUNDLE_TAG_INSTANCE_STATE   = "instanceState";

    // 3 attributes
    private              int        m_iColorTheme;
    private              boolean    m_bIsOpen;
    private              int        m_iShape;

    // varials of drawing
    private Paint m_paint;
    private Rect m_rectBack;
    private Rect m_rectFront;
    private RectF m_rfFrontCircle;
    private RectF m_rfBackCircle;
    private              int        m_iAlpha;
    private              int        m_iMaxLeft;
    private              int        m_iMinLeft;
    private              int        m_iFrontRectLeft;
    private              int        m_iFrontRectLeftBegin   = RIM_SIZE;
    private              int        m_iEventStartX;
    private              int        m_iEventLastX;
    private              int        m_iDiffX                = 0;
    private              boolean    m_bSlideable            = true;

    private SlideListener           m_listener;

    public interface SlideListener {
        //判斷是否可以開啟遊戲開關
        boolean IsOpenSwitch(boolean isOpen);
        //開關切換完成後，回呼onChanged()
        void onChanged(LSwitchButton toggleButton, boolean isOpen);
    }

    public LSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_listener = null;
        m_paint = new Paint();
        m_paint.setAntiAlias(true);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.slideSwitch);
        m_iColorTheme = a.getColor(R.styleable.slideSwitch_themeColor,
                ContextCompat.getColor(context, R.color.pink));
        m_bIsOpen = a.getBoolean(R.styleable.slideSwitch_isOpen, false);
        m_iShape = a.getInt(R.styleable.slideSwitch_shape, SHAPE_RECT);
        a.recycle();
    }

    public LSwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LSwitchButton(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(280, widthMeasureSpec);
        int height = measureDimension(140, heightMeasureSpec);
        if (m_iShape == SHAPE_CIRCLE) {
            if (width < height)
                width = height * 2;
        }
        setMeasuredDimension(width, height);
        initDrawingVal();
    }

    public void initDrawingVal() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        m_rfBackCircle = new RectF();
        m_rfFrontCircle = new RectF();
        m_rectFront = new Rect();
        m_rectBack = new Rect(0, 0, width, height);
        m_iMinLeft = RIM_SIZE;
        if (m_iShape == SHAPE_RECT)
            m_iMaxLeft = width / 2;
        else
            m_iMaxLeft = width - (height - 3 * RIM_SIZE) - RIM_SIZE;
        if (m_bIsOpen) {
            m_iFrontRectLeft = m_iMaxLeft;
            m_iAlpha = 255;
        } else {
            m_iFrontRectLeft = RIM_SIZE;
            m_iAlpha = 0;
        }
        m_iFrontRectLeftBegin = m_iFrontRectLeft;
    }

    public int measureDimension(int defaultSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize; // UNSPECIFIED
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (m_iShape == SHAPE_RECT) {
            m_paint.setColor(Color.GRAY);
            canvas.drawRect(m_rectBack, m_paint);
            m_paint.setColor(m_iColorTheme);
            m_paint.setAlpha(m_iAlpha);
            canvas.drawRect(m_rectBack, m_paint);
            m_rectFront.set(m_iFrontRectLeft, RIM_SIZE, m_iFrontRectLeft
                    + getMeasuredWidth() / 2 - RIM_SIZE, getMeasuredHeight()
                    - RIM_SIZE);
            m_paint.setColor(Color.WHITE);
            canvas.drawRect(m_rectFront, m_paint);
        } else {
            // draw circle
            int radius;
            radius = m_rectBack.height() / 2 - RIM_SIZE;
            m_paint.setColor(Color.GRAY);
            m_rfBackCircle.set(m_rectBack);
            canvas.drawRoundRect(m_rfBackCircle, radius, radius, m_paint);
            m_paint.setColor(m_iColorTheme);
            m_paint.setAlpha(m_iAlpha);
            canvas.drawRoundRect(m_rfBackCircle, radius, radius, m_paint);
            m_rectFront.set(m_iFrontRectLeft, RIM_SIZE, m_iFrontRectLeft
                    + m_rectBack.height() - 3 * RIM_SIZE, m_rectBack.height()
                    - RIM_SIZE);
            m_rfFrontCircle.set(m_rectFront);
            m_paint.setColor(Color.WHITE);
            canvas.drawRoundRect(m_rfFrontCircle, radius, radius, m_paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (m_bSlideable == false)
            return super.onTouchEvent(event);
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            //使用者開始觸摸
            case MotionEvent.ACTION_DOWN:
                m_iEventStartX = (int) event.getRawX();
                break;
            //使用者的指標移動中
            case MotionEvent.ACTION_MOVE:
                m_iEventLastX = (int) event.getRawX();
                m_iDiffX = m_iEventLastX - m_iEventStartX;
                int tempX = m_iDiffX + m_iFrontRectLeftBegin;
                tempX = (tempX > m_iMaxLeft ? m_iMaxLeft : tempX);
                tempX = (tempX < m_iMinLeft ? m_iMinLeft : tempX);
                if (tempX >= m_iMinLeft && tempX <= m_iMaxLeft) {
                    m_iFrontRectLeft = tempX;
                    m_iAlpha = (int) (255 * (float) tempX / (float) m_iMaxLeft);
                    invalidateView();
                }
                break;
            //使用者抬起手指
            case MotionEvent.ACTION_UP:
                int wholeX = (int) (event.getRawX() - m_iEventStartX);
                m_iFrontRectLeftBegin = m_iFrontRectLeft;
                boolean toRight;
                toRight = (m_iFrontRectLeftBegin > m_iMaxLeft / 2 ? true : false);
                if (Math.abs(wholeX) < 3) {
                    toRight = !toRight;
                }
                //執行動畫
                moveToDest(toRight);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * draw again
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setSlideListener(SlideListener listener) {
        this.m_listener = listener;
    }

    public void moveToDest(final boolean toRight) {
        if (m_listener != null && toRight == m_listener.IsOpenSwitch(toRight)){
            //符合條件才可以開啟開關
            ValueAnimator toDestAnim = ValueAnimator.ofInt(m_iFrontRectLeft,
                    toRight ? m_iMaxLeft : m_iMinLeft);
            toDestAnim.setDuration(300);
            toDestAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            toDestAnim.start();
            toDestAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    m_iFrontRectLeft = (Integer) animation.getAnimatedValue();
                    m_iAlpha = (int) (255 * (float) m_iFrontRectLeft / (float) m_iMaxLeft);
                    invalidateView();
                }
            });

            toDestAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    if (toRight) {
                        m_bIsOpen = true;
                        m_iFrontRectLeftBegin = m_iMaxLeft;
                    } else {
                        m_bIsOpen = false;
                        m_iFrontRectLeftBegin = m_iMinLeft;
                    }

                    m_listener.onChanged(LSwitchButton.this, m_bIsOpen);
                }
            });
        }else {
            //否則一律為關閉狀態
            ValueAnimator toDestAnim = ValueAnimator.ofInt(m_iFrontRectLeft, RIM_SIZE);
            toDestAnim.setDuration(300);
            toDestAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            toDestAnim.start();
            toDestAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    m_iFrontRectLeft = (Integer) animation.getAnimatedValue();
                    m_iAlpha = 0;
                    invalidateView();
                }
            });

            toDestAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    m_bIsOpen = false;
                    m_iFrontRectLeftBegin = RIM_SIZE;

                    m_listener.onChanged(LSwitchButton.this, false);
                }
            });
        }
    }

    public void setState(boolean isOpen) {
        this.m_bIsOpen = isOpen;
        initDrawingVal();
        invalidateView();

        if (m_listener != null)
            if (isOpen == true) {
                m_listener.onChanged(LSwitchButton.this, true);
            } else {
                m_listener.onChanged(LSwitchButton.this, false);
            }
    }

    public void setShapeType(int shapeType) {
        this.m_iShape = shapeType;
    }

    public void setSlideable(boolean bSlideable) {
        this.m_bSlideable = bSlideable;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.m_bIsOpen = bundle.getBoolean(BUNDLE_TAG_IS_OPEN);
            state = bundle.getParcelable(BUNDLE_TAG_INSTANCE_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_TAG_INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putBoolean(BUNDLE_TAG_IS_OPEN, this.m_bIsOpen);
        return bundle;
    }

}