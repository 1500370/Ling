package ling.testapp.function.NBNS.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ling.testapp.R;
import ling.testapp.function.NBNS.item.LNetBSItem;
import ling.testapp.function.NBNS.object.LNetBSViewScaleDef;

/**
 * Created by jlchen on 2016/10/21.
 */

public class LNetBSSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String     TAG             = "LNetBSSurfaceView";
    private static final int        MAX_WIDTH       = 100;

    public boolean                  m_bIsCreat      = false;

    private Context                 m_context       = null;
    private int                     m_iWidth        = 0;
    private int                     m_iHeight       = 0;
    private ArrayList<LNetBSItem>   m_alData        = null;
    private Canvas                  m_canvas;

    //單位
    private double                  m_dUnit         = 0.0;

    public LNetBSSurfaceView(Context context) {
        super(context);
        Log.d(TAG, "LNetBSSurfaceView()");

        m_context = context;

        setZOrderOnTop(true);

        SurfaceHolder sfHolder = this.getHolder();
        sfHolder.setFormat(PixelFormat.TRANSLUCENT);
        sfHolder.addCallback(this);
    }

    public LNetBSSurfaceView(Context context, ArrayList<LNetBSItem> arrayList) {
        super(context);
        Log.d(TAG, "LNetBSSurfaceView() array");

        m_context   = context;
        m_alData    = arrayList;

        setZOrderOnTop(true);

        SurfaceHolder sfHolder = this.getHolder();
        sfHolder.setFormat(PixelFormat.TRANSLUCENT);
        sfHolder.addCallback(this);
    }

    public void setWidthAndHeight(int iWidth, int iHeight){
        Log.d(TAG, "setWidthAndHeight()");

        m_iWidth    = iWidth;
        m_iHeight   = iHeight;
    }

    public void setNetBSData(ArrayList<LNetBSItem> arrayList){
        Log.d(TAG, "setNetBSData()");

        m_alData    = arrayList;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated()");

        drawLoading(holder);

        m_bIsCreat = true;

        if ( null != m_alData ){
            drawNetBS(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed()");

    }

    private void drawLoading(SurfaceHolder holder){
        Log.d(TAG, "drawLoading()");

        m_canvas = holder.lockCanvas();
        //設置畫布背景為透明
        m_canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        holder.unlockCanvasAndPost(m_canvas);
    }

    public void drawNetBS(SurfaceHolder holder) {
        Log.d(TAG, "drawNetBS()");

        if ( null == m_alData )
            return;

        LNetBSViewScaleDef vScaleDef = LNetBSViewScaleDef.getInstance(m_context, m_iWidth, m_iHeight);

        m_canvas = holder.lockCanvas();
        //設置畫布背景為透明
        m_canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        //畫文字
        //文字大小
        int iTextSize = vScaleDef.getTextSize(3);
        Paint pText = new Paint();
        pText.setColor(Color.WHITE);
        pText.setTextSize(iTextSize);
        pText.setTextAlign(Paint.Align.RIGHT);
        pText.setAntiAlias(true);

        //左邊的數字區間
        //Y軸數字的x座標
        int iStartX = vScaleDef.getLayoutWidth(11);
        //Y軸數字的y座標起始點
        int iStartY = vScaleDef.getLayoutHeight(3);
        //Y軸數字的y座標結束點
        int iEndY = vScaleDef.getLayoutHeight(95);
        //Y軸:每個數字間的間距
        int iSpacingY = (iEndY - iStartY) / 8;
        int iNumY = iStartY;
        int iMillion = iStartY + (iSpacingY / 2);

        double dMax = getMax();
        double dNum = dMax;
        double fNumSpacing = dNum / 4;
        DecimalFormat dfFloat = new DecimalFormat("0.00");
        DecimalFormat dfFloatZero = new DecimalFormat("0.##");
        for (int i = 0; i < 9; i++) {
            if (4 == i) {
                m_canvas.drawText(dfFloatZero.format(dNum), iStartX, iNumY, pText);
            } else {
                if (i == 0) {
                    //左邊的"億"單位
                    m_canvas.drawText("" + getResources().getText(R.string.nbns_hundred_million)
                            , iStartX
                            , iMillion
                            , pText);
                }
                m_canvas.drawText(dfFloat.format(dNum), iStartX, iNumY, pText);
            }
            dNum = dNum - fNumSpacing;
            iNumY = iNumY + iSpacingY;
        }

        //下方的每日日期
        //X軸日期的x座標
        int iDateStartX = vScaleDef.getLayoutWidth(19);
        int iDateEndX = vScaleDef.getLayoutWidth(99);
        //X軸日期的y座標
        int iDateEndY = vScaleDef.getLayoutHeight(99);
        //柱寬
        int iSpacingX = (vScaleDef.getLayoutWidth(82) - 90) / 10;
        int iDateX = iDateStartX;
        for (int i = m_alData.size(); i > 0; i--) {
            m_canvas.drawText(m_alData.get(i - 1).m_strSimpleDate, iDateX, iDateEndY, pText);
            iDateX = iDateX + (iSpacingX + 10);
        }
        //右下方的"日"
        m_canvas.drawText("" + getResources().getText(R.string.nbns_day).charAt(0)
                , iDateEndX
                , iDateEndY
                , pText);

        //柱狀圖背景

        //Y軸數字的y座標結束點
        int iNumEndY            = vScaleDef.getLayoutHeight(95);
        //柱狀圖x座標
        int iHistogramStartX    = vScaleDef.getLayoutWidth(17);
        int iHistogramX         = iHistogramStartX;

        Paint pDraw = new Paint();
        //柱狀圖 背景色 灰色
        pDraw.setColor(ContextCompat.getColor(m_context, R.color.sv_bg_gray));
        pDraw.setStrokeWidth(iSpacingX);
        pDraw.setAntiAlias(true);
        for (int i = 0; i < m_alData.size(); i++) {
            if (i == m_alData.size() - 1) {
                //柱狀圖 日期若為當日 背景色改用黑色
                pDraw.setColor(Color.BLACK);
            }
            m_canvas.drawLine(iHistogramX, 0, iHistogramX, iNumEndY, pDraw);
            iHistogramX = iHistogramX + (iSpacingX + 10);
        }

        //Y軸中線
        int iCenterY = iNumEndY / 2;
        //柱狀圖x座標 重置起始點
        iHistogramX = iHistogramStartX;

        //柱狀圖(藍)
        for (int i = (m_alData.size() - 1); i >= 0; i--) {
            if (0 < m_alData.get(i).m_dTotal) {
                pDraw.setColor(ContextCompat.getColor(m_context, R.color.sv_positive_blue));
                m_canvas.drawLine(
                        iHistogramX
                        , iCenterY
                        , iHistogramX
                        , (float)(iCenterY - iCenterY * (m_alData.get(i).m_dTotal / dMax))
                        , pDraw);
            } else {
                pDraw.setColor(ContextCompat.getColor(m_context, R.color.sv_negative_blue));
                m_canvas.drawLine(
                        iHistogramX
                        , iCenterY
                        , iHistogramX
                        , (float)(iCenterY + iCenterY * (m_alData.get(i).m_dTotal / (0 - dMax)))
                        , pDraw);
            }
            iHistogramX = iHistogramX + (iSpacingX + 10);
        }

        //畫柱狀圖合計數字(三條曲線圖都不顯示時,才會顯示合計數字)
        pText.setTextAlign(Paint.Align.CENTER);
        pText.setTextSize(m_iWidth * 25 / 1000);
        pText.setColor(Color.WHITE);
        pText.setShadowLayer(5, 1, 1, Color.BLACK);
        iHistogramX = iHistogramStartX;
        int iMoveUpY = m_iHeight * 1 / 100;
        int iMoveDownY = m_iHeight * 3 / 100;
//        if (false == m_bBrk
//                && false == m_bIt
//                && false == m_bQfii) {
//            for (int i = (m_alSum.size() - 1); i >= 0; i--) {
//                if (0 < m_alSum.get(i)) {
//                    m_canvas.drawText(dfFloat.format(m_alSum.get(i))
//                            , iHistogramX
//                            , m_iCenterY - m_iCenterY * (m_alSum.get(i) / m_fMax)
//                                    + m_iMoveDownY
//                            , pText);
//                } else {
//                    m_canvas.drawText(dfFloat.format(m_alSum.get(i))
//                            , iHistogramX
//                            , m_iCenterY + m_iCenterY * (m_alSum.get(i) / (0 - m_fMax))
//                                    -  m_iMoveUpY
//                            , pText);
//                }
//                iHistogramX = iHistogramX + (m_iSpacingX + 10);
//            }
//        }

        //曲線圖
        pDraw.setStrokeWidth(2);
        Paint pDrawCircle = new Paint();
        pDrawCircle.setAntiAlias(true);

//        if (true == m_bQfii) {
            pDraw.setColor(ContextCompat.getColor(m_context, R.color.qfii_red));
            pDrawCircle.setColor(ContextCompat.getColor(m_context, R.color.qfii_red));
//            if (false == m_bIt && false == m_bBrk) {
//                drawGraph(pDraw, pDrawCircle, m_alQfii, pText);
//            } else {
//                drawGraph(pDraw, pDrawCircle, m_alQfii, null);
//            }
//        }
//        if (true == m_bIt) {
            pDraw.setColor(ContextCompat.getColor(m_context, R.color.brk_orange));
            pDrawCircle.setColor(ContextCompat.getColor(m_context, R.color.brk_orange));
//            if (false == m_bQfii && false == m_bBrk) {
//                drawGraph(pDraw, pDrawCircle, m_alIt, pText);
//            } else {
//                drawGraph(pDraw, pDrawCircle, m_alIt, null);
//            }
//        }
//        if (true == m_bBrk) {
            pDraw.setColor(ContextCompat.getColor(m_context, R.color.it_green));
            pDrawCircle.setColor(ContextCompat.getColor(m_context, R.color.it_green));
//            if (false == m_bQfii && false == m_bIt) {
//                drawGraph(pDraw, pDrawCircle, m_alBrk, pText);
//            } else {
//                drawGraph(pDraw, pDrawCircle, m_alBrk, null);
//            }
//        }

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

    public void drawGraph(Paint pDrawLine
            , Paint pDrawCircle
            , ArrayList<Float> alFloat
            , Paint pText) {
//        float fLastX = 0;            //記錄上一個xy座標
//        float fLastY = 0;
//        int iGraphX = m_iHeight * 17 / 100;
//        int iCircleSize = m_iWidth / 100;
//        DecimalFormat dfFloat = new DecimalFormat("0.00");
//        for (int i = (alFloat.size() - 1); i >= 0; i--) {
//            //畫線
//            if ((alFloat.size() - 1) != i) {
//                m_canvas.drawLine(fLastX
//                        , fLastY
//                        , iGraphX
//                        , m_iCenterY - m_iCenterY * (alFloat.get(i) / m_fMax)
//                        , pDrawLine);
//            }
//
//            //畫點
//            if (0 <= alFloat.get(i)) {
//                m_canvas.drawCircle(
//                        iGraphX
//                        , m_iCenterY - m_iCenterY * (alFloat.get(i) / m_fMax)
//                        , iCircleSize
//                        , pDrawCircle);
//                fLastY = m_iCenterY - m_iCenterY * (alFloat.get(i) / m_fMax);
//
//            } else {
//                m_canvas.drawCircle(
//                        iGraphX
//                        , m_iCenterY + m_iCenterY * (alFloat.get(i) / (0 - m_fMax))
//                        , iCircleSize
//                        , pDrawCircle);
//                fLastY = m_iCenterY + m_iCenterY * (alFloat.get(i) / (0 - m_fMax));
//            }
//            fLastX = iGraphX;
//            iGraphX = iGraphX + (m_iSpacingX + 10);
//
//        }
//
//        //畫曲線圖數字
//        if (null != pText) {
//            int iMoveUpY = m_iHeight * 3 / 100;
//            int iMoveDownY = m_iHeight * 5 / 100;
//            iGraphX = m_iWidth * 17 / 100;
//            for (int i = (alFloat.size() - 1); i >= 0; i--) {
//                if (0 <= alFloat.get(i)) {
//                    m_canvas.drawText(dfFloat.format(alFloat.get(i))
//                            , iGraphX
//                            , m_iCenterY - m_iCenterY * (alFloat.get(i) / m_fMax) + iMoveDownY
//                            , pText);
//                } else {
//                    m_canvas.drawText(dfFloat.format(alFloat.get(i))
//                            , iGraphX
//                            , m_iCenterY + m_iCenterY * (alFloat.get(i) / (0 - m_fMax)) - iMoveUpY
//                            , pText);
//                }
//                iGraphX = iGraphX + (m_iSpacingX + 10);
//            }
//        }
    }

    private double getMax(){
        double dNum = 0.0;
        for (int i = 0; i < m_alData.size(); i ++ ){
            double dQfii = Math.abs(m_alData.get(i).m_dQfii);
            if ( dNum < dQfii ){
                dNum = dQfii;
            }

            double dBrk = Math.abs(m_alData.get(i).m_dBrk);
            if ( dNum < dBrk ){
                dNum = dBrk;
            }

            double dIt = Math.abs(m_alData.get(i).m_dIt);
            if ( dNum < dIt ){
                dNum = dIt;
            }

            double dTotal = Math.abs(m_alData.get(i).m_dTotal);
            if ( dNum < dTotal ){
                dNum = dTotal;
            }
        }

        return dNum;
    }
}
