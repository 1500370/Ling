package ling.testapp.function.NBNS.object;

import android.content.Context;
import android.content.SharedPreferences;

import ling.testapp.function.NBNS.LNetBSActivity;

/**
 * Created by jlchen on 16/9/29
 * 賓果遊戲
 */
public class LNetBSInfo {

    private final String        TAG             = "NetBSInfo";
    private Context             m_context       = null;
    private SharedPreferences   m_spNBNSInfo    = null;

    //SharedPreferences 檔名定義
    /**
     * 三大法人 類型（上市/上櫃）
     */
    private static final String KEY_NBNS_TYPE   = "KEY_NBNS_TYPE";
    /**
     * 外資
     */
    private static final String KEY_NBNS_QFII   = "KEY_NBNS_QFII";
    /**
     * 自營
     */
    private static final String KEY_NBNS_BRK    = "KEY_NBNS_BRK";
    /**
     * 投資
     */
    private static final String KEY_NBNS_IT     = "KEY_NBNS_IT";


    //SharedPreferences 欄位預設值
    /**
     * 三大法人 類型 預設值
     */
    private static final String DEF_NBNS_TYPE  = LNetBSActivity.eType.TES.toString();
    /**
     * 外資/自營/投資 預設值
     */
    private static final boolean DEF_NBNS_CLICK= true;

    public LNetBSInfo(Context context) {

        this.m_context      = context;
        this.m_spNBNSInfo   = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    /**
     * 設定「三大法人 類型」
     */
    public void setNetBSType(LNetBSActivity.eType type) {

        if (null == m_spNBNSInfo) {
            return;
        }

        m_spNBNSInfo.edit().putString(KEY_NBNS_TYPE, type.toString()).commit();
    }

    /**
     * @return 三大法人 類型
     */
    public String getNetBSType() {

        if (null == m_spNBNSInfo) {
            return DEF_NBNS_TYPE;
        }

        return m_spNBNSInfo.getString(KEY_NBNS_TYPE, DEF_NBNS_TYPE);
    }

    /**
     * 設定「是否勾選 外資」
     */
    public void setQfiiClick(boolean bClick) {

        if (null == m_spNBNSInfo) {
            return;
        }

        m_spNBNSInfo.edit().putBoolean(KEY_NBNS_QFII, bClick).commit();
    }

    /**
     * @return 是否勾選 外資
     */
    public boolean getQfiiClick() {

        if (null == m_spNBNSInfo) {
            return DEF_NBNS_CLICK;
        }

        return m_spNBNSInfo.getBoolean(KEY_NBNS_QFII, DEF_NBNS_CLICK);
    }

    /**
     * 設定「是否勾選 自營」
     */
    public void setBrkClick(boolean bClick) {

        if (null == m_spNBNSInfo) {
            return;
        }

        m_spNBNSInfo.edit().putBoolean(KEY_NBNS_BRK, bClick).commit();
    }

    /**
     * @return 是否勾選 自營
     */
    public boolean getBrkClick() {

        if (null == m_spNBNSInfo) {
            return DEF_NBNS_CLICK;
        }

        return m_spNBNSInfo.getBoolean(KEY_NBNS_BRK, DEF_NBNS_CLICK);
    }

    /**
     * 設定「是否勾選 投資」
     */
    public void setItClick(boolean bClick) {

        if (null == m_spNBNSInfo) {
            return;
        }

        m_spNBNSInfo.edit().putBoolean(KEY_NBNS_IT, bClick).commit();
    }

    /**
     * @return 是否勾選 投資
     */
    public boolean getItClick() {

        if (null == m_spNBNSInfo) {
            return DEF_NBNS_CLICK;
        }

        return m_spNBNSInfo.getBoolean(KEY_NBNS_IT, DEF_NBNS_CLICK);
    }
}
