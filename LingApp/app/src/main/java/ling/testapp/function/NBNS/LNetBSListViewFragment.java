package ling.testapp.function.NBNS;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseFragment;
import ling.testapp.function.NBNS.item.LNetBSItem;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.view.LShadowListView;

/**
 * Created by jlchen on 2016/10/21.
 */

public class LNetBSListViewFragment extends LBaseFragment{

    private static final String     TAG         = "LNetBSListViewFragment";

    private LShadowListView         m_shadow    = null;
    private ListView                m_lv        = null;
    private TextView                m_tvMsg     = null;
    private LNetBSListViewAdapter   m_adapter   = null;

    private LinearLayout            m_lLayout   = null;
    private TextView                m_tvDate    = null;
    private TextView                m_tvQfii    = null;
    private TextView                m_tvBrk     = null;
    private TextView                m_tvIt      = null;
    private TextView                m_tvTotal   = null;
    private View                    m_vLine     = null;

    private int                     m_iTotalW   = 0;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_nbns_lv;
    }

    @Override
    protected void initialLayoutComponent(LayoutInflater inflater, View view) {
        Log.d(TAG, "initialLayoutComponent");

        FrameLayout fLayout = (FrameLayout)view.findViewById(R.id.fl_header);
        View        vHeader = m_layoutInflater.inflate(R.layout.view_nbs_item, null);

        fLayout.addView(vHeader);

        m_lLayout   = (LinearLayout)vHeader.findViewById(R.id.ll_nbns_item);
        m_tvDate    = (TextView)vHeader.findViewById(R.id.tv_date);
        m_tvQfii    = (TextView)vHeader.findViewById(R.id.tv_qfii);
        m_tvBrk     = (TextView)vHeader.findViewById(R.id.tv_brk);
        m_tvIt      = (TextView)vHeader.findViewById(R.id.tv_it);
        m_tvTotal   = (TextView)vHeader.findViewById(R.id.tv_total);
        m_vLine     = vHeader.findViewById(R.id.v_line);

        m_shadow    = (LShadowListView)view.findViewById(R.id.lv);
        m_lv        = m_shadow.getListView();
        m_tvMsg     = (TextView)view.findViewById(R.id.tv_msg);
    }

    @Override
    protected void setTextSizeAndLayoutParams(View view, LViewScaleDef vScaleDef) {
        vScaleDef.setTextSize(48, m_tvMsg);
    }

    @Override
    protected void setOnParameterAndListener(View view) {

    }

    @Override
    protected void registerFragment(FragmentManager fragmentManager) {

    }

    @Override
    protected void onLanguageChangeUpdateUI() {

    }

    public void setTotalWidth(int width){
        Log.d(TAG, "setTotalWidth");

        LViewScaleDef vScaleDef = LViewScaleDef.getInstance(getActivity());

        if ( 0 < width ){
            m_iTotalW = width;
        }else {
            m_iTotalW = vScaleDef.getLayoutWidth(1020);
        }

        int iPadding = vScaleDef.getLayoutMinUnit(30);
        int iWidth = (m_iTotalW - iPadding) / 5;
        float fTextSize = iWidth * 0.3f;

        m_lLayout.setPadding(0, 0, iPadding, 0);
        m_lLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lv_head_gray));

        m_tvDate.getLayoutParams().width = iWidth;
        m_tvQfii.getLayoutParams().width = iWidth;
        m_tvBrk.getLayoutParams().width = iWidth;
        m_tvIt.getLayoutParams().width = iWidth;
        m_tvTotal.getLayoutParams().width = iWidth;

        m_tvDate.setTextColor(Color.BLACK);
        m_tvQfii.setTextColor(Color.BLACK);
        m_tvBrk.setTextColor(Color.BLACK);
        m_tvIt.setTextColor(Color.BLACK);
        m_tvTotal.setTextColor(Color.BLACK);

        m_tvDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTextSize);
        m_tvQfii.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTextSize);
        m_tvBrk.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTextSize);
        m_tvIt.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTextSize);
        m_tvTotal.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTextSize);

        m_vLine.getLayoutParams().height = vScaleDef.getLayoutHeight(1);
    }

    public void setNetBSData(ArrayList<LNetBSItem> arrayList){
        Log.d(TAG, "setNetBSData");

        m_adapter = new LNetBSListViewAdapter(getActivity(), arrayList);
        m_lv.setAdapter(m_adapter);

        if ( null == arrayList || 0 >= arrayList.size() ){
            m_tvMsg.setText(R.string.no_data);
            setMsgVisibility(View.VISIBLE);
        }else {
            setMsgVisibility(View.GONE);
        }
    }

    public void setErrorMsg(String strMsg){
        Log.d(TAG, "setErrorMsg");

        m_tvMsg.setText(strMsg);
        setMsgVisibility(View.VISIBLE);
    }

    public void setMsgVisibility(int visibility){
        Log.d(TAG, "setMsgVisibility");

        if ( View.VISIBLE == visibility ){
            m_shadow.setVisibility(View.INVISIBLE);
        }else {
            m_shadow.setVisibility(View.VISIBLE);
        }

        m_tvMsg.setVisibility(visibility);
    }

    class ItemHolder{

        LinearLayout    lLayout;
        TextView        tvDate;
        TextView        tvQfii;
        TextView        tvBrk;
        TextView        tvIt;
        TextView        tvTotal;
        View            vLine;
    }

    class LNetBSListViewAdapter extends BaseAdapter{

        private Context                 m_context   = null;
        private ArrayList<LNetBSItem>   m_alData    = new ArrayList<>();

        LNetBSListViewAdapter(Context context, ArrayList<LNetBSItem> arrayList){
            m_context   = context;
            m_alData    = arrayList;
        }

        @Override
        public int getCount() {
            return m_alData == null ? 0 : m_alData.size();
        }

        @Override
        public Object getItem(int position) {
            return m_alData == null ? null : m_alData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ItemHolder itemHolder = null;

            if ( null == convertView ){
                convertView = LayoutInflater.from(m_context).inflate( R.layout.view_nbs_item, null);

                itemHolder = new ItemHolder();
                itemHolder.lLayout  = (LinearLayout) convertView.findViewById(R.id.ll_nbns_item);
                itemHolder.tvDate   = (TextView) convertView.findViewById(R.id.tv_date);
                itemHolder.tvQfii   = (TextView) convertView.findViewById(R.id.tv_qfii);
                itemHolder.tvBrk    = (TextView) convertView.findViewById(R.id.tv_brk);
                itemHolder.tvIt     = (TextView) convertView.findViewById(R.id.tv_it);
                itemHolder.tvTotal  = (TextView) convertView.findViewById(R.id.tv_total);
                itemHolder.vLine    = convertView.findViewById(R.id.v_line);

                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder)convertView.getTag();
            }

            LViewScaleDef viewScaleDef = LViewScaleDef.getInstance(m_context);

            int iPadding = viewScaleDef.getLayoutMinUnit(30);
            int iWidth = (m_iTotalW - iPadding) / 5;
            float fTextSize = iWidth * 0.25f;

            itemHolder.lLayout.setPadding(0, 0, iPadding, 0);
            if (position == 0){
                itemHolder.lLayout.setBackgroundColor(Color.BLACK);
            }else {
                if ((position+1)%2 == 0){
                    itemHolder.lLayout.setBackgroundColor(ContextCompat.getColor(m_context, R.color.lv_even_gray));
                }else {
                    itemHolder.lLayout.setBackgroundColor(ContextCompat.getColor(m_context, R.color.lv_odd_gray));
                }
            }

            itemHolder.vLine.getLayoutParams().height = viewScaleDef.getLayoutHeight(3);

            itemHolder.tvDate.getLayoutParams().width = iWidth;
            itemHolder.tvQfii.getLayoutParams().width = iWidth;
            itemHolder.tvBrk.getLayoutParams().width = iWidth;
            itemHolder.tvIt.getLayoutParams().width = iWidth;
            itemHolder.tvTotal.getLayoutParams().width = iWidth;

            itemHolder.tvDate.setText(m_alData.get(position).m_strSimpleDate);
            itemHolder.tvTotal.setText(m_alData.get(position).m_strTotal);
            itemHolder.tvQfii.setText(m_alData.get(position).m_strQfii);
            itemHolder.tvBrk.setText(m_alData.get(position).m_strBrk);
            itemHolder.tvIt.setText(m_alData.get(position).m_strIt);

            itemHolder.tvDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTextSize);
            itemHolder.tvQfii.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTextSize);
            itemHolder.tvBrk.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTextSize);
            itemHolder.tvIt.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTextSize);
            itemHolder.tvTotal.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTextSize);

            itemHolder.tvDate.setTextColor(Color.WHITE);
            setTextViewColor(itemHolder.tvQfii,     m_alData.get(position).m_dQfii);
            setTextViewColor(itemHolder.tvBrk,      m_alData.get(position).m_dBrk);
            setTextViewColor(itemHolder.tvIt,       m_alData.get(position).m_dIt);
            setTextViewColor(itemHolder.tvTotal,    m_alData.get(position).m_dTotal);

            return convertView;
        }

        private void setTextViewColor(TextView tv, double dNum){
            if ( 0 > dNum ){
                //負數
                tv.setTextColor(ContextCompat.getColor(m_context, R.color.lv_negative_green));
            }else {
                tv.setTextColor(ContextCompat.getColor(m_context, R.color.lv_positive_red));
            }
        }
    }
}
