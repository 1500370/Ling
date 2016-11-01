package ling.testapp.function.NBNS.object;

import android.content.Context;

/**
 * Created by jlchen on 2016/11/1.
 */

public class LNetBSViewScaleDef {

    private static LNetBSViewScaleDef m_instance    = null;

    private int     m_iHeight                       = 0;
    private int     m_iWidth                        = 0;
    private int		m_iMaxLayoutHeightWeight        = 100;	//長度單位最大值
    private int		m_iMaxLayoutWidthWeight         = 100;	//寬度單位最大值
    private float	m_fHeightUnit                   = 0;	//長度單位
    private float	m_fWidthUnit                    = 0;	//寬度單位
    private float	m_fFontUnit                     = 0;	//字型單位

    public static LNetBSViewScaleDef getInstance(Context context, int iWidth, int iHeight)
    {
        if (null == m_instance) {
            m_instance = new LNetBSViewScaleDef();
            m_instance.m_iHeight = iHeight;
            m_instance.m_iWidth = iWidth;
            m_instance.initial(context);
        }

        return m_instance;
    }

    public static void setInstance(LNetBSViewScaleDef instance){
        m_instance = instance;
    }

    private void initial(Context context)
    {
        m_fHeightUnit = (float) m_iHeight / m_iMaxLayoutHeightWeight;
        m_fWidthUnit = (float) m_iWidth / m_iMaxLayoutWidthWeight;
        m_fFontUnit = Math.min(m_fHeightUnit, m_fWidthUnit);
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

    private int getIntHeight(int iHeight){
        if ( 1 > iHeight )
            return 1;
        return iHeight;
    }
}
