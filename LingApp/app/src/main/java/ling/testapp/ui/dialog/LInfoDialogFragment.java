package ling.testapp.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import ling.testapp.R;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.view.LShadowScrollView;

/**
 * Created by jlchen on 2016/10/21.
 * 自定義訊息視窗(黑白)
 * 因應切換橫豎屏，改用DialogFragment
 */

public class LInfoDialogFragment extends DialogFragment {

    public interface OnListener {
        void OnClose();
    }

    private View.OnClickListener m_onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int viewID = v.getId();
            if(viewID == R.id.ibtn_close) {
                if (m_onListener != null) {
                    m_onListener.OnClose();
                }
                dismiss();
            }
        }
    };

    private OnListener          m_onListener                = null;

    private static final String TAG_TITLE                   = "Info_Title";
    private static final String TAG_CONTENT                 = "Info_Content";
    private static final double WEIGHT_PADDING              = 30;
    private static final double WEIGHT_DIALOG_TOP			= 50;
    private static final double WEIGHT_HEIGHT_TITLE 		= 150;
    private static final double WEIGHT_HEIGHT_CONTENT_TOP 	= 5;
    private static final double WEIGHT_HEIGHT_CONTENT_GAP 	= 30;
    private static final double TEXT_SIZE_TITLE				= 60;
    private static final double TEXT_SIZE_CONTENT			= 48;

    private LShadowScrollView	m_shadowScrollView          = null;
    private LinearLayout        m_llContent                 = null;
    private String              m_tvTitle                   = null;
    private String              m_tvContent                 = null;

    public static LInfoDialogFragment newInstance(String strTitle, String strContent) {
        LInfoDialogFragment fragment = new LInfoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG_TITLE, strTitle);
        bundle.putString(TAG_CONTENT, strContent);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        m_tvTitle = bundle.getString(TAG_TITLE);
        m_tvContent = bundle.getString(TAG_CONTENT);

        //定義dialog style
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.InfoDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup      container,
                             Bundle         savedInstanceState) {

        //移除原生dialog標題欄
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        //使原生dialog內容欄背景透明
        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);

        //自定義dialog動畫, 排版等屬性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setWindowAnimations(android.R.style.Animation_Dialog);
        dialogWindow.setAttributes(lp);

        View            v           = inflater.inflate(R.layout.view_info_dialog, container, false);

        RelativeLayout  root        = (RelativeLayout)v.findViewById(R.id.rl_dialog_root);
        LinearLayout    layout_bg   = (LinearLayout)v.findViewById(R.id.ll_dialog_bg);
        TextView        tvTitle		= (TextView)v.findViewById(R.id.tv_title);
        //滾軸
        m_shadowScrollView          = (LShadowScrollView) v.findViewById(R.id.shadowlayout);
        m_llContent                 = m_shadowScrollView.getContentView();
        ScrollView      scrollView  = m_shadowScrollView.getScrollView();
        //內容
        View            ViewContent = View.inflate( getActivity(), R.layout.view_info_dialog_content, null);
        TextView        tvContent	= (TextView)ViewContent.findViewById(R.id.tv_content);
        //按鈕區
        ImageButton     ibtnClose   = (ImageButton)v.findViewById(R.id.ibtn_close);
        ibtnClose.setOnClickListener(m_onClickListener);
        View            vClose      = v.findViewById(R.id.v_close);

        //如果沒有設定Title文字就Gone掉
        if(!TextUtils.isEmpty(m_tvTitle)){
            tvTitle.setText(m_tvTitle);
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        //如果沒有設定Content文字就Gone掉
        if(!TextUtils.isEmpty(m_tvContent)){
            tvContent.setText(m_tvContent);
            tvContent.setVisibility(View.VISIBLE);
            m_shadowScrollView.setVisibility(View.VISIBLE);
        } else {
            tvContent.setVisibility(View.GONE);
            m_shadowScrollView.setVisibility(View.GONE);
        }

        //訊息視窗 自適應
        LViewScaleDef vScaleDef = LViewScaleDef.getInstance(getActivity());

        root.setPadding(
                vScaleDef.getLayoutMinUnit(WEIGHT_PADDING),
                vScaleDef.getLayoutMinUnit(WEIGHT_PADDING),
                vScaleDef.getLayoutMinUnit(WEIGHT_PADDING),
                vScaleDef.getLayoutMinUnit(WEIGHT_PADDING));

        RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams)layout_bg.getLayoutParams();
        rp.leftMargin = vScaleDef.getLayoutMinUnit(WEIGHT_HEIGHT_CONTENT_GAP);
        rp.rightMargin = vScaleDef.getLayoutMinUnit(WEIGHT_HEIGHT_CONTENT_GAP);
        rp.topMargin = vScaleDef.getLayoutMinUnit(WEIGHT_DIALOG_TOP);
        rp.bottomMargin = vScaleDef.getLayoutMinUnit(WEIGHT_HEIGHT_CONTENT_GAP);

        vScaleDef.setTextSize( TEXT_SIZE_TITLE, tvTitle);
        tvTitle.getLayoutParams().height = vScaleDef.getLayoutMinUnit(WEIGHT_HEIGHT_TITLE);
        int nLeft = vScaleDef.getLayoutMinUnit(WEIGHT_PADDING);
        int nRight= vScaleDef.getLayoutMinUnit(WEIGHT_PADDING);
        tvTitle.setPadding(nLeft, 0, nRight, 0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        m_llContent.addView(ViewContent, params);

        params = (LinearLayout.LayoutParams)m_shadowScrollView.getLayoutParams();
//        params.height =  vScaleDef.getLayoutHeight(WEIGHT_HEIGHT_CONTENT_MAX);
        //當沒有輸入title時, 將上方間距加大
        if ( View.VISIBLE == tvTitle.getVisibility() ){
            params.topMargin = vScaleDef.getLayoutMinUnit(WEIGHT_HEIGHT_CONTENT_TOP);
        } else {
            params.topMargin = vScaleDef.getLayoutMinUnit(WEIGHT_HEIGHT_CONTENT_GAP);
        }
//		params.bottomMargin = m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_CONTENT_GAP);
        m_shadowScrollView.setLayoutParams(params);
        m_shadowScrollView.setPadding(
                vScaleDef.getLayoutMinUnit(WEIGHT_HEIGHT_CONTENT_TOP), 0,
                vScaleDef.getLayoutMinUnit(WEIGHT_HEIGHT_CONTENT_TOP), 0);

        //須依content設定長度
        ViewTreeObserver vto = m_shadowScrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                m_shadowScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int iShadowSvHeight = m_shadowScrollView.getHeight();
                int iContentHeight = m_llContent.getHeight();

                Log.e("iShadowSvHeight",""+m_shadowScrollView.getHeight());
                Log.e("iContentHeight",""+m_llContent.getHeight());

                if ( iShadowSvHeight > iContentHeight ){
                    m_shadowScrollView.getLayoutParams().height = iContentHeight;
                }
            }
        });

        vScaleDef.setTextSize( TEXT_SIZE_CONTENT, tvContent);
        tvContent.setPadding(nLeft, 0, nRight, 0);

        ibtnClose.getLayoutParams().height = vScaleDef.getLayoutMinUnit(100);
        ibtnClose.getLayoutParams().width = vScaleDef.getLayoutMinUnit(100);
        vClose.getLayoutParams().height = vScaleDef.getLayoutMinUnit(100);
        vClose.getLayoutParams().width = vScaleDef.getLayoutMinUnit(100);

        return v;
    }

    public void uiSetParameterListener(OnListener listener) {
        m_onListener    = listener;
    }
}
