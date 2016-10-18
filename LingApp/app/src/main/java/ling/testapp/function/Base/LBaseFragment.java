package ling.testapp.function.Base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import ling.testapp.R;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.dialog.LAlertDialog;

/**
 * Created by jlchen on 2016/9/21.
 */

public abstract class LBaseFragment extends Fragment {

    private     LAlertDialog    m_dialog            = null;
    protected   LayoutInflater  m_layoutInflater    = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_layoutInflater = inflater;
        return inflater.inflate(getLayoutResourceId(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialLayoutComponent(m_layoutInflater, view);

        LViewScaleDef vSacleDef = LViewScaleDef.getInstance(getActivity());

        setTextSizeAndLayoutParams(view, vSacleDef);

        setOnParameterAndListener(view);

        registerFragment(getChildFragmentManager());
    }

    //關閉鍵盤
    public void HidekeyBoard() {
        InputMethodManager inputmanager
                = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null != inputmanager
                && null != getActivity().getCurrentFocus()
                && null != getActivity().getCurrentFocus().getWindowToken()) {
            inputmanager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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

        if ( null == getActivity() || getActivity().isFinishing() || true == isDetached() ){
            return;
        }

        if ( null != m_dialog && true == m_dialog.isShowing() ){
            m_dialog.dismiss();
        }

        if ( null == listener ){
            m_dialog = new LAlertDialog(getActivity(), new LAlertDialog.OnAlertMsgDialogListener() {

                @Override
                public void onDialogConfirm() {

                }

                @Override
                public void onDialogCancel() {

                }
            });
        } else {
            m_dialog  = new LAlertDialog(getActivity(), listener);
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
     * 當{@link LBaseActivity}收到Back Event時，會往下通知子LBaseFragment，
     * 若有需要的子LBaseFragment接到後並處理完成，則{@link LBaseActivity}就不會往下傳遞
     * @return	true 代表該LBaseFragment已處理此Back Event<p>
     * 			false 代表該LBaseFragment不處理此Back Event<p>
     */
    public boolean uiOnBackPressed() {
        return false;
    }

    /**
     * BaseFragment在
     * {@link LBaseFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     * onCreateView()}時 設定{@link LayoutInflater#inflate(int, ViewGroup, boolean)
     * inflate()}用
     *
     * @return 此畫面的 Layout Resource Id
     */
    protected abstract int getLayoutResourceId();

    /**
     * 元件初始化，靜態取得元件實體、動態建製元件實體…等
     *
     * @param view
     */
    protected abstract void initialLayoutComponent(LayoutInflater inflater, View view);

    /**
     * 設定字型大小及版面大小
     * @param view
     * @param vScaleDef 請參閱{@link LViewScaleDef}
     */
    protected abstract void setTextSizeAndLayoutParams(View view, LViewScaleDef vScaleDef);

    /**
     * 通知父BaseActivity對子BaseFragment設定客製的「OnParameter(參數讀取)」跟「OnListener(
     * 動線通知)」介面
     *
     * @param view
     */
    protected abstract void setOnParameterAndListener(View view);

    /**
     * 依需求調用以下函式
     *
     * @param fragmentManager
     * @see FragmentTransaction FragmentTransaction相關操作
     */
    protected abstract void registerFragment(FragmentManager fragmentManager);
}
