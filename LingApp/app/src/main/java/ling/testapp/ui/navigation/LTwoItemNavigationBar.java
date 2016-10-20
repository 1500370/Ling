package ling.testapp.ui.navigation;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseRelativeView;
import ling.testapp.ui.define.LViewScaleDef;

/**
 * Created by jlchen on 2016/9/21.
 * 引導欄
 */
public class LTwoItemNavigationBar extends LBaseRelativeView implements View.OnClickListener{

    public enum eItemType {
        LEFT,RIGHT
    }

    public interface OnParameter {
        /**取得左邊按鈕字串*/
        String GetLeftText();
        /**取得右邊按鈕字串*/
        String GetRightText();
        /**取得右邊按鈕字串*/
        eItemType GetInitType();
    }

    public interface OnListener {
        /**點擊左側按鈕*/
        void OnLeftClick();
        /**點擊右側按鈕*/
        void OnRightClick();
    }

    private static final double TEXT_TITLE_SIZE = 48;

    private OnParameter     m_onParameter       = null;
    private OnListener      m_onListener        = null;

    private RelativeLayout  m_rlLeftBg          = null,
                            m_rlRightBg         = null;
    private TextView        m_tvLeftItem        = null,
                            m_tvRightItem       = null;
    private String          m_strLeftText       = null,
                            m_strRightText      = null;
    private eItemType       m_type              = eItemType.LEFT;

    public LTwoItemNavigationBar(Context context) {
        this(context, null);
    }

    public LTwoItemNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LTwoItemNavigationBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void uiSetParameterListener(OnParameter   onParameter,
                                       OnListener    onListener) {
        m_onParameter = onParameter;
        m_onListener = onListener;

        initial();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.view_two_item_navigation_bar;
    }

    @Override
    public void initialLayoutComponent(LayoutInflater inflater) {
        m_rlLeftBg    = (RelativeLayout)findViewById(R.id.rl_left_bg);
        m_rlLeftBg.setOnClickListener(this);
        m_rlRightBg   = (RelativeLayout)findViewById(R.id.rl_right_bg);
        m_rlRightBg.setOnClickListener(this);
        m_tvLeftItem  = (TextView)findViewById(R.id.tv_left_item);
        m_tvLeftItem.setOnClickListener(this);
        m_tvRightItem = (TextView)findViewById(R.id.tv_right_item);
        m_tvRightItem.setOnClickListener(this);

        if(null != m_onParameter){
            m_strLeftText = m_onParameter.GetLeftText();
            if (null != m_strLeftText)
                m_tvLeftItem.setText(m_strLeftText);

            m_strRightText = m_onParameter.GetRightText();
            if (null != m_strRightText)
                m_tvRightItem.setText(m_strRightText);


            if (null != m_onParameter.GetInitType()){
                m_type = m_onParameter.GetInitType();
            }
        }

        //init
        switchItemStyle();
    }

    @Override
    public void setTextSizeAndLayoutParams(LViewScaleDef viewScaleDef) {
        viewScaleDef.setTextSize(TEXT_TITLE_SIZE, m_tvLeftItem);
        viewScaleDef.setTextSize(TEXT_TITLE_SIZE, m_tvRightItem);
    }

    private void switchItemStyle(){
        switch (m_type){
            case LEFT:
                setStyle(ContextCompat.getColor(m_context, R.color.pink),
                        Color.WHITE,
                        R.drawable.bg_two_item_left,
                        R.drawable.bg_two_item_right_unclick);
                break;
            case RIGHT:
                setStyle(Color.WHITE,
                        ContextCompat.getColor(m_context, R.color.pink),
                        R.drawable.bg_two_item_left_unclick,
                        R.drawable.bg_two_item_right);
                break;
        }
    }

    /**
     * 設置背景及文字style
     * @param leftTextColor     左邊文字顏色
     * @param rightTextColor    右邊文字顏色
     * @param leftBg            左邊背景顏色
     * @param rightBg           右邊背景顏色
     */
    private void setStyle(int leftTextColor,int rightTextColor,int leftBg,int rightBg){
        m_rlRightBg.setBackgroundResource(rightBg);
        m_rlLeftBg.setBackgroundResource(leftBg);
        m_tvLeftItem.setTextColor(leftTextColor);
        m_tvRightItem.setTextColor(rightTextColor);
    }

    public eItemType getType(){
        return m_type;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_left_bg:
            case R.id.tv_left_item:
                m_type = eItemType.LEFT;

                if ( null != m_onListener ){
                    m_onListener.OnLeftClick();
                }
                break;
            case R.id.rl_right_bg:
            case R.id.tv_right_item:
                m_type = eItemType.RIGHT;

                if ( null != m_onListener ){
                    m_onListener.OnRightClick();
                }
                break;
        }

        switchItemStyle();
    }
}
