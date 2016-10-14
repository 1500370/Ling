package ling.testapp.function.Main;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseFragment;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.listener.LNaviBarToMainListener;
import ling.testapp.ui.navigation.LNavigationBar;

/**
 * Created by jlchen on 2016/9/23.
 * 首頁
 */

public class LHomeFragment extends LBaseFragment{

    private LNavigationBar.OnListener   m_navigationListener
            = new LNavigationBar.OnListener() {
        @Override
        public void OnRightImgClick() {
            if ( null != m_onMainListener ){
                m_onMainListener.OnNaviBarRightImgClick();
            }
        }

        @Override
        public void OnLeftImgClick() {
            m_onMainListener.OnNaviBarLeftImgClick();
        }
    };

    private LNavigationBar.OnParameter  m_navigationParameter
            = new LNavigationBar.OnParameter() {
        @Override
        public String GetTitle() {
            return getActivity().getResources().getString(R.string.title_home);
        }
    };

    private LNaviBarToMainListener      m_onMainListener            = null;

    private LNavigationBar              m_navigationBar             = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initialLayoutComponent(LayoutInflater inflater, View view) {

        m_navigationBar = (LNavigationBar)view.findViewById(R.id.navigation_bar);
    }

    @Override
    protected void setTextSizeAndLayoutParams(View view, LViewScaleDef vScaleDef) {

        m_navigationBar.getLayoutParams().height
                = vScaleDef.getLayoutHeight(LNavigationBar.NAVIGATION_BAR_HEIGHT);
    }

    @Override
    protected void setOnParameterAndListener(View view) {
        m_navigationBar.uiSetParameterListener(
                m_navigationParameter, m_navigationListener);
    }

    @Override
    protected void registerFragment(FragmentManager fragmentManager) {

    }

    public void uiSetParameterListener(LNaviBarToMainListener onListener) {
        m_onMainListener = onListener;
    }
}
