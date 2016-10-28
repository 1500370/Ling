package ling.testapp.function.NBNS;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseActivity;
import ling.testapp.function.NBNS.item.LNetBSResp;
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

public class LNetBSActivity extends LBaseActivity implements LNetBSListener{

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
            if ( m_type == eType.TES )
                return;

            m_type = eType.TES;
            LNetBSPresenter.getInstance(LNetBSActivity.this).loadData(m_type);
        }

        @Override
        public void OnRightClick() {
            if ( m_type == eType.OTC )
                return;

            m_type = eType.OTC;
            LNetBSPresenter.getInstance(LNetBSActivity.this).loadData(m_type);
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
            return m_twoItemType;
        }
    };

    public enum eType {
        TES, OTC
    }

    private static final String     TAG             = "LNetBSActivity";

    private static final double     TWOITEM_WIDTH   = 75;
    private static final double     TWOITEM_PADDING = 2.5;
    private static final double     TWOITEM_MARGIN  = 30;

    private LNavigationBar          m_navigationBar = null;
    private LTwoItemNavigationBar   m_twoItemBar    = null;
    private eItemType               m_twoItemType   = eItemType.LEFT;

    private View                    m_vRootBg       = null;

    private LinearLayout            m_llContent     = null;
    private FrameLayout             m_flSurfaceView = null;
    private FrameLayout             m_flListView    = null;
    private eType                   m_type          = eType.TES;

    private LNetBSListViewFragment      m_fgListView    = null;
    private LNetBSSurfaceViewFragment   m_fgSurfaceView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");

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
        Log.d(TAG, "initialLayoutComponent");

        m_navigationBar = (LNavigationBar)findViewById(R.id.navigation_bar);
        m_twoItemBar    = (LTwoItemNavigationBar)findViewById(R.id.two_bar);

        m_vRootBg       = findViewById(R.id.v_background);

        m_llContent     = (LinearLayout)findViewById(R.id.ll_content);
        m_flSurfaceView = (FrameLayout)findViewById(R.id.fl_surfaceview);
        m_flListView    = (FrameLayout)findViewById(R.id.fl_listview);

        m_fgListView = new LNetBSListViewFragment();
        m_fgSurfaceView = new LNetBSSurfaceViewFragment();

        m_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LNetBSPresenter.getInstance(LNetBSActivity.this).loadData(m_type);
            }
        }, 500);
    }

    @Override
    protected void setTextSizeAndLayoutParams(final LViewScaleDef vScaleDef) {
        Log.d(TAG, "setTextSizeAndLayoutParams");

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
        final int iPadding = vScaleDef.getLayoutMinUnit(30);

        //抓整個螢幕大小
        DisplayMetrics dm = vScaleDef.getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int iDmW = dm.widthPixels;
        int iDmH = dm.heightPixels;

        //橫/豎屏不同排版
        if (iDmH < iDmW){
            //橫
            m_vRootBg.setBackgroundResource(R.drawable.bg_nbns);

            m_llContent.setOrientation(LinearLayout.HORIZONTAL);

            lParams = (LinearLayout.LayoutParams)m_flSurfaceView.getLayoutParams();
            lParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flSurfaceView.setLayoutParams(lParams);
            m_flSurfaceView.setPadding(iPadding, iPadding, iPadding, iPadding);

            lParams = (LinearLayout.LayoutParams)m_flListView.getLayoutParams();
            lParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flListView.setLayoutParams(lParams);
            m_flListView.setPadding(
                    0, iPadding, iPadding, iPadding);

            ViewTreeObserver vto = m_llContent.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    m_llContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    int iSvWidth = (int)(m_llContent.getWidth() * 0.6f);
                    int iLvWidth = m_llContent.getWidth() - iSvWidth;

                    m_flSurfaceView.getLayoutParams().width    = iSvWidth;
                    m_flListView.getLayoutParams().width       = iLvWidth;

                    m_fgSurfaceView.setWidthAndHeight(
                            iSvWidth-(iPadding*2),
                            m_llContent.getHeight()-(iPadding*2)) ;
                    m_fgListView.setTotalWidth(iLvWidth - iPadding);
                }
            });
        }else {
            //豎
            m_llContent.setOrientation(LinearLayout.VERTICAL);

            lParams = (LinearLayout.LayoutParams)m_flSurfaceView.getLayoutParams();
            lParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flSurfaceView.setLayoutParams(lParams);
            m_flSurfaceView.setPadding(
                    vScaleDef.getLayoutMinUnit(30),
                    vScaleDef.getLayoutMinUnit(30),
                    vScaleDef.getLayoutMinUnit(30),
                    0);

            lParams = (LinearLayout.LayoutParams)m_flListView.getLayoutParams();
            lParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flListView.setPadding(
                    vScaleDef.getLayoutMinUnit(30),
                    vScaleDef.getLayoutMinUnit(30),
                    vScaleDef.getLayoutMinUnit(30),
                    vScaleDef.getLayoutMinUnit(30));

            ViewTreeObserver vto = m_llContent.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    m_llContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    int iSvHeight = (int)(m_llContent.getHeight() * 0.6f);
                    int iLvHeight = m_llContent.getHeight() - iSvHeight;

                    m_flSurfaceView.getLayoutParams().height    = iSvHeight;
                    m_flListView.getLayoutParams().height       = iLvHeight;

                    m_fgSurfaceView.setWidthAndHeight(
                            m_llContent.getWidth()-(iPadding*2),
                            iSvHeight-iPadding);
                    m_fgListView.setTotalWidth(m_llContent.getWidth() - (iPadding*2));
                }
            });
        }
    }

    @Override
    protected void setOnParameterAndListener() {
        Log.d(TAG, "setOnParameterAndListener");

        m_navigationBar.uiSetParameterListener(
                m_navigationParameter, m_navigationListener);

        m_twoItemBar.uiSetParameterListener(m_twobarParameter, m_twobarListener);
    }

    @Override
    protected void registerFragment(FragmentManager fragmentManager) {
        Log.d(TAG, "registerFragment");

        // 開始Fragment的事務Transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 替换容器(container)原来的Fragment
        fragmentTransaction.replace(m_flSurfaceView.getId(), m_fgSurfaceView);
        fragmentTransaction.replace(m_flListView.getId(), m_fgListView);
        // 提交事務
        fragmentTransaction.commitAllowingStateLoss();
    }

    //切換橫豎屏
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LViewScaleDef.setInstance(null);
        LViewScaleDef vScaleDef = LViewScaleDef.getInstance(m_context);

        LinearLayout.LayoutParams lParams;
        final int iPadding = vScaleDef.getLayoutMinUnit(30);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 橫
            Log.d(TAG, "onConfigurationChanged-橫");

            m_vRootBg.setBackgroundResource(R.drawable.bg_nbns);

            m_llContent.setOrientation(LinearLayout.HORIZONTAL);

            lParams = (LinearLayout.LayoutParams)m_flSurfaceView.getLayoutParams();
            lParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flSurfaceView.setLayoutParams(lParams);
            m_flSurfaceView.setPadding(iPadding, iPadding, iPadding, iPadding);

            lParams = (LinearLayout.LayoutParams)m_flListView.getLayoutParams();
            lParams.height = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flListView.setLayoutParams(lParams);
            m_flListView.setPadding(0, iPadding, iPadding, iPadding);

        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 豎
            Log.d(TAG, "onConfigurationChanged-豎");

            m_llContent.setOrientation(LinearLayout.VERTICAL);

            m_vRootBg.setBackgroundResource(R.drawable.bg_bingo);

            lParams = (LinearLayout.LayoutParams)m_flSurfaceView.getLayoutParams();
            lParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flSurfaceView.setLayoutParams(lParams);
            m_flSurfaceView.setPadding(iPadding, iPadding, iPadding, 0);

            lParams = (LinearLayout.LayoutParams)m_flListView.getLayoutParams();
            lParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            m_flListView.setPadding(iPadding, iPadding, iPadding, iPadding);
        }

        m_handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // 橫
                    Log.d(TAG, "handler.postDelayed-橫");

                    m_vRootBg.setBackgroundResource(R.drawable.bg_nbns);

                    m_llContent.setOrientation(LinearLayout.HORIZONTAL);

                    int iSvWidth = (int)(m_llContent.getWidth() * 0.6f);
                    int iLvWidth = m_llContent.getWidth() - iSvWidth;

                    m_flSurfaceView.getLayoutParams().width    = iSvWidth;
                    m_flListView.getLayoutParams().width       = iLvWidth;

                    m_fgSurfaceView.setWidthAndHeight(
                            iSvWidth-(iPadding*2),
                            m_llContent.getHeight()-(iPadding*2)) ;
                    m_fgListView.setTotalWidth(iLvWidth - iPadding);

                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // 豎
                    Log.d(TAG, "handler.postDelayed-豎");

                    int iSvHeight = (int)(m_llContent.getHeight() * 0.6f);
                    int iLvHeight = m_llContent.getHeight() - iSvHeight;

                    m_flSurfaceView.getLayoutParams().height    = iSvHeight;
                    m_flListView.getLayoutParams().height       = iLvHeight;

                    m_fgSurfaceView.setWidthAndHeight(
                            m_llContent.getWidth()-(iPadding*2),
                            iSvHeight-iPadding);
                    m_fgListView.setTotalWidth(m_llContent.getWidth()-(iPadding*2));
                }
            }
        }, 1000);
    }

    @Override
    protected void onLanguageChangeUpdateUI() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LViewScaleDef.setInstance(null);
        LNetBSPresenter.setInstance(null);
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
    }

    @Override
    public void onSuccess(LNetBSResp resultData) {
        Log.d(TAG, "onSuccess");

        m_fgListView.setNetBSData(resultData.m_alData);
    }

    @Override
    public void onError(String strError) {
        Log.d(TAG, "onError");

        m_fgListView.setErrorMsg(strError);
    }

    @Override
    public void showProgress() {
        m_fgListView.setMsgVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
//        m_fgListView.setMsgVisibility(View.GONE);
    }
}
