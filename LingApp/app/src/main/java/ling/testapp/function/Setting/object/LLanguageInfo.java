package ling.testapp.function.Setting.object;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Locale;

import ling.testapp.R;
import ling.testapp.function.Setting.item.LLanguageItem;
import ling.testapp.ui.object.LApplication;

/**
 * Created by jlchen on 16/10/18
 * 語系設定
 */
public class LLanguageInfo {

    private final String        TAG             = "LanguageInfo";
    private Context             m_context       = null;
    private SharedPreferences   m_spLanguageInfo= null;

    //SharedPreferences 檔名定義
    /**
     * 賓果SIZE
     */
    private static final String KEY_LANGUAGE    = "KEY_LANGUAGE";

    public enum eLanguage{
        TAIWAN,
        CHINA,
        ENGLISH,
        JAPAN
    }

    public static   Locale[]    LANGUAGE        = new Locale[]{
            Locale.TAIWAN, Locale.CHINA, Locale.ENGLISH, Locale.JAPAN };
    public          int[]       LANGUAGE_NAME   = new int[]{
            R.string.setting_language_tw,
            R.string.setting_language_cn,
            R.string.setting_language_en,
            R.string.setting_language_jp };

    private ArrayList<LLanguageItem> m_arLanguageList = new ArrayList<>();

    public LLanguageInfo(Context context){
        initial(context);
    }

    private void initial( Context context ){

        this.m_context          = context;
        this.m_spLanguageInfo   = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);

        m_arLanguageList.clear();

        int ilength = LANGUAGE.length;
        for ( int iIdx = 0; iIdx < ilength; iIdx++ ){

            LLanguageItem item = new LLanguageItem();
            item.locale = LANGUAGE[iIdx];
            item.strTag = item.locale.toString();
            item.strDisplayName = context.getResources().getString(LANGUAGE_NAME[iIdx]);
            m_arLanguageList.add(item);
        }
    }
    public ArrayList<LLanguageItem> getLanguageList(){
        return m_arLanguageList;
    }

    /**初始化App語系, 先抓取手機記憶體儲存的語系, 若無語系預設為繁體中文*/
    public void initialAppLanguage(){
        Locale locale = getLanguageLocale();
        setLanguage( locale );
    }

    /**取得目前記憶體內的語系*/
    public Locale getLanguageLocale(){

        Locale locale = null;

        String strLan = loadLanguage();
        for ( LLanguageItem lanItem : m_arLanguageList ){
            if ( strLan.equals( lanItem.strTag ) ){

                locale = lanItem.locale;
                break;
            }
        }

        return locale;
    }

    /**取得目前記憶體內的語系的資料結構*/
    public LLanguageItem getLanguage(){

        LLanguageItem localeItem = null;

        String strLan = loadLanguage();
        for ( LLanguageItem lanItem : m_arLanguageList ){
            if ( strLan.equals( lanItem.strTag ) ){

                localeItem = lanItem;
                break;
            }
        }

        return localeItem;
    }

    public void setLanguage( Locale locale ){
        //設定預設語系
        Locale.setDefault(locale);
        //2016/04/07 修正App吃不到語系的問題 by ryan
        Resources res = LApplication.getContext().getResources();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, res.getDisplayMetrics());

        saveLanguage(locale.toString());
    }

    /**
     * 儲存語系
     * @param  strTag 請給如下的格式, ex zh_TW, "en", "en_US"*/
    public void saveLanguage( String strTag ){

        if (null == m_spLanguageInfo) {
            return;
        }

        m_spLanguageInfo.edit().putString(KEY_LANGUAGE, strTag).commit();
    }
    /**
     * 取得目前語系
     * @return String App的語系, ex zh_TW, "en", "en_US"*/
    public String loadLanguage(){
        if (null == m_spLanguageInfo) {
            return Locale.TAIWAN.toString();
        }

        return m_spLanguageInfo.getString(KEY_LANGUAGE, Locale.TAIWAN.toString() );
    }
}
