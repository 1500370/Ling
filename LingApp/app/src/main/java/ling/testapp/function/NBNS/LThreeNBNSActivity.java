package ling.testapp.function.NBNS;

import android.support.v4.app.FragmentManager;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseActivity;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.navigation.LNavigationBar;

/**
 * Created by jlchen on 2016/10/18.
 * 三大法人買賣超走勢圖
 */

public class LThreeNBNSActivity extends LBaseActivity{

    private LNavigationBar.OnListener   m_navigationListener
            = new LNavigationBar.OnListener() {
        @Override
        public void OnRightImgClick() {
        }

        @Override
        public void OnSecondRightImgClick() {

        }

        @Override
        public void OnLeftImgClick() {
            finish();
        }
    };

    private LNavigationBar.OnParameter  m_navigationParameter
            = new LNavigationBar.OnParameter() {
        @Override
        public String GetTitle() {
            return m_context.getResources().getString(R.string.title_nbns);
        }

        @Override
        public int GetLeftIconRes() {
            return R.drawable.ic_back_arrow;
        }

        @Override
        public int GetRightIconRes() {
            return R.drawable.ic_information;
        }

        @Override
        public int GetSecondRightIconRes() {
            return 0;
        }
    };

    private LNavigationBar m_navigationBar         = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_nbns;
    }

    @Override
    protected void initialLayoutComponent() {

        m_navigationBar = (LNavigationBar)findViewById(R.id.navigation_bar);
    }

    @Override
    protected void setTextSizeAndLayoutParams(LViewScaleDef vScaleDef) {

        m_navigationBar.getLayoutParams().height
                = vScaleDef.getLayoutHeight(LNavigationBar.NAVIGATION_BAR_HEIGHT);
    }

    @Override
    protected void setOnParameterAndListener() {

        m_navigationBar.uiSetParameterListener(
                m_navigationParameter, m_navigationListener);
    }

    @Override
    protected void registerFragment(FragmentManager fragmentManager) {

    }

    @Override
    protected void onLanguageChangeUpdateUI() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
    }
}
