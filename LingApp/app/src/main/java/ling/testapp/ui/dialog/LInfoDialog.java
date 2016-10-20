package ling.testapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import ling.testapp.R;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.view.LShadowScrollView;

/**自定義訊息視窗(透明灰)
 *<br>Title 為必要參數
 *<br>Content 為非必要參數, 放置要顯示的訊息,離開App時適用*/
public class LInfoDialog extends Dialog
{
	public interface OnDialogListener
	{
		void OnClose();
	}

	private static final double WEIGHT_LEFT_PADDING 		= 30;
	private static final double WEIGHT_RIGHT_PADDING 		= 30;
	private static final double WEIGHT_DIALOG_TOP			= 50;
	private static final double WEIGHT_WIDTH_DIALOG 		= 1080;
	private static final double WEIGHT_HEIGHT_TITLE 		= 150;
	private static final double WEIGHT_HEIGHT_CONTENT_TOP 	= 5;
	private static final double WEIGHT_HEIGHT_CONTENT_GAP 	= 30;
	private static final double WEIGHT_HEIGHT_CONTENT_MAX	= 1500;
	private static final double TEXT_SIZE_TITLE				= 60;
	private static final double TEXT_SIZE_CONTENT			= 48;

	private String 				m_strTitle 			= null;
	private String 				m_strContent 		= null;

	private Context				m_context 			= null;
	private LViewScaleDef 		m_viewScaleDef 		= null;

	private LShadowScrollView	m_shadowScrollView  = null;
	private ScrollView          m_scrollView        = null;
	private LinearLayout        m_llContent         = null;

	private OnDialogListener m_onAlertMsgDialogListener = null;

	private View.OnClickListener m_onClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			int viewID = v.getId();
			if(viewID == R.id.ibtn_close)
			{
				if (m_onAlertMsgDialogListener != null)
				{
					m_onAlertMsgDialogListener.OnClose();
				}
				dismiss();
			}
		}
	};

	public LInfoDialog(Context context, OnDialogListener listener)
	{
		super(context, R.style.AlertDialog);
		setCancelable(true);	//back 可關閉
		setCanceledOnTouchOutside(true);	//點擊外面dialog會dismiss
//		requestWindowFeature(Window.FEATURE_NO_TITLE);	//不顯示沒有title

		m_context 		= context;
		m_viewScaleDef 	= LViewScaleDef.getInstance(m_context);
		m_onAlertMsgDialogListener = listener;
	}

	public void uiSetTitleText(String strTitle) {
		m_strTitle = strTitle;
	}

	public void uiSetContentText(String strContent) {
		m_strContent = strContent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_info_dialog);

		RelativeLayout root = (RelativeLayout) findViewById(R.id.rl_dialog_root);
		root.getLayoutParams().width = m_viewScaleDef.getLayoutWidth(WEIGHT_WIDTH_DIALOG);

		LinearLayout layout_bg = (LinearLayout)findViewById(R.id.ll_dialog_bg);
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams)layout_bg.getLayoutParams();
		rp.width = m_viewScaleDef.getLayoutWidth(WEIGHT_WIDTH_DIALOG);
		rp.leftMargin = m_viewScaleDef.getLayoutWidth(WEIGHT_HEIGHT_CONTENT_GAP);
		rp.rightMargin = m_viewScaleDef.getLayoutWidth(WEIGHT_HEIGHT_CONTENT_GAP);
		rp.topMargin = m_viewScaleDef.getLayoutHeight(WEIGHT_DIALOG_TOP);

		TextView 	tvTitle		= (TextView)findViewById(R.id.tv_title);
		//如果沒有設定Title文字就Gone掉
		if(null != m_strTitle && 0 < m_strTitle.length()){
			tvTitle.setText(m_strTitle);
			tvTitle.setVisibility(View.VISIBLE);
		} else {
			tvTitle.setVisibility(View.GONE);
		}

		m_viewScaleDef.setTextSize( TEXT_SIZE_TITLE, tvTitle);
		tvTitle.getLayoutParams().height = m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_TITLE);
		int nLeft = m_viewScaleDef.getLayoutWidth(WEIGHT_LEFT_PADDING);
		int nRight= m_viewScaleDef.getLayoutWidth(WEIGHT_RIGHT_PADDING);
		tvTitle.setPadding(nLeft, 0, nRight, 0);

		//滾軸
		m_shadowScrollView	= (LShadowScrollView) findViewById(R.id.shadowlayout);
		m_scrollView		= m_shadowScrollView.getScrollView();
		m_llContent			= m_shadowScrollView.getContentView();

		View ViewContent    = View.inflate( m_context, R.layout.view_info_dialog_content, null);
		TextView tvContent	= (TextView)ViewContent.findViewById(R.id.tv_content);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		m_llContent.addView(ViewContent, params);

		params = (LinearLayout.LayoutParams)m_shadowScrollView.getLayoutParams();
		params.height =  m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_CONTENT_MAX);
		//當沒有輸入title時, 將上方間距加大
		if ( View.VISIBLE == tvTitle.getVisibility() ){
			params.topMargin = m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_CONTENT_TOP);
		} else {
			params.topMargin = m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_CONTENT_GAP);
		}
//		params.bottomMargin = m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_CONTENT_GAP);
		m_shadowScrollView.setLayoutParams(params);
		m_shadowScrollView.setPadding(
				m_viewScaleDef.getLayoutWidth(WEIGHT_HEIGHT_CONTENT_TOP), 0,
				m_viewScaleDef.getLayoutWidth(WEIGHT_HEIGHT_CONTENT_TOP), 0);

		if ( m_strContent != null ){
			tvContent.setText(m_strContent);
			tvContent.setVisibility(View.VISIBLE);
			m_shadowScrollView.setVisibility(View.VISIBLE);
		} else {
			tvContent.setVisibility(View.GONE);
			m_shadowScrollView.setVisibility(View.GONE);
		}
		m_viewScaleDef.setTextSize( TEXT_SIZE_CONTENT, tvContent);
		tvContent.setPadding(nLeft, 0, nRight, 0);

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
		
		//按鈕區
		ImageButton ibtnClose = (ImageButton)findViewById(R.id.ibtn_close);
		ibtnClose.setOnClickListener(m_onClickListener);

		View vClose = findViewById(R.id.v_close);

		ibtnClose.getLayoutParams().height = m_viewScaleDef.getLayoutMinUnit(100);
		ibtnClose.getLayoutParams().width = m_viewScaleDef.getLayoutMinUnit(100);
		vClose.getLayoutParams().height = m_viewScaleDef.getLayoutMinUnit(100);
		vClose.getLayoutParams().width = m_viewScaleDef.getLayoutMinUnit(100);
	}

}
