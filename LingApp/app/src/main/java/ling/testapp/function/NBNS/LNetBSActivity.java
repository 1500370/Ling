package ling.testapp.function.NBNS;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseActivity;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.dialog.LInfoDialogFragment;
import ling.testapp.ui.navigation.LNavigationBar;
import ling.testapp.ui.navigation.LTwoItemNavigationBar;
import ling.testapp.ui.object.LApplication;

import static ling.testapp.ui.navigation.LTwoItemNavigationBar.OnListener;
import static ling.testapp.ui.navigation.LTwoItemNavigationBar.OnParameter;
import static ling.testapp.ui.navigation.LTwoItemNavigationBar.eItemType;

/**
 * Created by jlchen on 2016/10/18.
 * 三大法人買賣超走勢圖
 */

public class LNetBSActivity extends LBaseActivity {

    private LNavigationBar.OnListener   m_navigationListener
            = new LNavigationBar.OnListener() {
        @Override
        public void OnRightImgClick() {

            //顯示說明dialog
            LInfoDialogFragment dialogFragment = LInfoDialogFragment.newInstance(
                    getString(R.string.nbns_info_title),
                    getString(R.string.nbns_info));

            dialogFragment.uiSetParameterListener(
                    new LInfoDialogFragment.OnListener() {
                @Override
                public void OnClose() {

                }
            });
            dialogFragment.show(getSupportFragmentManager(), dialogFragment.getTag());
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

    private OnListener m_twobarListener
            = new OnListener() {
        @Override
        public void OnLeftClick() {

        }

        @Override
        public void OnRightClick() {

        }
    };

    private OnParameter m_twobarParameter
            = new OnParameter(){

        @Override
        public String GetLeftText() {
            return m_context.getResources().getString(R.string.nbns_tes);
        }

        @Override
        public String GetRightText() {
            return m_context.getResources().getString(R.string.nbns_otc);
        }

        @NonNull
        @Override
        public eItemType GetInitType() {
            return m_type;
        }
    };

    public  static final double     TWOITEM_WIDTH   = 75;
    public  static final double     TWOITEM_PADDING = 2.5;
    public  static final double     TWOITEM_MARGIN  = 30;

    private LNavigationBar          m_navigationBar = null;
    private LTwoItemNavigationBar   m_twoItemBar    = null;

    private View                    m_vRootBg       = null;

    private LinearLayout            m_llContent     = null;
    private FrameLayout             m_flSurfaceView = null;
    private FrameLayout             m_flListView    = null;
    private eItemType               m_type          = eItemType.LEFT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //避免切換橫豎屏時，語系不正確，再重新初始化App語系
        LApplication.getLanguageInfo().initialAppLanguage();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_nbns;
    }

    @Override
    protected void initialLayoutComponent() {

        m_navigationBar = (LNavigationBar)findViewById(R.id.navigation_bar);
        m_twoItemBar    = (LTwoItemNavigationBar)findViewById(R.id.two_bar);

        m_vRootBg       = findViewById(R.id.v_background);

        m_llContent     = (LinearLayout)findViewById(R.id.ll_content);
        m_flSurfaceView = (FrameLayout)findViewById(R.id.fl_surfaceview);
        m_flListView    = (FrameLayout)findViewById(R.id.fl_listview);

    }

    @Override
    protected void setTextSizeAndLayoutParams(LViewScaleDef vScaleDef) {

        m_navigationBar.getLayoutParams().height
                = vScaleDef.getLayoutHeight(LNavigationBar.NAVIGATION_BAR_HEIGHT);

        RelativeLayout.LayoutParams params
                = (RelativeLayout.LayoutParams)m_twoItemBar.getLayoutParams();
        params.height = vScaleDef.getLayoutMinUnit(TWOITEM_WIDTH);
        params.rightMargin = vScaleDef.getLayoutMinUnit(TWOITEM_MARGIN);
        params.leftMargin = vScaleDef.getLayoutMinUnit(TWOITEM_MARGIN);
        m_twoItemBar.setPadding(vScaleDef.getLayoutMinUnit(TWOITEM_PADDING),
                vScaleDef.getLayoutMinUnit(TWOITEM_PADDING),
                vScaleDef.getLayoutMinUnit(TWOITEM_PADDING),
                vScaleDef.getLayoutMinUnit(TWOITEM_PADDING));

        LinearLayout.LayoutParams lParams;

        //抓整個螢幕大小
        DisplayMetrics dm = vScaleDef.getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int iDmW = dm.widthPixels;
        int iDmH = dm.heightPixels;

        //橫/豎屏不同排版
        if (iDmH < iDmW){
            m_vRootBg.setBackgroundResource(R.drawable.bg_nbns);

            m_llContent.setOrientation(LinearLayout.HORIZONTAL);

            lParams = (LinearLayout.LayoutParams)m_flSurfaceView.getLayoutParams();
            lParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flSurfaceView.setLayoutParams(lParams);
            m_flSurfaceView.setPadding(
                    vScaleDef.getLayoutWidth(30),
                    vScaleDef.getLayoutHeight(30),
                    vScaleDef.getLayoutWidth(30),
                    vScaleDef.getLayoutHeight(30));

            lParams = (LinearLayout.LayoutParams)m_flListView.getLayoutParams();
            lParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flListView.setLayoutParams(lParams);
            m_flListView.setPadding(
                    0,
                    vScaleDef.getLayoutHeight(30),
                    vScaleDef.getLayoutWidth(30),
                    vScaleDef.getLayoutHeight(30));

            ViewTreeObserver vto = m_llContent.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int iSvWidth = (int)(m_llContent.getWidth() * 0.6f);
                    int iLvWidth = m_llContent.getWidth() - iSvWidth;

                    m_flSurfaceView.getLayoutParams().width    = iSvWidth;
                    m_flListView.getLayoutParams().width       = iLvWidth;

                }
            });
        }else {
            m_llContent.setOrientation(LinearLayout.VERTICAL);

            lParams = (LinearLayout.LayoutParams)m_flSurfaceView.getLayoutParams();
            lParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flSurfaceView.setLayoutParams(lParams);
            m_flSurfaceView.setPadding(
                    vScaleDef.getLayoutWidth(30),
                    vScaleDef.getLayoutHeight(30),
                    vScaleDef.getLayoutWidth(30),
                    0);

            lParams = (LinearLayout.LayoutParams)m_flListView.getLayoutParams();
            lParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flListView.setPadding(
                    vScaleDef.getLayoutWidth(30),
                    vScaleDef.getLayoutHeight(30),
                    vScaleDef.getLayoutWidth(30),
                    vScaleDef.getLayoutHeight(30));

            ViewTreeObserver vto = m_llContent.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int iSvHeight = (int)(m_llContent.getHeight() * 0.6f);
                    int iLvHeight = m_llContent.getHeight() - iSvHeight;

                    m_flSurfaceView.getLayoutParams().height    = iSvHeight;
                    m_flListView.getLayoutParams().height       = iLvHeight;

                }
            });
        }
    }

    @Override
    protected void setOnParameterAndListener() {

        m_navigationBar.uiSetParameterListener(
                m_navigationParameter, m_navigationListener);

        m_twoItemBar.uiSetParameterListener(m_twobarParameter, m_twobarListener);
    }

    @Override
    protected void registerFragment(FragmentManager fragmentManager) {

        LNetBSSurfaceViewFragment   surfaceViewFragment = new LNetBSSurfaceViewFragment();
        LNetBSListViewFragment      listViewFragment    = new LNetBSListViewFragment();

        // 開始Fragment的事務Transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 替换容器(container)原来的Fragment
        fragmentTransaction.replace(m_flSurfaceView.getId(), surfaceViewFragment);
        fragmentTransaction.replace(m_flListView.getId(), listViewFragment);
        // 提交事務
        fragmentTransaction.commitAllowingStateLoss();
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
