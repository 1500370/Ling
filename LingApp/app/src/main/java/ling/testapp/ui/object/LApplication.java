package ling.testapp.ui.object;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import ling.testapp.function.Bingo.object.LBingoInfo;
import ling.testapp.function.NBNS.object.LNetBSInfo;
import ling.testapp.function.Setting.object.LLanguageInfo;

/**
 * Created by jlchen on 16/9/29
 */
public class LApplication extends Application {

	private static final String 	TAG				= "[LApplication]";

	private static Context 			s_context		= null;
	public  static LBingoInfo 		s_bingoInfo		= null;
	public  static LNetBSInfo 		s_netBSInfo		= null;
	public  static LLanguageInfo	s_languageInfo 	= null;

	@Override
	public void onCreate() {
		super.onCreate();
		s_context = getApplicationContext();
		Log.i(TAG, "[onCreate]");
	}

	public static Context getContext() {
		return s_context;
	}

	/**
	 * 取得賓果遊戲相關參數
	 * @return LBingoInfo
	 */
	public static synchronized LBingoInfo getBingoInfo(){
		if (null == s_bingoInfo) {
			s_bingoInfo = new LBingoInfo(s_context);
		}
		return s_bingoInfo;
	}

	/**
	 * 取得三大法人相關參數
	 * @return LNetBSInfo
	 */
	public static synchronized LNetBSInfo getNetBSInfo(){
		if (null == s_netBSInfo) {
			s_netBSInfo = new LNetBSInfo(s_context);
		}
		return s_netBSInfo;
	}

	/**
	 * 取得語系設定
	 * @return LLanguageInfo
	 */
	public static synchronized LLanguageInfo getLanguageInfo(){
		if (null == s_languageInfo) {
			s_languageInfo = new LLanguageInfo(s_context);
		}
		return s_languageInfo;
	}
}