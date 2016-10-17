package ling.testapp.function.Bingo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ling.testapp.R;
import ling.testapp.ui.define.LViewScaleDef;

/**
 * Created by jlchen on 2016/9/30.
 */

public class LSelectBingoSizeAdapter extends BaseAdapter {

    private static class Holder {
        public TextView tvText;
        public View vDiv;
    }

    private Context m_context         = null;
    private ArrayList<String> m_alAllData       = null;

    public LSelectBingoSizeAdapter(Context context,
                          ArrayList<String> alAllData) {
        this.m_alAllData = alAllData;
        this.m_context = context;
    }

    @Override
    public int getCount() {
        if ( null != m_alAllData ) {
            return m_alAllData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int iPosition) {
        if ( null != m_alAllData ) {
            return m_alAllData.get(iPosition);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int iPosition) {
        return iPosition;
    }

    @Override
    public View getView(int iPosition, View convertView, ViewGroup viewGroup) {
        Holder holder;
        if (convertView == null) {
            holder          = new Holder();
            convertView     = LayoutInflater.from(m_context)
                    .inflate(R.layout.view_select_size_list_item, viewGroup, false);
            holder.tvText   = (TextView)convertView.findViewById(R.id.tv_content);
            holder.vDiv     = convertView.findViewById(R.id.v_line);

            LViewScaleDef viewScaleDef = LViewScaleDef.getInstance(m_context);

            RelativeLayout.LayoutParams params
                    = (RelativeLayout.LayoutParams)holder.tvText.getLayoutParams();
            params.height = viewScaleDef.getLayoutHeight(80);
            viewScaleDef.setTextSize(48, holder.tvText);

            params = (RelativeLayout.LayoutParams)holder.vDiv.getLayoutParams();
            params.height = viewScaleDef.getLayoutHeight(1);

        } else {
            holder = (Holder) convertView.getTag();
        }

        String strText = m_alAllData.get(iPosition);

        if ( null != holder
                && null != holder.tvText
                && !TextUtils.isEmpty(strText) ) {
            holder.tvText.setText(strText);
        }

        if ( null != holder
                && null != holder.vDiv ) {
            if( (m_alAllData.size() - 1) <= iPosition ){
                holder.vDiv.setVisibility(View.GONE);
            } else {
                holder.vDiv.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }
}
