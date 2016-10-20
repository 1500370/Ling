package ling.testapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseLinearView;
import ling.testapp.ui.define.LViewScaleDef;

/**
 * Created by jlchen on 2016/10/20.
 */

public class LShadowScrollView extends LBaseLinearView {

    private ScrollView      m_scrollView        = null;
    private LinearLayout    m_llayout_Content   = null;
    private View            m_vGradient         = null;
    private View            m_vGradientTop      = null;

    //Y軸可滾動的範圍大小,
    private int             m_iScrollHeight     = 0;
    private boolean         m_bIsShowTop        = true;

    public LShadowScrollView(Context context) {
        super(context);
        initial();
    }

    public LShadowScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }
    @Override
    public int getLayoutResourceId() {
        return R.layout.view_shadow_scrollview;
    }

    @Override
    public void initialLayoutComponent(LayoutInflater inflater) {

        m_scrollView        = (ScrollView)findViewById(R.id.sv);
        m_llayout_Content   = (LinearLayout)findViewById(R.id.llayout_content);
        m_vGradient         = findViewById(R.id.v_gradient);
        m_vGradientTop      = findViewById(R.id.v_gradient_top);
        m_vGradientTop.setVisibility(GONE);

        m_scrollView.getViewTreeObserver().addOnScrollChangedListener(m_onScroll);

        ViewTreeObserver vto = m_scrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                m_scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                //Log.e("sv.getHeight()",""+m_scrollView.getHeight());

                int iCnt = m_scrollView.getChildCount();
                if ( iCnt <= 0 ){

                } else {

                    //內容高度 <= ScrollView高度, 沒有可滾動區域, 無法監聽onScrollChanged(), 需隱藏陰影
                    if ( m_scrollView.getChildAt(0).getHeight() <= m_scrollView.getHeight() ){
                        m_vGradient.setAlpha(0);
                        m_vGradientTop.setAlpha(0);
                    }
                    //Log.e("sv.Child(0).getHeight()",""+m_scrollView.getChildAt(0).getHeight());
                }
            }
        });
    }

    @Override
    public void setTextSizeAndLayoutParams(LViewScaleDef viewScaleDef) {
        m_vGradient.getLayoutParams().height = viewScaleDef.getLayoutHeight(54);
        m_vGradientTop.getLayoutParams().height = viewScaleDef.getLayoutHeight(54);
    }

    public ScrollView getScrollView(){
        return m_scrollView;
    }

    public LinearLayout getContentView(){
        return m_llayout_Content;
    }

    public void setShadowBarHeight( int iPx ){
        m_vGradient.getLayoutParams().height = iPx;
        m_vGradientTop.getLayoutParams().height = iPx;
    }

    public void setTopGradientVisibility( boolean bVisibility ){
        m_bIsShowTop = bVisibility;

        if ( true == bVisibility ){
            m_vGradientTop.setVisibility(VISIBLE);
        }else {
            m_vGradientTop.setVisibility(GONE);
        }
    }

    ViewTreeObserver.OnScrollChangedListener m_onScroll = new ViewTreeObserver.OnScrollChangedListener() {
        @Override
        public void onScrollChanged() {

            int iCnt = m_scrollView.getChildCount();
            if (iCnt > 0) {
                m_iScrollHeight = m_scrollView.getChildAt(0).getHeight() - m_scrollView.getHeight();

                if ( m_scrollView.getChildAt(0).getHeight() <= m_scrollView.getHeight() )
                    return;
            }

            //利用百分比決定 blurr 效果
            //減少分母, 提高Blur的效果
            float fAlpha = (m_scrollView.getScrollY() / (float) m_iScrollHeight);

            //如果滑動至頂部或底部 隱藏上方或下方陰影
            if (fAlpha >= 1) {
                m_vGradientTop.setVisibility(View.VISIBLE);
                m_vGradient.setVisibility(View.GONE);
            } else if (fAlpha <= 0) {
                m_vGradientTop.setVisibility(View.GONE);
                m_vGradient.setVisibility(View.VISIBLE);
            } else {
                m_vGradientTop.setVisibility(View.VISIBLE);
                m_vGradient.setVisibility(View.VISIBLE);
            }

            m_vGradientTop.setAlpha(fAlpha);

            fAlpha = 1 - fAlpha;
            m_vGradient.setAlpha(fAlpha);

            if ( true == m_bIsShowTop ){
                m_vGradientTop.setVisibility(VISIBLE);
            }else {
                m_vGradientTop.setVisibility(GONE);
            }
        }
    };
}
