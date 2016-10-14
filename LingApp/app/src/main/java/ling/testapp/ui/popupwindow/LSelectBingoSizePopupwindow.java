package ling.testapp.ui.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import ling.testapp.R;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.popupwindow.adapter.LSelectBingoSizeAdapter;

/**
 * Created by jlchen on 2016/9/30.
 */

public class LSelectBingoSizePopupwindow extends PopupWindow implements AdapterView.OnItemClickListener{

    private Context m_context  = null;
    private ListView m_listView = null;
    private AdapterView.OnItemClickListener m_listener = null;
    private ArrayList<String> m_alItems  = null;
    private LSelectBingoSizeAdapter         m_adapter  = null;

    public LSelectBingoSizePopupwindow(Context context,
                               int width,
                               int height,
                               ArrayList<String> alItems,
                               AdapterView.OnItemClickListener listener){
        m_context  = context;
        m_listener = listener;
        m_alItems  = alItems;
        LayoutInflater layoutInflater = LayoutInflater.from(m_context);
        View view = layoutInflater.inflate(R.layout.popupwindow_select_bingo_size, null);

        initialLayoutComponent(view);
        setTextSizeAndLayoutParams(view, LViewScaleDef.getInstance(m_context));

        setContentView(view);
        setWidth(width);
        setHeight(height);
        setOutsideTouchable(true);
        setTouchable(true);
        //SDK 19 需設定Focus，ListView的item被點擊才能接收到onItemClick事件
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(m_context, R.color.transparent)));
    }
    private void initialLayoutComponent(View view) {
        m_listView = (ListView)view.findViewById(R.id.lv_select_bingo_size);
        m_adapter = new LSelectBingoSizeAdapter(m_context, m_alItems);
        m_listView.setAdapter(m_adapter);
        m_listView.setOnItemClickListener(this);
    }

    private void setTextSizeAndLayoutParams(View view, LViewScaleDef scaleDef) {
        m_listView.getLayoutParams().width = scaleDef.getLayoutWidth(300);
        m_listView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        m_listView.setDividerHeight(scaleDef.getLayoutHeight(1));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        m_listener.onItemClick(adapterView,view,i,l);
        dismiss();
    }
}
