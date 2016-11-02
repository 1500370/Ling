package ling.testapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseLinearView;
import ling.testapp.ui.define.LViewScaleDef;

/**
 * Created by jlchen on 2016/10/20.
 */

public class LShadowListView extends LBaseLinearView {

    private ListView        m_listView          = null;
    private View            m_vGradient         = null;
    private View            m_vGradientTop      = null;

    //Y軸可滾動的範圍大小,
    private int             m_iScrollHeight     = 0;
    private int             m_iListViewHeight   = 0;
    private boolean         m_bIsShowTop        = true;

    public LShadowListView(Context context) {
        super(context);
        initial();
    }

    public LShadowListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }
    @Override
    public int getLayoutResourceId() {
        return R.layout.view_shadow_listview;
    }

    @Override
    public void initialLayoutComponent(LayoutInflater inflater) {

        m_listView          = (ListView)findViewById(R.id.listview);
        m_vGradient         = findViewById(R.id.v_gradient);
        m_vGradientTop      = findViewById(R.id.v_gradient_top);
//        m_vGradientTop.setAlpha(0);
        m_vGradient.setVisibility(INVISIBLE);
        m_vGradientTop.setVisibility(INVISIBLE);

        m_listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                int iCnt = m_listView.getChildCount();
                if ( iCnt > 0 ){
                    //內容高度 <= ScrollView高度, 沒有可滾動區域, 無法監聽onScrollChanged(), 需隱藏陰影
                    if (m_iListViewHeight <= m_listView.getHeight()) {

                        m_vGradient.setVisibility(INVISIBLE);
                        m_vGradientTop.setVisibility(INVISIBLE);
                        return;
                    }
                }

                //切換橫豎屏時，陰影透明度要依新的比例重抓
                if ( 0 != m_iScrollHeight )
                    setGradientAlpha(getListViewScrollY(), m_iScrollHeight);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        m_listView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                m_listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int iCnt = m_listView.getChildCount();
                if ( iCnt > 0 ){

                    m_iListViewHeight   = getListViewHeight();
                    m_iScrollHeight     = m_iListViewHeight - m_listView.getHeight();

                    //內容高度 <= ScrollView高度, 沒有可滾動區域, 無法監聽onScrollChanged(), 需隱藏陰影
                    if (m_iListViewHeight <= m_listView.getHeight()) {
//                        m_vGradient.setAlpha(0);
//                        m_vGradientTop.setAlpha(0);

                        m_vGradient.setVisibility(INVISIBLE);
                        m_vGradientTop.setVisibility(INVISIBLE);
                        return;
                    }else {
                        m_vGradient.setVisibility(VISIBLE);
                    }
                }

                //切換橫豎屏時，陰影透明度要依新的比例重抓
                if ( 0 != m_iScrollHeight )
                    setGradientAlpha(m_listView.getScrollY(), m_iScrollHeight);
            }
        });
    }

    @Override
    public void setTextSizeAndLayoutParams(LViewScaleDef viewScaleDef) {
        m_vGradient.getLayoutParams().height = viewScaleDef.getLayoutHeight(54);
        m_vGradientTop.getLayoutParams().height = viewScaleDef.getLayoutHeight(54);
    }

    public ListView getListView(){
        return m_listView;
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

    public void resetShadowListener(){
        if ( null == m_listView )
            return;

        m_listView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        m_listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int iCnt = m_listView.getChildCount();
                        if ( iCnt > 0 ){

                            m_iListViewHeight   = getListViewHeight();
                            m_iScrollHeight     = m_iListViewHeight - m_listView.getHeight();

                            //內容高度 <= ScrollView高度, 沒有可滾動區域, 無法監聽onScrollChanged(), 需隱藏陰影
                            if (m_iListViewHeight <= m_listView.getHeight()) {
//                        m_vGradient.setAlpha(0);
//                        m_vGradientTop.setAlpha(0);

                                m_vGradient.setVisibility(INVISIBLE);
                                m_vGradientTop.setVisibility(INVISIBLE);
                                return;
                            }else {
                                m_vGradient.setVisibility(VISIBLE);
                            }
                        }

                        //切換橫豎屏時，陰影透明度要依新的比例重抓
                        if ( 0 != m_iScrollHeight )
                            setGradientAlpha(m_listView.getScrollY(), m_iScrollHeight);
                    }
                });
    }

    public int getListViewScrollY() {
        View c = m_listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = m_listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight() ;
    }

    public int getListViewHeight() {
        ListAdapter listAdapter = m_listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = m_listView.getPaddingTop() + m_listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, m_listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        totalHeight = totalHeight + (m_listView.getDividerHeight() * (listAdapter.getCount() - 1));

        return totalHeight;
    }

    private void setGradientAlpha(int iY, int iHeight){

        //利用百分比決定 blurr 效果
        //減少分母, 提高Blur的效果
        float fAlpha = (iY / (float) iHeight);

//        m_vGradientTop.setAlpha(fAlpha);
        m_vGradientTop.setVisibility(VISIBLE);

        float fAlphaButtom = 1 - fAlpha;
//        m_vGradient.setAlpha(fAlphaButtom);
        m_vGradient.setVisibility(VISIBLE);

        //如果滑動至頂部或底部 隱藏上方或下方陰影
        if (fAlpha >= 1) {
//            m_vGradient.setAlpha(0);
            m_vGradient.setVisibility(INVISIBLE);
        } else if (fAlpha <= 0) {
//            m_vGradientTop.setAlpha(0);
            m_vGradientTop.setVisibility(INVISIBLE);
        }

        if ( true == m_bIsShowTop ){
            m_vGradientTop.setVisibility(VISIBLE);
        }else {
            m_vGradientTop.setVisibility(INVISIBLE);
        }
    }
}
