package ling.testapp.function.Bingo.object;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;

/**
 * Created by jlchen on 16/9/29
 * 賓果遊戲
 */
public class LBingoInfo {

    private final String        TAG             = "BingoInfo";
    private Context             m_context       = null;
    private SharedPreferences   m_spBingoInfo   = null;

    //SharedPreferences 檔名定義
    /**
     * 賓果SIZE
     */
    private static final String KEY_BINGO_SIZE  = "KEY_BINGO_SIZE";

    //SharedPreferences 欄位預設值
    /**
     * 賓果SIZE預設值
     */
    private static final int    DEF_BINGO_SIZE  = 3;

    //賓果盤基本預設值
    public  static final int    DEF_MIN         = 1;
    public  static final int    DEF_MAX         = 999;

    public LBingoInfo(Context context) {

        this.m_context      = context;
        this.m_spBingoInfo  = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    /**
     * 設定「賓果Size N x N」
     *
     * @param iSize 賓果Size
     */
    public void setBingoSize(int iSize) {

        if (null == m_spBingoInfo) {
            return;
        }

        m_spBingoInfo.edit().putInt(KEY_BINGO_SIZE, iSize).commit();
    }

    /**
     * @return 賓果Size
     */
    public int getBingoSize() {

        if (null == m_spBingoInfo) {
            return DEF_BINGO_SIZE;
        }

        return m_spBingoInfo.getInt(KEY_BINGO_SIZE, DEF_BINGO_SIZE);
    }

    /**
     * 1.輸入框數字一律前面去零
     * 2.不可輸入0
     */
    public String replaceZero(Editable s){
        try {
            Integer integer = Integer.valueOf(s.toString());
            if ( 0 != integer)
                return integer.toString();
            else
                return "";
        }catch (Exception e){
            return "";
        }
    }

    /**
     * 確認賓果盤數字符合範圍之內
     */
    public boolean checkNumRange(int iNum, int iMin, int iMax){
        if ( iNum >= iMin && iNum <= iMax ){
            return true;
        }else {
            return false;
        }
    }
}
