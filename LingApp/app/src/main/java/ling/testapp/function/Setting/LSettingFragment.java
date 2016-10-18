package ling.testapp.function.Setting;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseFragment;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.listener.LNaviBarToMainListener;
import ling.testapp.ui.navigation.LNavigationBar;

/**
 * Created by jlchen on 2016/9/23.
 */

public class LSettingFragment extends LBaseFragment implements View.OnClickListener{

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
            return getActivity().getResources().getString(R.string.title_setting);
        }

        @Override
        public int GetLeftIconRes() {
            return 0;
        }

        @Override
        public int GetRightIconRes() {
            return 0;
        }
    };

    private static final double         FIRST_LAYOUT_HEIGHT     = 380;
    private static final double         FIRST_LAYOUT_PADDING    = 180;
    private static final double         LAYOUT_HEIGHT           = 200;
    private static final double         LAYOUT_PADDING          = 30;
    private static final double         LAYOUT_PADDING_RIGHT    = 10;
    private static final double         TEXTVIEW_MARGIN         = 20;
    private static final double         TEXT_TITLE_SIZE         = 54;
    private static final double         TEXT_SIZE               = 36;
    private static final double         IMAGE_WIDTH             = 150;
    private static final double         IMAGE_MARGIN_TOP        = 25;
    private static final double         IMAGE_MARGIN_RIGHT      = 2;
    private static final double         IMAGE_SHADOW_MARGIN     = 27;
    private static final double         LINE_HEIGHT             = 1;

    private LNaviBarToMainListener      m_onMainListener        = null;

    private LNavigationBar              m_navigationBar         = null;

    private RelativeLayout              m_rlLanguage            = null;
    private LinearLayout                m_llLanguage            = null;
    private TextView                    m_tvLanTitle            = null;
    private TextView                    m_tvLanguage            = null;
    private ImageButton                 m_ibtnArrow             = null;
    private ImageView                   m_ivArrowShasow         = null;
    private View                        m_vLine1                = null;

    private RelativeLayout              m_rlVersion             = null;
    private TextView                    m_tvVerTitle            = null;
    private TextView                    m_tvVersion             = null;
    private TextView                    m_tvDevelop             = null;
    private View                        m_vLine2                = null;

    private TextView                    m_tvAppName             = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initialLayoutComponent(LayoutInflater inflater, View view) {

        m_navigationBar = (LNavigationBar)view.findViewById(R.id.navigation_bar);

        m_rlLanguage    = (RelativeLayout)view.findViewById(R.id.rl_language);
        m_llLanguage    = (LinearLayout)view.findViewById(R.id.ll_language_text);
        m_tvLanTitle    = (TextView)view.findViewById(R.id.tv_title_language);
        m_tvLanguage    = (TextView)view.findViewById(R.id.tv_language);
        m_ibtnArrow     = (ImageButton)view.findViewById(R.id.ibtn_language);
        m_ivArrowShasow = (ImageView)view.findViewById(R.id.iv_language_shadow);
        m_vLine1        = view.findViewById(R.id.v_line_1);

        m_rlVersion     = (RelativeLayout)view.findViewById(R.id.rl_version);
        m_tvVerTitle    = (TextView)view.findViewById(R.id.tv_title_version);
        m_tvVersion     = (TextView)view.findViewById(R.id.tv_version);
        m_tvDevelop     = (TextView)view.findViewById(R.id.tv_develop);
        m_vLine2        = view.findViewById(R.id.v_line_2);

        m_tvAppName     = (TextView)view.findViewById(R.id.tv_app_name);
    }

    @Override
    protected void setTextSizeAndLayoutParams(View view, LViewScaleDef vScaleDef) {

        m_navigationBar.getLayoutParams().height
                = vScaleDef.getLayoutHeight(LNavigationBar.NAVIGATION_BAR_HEIGHT);

        RelativeLayout.LayoutParams params
                = (RelativeLayout.LayoutParams) m_rlLanguage.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(FIRST_LAYOUT_HEIGHT);
        m_rlLanguage.setPadding(
                vScaleDef.getLayoutWidth(LAYOUT_PADDING),
                vScaleDef.getLayoutHeight(FIRST_LAYOUT_PADDING),
                vScaleDef.getLayoutWidth(LAYOUT_PADDING_RIGHT),
                0);

        params = (RelativeLayout.LayoutParams) m_llLanguage.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(LAYOUT_HEIGHT);
        params.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_PADDING);

        LinearLayout.LayoutParams layoutParams
                = (LinearLayout.LayoutParams) m_tvLanguage.getLayoutParams();
        layoutParams.topMargin = vScaleDef.getLayoutHeight(TEXTVIEW_MARGIN);

        params = (RelativeLayout.LayoutParams) m_ibtnArrow.getLayoutParams();
        params.height = vScaleDef.getLayoutMinUnit(IMAGE_WIDTH);
        params.width = vScaleDef.getLayoutMinUnit(IMAGE_WIDTH);
        params.topMargin = vScaleDef.getLayoutHeight(IMAGE_MARGIN_TOP);
        params.rightMargin = vScaleDef.getLayoutHeight(IMAGE_MARGIN_RIGHT);

        params = (RelativeLayout.LayoutParams) m_ivArrowShasow.getLayoutParams();
        params.height = vScaleDef.getLayoutMinUnit(IMAGE_WIDTH);
        params.width = vScaleDef.getLayoutMinUnit(IMAGE_WIDTH);
        params.topMargin = vScaleDef.getLayoutHeight(IMAGE_SHADOW_MARGIN);

        params = (RelativeLayout.LayoutParams) m_vLine1.getLayoutParams();
        params.height = vScaleDef.getLayoutMinUnit(LINE_HEIGHT);

        params = (RelativeLayout.LayoutParams) m_rlVersion.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(LAYOUT_HEIGHT);
        m_rlVersion.setPadding(
                vScaleDef.getLayoutWidth(LAYOUT_PADDING),
                0,
                vScaleDef.getLayoutWidth(LAYOUT_PADDING),
                0);

        params = (RelativeLayout.LayoutParams) m_tvVerTitle.getLayoutParams();
        params.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_PADDING);

        params = (RelativeLayout.LayoutParams) m_tvVersion.getLayoutParams();
        params.topMargin = vScaleDef.getLayoutHeight(TEXTVIEW_MARGIN);
        params.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_PADDING);

        params = (RelativeLayout.LayoutParams) m_tvDevelop.getLayoutParams();
        params.topMargin = vScaleDef.getLayoutHeight(TEXTVIEW_MARGIN);
        params.leftMargin = vScaleDef.getLayoutWidth(TEXTVIEW_MARGIN);

        params = (RelativeLayout.LayoutParams) m_vLine2.getLayoutParams();
        params.height = vScaleDef.getLayoutMinUnit(LINE_HEIGHT);

        m_tvAppName.setPadding(
                vScaleDef.getLayoutWidth(LAYOUT_PADDING),
                vScaleDef.getLayoutHeight(LAYOUT_PADDING),
                vScaleDef.getLayoutWidth(LAYOUT_PADDING),
                vScaleDef.getLayoutHeight(LAYOUT_PADDING));

        vScaleDef.setTextSize(TEXT_TITLE_SIZE, m_tvLanTitle);
        vScaleDef.setTextSize(TEXT_TITLE_SIZE, m_tvVerTitle);
        vScaleDef.setTextSize(TEXT_TITLE_SIZE, m_tvAppName);

        vScaleDef.setTextSize(TEXT_SIZE, m_tvLanguage);
        vScaleDef.setTextSize(TEXT_SIZE, m_tvVersion);
        vScaleDef.setTextSize(TEXT_SIZE, m_tvDevelop);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_language:
                break;
        }
    }
}
