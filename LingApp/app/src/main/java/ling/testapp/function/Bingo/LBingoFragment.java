package ling.testapp.function.Bingo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
import ling.testapp.ui.view.LSwitchButton;

import static ling.testapp.function.Bingo.object.LBingoInfo.DEF_MAX;
import static ling.testapp.function.Bingo.object.LBingoInfo.DEF_MIN;

/**
 * Created by jlchen on 2016/9/23.
 * 賓果遊戲
 *
 * *待完成功能*
 * 1. 單一輸入框清除內容功能
 * 2. 當賓果盤數字驗證錯誤時, 數字顏色要變紅
 * 3. 最小值/最大值, 若驗證錯誤則show出訊息(非show dialog)
 * 4. 改變賓果盤大小時, 不清除最小值/最大值
 * 5. 自動產生最小值/最大值
 * 6. 儲存上次玩的紀錄
 * 7. 題目說明
 */

public class LBingoFragment extends LBaseFragment implements
        View.OnTouchListener,
        View.OnFocusChangeListener,
        View.OnClickListener{

    //傳給引導欄一些必須的初始值
    private LNavigationBar.OnParameter  m_navigationParameter
            = new LNavigationBar.OnParameter() {
        @Override
        public String GetTitle() {
            return getActivity().getResources().getString(R.string.title_bingo);
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

    //傳給賓果盤adapter一些必須的初始值
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
            //點擊模式, 點擊賓果盤

            //1.取消輸入框focus
            //2.隱藏鍵盤
            cancelEditTextFocus();
            HidekeyBoard();

            //3.已經顯示BingoActivity時或是switch按鈕還在執行動畫時, 不做事
            if (true == m_bShow || false == m_switchButton.getSlideable()){
                return;
            }

            //4.非遊戲狀態, 才可以進行編輯賓果盤
            if (false == m_bSwitch){
                //5.如果未設定最小/最大值, 無法進行編輯賓果盤
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
        public void OnItemSelect(View v, ArrayList<LBingoItem> arrayList, int iPosition) {
            //遊戲模式, 點擊賓果盤

            //1.更新賓果盤資料
            m_alData = arrayList;

            //2.改變選取狀態, 需更換背景色 & 更新未選取資料
            if (true == arrayList.get(iPosition).m_bSelect){
                v.setBackgroundResource(R.drawable.bg_bingo_item_select);
                m_alUnSelect.remove(arrayList.get(iPosition));
            }else {
                v.setBackgroundResource(R.drawable.bg_bingo_item);
                m_alUnSelect.add(arrayList.get(iPosition));
            }

            //3.重新計算連線數
            setBingoLine();
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

    //監聽最小值內容改變事件
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

    //監聽最大值內容改變事件
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
    LSwitchButton.SlideListener m_switchListener = new LSwitchButton.SlideListener() {
        @Override
        public boolean IsOpenSwitch(boolean isOpen) {

            cancelEditTextFocus();
            HidekeyBoard();

            if (false == m_bSwitch){
                //未輸入範圍
                if (!LApplication.getBingoInfo().checkBingoRange(m_iMin, m_iMax, m_iRange)){
                    String str = String.format(
                            getString(R.string.bingo_range_error),
                            m_etSize.getText().toString());
                    showDialog(getString(R.string.warn), str);

                    m_switchButton.setSlideable(true);
                    return m_bSwitch;
                }

                //未輸入賓果盤數字
                if (!LApplication.getBingoInfo().checkBingoDataNotEmpty(m_alData)){
                    showDialog(getString(R.string.warn), getString(R.string.bingo_plate_error));

                    m_switchButton.setSlideable(true);
                    return m_bSwitch;
                }

                //輸入賓果盤數字超出範圍
                for (int i = 0; i < m_alData.size(); i ++){
                    if (!LApplication.getBingoInfo().checkNumRange(
                            Integer.valueOf(m_alData.get(i).m_strNum),
                            m_iMin,
                            m_iMax)){


                        showDialog(getString(R.string.warn), getString(R.string.bingo_plate_range_error));

                        m_switchButton.setSlideable(true);
                        return m_bSwitch;
                    }
                }

                //輸入重複數字
                if (!LApplication.getBingoInfo().checkBingoDataNotRepeat(m_alData)){
                    showDialog(getString(R.string.warn), getString(R.string.bingo_plate_repeat_error));

                    m_switchButton.setSlideable(true);
                    return m_bSwitch;
                }
            }

            return isOpen;
        }

        @Override
        public void onChanged(LSwitchButton toggleButton, boolean isOpen) {
            //和上次狀態一樣，不用更新畫面
            if (m_bSwitch == isOpen){
                m_switchButton.setSlideable(true);
                return;
            }

            //切換模式，需更新當前畫面
            setMode(isOpen);
        }
    };

    //隱藏輸入模式的一些view, 僅保留遊戲模式所需的view, 加入動畫來提升使用者體驗
    private Animation.AnimationListener m_hideTraAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            TranslateAnimation anim = new TranslateAnimation(0,0,0,0);
            m_flContent.startAnimation(anim);
            m_rlMode.startAnimation(anim);

            m_rlRange.setVisibility(View.GONE);
            m_rlSize.setVisibility(View.GONE);
            m_rlBingoPlate.setVisibility(View.GONE);
            m_rlLine.setVisibility(View.VISIBLE);
            m_btnRestart.setVisibility(View.INVISIBLE);

            m_type = LBingoAdapter.BingoType.GAME;
            //重置已選數字
            resetSelectBingoData();
            resetGridView();

            m_bSwitch = true;
            m_switchButton.setSlideable(true);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener m_hideAlpAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            TranslateAnimation hideAnim = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, //fromX
                    Animation.RELATIVE_TO_SELF, 0.0f, //toX
                    Animation.RELATIVE_TO_SELF, 0.0f, //fromY
                    Animation.RELATIVE_TO_SELF, -2.0f);//toY
            hideAnim.setDuration(200);
            hideAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            m_rlMode.startAnimation(hideAnim);

            //layout隱藏/顯示的動畫
            hideAnim = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0.0f, //fromX
                    Animation.RELATIVE_TO_SELF, 0.0f, //toX
                    Animation.RELATIVE_TO_SELF, 0.0f, //fromY
                    Animation.RELATIVE_TO_SELF, -0.26f);//toY
            hideAnim.setDuration(200);
            hideAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            hideAnim.setAnimationListener(m_hideTraAnimListener);
            m_flContent.startAnimation(hideAnim);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    //隱藏遊戲模式的一些view, 僅保留輸入模式所需的view, 加入動畫來提升使用者體驗
    private Animation.AnimationListener m_showTraAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            TranslateAnimation anim = new TranslateAnimation(0,0,0,0);
            m_flContent.startAnimation(anim);
            m_rlMode.startAnimation(anim);

            AlphaAnimation showAnim = new AlphaAnimation(0.0f, 1.0f);
            showAnim.setDuration(200);
            showAnim.setAnimationListener(m_showAlpAnimListener);

            m_rlRange.startAnimation(showAnim);
            m_rlSize.startAnimation(showAnim);
            m_rlBingoPlate.startAnimation(showAnim);

            m_rlRange.setVisibility(View.VISIBLE);
            m_rlSize.setVisibility(View.VISIBLE);
            m_rlBingoPlate.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener m_showAlpAnimListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            m_type = LBingoAdapter.BingoType.CLICK;
            resetGridView();

            m_bSwitch = false;
            m_switchButton.setSlideable(true);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    //自適應所需的初始值
    private static final double         LAYOUT_HEIGHT           = 100;
    private static final double         LAYOUT_MARGIN           = 60;
    private static final double         EDIT_TEXT_WIDTH         = 180;
    private static final double         EDIT_TEXT_WIDTH_BIG     = 300;
    private static final double         EDIT_TEXT_HEIGHT        = 85;
    private static final double         TEXT_SIZE               = 48;
    private static final double         TEXT_MARGIN             = 30;
    private static final double         BUTTON_WIDTH            = 150;
    private static final double         BUTTON_HEIGHT           = 80;
    private static final double	        SWITCH_BUTTON_WIDTH     = 130;
    private static final double	        SWITCH_BUTTON_HEIGHT    = 75;
    private static final double         LINE_HEIGHT             = 1;
    private static final double         POPUPWINDOW_X           = 0;
    private static final double         POPUPWINDOW_Y           = 6;
    private static final double         WEIGHT_TOTAL_WIDTH      = 1020;
    private static final double         WEIGHT_PADDING          = 15;

    private LNaviBarToMainListener      m_onMainListener    = null;

    private LNavigationBar              m_navigationBar     = null;

    private LinearLayout                m_llRoot            = null;
    private LinearLayout                m_llContent         = null;
    private RelativeLayout              m_rlRange           = null;
    private TextView                    m_tvRange           = null;
    private TextView                    m_tvTilde           = null;
    private EditText                    m_etMin             = null;
    private EditText                    m_etMax             = null;
    private Button                      m_btnClear          = null;
    private View                        m_vLine1            = null;

    private RelativeLayout              m_rlSize            = null;
    private TextView                    m_tvSelectSize      = null;
    private EditText                    m_etSize            = null;
    private View                        m_vLine2            = null;

    private RelativeLayout              m_rlMode            = null;
    private TextView                    m_tvCurrMode        = null;
    private TextView                    m_tvMode            = null;
    private LSwitchButton               m_switchButton      = null;
    private View                        m_vLine3            = null;

    private RelativeLayout              m_rlBingoPlate      = null;
    private TextView                    m_tvBingo           = null;
    private Button                      m_btnRandom         = null;

    private RelativeLayout              m_rlLine            = null;
    private TextView                    m_tvLine            = null;
    private Button                      m_btnRestart        = null;
    private Button                      m_btnSelect         = null;

    private FrameLayout                 m_flContent         = null;

    private GridView                    m_gridView          = null;
    private LBingoAdapter               m_adapter           = null;

    private int                         m_iGvWidth          = 0;
    private int                         m_iMin              = 0;
    private int                         m_iMax              = 0;
    private int                         m_iCol              = 3;
    private int                         m_iRange            = m_iCol * m_iCol;
    private ArrayList<LBingoItem>       m_alData            = new ArrayList<>();
    private ArrayList<String>           m_alSize            = new ArrayList<>();
    private ArrayList<LBingoItem>       m_alUnSelect = new ArrayList<>();
    private LBingoAdapter.BingoType     m_type              = LBingoAdapter.BingoType.CLICK;

    private LSelectBingoSizePopupwindow m_popupWindow       = null;

    //切換模式開關
    private Boolean                     m_bSwitch           = false;
    private Boolean                     m_bShow             = false;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_bingo;
    }

    @Override
    protected void initialLayoutComponent(LayoutInflater inflater, View view) {

        m_llRoot        = (LinearLayout) view.findViewById(R.id.ll_bingo_root);
        m_llContent     = (LinearLayout) view.findViewById(R.id.ll_bingo_content);
        m_llContent.setOnTouchListener(this);
        m_navigationBar = (LNavigationBar)view.findViewById(R.id.navigation_bar);

        m_rlRange       = (RelativeLayout)view.findViewById(R.id.rl_range);
        m_tvRange       = (TextView)view.findViewById(R.id.tv_input_range);
        m_tvTilde       = (TextView)view.findViewById(R.id.tv_tilde);
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
        m_switchButton.setSlideListener(m_switchListener);

        m_rlBingoPlate  = (RelativeLayout)view.findViewById(R.id.rl_bingo_plate);
        m_tvBingo       = (TextView)view.findViewById(R.id.tv_please_input_num);
        m_btnClear      = (Button)view.findViewById(R.id.btn_clear_bingo);
        m_btnClear.setOnClickListener(this);
        m_btnRandom     = (Button)view.findViewById(R.id.btn_random);
        m_btnRandom.setOnClickListener(this);

        m_rlLine        = (RelativeLayout)view.findViewById(R.id.rl_line);
        m_tvLine        = (TextView)view.findViewById(R.id.tv_line);
        m_btnRestart    = (Button)view.findViewById(R.id.btn_restart);
        m_btnRestart.setOnClickListener(this);
        m_btnSelect     = (Button)view.findViewById(R.id.btn_select);
        m_btnSelect.setOnClickListener(this);

        View vContent   = m_layoutInflater.inflate(R.layout.view_bingo, null);
        m_flContent     = (FrameLayout)view.findViewById(R.id.fl_content);
        m_flContent.addView(vContent);
        m_gridView      = (GridView)vContent.findViewById(R.id.gv_bingo);

        resetBingoData();

        //賓果盤size選單字串
        String[] strArray = getResources().getStringArray(R.array.bingo_size_list);
        Collections.addAll(m_alSize, strArray);
    }

    @Override
    protected void setTextSizeAndLayoutParams(View view, LViewScaleDef vScaleDef) {

        m_navigationBar.getLayoutParams().height
                = vScaleDef.getLayoutHeight(LNavigationBar.NAVIGATION_BAR_HEIGHT);

        LinearLayout.LayoutParams layoutParams
                = (LinearLayout.LayoutParams)m_rlRange.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LAYOUT_HEIGHT+LINE_HEIGHT);

        RelativeLayout.LayoutParams
        params = (RelativeLayout.LayoutParams)m_tvRange.getLayoutParams();
        params.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

        params = (RelativeLayout.LayoutParams) m_tvTilde.getLayoutParams();
        params.leftMargin = vScaleDef.getLayoutWidth(TEXT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_etMin.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(EDIT_TEXT_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(EDIT_TEXT_WIDTH);
        params.leftMargin = vScaleDef.getLayoutWidth(TEXT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_etMax.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(EDIT_TEXT_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(EDIT_TEXT_WIDTH);
        params.leftMargin = vScaleDef.getLayoutWidth(TEXT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_vLine1.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(LINE_HEIGHT);

        layoutParams = (LinearLayout.LayoutParams)m_rlSize.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LAYOUT_HEIGHT+LINE_HEIGHT);

        params = (RelativeLayout.LayoutParams)m_tvSelectSize.getLayoutParams();
        params.rightMargin = vScaleDef.getLayoutWidth(TEXT_MARGIN);
        params.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_etSize.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(EDIT_TEXT_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(EDIT_TEXT_WIDTH_BIG);

        params = (RelativeLayout.LayoutParams)m_vLine2.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(LINE_HEIGHT);

        layoutParams = (LinearLayout.LayoutParams)m_rlMode.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LAYOUT_HEIGHT);

        params = (RelativeLayout.LayoutParams)m_tvCurrMode.getLayoutParams();
        params.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_switchButton.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(SWITCH_BUTTON_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(SWITCH_BUTTON_WIDTH);
        params.rightMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

        layoutParams = (LinearLayout.LayoutParams)m_rlBingoPlate.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LAYOUT_HEIGHT+LINE_HEIGHT);

        params = (RelativeLayout.LayoutParams)m_tvBingo.getLayoutParams();
        params.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_vLine3.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(LINE_HEIGHT);

        params = (RelativeLayout.LayoutParams)m_btnClear.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(BUTTON_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(BUTTON_WIDTH);
        params.rightMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_btnRandom.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(BUTTON_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(BUTTON_WIDTH);
        params.rightMargin = vScaleDef.getLayoutWidth(TEXT_MARGIN);

        layoutParams = (LinearLayout.LayoutParams)m_rlLine.getLayoutParams();
        layoutParams.height = vScaleDef.getLayoutHeight(LAYOUT_HEIGHT+LINE_HEIGHT);

        params = (RelativeLayout.LayoutParams)m_tvLine.getLayoutParams();
        params.leftMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_btnRestart.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(BUTTON_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(BUTTON_WIDTH);
        params.rightMargin = vScaleDef.getLayoutWidth(TEXT_MARGIN);

        params = (RelativeLayout.LayoutParams)m_btnSelect.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(BUTTON_HEIGHT);
        params.width = vScaleDef.getLayoutWidth(BUTTON_WIDTH);
        params.rightMargin = vScaleDef.getLayoutWidth(LAYOUT_MARGIN);

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
        vScaleDef.setTextSize(TEXT_SIZE, m_tvTilde);
        vScaleDef.setTextSize(TEXT_SIZE, m_etMin);
        vScaleDef.setTextSize(TEXT_SIZE, m_etMax);

        vScaleDef.setTextSize(TEXT_SIZE, m_tvSelectSize);
        vScaleDef.setTextSize(TEXT_SIZE, m_etSize);

        vScaleDef.setTextSize(TEXT_SIZE, m_tvCurrMode);
        vScaleDef.setTextSize(TEXT_SIZE, m_tvMode);

        vScaleDef.setTextSize(TEXT_SIZE, m_tvBingo);
        vScaleDef.setTextSize(TEXT_SIZE, m_btnClear);
        vScaleDef.setTextSize(TEXT_SIZE, m_btnRandom);

        vScaleDef.setTextSize(TEXT_SIZE, m_tvLine);
        vScaleDef.setTextSize(TEXT_SIZE, m_btnRestart);
        vScaleDef.setTextSize(TEXT_SIZE, m_btnSelect);

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

        if (true == m_bSwitch || false == m_switchButton.getSlideable())
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

    //size改變或清空時, 重設賓果盤data
    private void resetBingoData(){

        m_etMax.setText("");
        m_etMin.setText("");

        m_alData.clear();
        for ( int i = 0; i < m_iRange; i ++ ){
            m_alData.add(new LBingoItem(i, ""));
        }
    }

    //重玩或開始新遊戲時, 重設賓果盤資料, 統一為未選狀態
    private void resetSelectBingoData(){
        for ( int i = 0; i < m_alData.size(); i ++ ){
            m_alData.get(i).m_bSelect = false;
        }

        m_alUnSelect.clear();
        for (int i = 0; i < m_alData.size(); i ++){
            m_alUnSelect.add(m_alData.get(i));
        }

        //連線數歸0
        m_tvLine.setText("");
        m_btnRestart.setVisibility(View.INVISIBLE);
    }

    //當賓果盤內容/賓果盤大小...等資料改變時, 需重設賓果盤view
    private void resetGridView(){
        m_adapter   = new LBingoAdapter(
                getActivity(),
                m_adapterParameter,
                m_adapterListener);
        m_gridView.setNumColumns(m_iCol);
        m_gridView.setAdapter(m_adapter);
    }

    //切換模式
    private void setMode(final boolean bSwitch){
        HidekeyBoard();

        //layout隱藏/顯示的動畫
        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if (bSwitch){
                    //切換為遊戲模式
                    AlphaAnimation hideAnim = new AlphaAnimation(1.0f, 0.0f);
                    hideAnim.setDuration(200);
                    hideAnim.setAnimationListener(m_hideAlpAnimListener);
                    m_rlRange.startAnimation(hideAnim);
                    m_rlSize.startAnimation(hideAnim);
                    m_rlBingoPlate.startAnimation(hideAnim);

                    m_tvMode.setText(R.string.bingo_mode_play);
                    m_tvMode.setTextColor(ContextCompat.getColor(
                            getActivity(),
                            R.color.pink_dark2));

                    m_rlRange.setVisibility(View.INVISIBLE);
                    m_rlSize.setVisibility(View.INVISIBLE);
                    m_rlBingoPlate.setVisibility(View.INVISIBLE);

                }else {
                    //切換為輸入模式
                    TranslateAnimation hideAnim = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f, //fromX
                            Animation.RELATIVE_TO_SELF, 0.0f, //toX
                            Animation.RELATIVE_TO_SELF, 0.0f, //fromY
                            Animation.RELATIVE_TO_SELF, 2.0f);//toY
                    hideAnim.setDuration(200);
                    hideAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                    m_rlMode.startAnimation(hideAnim);

                    hideAnim = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0.0f, //fromX
                            Animation.RELATIVE_TO_SELF, 0.0f, //toX
                            Animation.RELATIVE_TO_SELF, 0.0f, //fromY
                            Animation.RELATIVE_TO_SELF, 0.26f);//toY
                    hideAnim.setDuration(200);
                    hideAnim.setInterpolator(new AccelerateDecelerateInterpolator());
                    hideAnim.setAnimationListener(m_showTraAnimListener);
                    m_flContent.startAnimation(hideAnim);

                    m_tvMode.setText(R.string.bingo_mode_input);
                    m_tvMode.setTextColor(ContextCompat.getColor(
                            getActivity(),
                            R.color.black_80));

                    m_btnSelect.setVisibility(View.VISIBLE);
                    m_rlLine.setVisibility(View.GONE);
                }
            }
        });
    }

    //顯示賓果盤連線數
    private void setBingoLine(){

        int iLine = LApplication.getBingoInfo().getBingoLine(m_alData, m_iCol);

        if ( 0 != iLine ){
            String strLine = String.format(
                    getActivity().getString(R.string.bingo_line),
                    String.valueOf(iLine));
            m_tvLine.setText(strLine);
        }else {
            m_tvLine.setText("");
        }

        if (m_alData.size() == m_alUnSelect.size()){
            m_btnRestart.setVisibility(View.INVISIBLE);
        }else {
            m_btnRestart.setVisibility(View.VISIBLE);
        }

        if (0 == m_alUnSelect.size()){
            m_btnSelect.setVisibility(View.INVISIBLE);
        }else {
            m_btnSelect.setVisibility(View.VISIBLE);
        }
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
                //輸入模式-賓果盤填入亂數
                if (true == m_bSwitch || false == m_switchButton.getSlideable())
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
                //輸入模式-清空賓果盤/最大/最小值
                if (true == m_bSwitch || false == m_switchButton.getSlideable())
                    return;

                cancelEditTextFocus();
                HidekeyBoard();

                resetBingoData();
                resetGridView();
                break;
            case R.id.btn_restart:
                //遊戲模式-重玩,清除所有已選item
                resetSelectBingoData();
                resetGridView();
                break;
            case R.id.btn_select:
                //遊戲模式-隨選,亂數選擇一個尚未被選過的item
                int iRandom = (int)(Math.random() * m_alUnSelect.size());
                int iIndex = m_alUnSelect.get(iRandom).m_iIndex;

                m_alData.get(iIndex).m_bSelect = true;

                m_alUnSelect.remove(m_alData.get(iIndex));

                resetGridView();
                setBingoLine();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        //BingoActivity回傳編輯結果
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
