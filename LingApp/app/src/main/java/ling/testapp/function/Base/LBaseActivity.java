package ling.testapp.function.Base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import ling.testapp.R;
import ling.testapp.ui.core.LWifiConnectionStateReceiver;
import ling.testapp.ui.define.LUiMessageDef;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.dialog.LAlertDialog;

/**
 * Created by jlchen on 2016/9/21.
 */

public abstract class LBaseActivity extends AppCompatActivity
        implements LWifiConnectionStateReceiver.Callback{

    protected   Context         m_context   = null;
    private     LAlertDialog    m_dialog    = null;

    private     LWifiConnectionStateReceiver m_wifiReceiver = null;

    protected   Handler         m_handler   = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case LUiMessageDef.MSG_EXIT_APP: {
                    System.exit(0);
                }
                break;
                default:
//                    if (bOtherHandleMessage(msg)) {
//                        break;
//                    }

                    super.handleMessage(msg);
                    break;
            }
        }
        private void handleMessage_RestartApp() {
            Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(
                    getBaseContext().getPackageName() );
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent);

            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_context = this;

        setContentView(getLayoutResourceId());

        initialLayoutComponent();

        LViewScaleDef vScaleDef = LViewScaleDef.getInstance(this);

        setTextSizeAndLayoutParams(vScaleDef);

        setOnParameterAndListener();

        registerFragment(getSupportFragmentManager());
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 註冊監聽網路狀態的廣播
        m_wifiReceiver = new LWifiConnectionStateReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(m_wifiReceiver, filter);

    }

    @Override
    public void onPause() {
        super.onPause();

        // 反註冊監聽網路狀態的廣播
        unregisterReceiver(m_wifiReceiver);
        m_wifiReceiver = null;
    }

    @Override
    public void onBackPressed() {

        if( isTaskRoot() ) //最後一個Activity
        {
            showDialog(m_context.getResources().getString(R.string.warn),
                    m_context.getResources().getString(R.string.exit_app_message),
                    m_context.getResources().getString(R.string.confirm),
                    m_context.getResources().getString(R.string.cancel),
                    new LAlertDialog.OnAlertMsgDialogListener() {
                        @Override
                        public void onDialogConfirm() {
                            m_handler.obtainMessage(LUiMessageDef.MSG_EXIT_APP).sendToTarget();
                        }

                        @Override
                        public void onDialogCancel() {

                        }
                    });
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != getSupportFragmentManager().getFragments()) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment instanceof LBaseFragment) {
                        LBaseFragment fragmentBack = (LBaseFragment) fragment;
                        if (fragmentBack.uiOnBackPressed()) {
                            // 若子Fragment已處理Back
                            // Event，則不再往下傳遞
                            return true;
                        }
                    }
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    //關閉鍵盤
    public void HidekeyBoard() {
        InputMethodManager inputmanager
                = (InputMethodManager) m_context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null != inputmanager
                && null != getCurrentFocus()
                && null != getCurrentFocus().getWindowToken()) {
            inputmanager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    //顯示警示對話筐
    protected void showDialog(String strMsg) {
        showDialog(null, strMsg, getString(R.string.confirm), null, null);
    }

    protected void showDialog(String strTitle, String strMsg) {
        showDialog(strTitle, strMsg, getString(R.string.confirm), null, null);
    }

    protected void showDialog(String strTitle, String strMsg, String strConfirm) {
        showDialog(strTitle, strMsg, strConfirm, null, null);
    }

    protected void showDialog(String strTitle,
                              String strMsg,
                              String strConfirm,
                              String strCancel,
                              LAlertDialog.OnAlertMsgDialogListener listener) {

        if ( null == m_context || isFinishing()){
            return;
        }

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ) {
            if(true == isDestroyed()){
                return;
            }
        }

        if ( null != m_dialog && true == m_dialog.isShowing() ){
            m_dialog.dismiss();
        }

        if ( null == listener ){
            m_dialog = new LAlertDialog(m_context, new LAlertDialog.OnAlertMsgDialogListener() {

                @Override
                public void onDialogConfirm() {

                }

                @Override
                public void onDialogCancel() {

                }
            });
        } else {
            m_dialog  = new LAlertDialog(m_context, listener);
        }

        if(false == TextUtils.isEmpty(strTitle)) {
            m_dialog.uiSetTitleText(strTitle);
        }
        if(false == TextUtils.isEmpty(strMsg)) {
            m_dialog.uiSetContentText(strMsg);
        }
        if(false == TextUtils.isEmpty(strConfirm)) {
            m_dialog.uiSetConfirmText(strConfirm);
        }
        if(false == TextUtils.isEmpty(strCancel)){
            m_dialog.uiSetCancelText(strCancel);
        }

        m_dialog.show();
    }

    /**
     * 根據傳入的clazz 開啟新activity
     */
    public void openActivity(Class clazz){
        openActivity(new Intent(), clazz);
    }

    public void openActivity(Intent intent, Class clazz){
        openActivity(intent, clazz, 0);
    }

    public void openActivity(Intent intent, Class clazz, int iRequestCode){
        openActivity(intent, clazz, iRequestCode, R.anim.anim_right_in, R.anim.anim_left_out);
    }

    public void openActivity(Intent intent, Class clazz, int iRequestCode, int iEnterAnim, int iExitAnim){

        if ( null == m_context || isFinishing()){
            return;
        }
        intent.setClass(m_context, clazz);

        if (0 == iRequestCode){
            startActivity(intent);
        }else {
            startActivityForResult(intent, iRequestCode);
        }

        overridePendingTransition(iEnterAnim, iExitAnim);
    }

    //更新語系
    public void onLocaleChangeUpdateView(){

        //先更新Activity
        onLanguageChangeUpdateUI();

        //更新ragment
        if (null != getSupportFragmentManager().getFragments()) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment instanceof LBaseFragment) {
                    LBaseFragment baseFragment = (LBaseFragment) fragment;
                    baseFragment.onLanguageChangeUpdateUI();
                }
            }
        }
    }

    /**
     * BaseActivity在{@link LBaseActivity#onCreate(Bundle) onCreate()}時 設定
     * {@link LBaseActivity#setContentView(int) setContentView()}用
     *
     * @return 此畫面的 Layout Resource Id
     */
    protected abstract int getLayoutResourceId();

    /**
     * 元件初始化，靜態取得元件實體、動態建製元件實體…等
     */
    protected abstract void initialLayoutComponent();

    /**
     * 設定字型大小及版面所需參數
     *
     * @param vScaleDef
     *            請參閱{@link LViewScaleDef}
     */
    protected abstract void setTextSizeAndLayoutParams(LViewScaleDef vScaleDef );

    /**
     * 通知父BaseActivity對子fragment
     * 設定客製的「OnParameter(參數讀取)」跟「OnListener(動線通知)」介面
     */
    protected abstract void setOnParameterAndListener();

    /**
     * 依需求調用以下函式
     *
     * @param fragmentManager
     * @see FragmentTransaction FragmentTransaction相關操作
     */
    protected abstract void registerFragment(FragmentManager fragmentManager);

    /** 當App語言變更後, 會呼叫此介面，藉此更新畫面UI,需要重新呼叫setText*/
    protected abstract void onLanguageChangeUpdateUI();

    @Override
    public void onNetworkConnect() {

    }

    @Override
    public void onNetworkDisconnect() {

    }
}
