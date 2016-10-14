package ling.testapp.function.Bingo.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ling.testapp.R;
import ling.testapp.function.Bingo.item.LBingoItem;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.object.LApplication;

/**
 * Created by jlchen on 2016/9/30.
 */

public class LBingoAdapter extends BaseAdapter {

    public interface OnBingoAdapterParameter{
        //取得GridView寬高
        int getWidth();
        //取得最小值
        int getMin();
        //取得最大值
        int getMax();
        //取得列數
        int getCol();
        //取得賓果盤資料
        ArrayList<LBingoItem> getData();
        //當前模式
        BingoType getType();
    }

    public interface OnBingoAdapterListener{
        //點擊模式下 點擊輸入框
        void OnItemClick(ArrayList<LBingoItem> arrayList);
        //遊戲模式下 點擊輸入框
        void OnItemSelect(ArrayList<LBingoItem> arrayList);
        //輸入模式下 更新賓果盤資料
        void updateData(ArrayList<LBingoItem> arrayList);
        //關閉activity
        void closeActivity();
        //顯示錯誤訊息
        void showToast(String str);
    }

    public enum BingoType{
        //輸入模式、遊戲模式、點擊模式
        INPUT, GAME, CLICK
    }

    private static class Holder {
        public EditText etNum;
        public LinearLayout llRoot;
    }

    private static final double WEIGHT_PADDING      = 18;
    private static final double WEIGHT_TEXT_SIZE    = 343;

    private Context m_context       = null;
    private LViewScaleDef       m_viewScaleDef  = null;

    private int                 m_iWidth        = 0;
    private int                 m_iItemWidth    = 0;
    private int                 m_iItemPadding  = 0;
    private double              m_dItemTextSize = 0;
    private int                 m_iMin          = 0;
    private int                 m_iMax          = 0;
    private int                 m_iCol          = 1;
    private BingoType           m_type          = BingoType.CLICK;
    private ArrayList<LBingoItem> m_alData      = null;
    private Map<Integer, EditText> m_map        = new HashMap<>();

    private OnBingoAdapterListener m_listener   = null;

    public LBingoAdapter(Context context,
                         OnBingoAdapterParameter    parameter,
                         OnBingoAdapterListener     listener) {
        this.m_context      = context;
        this.m_viewScaleDef = LViewScaleDef.getInstance(m_context);
        this.m_iWidth       = parameter.getWidth();
        this.m_alData       = parameter.getData();
        this.m_iCol         = parameter.getCol();
        this.m_iMin         = parameter.getMin();
        this.m_iMax         = parameter.getMax();
        this.m_type         = parameter.getType();
        this.m_listener     = listener;

        this.m_iItemWidth   = m_iWidth / m_iCol;
        this.m_iItemPadding = m_viewScaleDef.getLayoutWidth((WEIGHT_PADDING - m_iCol));
        this.m_dItemTextSize= WEIGHT_TEXT_SIZE / m_iCol;
    }

    @Override
    public int getCount() {
        return (null == m_alData) ? 0 : m_alData.size();
    }

    @Override
    public Object getItem(int iPosition) {
        return (null == m_alData) ? null : m_alData.get(iPosition);
    }

    @Override
    public long getItemId(int iPosition) {
        return iPosition;
    }

    @Override
    public View getView(final int iPosition, View convertView, ViewGroup viewGroup) {
        final Holder holder;

        if ( null == convertView ){
            holder = new Holder();

            convertView     = LayoutInflater.from(m_context)
                    .inflate(R.layout.view_bingo_gridview_item, viewGroup, false);

            holder.llRoot   = (LinearLayout)convertView.findViewById(R.id.ll_bingo_item_root);
            holder.etNum    = (EditText)convertView.findViewById(R.id.et_num);

            holder.llRoot.getLayoutParams().height = m_iItemWidth;
            holder.llRoot.getLayoutParams().width = m_iItemWidth;

            holder.llRoot.setPadding(m_iItemPadding, m_iItemPadding, m_iItemPadding, m_iItemPadding);

            if (!TextUtils.isEmpty(m_alData.get(iPosition).m_strNum))
                holder.etNum.setText(m_alData.get(iPosition).m_strNum);

            m_viewScaleDef.setTextSize(m_dItemTextSize, holder.etNum);

            convertView.setTag(holder);
            m_map.put(iPosition, holder.etNum);
        }else {
            holder = (Holder) convertView.getTag();
        }

        switch (m_type){
            //點擊模式
            case CLICK:{
                setClickMode(holder, iPosition);
                break;
            }
            //輸入模式
            case INPUT:{
                setInputMode(holder, iPosition);
                break;
            }
            //遊戲模式
            case GAME:{

                break;
            }
        }

        return convertView;
    }

