package ling.testapp.function.NBNS.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
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

    enum eType{
        QFII, BRK, IT
    }

    private static final String     TAG             = "LNetBSSurfaceView";

    public boolean                  m_bIsCreat      = false;

    private Context                 m_context       = null;
    private int                     m_iWidth        = 0;
    private int                     m_iHeight       = 0;
    private String                  m_strMsg        = null;
    private ArrayList<LNetBSItem>   m_alData        = null;
    private Canvas                  m_canvas;

    //柱寬
    private int                     m_iSpacing      = 0;
    //最大值
    private double                  m_dMax          = 0.0;
    //柱狀圖Y軸座標結束點
    private int                     m_iHistogramEndY= 0;
    //柱狀圖Y軸中線
    private int                     m_iCenterY      = 0;
    private LNetBSViewScaleDef      m_vScaleDef     = null;
    private boolean                 m_bQfii         = true;
    private boolean                 m_bBrk          = true;
    private boolean                 m_bIt           = true;

    public LNetBSSurfaceView(Context context) {
        super(context);
        Log.d(TAG, "LNetBSSurfaceView()");

        initial(context, null, null);
    }

    public LNetBSSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initial(context, null, null);
    }

    public LNetBSSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initial(context, null, null);
    }

    private void initial(Context context, ArrayList<LNetBSItem> arrayList, String strMsg) {
        Log.d(TAG, "init()");

        m_context   = context;

        if ( null != arrayList )
            m_alData    = arrayList;

        if ( null != strMsg )
            m_strMsg    = strMsg;
        else
            m_strMsg    = m_context.getString(R.string.loading);

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

    public void setShowLine(boolean bQfii, boolean bIt, boolean bBrk){
        Log.d(TAG, "setShowLine()");

        m_bQfii     = bQfii;
        m_bBrk      = bBrk;
        m_bIt       = bIt;
    }

    public void setErrorMsg(String strMsg){
        Log.d(TAG, "setErrorMsg()");

        m_strMsg    = strMsg;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated()");

        m_bIsCreat = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged()");

        LNetBSViewScaleDef.setInstance(null);
        m_vScaleDef = LNetBSViewScaleDef.getInstance(m_context, width, height);

        if ( null != m_alData ){
            drawNetBS(holder);
        }else {
            drawMsg(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed()");

    }

    public void drawMsg(SurfaceHolder holder){
        Log.d(TAG, "drawLoading()");

        m_canvas = holder.lockCanvas();
        //設置畫布背景為透明
        m_canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        //文字大小
        int iTextSize = m_vScaleDef.getTextSize(6);

        //畫文字
        Paint pText = new Paint();
        pText.setColor(Color.WHITE);
        pText.setTextSize(iTextSize);
        pText.setTextAlign(Paint.Align.CENTER);
        pText.setAntiAlias(true);
        m_canvas.drawText(
                m_strMsg,
                m_vScaleDef.getLayoutWidth(50),
                m_vScaleDef.getLayoutHeight(50),
                pText);

        holder.unlockCanvasAndPost(m_canvas);
    }

    public void drawNetBS(SurfaceHolder holder) {
        Log.d(TAG, "drawNetBS()");

        if ( null == m_alData || 0 == m_alData.size() ){
            drawMsg(holder);

            return;
        }

        if ( null == m_vScaleDef )
            m_vScaleDef = LNetBSViewScaleDef.getInstance(m_context, m_iWidth, m_iHeight);

        //柱寬
        m_iSpacing          = (m_vScaleDef.getLayoutWidth(82) - 90) / 10;
        //最大值
        m_dMax              = getMax();
        //柱狀圖Y軸座標結束點
        m_iHistogramEndY    = m_vScaleDef.getLayoutHeight(95);
        //柱狀圖Y軸中線
        m_iCenterY          = m_iHistogramEndY / 2;

        m_canvas = holder.lockCanvas();
        //設置畫布背景為透明 (將上次畫的覆蓋掉)
        m_canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        //畫文字的筆
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(m_vScaleDef.getTextSize(4));
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setAntiAlias(true);
        //畫左邊y軸的數字
        drawLeftNum(paint);
        //畫下方x軸的每日日期
        drawDayText(paint);

        //畫柱狀圖的筆
        paint.setColor(ContextCompat.getColor(m_context, R.color.sv_bg_gray));
        paint.setStrokeWidth(m_iSpacing);
        paint.setAntiAlias(true);
        //柱狀圖起始點
        int iHistogramX = m_vScaleDef.getLayoutWidth(17);
        //畫柱狀圖背景(灰/黑)
        drawHistogramBg(paint, iHistogramX);
        //畫柱狀圖(藍) 顯示三大法人合計數與最大正負值的差距 亮藍色表示正數; 深藍表示負數
        drawHistogram(paint, iHistogramX);

        //畫數據文字的筆
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(m_vScaleDef.getTextSize(3));
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setShadowLayer(3, 0, 0, Color.BLACK);
        if (false == m_bBrk && false == m_bIt && false == m_bQfii) {
            //畫柱狀圖合計數字(三條曲線圖都不顯示時,才會顯示合計數字)
            drawTotalText(paint, iHistogramX);
        }else {
            //三大法人曲線圖
            int iGraphX =  m_vScaleDef.getLayoutWidth(17);

            Paint pLine = new Paint();
            pLine.setStrokeWidth(2);
            pLine.setAntiAlias(true);
            Paint pCircle = new Paint();
            pCircle.setAntiAlias(true);

            paint.setShadowLayer(3, 0, 0, Color.BLACK);
            //外資
            if (true == m_bQfii) {
                paint.setColor(ContextCompat.getColor(m_context, R.color.qfii_red_press));
                pLine.setColor(ContextCompat.getColor(m_context, R.color.qfii_red));
                pCircle.setColor(ContextCompat.getColor(m_context, R.color.qfii_red));

                ArrayList arrayList = getDoubleArrayList(eType.QFII);
                //畫外資曲線圖
                drawGraph(pLine, pCircle, iGraphX, arrayList);

                if (false == m_bIt && false == m_bBrk) {
                    //只顯示外資時 另顯示外資數字
                    drawGraphText(paint, iGraphX, arrayList);
                }
            }
            //投信
            if (true == m_bIt) {
                paint.setColor(ContextCompat.getColor(m_context, R.color.it_green_press));
                pLine.setColor(ContextCompat.getColor(m_context, R.color.it_green));
                pCircle.setColor(ContextCompat.getColor(m_context, R.color.it_green));

                ArrayList arrayList = getDoubleArrayList(eType.IT);
                drawGraph(pLine, pCircle, iGraphX, arrayList);

                if (false == m_bQfii && false == m_bBrk) {
                    drawGraphText(paint, iGraphX, arrayList);
                }
            }
            //自營
            if (true == m_bBrk) {
                paint.setColor(ContextCompat.getColor(m_context, R.color.brk_orange_press));
                pLine.setColor(ContextCompat.getColor(m_context, R.color.brk_orange));
                pCircle.setColor(ContextCompat.getColor(m_context, R.color.brk_orange));

                ArrayList arrayList = getDoubleArrayList(eType.BRK);
                drawGraph(pLine, pCircle, iGraphX, arrayList);

                if (false == m_bQfii && false == m_bIt) {
                    drawGraphText(paint, iGraphX, arrayList);
                }
            }
        }

        holder.unlockCanvasAndPost(m_canvas);
    }

    private void drawLeftNum(Paint paint){

        //Y軸數字的x座標
        int iStartX = m_vScaleDef.getLayoutWidth(11);
        //Y軸數字的y座標起始點
        int iStartY = m_vScaleDef.getLayoutHeight(3);
        //Y軸數字的y座標結束點
        int iEndY = m_vScaleDef.getLayoutHeight(94);
        //Y軸:每個數字間的間距
        int iSpacingY = (iEndY - iStartY) / 8;
        int iNumY = iStartY;
        int iMillion = iStartY + (iSpacingY / 2);

        double dNum = m_dMax;
        double dNumSpacing = dNum / 4;
        DecimalFormat dfFloat = new DecimalFormat("0.00");
        DecimalFormat dfFloatZero = new DecimalFormat("0.##");
        for (int i = 0; i < 9; i++) {
            if (4 == i) {
                m_canvas.drawText(dfFloatZero.format(dNum), iStartX, iNumY, paint);
            } else {
                if (i == 0) {
                    //左邊的"億"單位
                    m_canvas.drawText("" + getResources().getText(R.string.nbns_hundred_million)
                            , iStartX
                            , iMillion
                            , paint);
                }
                m_canvas.drawText(dfFloat.format(dNum), iStartX, iNumY, paint);
            }
            dNum = dNum - dNumSpacing;
            iNumY = iNumY + iSpacingY;
        }
    }

    private void drawDayText(Paint paint){
        //X軸日期的x座標
        int iDateStartX = m_vScaleDef.getLayoutWidth(19);
        int iDateEndX = m_vScaleDef.getLayoutWidth(99);
        //X軸日期的y座標
        int iDateEndY = m_vScaleDef.getLayoutHeight(99);

        int iDateX = iDateStartX;
        for (int i = m_alData.size(); i > 0; i--) {
            m_canvas.drawText(m_alData.get(i - 1).m_strDay, iDateX, iDateEndY, paint);
            iDateX = iDateX + (m_iSpacing + 10);
        }
        //右下方的"日"
        m_canvas.drawText(m_context.getString(R.string.nbns_day)
                , iDateEndX
                , iDateEndY
                , paint);
    }

    private void drawHistogramBg(Paint paint, int iHistogramX){

        for (int i = 0; i < m_alData.size(); i++) {
            if (i == m_alData.size() - 1) {
                //柱狀圖 日期若為當日 背景色改用黑色
                paint.setColor(Color.BLACK);
            }
            m_canvas.drawLine(iHistogramX, 0, iHistogramX, m_iHistogramEndY, paint);
            iHistogramX = iHistogramX + (m_iSpacing + 10);
        }
    }

    private void drawHistogram(Paint paint, int iHistogramX){

        for (int i = (m_alData.size() - 1); i >= 0; i--) {
            if (0 < m_alData.get(i).m_dTotal) {
                paint.setColor(ContextCompat.getColor(m_context, R.color.sv_positive_blue));
                m_canvas.drawLine(
                        iHistogramX
                        , m_iCenterY
                        , iHistogramX
                        , (float)(m_iCenterY - m_iCenterY * (m_alData.get(i).m_dTotal / m_dMax))
                        , paint);
            } else {
                paint.setColor(ContextCompat.getColor(m_context, R.color.sv_negative_blue));
                m_canvas.drawLine(
                        iHistogramX
                        , m_iCenterY
                        , iHistogramX
                        , (float)(m_iCenterY + m_iCenterY * (m_alData.get(i).m_dTotal / (0 - m_dMax)))
                        , paint);
            }
            iHistogramX = iHistogramX + (m_iSpacing + 10);
        }
    }

    private void drawTotalText(Paint paint, int iHistogramX){

        //合計數字位置 不可超出柱狀圖範圍
        int iMoveUpY    = m_vScaleDef.getLayoutHeight(1);
        int iMoveDownY  = m_vScaleDef.getLayoutHeight(3.5);

        final float fTopY = 0 + iMoveDownY;
        final float fBottomY = m_iHistogramEndY - iMoveUpY;

        for (int i = (m_alData.size() - 1); i >= 0; i--) {

            float fY;

            if (0 < m_alData.get(i).m_dTotal) {
                fY = (float)(m_iCenterY - m_iCenterY * (m_alData.get(i).m_dTotal / m_dMax) - iMoveUpY);

                if ( fY < fTopY )
                    fY = fTopY;

            } else {
                fY = (float)(m_iCenterY + m_iCenterY * (m_alData.get(i).m_dTotal / (0 - m_dMax))) + iMoveDownY;

                if ( fY > fBottomY )
                    fY = fBottomY;
            }

            m_canvas.drawText(m_alData.get(i).m_strTotal
                    , iHistogramX
                    , fY
                    , paint);

            iHistogramX = iHistogramX + (m_iSpacing + 10);
        }
    }

    public void drawGraph(Paint pDrawLine, Paint pDrawCircle, int iGraphX, ArrayList<Double> arrayList) {

        //記錄上一個xy座標
        float fLastX = 0;
        float fLastY = 0;

        int iCircleSize = m_vScaleDef.getLayoutMinUnit(1);

        for (int i = (arrayList.size() - 1); i >= 0; i--) {
            //畫線
            if ((m_alData.size() - 1) != i) {
                m_canvas.drawLine(
                        fLastX
                        , fLastY
                        , iGraphX
                        , (float)(m_iCenterY - m_iCenterY * (arrayList.get(i) / m_dMax))
                        , pDrawLine);
            }

            //畫點
            if (0 <= arrayList.get(i)) {
                fLastY = (float)(m_iCenterY - m_iCenterY * (arrayList.get(i) / m_dMax));
            } else {
                fLastY = (float)(m_iCenterY + m_iCenterY * (arrayList.get(i) / (0 - m_dMax)));
            }
            m_canvas.drawCircle(iGraphX, fLastY, iCircleSize, pDrawCircle);

            fLastX = iGraphX;
            iGraphX = iGraphX + (m_iSpacing + 10);
        }
    }

    private void drawGraphText(Paint paint, int iGraphX, ArrayList<Double> arrayList){

        //合計數字位置 不可超出柱狀圖範圍
        int iMoveUpY    = m_vScaleDef.getLayoutHeight(3);
        int iMoveDownY  = m_vScaleDef.getLayoutHeight(5);

        final float fTopY = 0 + iMoveDownY;
        final float fBottomY = m_iHistogramEndY - iMoveUpY;

        DecimalFormat dfFloat = new DecimalFormat("0.00");

        for (int i = (arrayList.size() - 1); i >= 0; i--) {

            float fY;
            if (0 < arrayList.get(i)) {
                fY = (float)(m_iCenterY - m_iCenterY * (arrayList.get(i) / m_dMax) - iMoveUpY);

                if ( fY < fTopY )
                    fY = fTopY;

            } else {
                fY = (float)(m_iCenterY + m_iCenterY * (arrayList.get(i) / (0 - m_dMax))) + iMoveDownY;

                if ( fY > fBottomY )
                    fY = fBottomY;
            }

            m_canvas.drawText(dfFloat.format(arrayList.get(i))
                    , iGraphX
                    , fY
                    , paint);

            iGraphX = iGraphX + (m_iSpacing + 10);
        }
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

    private ArrayList<Double> getDoubleArrayList(eType type){
        ArrayList<Double> arrayList = new ArrayList<>();
        switch (type){
            case QFII:
                for (int i = 0; i < m_alData.size(); i ++ ){
                    arrayList.add(m_alData.get(i).m_dQfii);
                }
                break;
            case BRK:
                for (int i = 0; i < m_alData.size(); i ++ ){
                    arrayList.add(m_alData.get(i).m_dBrk);
                }
                break;
            case IT:
                for (int i = 0; i < m_alData.size(); i ++ ){
                    arrayList.add(m_alData.get(i).m_dIt);
                }
                break;
        }
        return arrayList;
    }
}
