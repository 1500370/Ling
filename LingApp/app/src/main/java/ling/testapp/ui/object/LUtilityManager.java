package ling.testapp.ui.object;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by jlchen on 2016/10/20.
 */

public class LUtilityManager {

    private static final String     TAG         = "[LUtilityManager]";

    private static Context          s_context   = null;
    private static LUtilityManager  s_instance  = null;

    public static LUtilityManager getInstance(Context context)
    {
        s_context = context;
        if (null == s_instance) {
            s_instance = new LUtilityManager();
        }
        return s_instance;
    }

    /**檢查是否有網路，不會送出錯誤訊息
     * @return true=有網路 false=無網路狀態**/
    public boolean bIsNetworkAvailable()
    {
        if ( s_context == null ){
            return false;
        }

        @SuppressWarnings("static-access")
        ConnectivityManager connectivityManager
                = (ConnectivityManager) s_context.getSystemService(s_context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if ( null == networkInfo || !networkInfo.isAvailable() ) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 取drawable ResourcesId
     * 檔名範例為 func01_workshop
     * @param strIconKey function_icon_key或menu_icon_key的值
     * @param strIconTag
     * @return ResourcesId
     */
    public int getIconResourceId(String strIconKey, String strIconTag) {

        int     iResourcesId;
        String  strIconName     = strIconKey + strIconTag;

        try {
            iResourcesId = s_context.getResources().getIdentifier(
                    strIconName,
                    "drawable",
                    s_context.getPackageName());
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

        return iResourcesId;
    }

    /**
     * 取string ResourcesId
     * @return ResourcesId
     */
    public int getStringResourceId(String strStringKey, String strStringTag) {

        int     iResourcesId;
        String  strStringName   = strStringKey + strStringTag;

        try {
            iResourcesId = s_context.getResources().getIdentifier(
                    strStringName,
                    "string",
                    s_context.getPackageName());
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

        return iResourcesId;
    }
}
