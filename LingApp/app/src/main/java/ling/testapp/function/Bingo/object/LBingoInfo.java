package ling.testapp.function.Bingo.object;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ling.testapp.function.Bingo.item.LBingoItem;

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
     * 確認最大值與最小值是否符合在範圍之內
     */
    public boolean checkBingoRange(int iMin, int iMax, int iRange){
        if ( checkMin(iMin, iRange) && checkMax(iMax, iRange) ){
            if ( iMax > iMin && (iMax - iMin) >= (iRange - 1) ){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public boolean checkMin(int iMin, int iRange){
        if ( DEF_MIN <= iMin && DEF_MAX >= (iMin + iRange - 1) ){
            return true;
        }
        return false;
    }

    public boolean checkMax(int iMax, int iRange){
        if ( (DEF_MIN + iRange - 1) <= iMax && DEF_MAX >= iMax ){
            return true;
        }
        return false;
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

    //確認賓果盤都有輸入數字
    public boolean checkBingoDataNotEmpty(ArrayList<LBingoItem> arrayList){
        for(int i = 0; i < arrayList.size(); i ++){
            if (TextUtils.isEmpty(arrayList.get(i).m_strNum))
                return false;
        }
        return true;
    }

    //確認賓果盤沒有重複資料
    public boolean checkBingoDataNotRepeat(ArrayList<LBingoItem> arrayList){
        Set<String> set = new HashSet<>();
        for(int i = 0; i < arrayList.size(); i ++){
            if (TextUtils.isEmpty(arrayList.get(i).m_strNum))
                return false;

            set.add(arrayList.get(i).m_strNum);
        }

        if ( set.size() == arrayList.size()){
            return true;
        }else {
            return false;
        }
    }

    //確認輸入數字沒有和其他數字重複
    public boolean checkBingoDataNotRepeat(String str, int iIndex, ArrayList<LBingoItem> arrayList){
        for(int i = 0; i < arrayList.size(); i ++){
            if ( i != iIndex
                    && !TextUtils.isEmpty(arrayList.get(i).m_strNum)
                    && str.equals(arrayList.get(i).m_strNum) )
                return false;
        }
        return true;
    }

    //返回亂數data
    public ArrayList<LBingoItem> getBingoArray(int iMin, int iMax, int iSize){

        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = iMin; i <= iMax; i ++){
            arrayList.add(i);
        }

        ArrayList<LBingoItem> result = new ArrayList<>();
        for (int i = 0; i < iSize; i ++){
            int iRandom = (int)(Math.random() * arrayList.size());

            result.add(new LBingoItem(i, String.valueOf(arrayList.get(iRandom))));

            arrayList.remove(iRandom);
        }
        return result;
    }

    //計算連線數
    public int getBingoLine(ArrayList<LBingoItem> arrayList, int iCol){
        int iLine = 0;
        int iSize = arrayList.size();

        //橫線
        for(int i = 0; i < iSize; i += iCol){
            boolean bLine = true;
            for (int j = i; j < (i + iCol); j ++){
                if (false == arrayList.get(j).m_bSelect)
                    bLine = false;
            }

            if (true == bLine){
                iLine ++;
            }
        }

        //直線
        for(int i = 0; i < iCol; i ++){
            boolean bLine = true;
            for (int j = i; j < iSize; j += iCol){
                if (false == arrayList.get(j).m_bSelect)
                    bLine = false;
            }

            if (true == bLine){
                iLine ++;
            }
        }

        //正斜線
        boolean bSlash = true;
        for(int i = 0; i < iSize; i += (iCol+1)){
            if (false == arrayList.get(i).m_bSelect)
                bSlash = false;
        }

        if (true == bSlash){
            iLine ++;
        }

        //反斜線
        boolean bBackslash = true;
        int iIndex = 0;
        for(int i = (iCol-1); i < iSize; i += (iCol-1)){
            //只要依列數確認n個數字是否都已選
            if (iIndex >= iCol)
                break;

            if (false == arrayList.get(i).m_bSelect)
                bBackslash = false;

            iIndex++;
        }

        if (true == bBackslash){
            iLine ++;
        }

        return iLine;
    }
}
