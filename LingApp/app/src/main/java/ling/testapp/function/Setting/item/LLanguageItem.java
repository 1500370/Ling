package ling.testapp.function.Setting.item;

import java.util.Locale;

/**
 * Created by jlchen on 2016/10/18.
 */

public class LLanguageItem {

    public String strTag;
    public Locale locale;
    public String strDisplayName;

    public LLanguageItem(){
        locale = Locale.TAIWAN;
        strDisplayName = "";
        strTag = "";
    }
}
