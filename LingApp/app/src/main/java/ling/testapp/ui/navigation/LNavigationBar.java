package ling.testapp.ui.navigation;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseRelativeView;
import ling.testapp.ui.define.LViewScaleDef;

/**
 * Created by jlchen on 2016/9/21.
 * 引導欄
 */
public class LNavigationBar extends LBaseRelativeView implements View.OnClickListener{

    public interface OnParameter {
        /**取得Title字串*/
        String GetTitle();
        /**取得左側按鈕icon*/
        int GetLeftIconRes();
        /**取得右側按鈕icon*/
        int GetRightIconRes();
        /**取得右側第二個按鈕icon*/
        int GetSecondRightIconRes();
    }

    public interface OnListener {
        /**點擊左側按鈕*/
        void OnLeftImgClick();
        /**點擊右側按鈕*/
        void OnRightImgClick();
        /**點擊右側第二個按鈕*/
        void OnSecondRightImgClick();
    }

    public interface OnInterface {
        /**變更語系*/
        void changeLanguageText(String strText);
    }

    private OnInterface m_onInterface   = new OnInterface() {
        @Override
        public void changeLanguageText(String strText) {
            m_tvTitle.setText(strText);
        }
    };

    public  static final double NAVIGATION_BAR_HEIGHT   = 180;
    private static final double BACKGROUND_HEIGHT       = 170.9;
    private static final double LINE_HEIGHT             = 1;
    private static final double SHADOW_HEIGHT           = 8.1;
    private static final double IMG_WIDTH               = 120;
    private static final double IMG_MARGIN              = 48;
    private static final double IMG_MARGIN_SECOND       = 156;
    private static final double TEXT_TITLE_SIZE         = 72;
    private static final double TEXT_MARGIN             = 200;

    private OnParameter     m_onParameter   = null;
    private OnListener      m_onListener    = null;

    private RelativeLayout  m_rlBg          = null;
    private TextView        m_tvTitle       = null;
    private ImageButton     m_ibtnLeft      = null;
    private ImageButton     m_ibtnRight     = null;
    private ImageButton     m_ibtnRightSec  = null;
    private View            m_vLine         = null;
    private View            m_vShadow       = null;

    public LNavigationBar(Context context) {
        this(context, null);
    }

    public LNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LNavigationBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OnInterface uiSetParameterListener(OnParameter   onParameter,
                                              OnListener    onListener) {
        m_onParameter = onParameter;
        m_onListener = onListener;

        initial();

        return m_onInterface;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.view_navigation_bar;
    }

    @Override
    public void initialLayoutComponent(LayoutInflater inflater) {

        m_rlBg          = (RelativeLayout) findViewById(R.id.rl_navigation_bar_content);

        m_tvTitle       = (TextView) findViewById(R.id.tv_title);

        m_ibtnLeft      = (ImageButton) findViewById(R.id.ibtn_left);
        m_ibtnLeft.setOnClickListener(this);

        m_ibtnRight     = (ImageButton) findViewById(R.id.ibtn_right);
        m_ibtnRight.setOnClickListener(this);

        m_ibtnRightSec  = (ImageButton) findViewById(R.id.ibtn_right_second);
        m_ibtnRightSec.setOnClickListener(this);

        m_vLine         = findViewById(R.id.v_line);
        m_vShadow       = findViewById(R.id.v_shadow);

        if ( null != m_onParameter ){
            if ( !TextUtils.isEmpty( m_onParameter.GetTitle() ) ){
                m_tvTitle.setText( m_onParameter.GetTitle() );
                m_tvTitle.setVisibility(VISIBLE);
            }else {
                m_tvTitle.setVisibility(INVISIBLE);
            }

            if ( 0 != m_onParameter.GetLeftIconRes() ){
                m_ibtnLeft.setImageResource( m_onParameter.GetLeftIconRes() );
            }

            if ( 0 != m_onParameter.GetRightIconRes() ){
                m_ibtnRight.setImageResource( m_onParameter.GetRightIconRes() );
                m_ibtnRight.setVisibility(VISIBLE);
            }else {
                m_ibtnRight.setVisibility(GONE);
            }

            if ( 0 != m_onParameter.GetSecondRightIconRes() ){
                m_ibtnRightSec.setImageResource( m_onParameter.GetSecondRightIconRes() );
                m_ibtnRightSec.setVisibility(VISIBLE);
            }else {
                m_ibtnRightSec.setVisibility(GONE);
            }
        }
    }

    @Override
    public void setTextSizeAndLayoutParams(LViewScaleDef viewScaleDef) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)m_rlBg.getLayoutParams();
        params.height = viewScaleDef.getLayoutHeight(BACKGROUND_HEIGHT);

        params = (RelativeLayout.LayoutParams)m_tvTitle.getLayoutParams();
        params.leftMargin = viewScaleDef.getLayoutWidth(TEXT_MARGIN);
        params.rightMargin = viewScaleDef.getLayoutWidth(TEXT_MARGIN);
        viewScaleDef.setTextSize(TEXT_TITLE_SIZE, m_tvTitle);

        params = (RelativeLayout.LayoutParams)m_ibtnLeft.getLayoutParams();
        params.height = viewScaleDef.getLayoutMinUnit(IMG_WIDTH);
        params.width = viewScaleDef.getLayoutMinUnit(IMG_WIDTH);
        params.leftMargin = viewScaleDef.getLayoutWidth(IMG_MARGIN);

        params = (RelativeLayout.LayoutParams)m_ibtnRight.getLayoutParams();
        params.height = viewScaleDef.getLayoutMinUnit(IMG_WIDTH);
        params.width = viewScaleDef.getLayoutMinUnit(IMG_WIDTH);
        params.rightMargin = viewScaleDef.getLayoutWidth(IMG_MARGIN);

        params = (RelativeLayout.LayoutParams)m_ibtnRightSec.getLayoutParams();
        params.height = viewScaleDef.getLayoutMinUnit(IMG_WIDTH);
        params.width = viewScaleDef.getLayoutMinUnit(IMG_WIDTH);
        if (VISIBLE == m_ibtnRight.getVisibility())
            params.rightMargin = viewScaleDef.getLayoutWidth(IMG_MARGIN_SECOND);
        else
            params.rightMargin = viewScaleDef.getLayoutWidth(IMG_MARGIN);

        params = (RelativeLayout.LayoutParams)m_vLine.getLayoutParams();
        params.height = viewScaleDef.getLayoutMinUnit(LINE_HEIGHT);

        params = (RelativeLayout.LayoutParams)m_vShadow.getLayoutParams();
        params.height = viewScaleDef.getLayoutMinUnit(SHADOW_HEIGHT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibtn_left:
                if ( null != m_onListener ){
                    m_onListener.OnLeftImgClick();
                }
                break;
            case R.id.ibtn_right:
                if ( null != m_onListener ){
                    m_onListener.OnRightImgClick();
                }
                break;
            case R.id.ibtn_right_second:
                if ( null != m_onListener ){
                    m_onListener.OnSecondRightImgClick();
                }
                break;
        }
    }
}
