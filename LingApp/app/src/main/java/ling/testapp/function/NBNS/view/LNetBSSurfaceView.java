package ling.testapp.function.NBNS.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import ling.testapp.function.NBNS.LNetBSActivity;
import ling.testapp.function.NBNS.item.LNetBSItem;

/**
 * Created by jlchen on 2016/10/21.
 */

public class LNetBSSurfaceView extends SurfaceView  implements SurfaceHolder.Callback {

    public interface OnParameter {
        /**取得上市買賣超*/
        ArrayList<LNetBSItem> GetTesData();
        /**取得上櫃買賣超*/
        ArrayList<LNetBSItem> GetOtcData();
    }

    public interface OnListener {
        void noData(LNetBSActivity.eType type);
    }

    public interface OnInterface {
        /**切換上市上櫃按鈕*/
        void changeMode(LNetBSActivity.eType type);
    }

    private OnInterface m_interface = new OnInterface() {
        @Override
        public void changeMode(LNetBSActivity.eType type) {
            if ( type == LNetBSActivity.eType.TES ){

                if (null == m_alTES){
                    m_onListener.noData(type);
                }
            }else {

                if (null == m_alOTC){
                    m_onListener.noData(type);
                }
            }

            m_type = type;
        }
    };

    private OnParameter             m_onParameter   = null;
    private OnListener              m_onListener    = null;
    private ArrayList<LNetBSItem>   m_alTES         = null;
    private ArrayList<LNetBSItem>   m_alOTC         = null;
    private LNetBSActivity.eType    m_type          = LNetBSActivity.eType.TES;
    private Canvas                  m_canvas;

    public LNetBSSurfaceView(Context        context,
                             OnParameter    onParameter,
                             OnListener     onListener) {
        super(context);

        m_onParameter   = onParameter;
        m_onListener    = onListener;

        m_alTES = m_onParameter.GetTesData();
        m_alOTC = m_onParameter.GetOtcData();

        setZOrderOnTop(true);

        SurfaceHolder sfHolder = this.getHolder();
        sfHolder.setFormat(PixelFormat.TRANSLUCENT);
        sfHolder.addCallback(this);
    }

    public OnInterface getInterface() {
        return m_interface;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        drawNetBS(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void drawNetBS(SurfaceHolder holder) {
        m_canvas = holder.lockCanvas();
        m_canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//設置畫布背景為透明



        holder.unlockCanvasAndPost(m_canvas);
    }

    //清除原本畫的內容
    private void clearSv(){

        Paint clearPaint = new Paint();
        clearPaint.setAntiAlias(true);
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        //畫一個矩形蓋住原本的畫面
        LNetBSSurfaceView.this.m_canvas.drawRect(0, 0, 800, 480, clearPaint);
        Canvas canvas = LNetBSSurfaceView.this.getHolder().lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        canvas.drawBitmap(
//                LNetBSSurfaceView.this.mBitmap,
//                0,
//                0,
//                LNetBSSurfaceView.this.clearPaint);
        LNetBSSurfaceView.this.getHolder().unlockCanvasAndPost(canvas);
    }
}
