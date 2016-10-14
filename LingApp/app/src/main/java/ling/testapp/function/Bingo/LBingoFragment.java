package ling.testapp.function.Bingo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseFragment;
import ling.testapp.function.Bingo.adapter.LBingoAdapter;
import ling.testapp.function.Bingo.item.LBingoItem;
import ling.testapp.ui.define.LUiMessageDef;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.listener.LNaviBarToMainListener;
import ling.testapp.ui.navigation.LNavigationBar;
import ling.testapp.ui.object.LApplication;
import ling.testapp.ui.popupwindow.LSelectBingoSizePopupwindow;
import ling.testapp.ui.view.LSwitchButton;

import static ling.testapp.ui.object.LBingoInfo.DEF_MAX;
import static ling.testapp.ui.object.LBingoInfo.DEF_MIN;

/**
 * Created by jlchen on 2016/9/23.
 */

public class LBingoFragment extends LBaseFragment implements
        View.OnTouchListener,
        View.OnFocusChangeListener,
        View.OnClickListener{

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
            return getActivity().getResources().getString(R.string.title_bingo);
        }
    };

    private LBingoAdapter.OnBingoAdapterParameter m_adapterParameter
            = new LBingoAdapter.OnBingoAdapterParameter() {
        @Override
        public int getWidth() {
            return m_iGvWidth;
        }

        @Override
        public int getMin() {
            return m_iMin;
        }

        @Override
        public int getMax() {
            return m_iMax;
        }

        @Override
        public int getCol() {
            return m_iCol;
        }

        @Override
        public ArrayList<LBingoItem> getData() {
            return m_alData;
        }

        @Override
        public LBingoAdapter.BingoType getType() {
            return m_type;
        }
    };

    private LBingoAdapter.OnBingoAdapterListener m_adapterListener
            = new LBingoAdapter.OnBingoAdapterListener() {
        @Override
        public void OnItemClick(ArrayList<LBingoItem> arrayList) {

            cancelEditTextFocus();
            HidekeyBoard();

            if ( true == m_bShow){
                return;
            }

            if (m_bSwitch){

            }else {
                if (!LApplication.getBingoInfo().checkBingoRange(m_iMin, m_iMax, m_iRange)){
                    showDialog(getString(R.string.warn),
                            String.format(
                                    getString(R.string.bingo_range_error),
                                    m_etSize.getText().toString()));
                    return;
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(LUiMessageDef.BUNDLE_TAG_BINGO_COL, m_iCol);
                bundle.putInt(LUiMessageDef.BUNDLE_TAG_BINGO_MAX, m_iMax);
                bundle.putInt(LUiMessageDef.BUNDLE_TAG_BINGO_MIN, m_iMin);
                bundle.putSerializable(LUiMessageDef.BUNDLE_TAG_BINGO_DATA, m_alData);
                intent.putExtras(bundle);
                intent.setClass(getActivity(), LBingoActivity.class);
                startActivityForResult(intent, LUiMessageDef.INTENT_REQUEST_CODE_BINGO);
                getActivity().overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);

                m_bShow = true;
                m_flContent.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void OnItemSelect(ArrayList<LBingoItem> arrayList) {

        }

        @Override
        public void updateData(ArrayList<LBingoItem> arrayList) {

        }

        @Override
        public void closeActivity() {

        }

        @Override
        public void showToast(String str) {

        }
    };

    TextWatcher m_minTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if ( 0 == s.length() ){
                m_iMin = 0;
                return;
            }

            //前面數字去0
            String str = LApplication.getBingoInfo().replaceZero(s);
            if ( !s.toString().equals(str) ){
                m_etMin.setText(str);
            }
        }
    };

    TextWatcher m_maxTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if ( 0 == s.length() ){
                m_iMax = 0;
                return;
            }

            //前面數字去0
            String str = LApplication.getBingoInfo().replaceZero(s);
            if ( !s.toString().equals(str) ){
                m_etMax.setText(str);
            }
        }
    };

    //監聽最大值按完成鍵的事件
    EditText.OnEditorActionListener m_etActionListener
            = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                cancelEditTextFocus();
                HidekeyBoard();
                return true;
            }
            return false;
        }
    };

    //遊戲開關
    LSwitchButton.SlideListener m_switchListenet = new LSwitchButton.SlideListener() {
        @Override
        public boolean IsOpenSwitch(boolean isOpen) {

            cancelEditTextFocus();
            HidekeyBoard();

            if ( false == m_bSwitch ){
                //未輸入範圍
                if (!LApplication.getBingoInfo().checkBingoRange(m_iMin, m_iMax, m_iRange)){
                    String str = String.format(
                            getString(R.string.bingo_range_error),
                            m_etSize.getText().toString());
                    showDialog(getString(R.string.warn), str);
                    return m_bSwitch;
                }

                //未輸入賓果盤數字
                if (!LApplication.getBingoInfo().checkBingoDataNotEmpty(m_alData)){
                    showDialog(getString(R.string.warn), getString(R.string.bingo_plate_error));
                    return m_bSwitch;
                }

                //未輸入重複數字
                if (!LApplication.getBingoInfo().checkBingoDataNotRepeat(m_alData)){
                    showDialog(getString(R.string.warn), getString(R.string.bingo_plate_repeat_error));
                    return m_bSwitch;
                }
            }

            return isOpen;
        }

        @Override
        public void onChanged(LSwitchButton toggleButton, boolean isOpen) {

            m_bSwitch = isOpen;
            if ( true == isOpen ){
                m_tvMode.setText(R.string.bingo_mode_play);
                m_tvMode.setTextColor(ContextCompat.getColor(getActivity(), R.color.pink_dark2));
            }else {
                m_tvMode.setText(R.string.bingo_mode_input);
                m_tvMode.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_80));
            }
        }
    };

    private static final double         LAYOUT_HEIGHT           = 100;
    private static final double         LAYOUT_MARGIN           = 60;
    private static final double         EDIT_TEXT_WIDTH         = 180;
    private static final double         EDIT_TEXT_WIDTH_BIG     = 300;
    private static final double         EDIT_TEXT_HEIGHT        = 85;
    private static final double         TEXT_SIZE               = 48;
    private static final double         TEXT_MARGIN             = 30;
    private static final double         BUTTON_WIDTH            = 150;
    private static final double         BUTTON_HEIGHT           = 80;
    private static final double	        SWITCH_BUTTON_WIDTH     = 150;
    private static final double	        SWITCH_BUTTON_HEIGHT    = 75;
    private static final double         LINE_HEIGHT             = 1;
    private static final double         POPUPWINDOW_X           = 0;
    private static final double         POPUPWINDOW_Y           = 6;
    private static final double         WEIGHT_TOTAL_WIDTH      = 1020;
    private static final double         WEIGHT_PADDING          = 15;

    private LNaviBarToMainListener      m_onMainListener    = null;

    private LNavigationBar              m_navigationBar     = null;

    private LinearLayout m_llRoot            = null;
    private RelativeLayout m_rlRange           = null;
    private TextView m_tvRange           = null;
    private EditText m_etMin             = null;
    private EditText m_etMax             = null;
    private Button m_btnClear          = null;
    private View m_vLine1            = null;

    private RelativeLayout m_rlSize            = null;
    private TextView m_tvSelectSize      = null;
    private EditText m_etSize            = null;
    private View m_vLine2            = null;

    private RelativeLayout m_rlMode            = null;
    private TextView m_tvCurrMode        = null;
    private TextView m_tvMode            = null;
    private LSwitchButton               m_switchButton      = null;
    private View m_vLine3            = null;

    private RelativeLayout m_rlBingoPlate      = null;
    private TextView m_tvBingo           = null;
    private Button m_btnRandom         = null;

    private FrameLayout m_flContent         = null;

    private GridView m_gridView          = null;
    private LBingoAdapter               m_adapter           = null;

    private int                         m_iGvWidth          = 0;
    private int                         m_iMin              = 0;
    private int                         m_iMax              = 0;
    private int                         m_iCol              = 3;
    private int                         m_iRange            = m_iCol * m_iCol;
    private ArrayList<LBingoItem> m_alData            = new ArrayList<>();
    private ArrayList<String> m_alSize            = new ArrayList<>();
    private LBingoAdapter.BingoType     m_type              = LBingoAdapter.BingoType.CLICK;

    private LSelectBingoSizePopupwindow m_popupWindow       = null;

    //切換模式開關
    private Boolean m_bSwitch           = false;
    private Boolean m_bShow             = false;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_bingo;
    }

    @Override
    protected void initialLayoutComponent(LayoutInflater inflater, View view) {

        m_llRoot        = (LinearLayout) view.findViewById(R.id.ll_bingo_root);
        m_llRoot.setOnTouchListener(this);
        m_navigationBar = (LNavigationBar)view.findViewById(R.id.navigation_bar);

        m_rlRange       = (RelativeLayout)view.findViewById(R.id.rl_range);
        m_tvRange       = (TextView)view.findViewById(R.id.tv_input_range);
        m_etMin         = (EditText)view.findViewById(R.id.et_min);
        m_etMax         = (EditText)view.findViewById(R.id.et_max);
        m_vLine1        = view.findViewById(R.id.v_line_1);
        m_etMin.setOnFocusChangeListener(this);
        m_etMin.addTextChangedListener(m_minTextWatcher);
        m_etMax.setOnFocusChangeListener(this);
        m_etMax.addTextChangedListener(m_maxTextWatcher);
        m_etMax.setImeOptions(EditorInfo.IME_ACTION_DONE);
        m_etMax.setOnEditorActionListener(m_etActionListener);

        m_rlSize        = (RelativeLayout)view.findViewById(R.id.rl_bingo_size);
        m_tvSelectSize  = (TextView)view.findViewById(R.id.tv_select_size);
        m_etSize        = (EditText)view.findViewById(R.id.et_size);
        m_vLine2        = view.findViewById(R.id.v_line_2);
        m_etSize.setOnTouchListener(this);
        m_etSize.setInputType(InputType.TYPE_NULL);//禁止彈出输鍵盤

        m_rlMode        = (RelativeLayout)view.findViewById(R.id.rl_mode);
        m_tvCurrMode    = (TextView)view.findViewById(R.id.tv_current_mode);
        m_tvMode        = (TextView)view.findViewById(R.id.tv_mode);
        m_switchButton  = (LSwitchButton)view.findViewById(R.id.switch_button);
        m_vLine3        = view.findViewById(R.id.v_line_3);
        m_switchButton.setState(m_bSwitch);
        m_switchButton.setSlideListener(m_switchListenet);

        m_rlBingoPlate  = (RelativeLayout)view.findViewById(R.id.rl_bingo_plate);
        m_tvBingo       = (TextView)view.findViewById(R.id.tv_please_input_num);
        m_btnClear      = (Button)view.findViewById(R.id.btn_clear_bingo);
        m_btnClear.setOnClickListener(this);
        m_btnRandom     = (Button)view.findViewById(R.id.btn_random);
        m_btnRandom.setOnClickListener(this);

        View vContent   = m_layoutInflater.inflate(R.layout.view_bingo, null);
        m_flContent     = (FrameLayout)view.findViewById(R.id.fl_content);
        m_flContent.addView(vContent);
        m_gridView      = (GridView)vContent.findViewById(R.id.gv_bingo);

        resetBingoData();

        String[] strArray = getResources().getStringArray(R.array.bingo_size_list);
        Collections.addAll(m_alSize, strArray);
    }

    @Override
    protected void setTextSizeAndLayoutParams(View view, LViewScaleDef vScaleDef) {

        m_navigationBar.getLayoutParams().height
                = vScaleDef.getLayoutHeight(LNavigationBar.NAVIGATION_BAR_HEIGHT);

        LinearLayout.LayoutParams layoutParams
                = (LinearLayout.LayoutParams)m_rlRange.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LAYOUT_HEIGHT);
        layoutParams.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);
        layoutParams.rightMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

        RelativeLayout.LayoutParams params
                = (RelativeLayout.LayoutParams)m_etMin.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(EDIT_TEXT_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(EDIT_TEXT_WIDTH);
        params.leftMargin = vScaleDef.getLayoutWidth(TEXT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_etMax.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(EDIT_TEXT_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(EDIT_TEXT_WIDTH);
        params.leftMargin = vScaleDef.getLayoutWidth(TEXT_MARGIN);

        layoutParams = (LinearLayout.LayoutParams)m_rlSize.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LAYOUT_HEIGHT);
        layoutParams.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);
        layoutParams.rightMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_tvSelectSize.getLayoutParams();
        params.rightMargin = vScaleDef.getLayoutWidth(TEXT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_etSize.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(EDIT_TEXT_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(EDIT_TEXT_WIDTH_BIG);

        layoutParams = (LinearLayout.LayoutParams)m_rlMode.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LAYOUT_HEIGHT);
        layoutParams.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);
        layoutParams.rightMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

        //模式設定
        m_switchButton.getLayoutParams().height = vScaleDef.getLayoutMinUnit(SWITCH_BUTTON_HEIGHT);
        m_switchButton.getLayoutParams().width = vScaleDef.getLayoutMinUnit(SWITCH_BUTTON_WIDTH);

        layoutParams = (LinearLayout.LayoutParams)m_vLine1.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LINE_HEIGHT);

        layoutParams = (LinearLayout.LayoutParams)m_vLine2.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LINE_HEIGHT);

        layoutParams = (LinearLayout.LayoutParams)m_vLine3.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LINE_HEIGHT);

        layoutParams = (LinearLayout.LayoutParams)m_rlBingoPlate.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LAYOUT_HEIGHT);
        layoutParams.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);
        layoutParams.rightMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_btnClear.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(BUTTON_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(BUTTON_WIDTH);

        params = (RelativeLayout.LayoutParams)m_btnRandom.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(BUTTON_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(BUTTON_WIDTH);
        params.rightMargin = vScaleDef.getLayoutWidth(TEXT_MARGIN);

        layoutParams = (LinearLayout.LayoutParams)m_flContent.getLayoutParams();
        layoutParams.setMargins(vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                vScaleDef.getLayoutWidth(WEIGHT_PADDING));
        m_flContent.setPadding(vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                vScaleDef.getLayoutWidth(WEIGHT_PADDING));

        m_iGvWidth = vScaleDef.getLayoutWidth(WEIGHT_TOTAL_WIDTH);
        m_gridView.getLayoutParams().height = m_iGvWidth;
        m_gridView.getLayoutParams().width = m_iGvWidth;

        vScaleDef.setTextSize(TEXT_SIZE, m_tvRange);
        vScaleDef.setTextSize(TEXT_SIZE, m_etMin);
        vScaleDef.setTextSize(TEXT_SIZE, m_etMax);

        vScaleDef.setTextSize(TEXT_SIZE, m_tvSelectSize);
        vScaleDef.setTextSize(TEXT_SIZE, m_etSize);

        vScaleDef.setTextSize(TEXT_SIZE, m_tvCurrMode);
        vScaleDef.setTextSize(TEXT_SIZE, m_tvMode);

        vScaleDef.setTextSize(TEXT_SIZE, m_tvBingo);
        vScaleDef.setTextSize(TEXT_SIZE, m_btnClear);
        vScaleDef.setTextSize(TEXT_SIZE, m_btnRandom);

        resetGridView();
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

    private void cancelEditTextFocus(){
        m_llRoot.setFocusable(true);
        m_llRoot.setFocusableInTouchMode(true);
        m_llRoot.requestFocus();
    }

    //下拉式選擇賓果盤大小
    private void pullDownMenu(View v){

        if (m_bSwitch)
            return;

        cancelEditTextFocus();

        if ( null != m_popupWindow && m_popupWindow.isShowing() )
            return;

        final ArrayList<String> alSize = m_alSize;

        if ( null == m_popupWindow ){
            m_popupWindow = new LSelectBingoSizePopupwindow(
                    getActivity()
                    , ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT
                    , m_alSize
                    , new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int iPosition, long l) {

                    m_etSize.setText(alSize.get(iPosition));
                    int iCol = Integer.valueOf(m_etSize.getText().toString().substring(0,1));

                    if ( m_iCol == iCol )
                        return;

                    m_iCol = iCol;
                    m_iRange = m_iCol * m_iCol;

                    resetBingoData();
                    resetGridView();
                }
            });
        }

        //menu顯示於edittext 下方, 要與輸入框底線靠近一點
        m_popupWindow.showAsDropDown(
                v,
                (int)POPUPWINDOW_X,
                -LViewScaleDef.getInstance(getActivity()).getLayoutHeight(POPUPWINDOW_Y));
    }

    private void resetBingoData(){

        m_etMax.setText("");
        m_etMin.setText("");

        m_alData.clear();
        for ( int i = 0; i < m_iRange; i ++ ){
            m_alData.add(new LBingoItem(i, "", false, false));
        }
    }

    private void resetGridView(){
        m_adapter   = new LBingoAdapter(
                getActivity(),
                m_adapterParameter,
                m_adapterListener);
        m_gridView.setNumColumns(m_iCol);
        m_gridView.setAdapter(m_adapter);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (view.getId()){
            case R.id.et_size:
                //顯示選擇賓果盤大小的選單
                pullDownMenu(view);
                break;
        }

        //使輸入框失去焦點
        cancelEditTextFocus();

        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null != imm
                && null != m_etMin.getWindowToken()
                && null != m_etMax.getWindowToken()) {

            imm.hideSoftInputFromWindow(m_etMin.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(m_etMax.getWindowToken(), 0);
        }

        //此層已處理onTouch，就不再向上傳遞
        return true;
    }

    @Override
    public void onFocusChange(View view, boolean bHasFocus) {

        switch (view.getId()){
            case R.id.et_min:
                if(bHasFocus){
                    m_etMin.setHint(null);
                }else{
                    m_etMin.setHint(R.string.bingo_range_min);

                    //確認是否輸入有效的最小值
                    String strMin = m_etMin.getText().toString();
                    if ( 0 >= strMin.length() )
                        return;

                    int iMin = Integer.valueOf(strMin);
                    if ( LApplication.getBingoInfo().checkMin(iMin, m_iRange) ){
                        //如果有輸入最大值，確認是否比最大值還小，且符合範圍之內
                        if ( 0 < m_etMax.getText().toString().length() ){
                            if ( LApplication.getBingoInfo().checkBingoRange(iMin, m_iMax, m_iRange) ){
                                m_iMin = iMin;
                            }else {
                                //最大值減最小值需大於賓果盤範圍
                                String string = String.format(
                                        getString(R.string.bingo_min_error),
                                        String.valueOf(DEF_MIN),
                                        String.valueOf(m_iMax-(m_iRange-1)));
                                showDialog(getString(R.string.warn), string);
                                m_etMin.setText("");
                            }
                        }else {
                            //還沒輸入最大值，不用比了
                            m_iMin = iMin;
                        }
                    }else {
                        int iMax = m_iMax;
                        if ( 0 >= iMax ){
                            iMax = DEF_MAX;
                        }
                        //無效的最小值
                        String string = String.format(
                                getString(R.string.bingo_min_error),
                                String.valueOf(DEF_MIN),
                                String.valueOf(iMax-(m_iRange-1)));
                        showDialog(getString(R.string.warn), string);
                        m_etMin.setText("");
                    }
                }
                break;
            case R.id.et_max:
                if(bHasFocus){
                    m_etMax.setHint(null);
                }else{
                    m_etMax.setHint(R.string.bingo_range_max);

                    //確認是否輸入有效的最大值
                    String strMax = m_etMax.getText().toString();
                    if ( 0 >= strMax.length() )
                        return;

                    int iMax = Integer.valueOf(strMax);
                    if ( LApplication.getBingoInfo().checkMax(iMax, m_iRange) ){
                        //如果有輸入最大值，確認是否比最大值還小，且符合範圍之內
                        if ( 0 < m_etMin.getText().toString().length() ){
                            if ( LApplication.getBingoInfo().checkBingoRange(
                                    m_iMin, iMax, m_iRange) ){
                                m_iMax = iMax;
                            }else {
                                //最大值減最小值需大於賓果盤範圍
                                String string = String.format(
                                        getString(R.string.bingo_max_error),
                                        String.valueOf(m_iMin+(m_iRange-1)),
                                        String.valueOf(DEF_MAX));
                                showDialog(getString(R.string.warn), string);
                                m_etMax.setText("");
                            }
                        }else {
                            //還沒輸入最小值，不用比了
                            m_iMax = iMax;
                        }
                    }else {
                        int iMin = m_iMin;
                        if ( 0 >= iMin ){
                            iMin = DEF_MIN;
                        }
                        //無效的最大值
                        String string = String.format(
                                getString(R.string.bingo_max_error),
                                String.valueOf(iMin+(m_iRange-1)),
                                String.valueOf(DEF_MAX));
                        showDialog(getString(R.string.warn), string);
                        m_etMax.setText("");
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_random:
                if (m_bSwitch)
                    return;

                cancelEditTextFocus();
                HidekeyBoard();

                if (!LApplication.getBingoInfo().checkBingoRange(m_iMin, m_iMax, m_iRange)){

                    showDialog(getString(R.string.warn),
                            String.format(
                                    getString(R.string.bingo_range_error),
                                    m_etSize.getText().toString()));
                }else {

                    m_alData = LApplication.getBingoInfo().getBingoArray(m_iMin, m_iMax, m_iRange);
                    resetGridView();
                }
                break;
            case R.id.btn_clear_bingo:
                if (m_bSwitch)
                    return;

                cancelEditTextFocus();
                HidekeyBoard();

                resetBingoData();
                resetGridView();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LUiMessageDef.INTENT_REQUEST_CODE_BINGO
                && resultCode == LUiMessageDef.INTENT_RESULT_CODE_BINGO) {

            if (null != data) {
                m_alData = (ArrayList)data.getExtras().getSerializable(
                        LUiMessageDef.BUNDLE_TAG_BINGO_DATA);
            }

            resetGridView();

            m_bShow = false;
            m_flContent.setVisibility(View.VISIBLE);
        }

    }
}
