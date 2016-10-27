package ling.testapp.function.NBNS;

import ling.testapp.function.NBNS.item.LNetBSResp;

/**
 * Created by jlchen on 2016/10/26.
 */

public class LNetBSPresenter implements LNetBSModel.Callback {

    private         LNetBSListener  m_view      = null;
    private         LNetBSModel     m_model     = null;
    private static  LNetBSPresenter s_Instance  = null;

    public LNetBSPresenter(LNetBSListener listener){
        this.m_view = listener;
    }

    public static LNetBSPresenter getInstance(LNetBSListener listener){
        if ( null == s_Instance)
            s_Instance = new LNetBSPresenter(listener);

        return s_Instance;
    }

    public static void setInstance(LNetBSPresenter presenter){
        s_Instance = presenter;
    }

    public void setCallback(LNetBSListener listener){
        this.m_view = listener;
    }

    public void loadData(LNetBSActivity.eType type) {
        if ( null == m_model )
            m_model = new LNetBSModel();

        m_model.findData(type, this);
    }

    @Override
    public void onSuccess(LNetBSResp resultData) {
        if ( null != m_view )
            m_view.onSuccess(resultData);
    }

    @Override
    public void onError(String strError) {
        if ( null != m_view )
            m_view.onError(strError);
    }

    @Override
    public void showProgress() {
        if ( null != m_view )
            m_view.showProgress();
    }

    @Override
    public void hideProgress() {
        if ( null != m_view )
            m_view.hideProgress();
    }
}