    private void setClickMode(final Holder holder, final int iPosition){

        holder.etNum.setInputType(InputType.TYPE_NULL);//禁止彈出输鍵盤
        holder.etNum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                m_alData.get(iPosition).m_bFocus = true;

                if ( null != m_listener ){
                    m_listener.OnItemClick(m_alData);
                }
                return false;
            }
        });
    }

    private void setInputMode(final Holder holder, final int iPosition){

        holder.etNum.setTag( m_alData.get(iPosition).m_iIndex );

        //依上一層的點擊點設定Focus
        if ( true == m_alData.get(iPosition).m_bFocus ){

            holder.etNum.setFocusable(true);
            holder.etNum.setFocusableInTouchMode(true);
            holder.etNum.requestFocus();

            m_alData.get(iPosition).m_bFocus = false;

            if ( null != m_listener ){
                m_listener.updateData(m_alData);
            }
        }

        if ( iPosition != getCount()-1 ){
            //讓gridview內的editText可以由左至右輸入
            holder.etNum.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if ( false == checkNum(holder.etNum)){
                        return true;
                    }

                    if ( null != m_map.get(iPosition + 1) ) {
                        m_map.get(iPosition + 1).requestFocus();
                        return true;
                    }
                    return false;
                }
            });
        }else {
            //最後一個editText 鍵盤顯示done
            holder.etNum.setImeOptions(EditorInfo.IME_ACTION_DONE);
            //按下done結束activity
            holder.etNum.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                        if ( false == checkNum(holder.etNum)){
                            return true;
                        }

                        if ( null!= m_listener ){
                            m_listener.closeActivity();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        //監聽輸入內容，數字前面要去0
        holder.etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (0 >= s.length()){
                    return;
                }

                String str = LApplication.getBingoInfo().replaceZero(s);

                if ( !s.toString().equals(str) ){
                    holder.etNum.setText(str);
                }
            }
        });

        //該輸入框Focus消失時，要記錄最後輸入的資料
        holder.etNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus){
                    return;
                }

                checkNum(v);
            }
        });
    }

    private boolean checkNum(View v){

        String str = ((EditText)v).getText().toString();
        int iIndex = Integer.valueOf(v.getTag().toString());

        if ( !m_alData.get(iIndex).m_strNum.equals(str) ){
            m_alData.get(iIndex).m_strNum = str;

            if ( null!= m_listener ){
                m_listener.updateData(m_alData);
            }
        }else {
            //和上次輸入的一樣
            return true;
        }

        //沒輸入
        if ( TextUtils.isEmpty(str) ){
            return true;
        }

        //有輸入
        int iNum = Integer.valueOf(str);

        //檢查是否符合範圍
        if ( !LApplication.getBingoInfo().checkNumRange(iNum, m_iMin, m_iMax) ){
            //不符合範圍，清除資料
            ((EditText) v).setText("");

            m_alData.get(iIndex).m_strNum = "";

            if ( null!= m_listener ){
                m_listener.updateData(m_alData);
                String strMsg = String.format(
                        m_context.getString(R.string.bingo_plate_num_error),
                        String.valueOf(m_iMin),
                        String.valueOf(m_iMax));
                m_listener.showToast(strMsg);
            }
            return false;
        }

        //檢查數字是否重複
        if ( !LApplication.getBingoInfo().checkBingoDataNotRepeat(str, iIndex, m_alData) ){
            //不符合條件，清除資料
            ((EditText) v).setText("");

            m_alData.get(iIndex).m_strNum = "";

            if ( null!= m_listener ){
                m_listener.updateData(m_alData);
                m_listener.showToast(
                        m_context.getString(R.string.bingo_plate_repeat_error));
            }
            return false;
        }

        return true;
    }
}
