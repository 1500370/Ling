package ling.testapp.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ling.testapp.R;
import ling.testapp.ui.define.LViewScaleDef;

/**自定義警告訊息視窗
 *<br>Title 為必要參數
 *<br>Content 為非必要參數, 放置要顯示的訊息,離開App時適用*/
public class LAlertDialog extends Dialog
{
	public interface OnAlertMsgDialogListener
	{
		void onDialogConfirm();
		void onDialogCancel();
	}

	private static final double WEIGHT_LEFT_PADDING 		= 60;
	private static final double WEIGHT_RIGHT_PADDING 		= 60;
	private static final double WEIGHT_WIDTH_DIALOG 		= 920;
	private static final double WEIGHT_HEIGHT_TITLE 		= 170;
	private static final double WEIGHT_HEIGHT_CONTENT_TOP 	= 18;
	private static final double WEIGHT_HEIGHT_CONTENT_GAP 	= 30;
	private static final double WEIGHT_HEIGHT_CONTENT_MAX	= 1350;
	private static final double WEIGHT_HEIGHT_BUTTON 		= 130;
	private static final double TEXT_SIZE_TITLE				= 60;
	private static final double TEXT_SIZE_CONTENT			= 48;
	private static final double DIV_HEIGHT					= 1;

	private String m_strTitle 	= null;
	private String m_strContent = null;
	private String m_strConfirm = null;
	private String m_strCancel 	= null;
	private LViewScaleDef m_viewScaleDef = null;

	private OnAlertMsgDialogListener m_onAlertMsgDialogListener = null;

	private View.OnClickListener m_onClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			int viewID = v.getId();
			if(viewID == R.id.btn_right)
			{
				if (m_onAlertMsgDialogListener != null)
				{
					m_onAlertMsgDialogListener.onDialogConfirm();
				}
				dismiss();
			}
			else if(viewID == R.id.btn_left)
			{
				if (m_onAlertMsgDialogListener != null)
				{
					m_onAlertMsgDialogListener.onDialogCancel();
				}
				dismiss();
			}
		}
	};

	public LAlertDialog(Context context, OnAlertMsgDialogListener onAlertDialogListener)
	{
		super(context, R.style.alert_dialog);
		setCancelable(false);	//back 不可關閉
		setCanceledOnTouchOutside(false);	//點擊外面dialog不會dismiss
//		requestWindowFeature(Window.FEATURE_NO_TITLE);	//不顯示沒有title

		m_viewScaleDef = LViewScaleDef.getInstance((Activity)context);
		m_onAlertMsgDialogListener = onAlertDialogListener;
	}

	public void uiSetTitleText(String strTitle) {
		m_strTitle = strTitle;
	}

	public void uiSetContentText(String strContent) {
		m_strContent = strContent;
	}

	public void uiSetConfirmText(String strConfirm) {
		m_strConfirm = strConfirm;
	}

	public void uiSetCancelText(String strCancel) {
		m_strCancel = strCancel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_dialog);

		LinearLayout layout_bg = (LinearLayout)findViewById(R.id.ll_dialog_root);
		layout_bg.getLayoutParams().width = m_viewScaleDef.getLayoutHeight(WEIGHT_WIDTH_DIALOG);


		TextView tvTitle			= (TextView)findViewById(R.id.tv_title);
		TextView tvContent		= (TextView)findViewById(R.id.tv_content);
		View vHeadLine 		= (View)findViewById(R.id.v_head_div);
		View vContentLine 	= (View)findViewById(R.id.v_content_div);

		//如果沒有設定Title文字就Gone掉
		if(null != m_strTitle && 0 < m_strTitle.length()){
			tvTitle.setText(m_strTitle);
			tvTitle.setVisibility(View.VISIBLE);
			vHeadLine.setVisibility(View.VISIBLE);
		} else {
			tvTitle.setVisibility(View.GONE);
			vHeadLine.setVisibility(View.GONE);
		}

		m_viewScaleDef.setTextSize( TEXT_SIZE_TITLE, tvTitle);
		tvTitle.getLayoutParams().height = m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_TITLE);
		int nLeft = m_viewScaleDef.getLayoutWidth(WEIGHT_LEFT_PADDING);
		int nRight= m_viewScaleDef.getLayoutWidth(WEIGHT_RIGHT_PADDING);
		tvTitle.setPadding(nLeft, 0, nRight, 0);

		m_viewScaleDef.setTextSize( TEXT_SIZE_CONTENT, tvContent);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)tvContent.getLayoutParams();
		//當沒有輸入title時, 將上方間距加大
		if ( View.VISIBLE == tvTitle.getVisibility() ){
			params.topMargin = m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_CONTENT_TOP);
		} else {
			params.topMargin = m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_CONTENT_GAP);
		}
		params.bottomMargin = m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_CONTENT_GAP);
		tvContent.setLayoutParams(params);
		tvContent.setPadding(nLeft, 0, nRight, 0);
		tvContent.setMaxHeight(m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_CONTENT_MAX));

		if ( m_strContent != null ){
			tvContent.setText(m_strContent);
			tvContent.setVisibility(View.VISIBLE);
			vContentLine.setVisibility(View.VISIBLE);
		} else {
			tvContent.setVisibility(View.GONE);
			vContentLine.setVisibility(View.GONE);
		}
		
		//按鈕區
		LinearLayout layout_button = (LinearLayout)findViewById(R.id.ll_button);
		layout_button.getLayoutParams().height = m_viewScaleDef.getLayoutHeight(WEIGHT_HEIGHT_BUTTON);
		
		View vBtnLin = (View)findViewById(R.id.v_btn_div);

		Button button;
		button = (Button) findViewById(R.id.btn_right);
		m_viewScaleDef.setTextSize( TEXT_SIZE_CONTENT, button);
		if (m_strConfirm != null)
		{
			button.setOnClickListener(m_onClickListener);
			button.setText(m_strConfirm);
		}
		else
		{
			vBtnLin.setVisibility(View.GONE);
			button.setVisibility(View.GONE);
		}

		button = (Button) findViewById(R.id.btn_left);
		m_viewScaleDef.setTextSize( TEXT_SIZE_CONTENT, button);
		if (m_strCancel != null)
		{
			button.setOnClickListener(m_onClickListener);
			button.setText(m_strCancel);
		}
		else
		{
			vBtnLin.setVisibility(View.GONE);
			button.setVisibility(View.GONE);
		}

		params = (LinearLayout.LayoutParams)vHeadLine.getLayoutParams();
		params.height = m_viewScaleDef.getLayoutHeight(DIV_HEIGHT);

		params = (LinearLayout.LayoutParams)vContentLine.getLayoutParams();
		params.height = m_viewScaleDef.getLayoutHeight(DIV_HEIGHT);

		params = (LinearLayout.LayoutParams)vBtnLin.getLayoutParams();
		params.height = m_viewScaleDef.getLayoutHeight(DIV_HEIGHT);
	}

}
