package ling.testapp.ui.define;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ling.testapp.R;

/**
 * Created by jlchen on 2016/9/21.
 * 處理自適應
 */

public class LViewScaleDef {

    private static LViewScaleDef m_instance     = null;

    private int		m_iMaxLayoutHeightWeight    = 0;	//長度單位最大值
    private int		m_iMaxLayoutWidthWeight     = 0;	//寬度單位最大值
    private float	m_fHeightUnit               = 0;	//長度單位
    private float	m_fWidthUnit                = 0;	//寬度單位
    private float	m_fFontUnit                 = 0;	//字型單位

    private float	m_fScreenScaleDensity       = 0;    //螢幕密度
    private int		m_iStatusBarHeight          = 0;	//狀態列高度

    private DisplayMetrics m_dm                 = null; //取的螢幕解析度

    public static LViewScaleDef getInstance(Context context)
    {
        if (null == m_instance) {
            m_instance = new LViewScaleDef();
            m_instance.initial(context);
        }

        return m_instance;
    }

    public static void setInstance(LViewScaleDef scaleDef){
        m_instance = scaleDef;
    }

    private void initial(Context context)
    {
        //設定「長度、寬度、字型單位」
        m_dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getMetrics(m_dm);

        //設定「長度、寬度單位」最大值
        //2016.10.28因應切換橫豎屏需求, 調整單位最大值
        if ( m_dm.widthPixels > m_dm.heightPixels ){
            m_iMaxLayoutHeightWeight = context.getResources().getInteger(R.integer.activity_weight_sum_horizontal);
            m_iMaxLayoutWidthWeight = context.getResources().getInteger(R.integer.activity_weight_sum_vertical);
        }else {
            m_iMaxLayoutHeightWeight = context.getResources().getInteger(R.integer.activity_weight_sum_vertical);
            m_iMaxLayoutWidthWeight = context.getResources().getInteger(R.integer.activity_weight_sum_horizontal);
        }

        m_fHeightUnit = (float) m_dm.heightPixels / m_iMaxLayoutHeightWeight;
        m_fWidthUnit = (float) m_dm.widthPixels / m_iMaxLayoutWidthWeight;
        m_fFontUnit = Math.min(m_fHeightUnit, m_fWidthUnit);

        m_fScreenScaleDensity = m_dm.scaledDensity;
    }


    /**
     * @param dHeight 高度比例，參數範圍為「1 ~ 1920」
     * @return 取出經長寬比運算出的高度
     */
    public int getLayoutHeight(double dHeight) {
        return getIntHeight((int) (dHeight * m_fHeightUnit));
    }

    /**
     * @param dWeight 寬度比例，參數範圍為「1 ~ 1080」
     * @return 取出經長寬比運算出的寬度
     */
    public int getLayoutWidth(double dWeight) {
        return getIntHeight((int) (dWeight * m_fWidthUnit));
    }

    /**
     * @param dWeight 單位比例，抓取寬高比最小的值，請小心使用
     * @return 取出經長寬比運算出的寬度
     */
    public int getLayoutMinUnit(double dWeight) {
        return getIntHeight((int) (dWeight * m_fFontUnit));
    }

    /**
     * @return 取出字型大小
     */
    public int getTextSize(double dTextSizeDefine) {
        return getIntHeight((int) (dTextSizeDefine * m_fFontUnit));
    }

    /**
     * 設定字型大小-TextView
     * @param textView
     */
    public void setTextSize(double dTextSizeDefine, TextView textView) {
        textView.setIncludeFontPadding(false);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine));
    }

    /**
     * 設定字型大小-Button
     * @param button
     */
    public void setTextSize(double dTextSizeDefine, Button button) {
        button.setIncludeFontPadding(false);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine));
    }

    /**
     * 設定字型大小-EditText
     * @param editText
     */
    public void setTextSize(double dTextSizeDefine, EditText editText) {
        editText.setIncludeFontPadding(false);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine));
    }

    /**
     * 取得
     * @return 取出經長寬比運算出的高度
     */
    public DisplayMetrics getDisplayMetrics() {
        return m_dm;
    }

    /**
     * 取得StatusBar的高度
     *
     * @return height px
     */
    public int getStatusBarHeight(){
        return m_iStatusBarHeight;
    }

    private int getIntHeight(int iHeight){
        if ( 1 > iHeight )
            return 1;
        return iHeight;
    }
}
