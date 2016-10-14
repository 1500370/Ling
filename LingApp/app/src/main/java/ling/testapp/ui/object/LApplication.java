package ling.testapp.ui.object;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by jlchen on 16/9/29
 */
public class LApplication extends Application {

	private static final String TAG			= "LApplication";
	private static Context s_context	= null;
	public  static LBingoInfo	s_bingoInfo	= null;

	@Override
	public void onCreate() {
		super.onCreate();
		s_context = getApplicationContext();
		Log.i(TAG, "onCreate");
	}

	public static Context getContext() {
		return s_context;
	}

	/**
	 * 取得賓果遊戲相關參數
	 * @return CILoginInfo
	 */
	public static synchronized LBingoInfo getBingoInfo(){
		if (null == s_bingoInfo) {
			s_bingoInfo = new LBingoInfo(s_context);
		}
		return s_bingoInfo;
	}
}