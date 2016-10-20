package ling.testapp.function.Bingo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import ling.testapp.function.Bingo.adapter.LSelectBingoSizeAdapter;

/**
 * Created by jlchen on 2016/9/30.
 */

public class LSelectBingoSizePopupwindow extends PopupWindow
        implements AdapterView.OnItemClickListener{

    private static final int                DEF_ANIM_TIME   = 200;
    private static final int                DEF_DELAYED_TIME= 300;
    private static final double             WEIGHT_LISTVIEW = 300;
    private static final double             WEIGHT_DIVIDER  = 1;
    private static final double             WEIGHT_HIDE_VIEW= 404;

    private Context                         m_context       = null;
    private View                            m_vContentView  = null;
    private ListView                        m_listView      = null;
    private AdapterView.OnItemClickListener m_listener      = null;
    private ArrayList<String>               m_alItems       = null;
    private LSelectBingoSizeAdapter         m_adapter       = null;
    private boolean                         m_bAnimEnd      = false;
    private boolean                         m_bDismiss      = false;

    public LSelectBingoSizePopupwindow(Context context,
                               int width,
                               int height,
                               ArrayList<String> alItems,
                               AdapterView.OnItemClickListener listener){
        m_context  = context;
        m_listener = listener;
        m_alItems  = alItems;
        LayoutInflater layoutInflater = LayoutInflater.from(m_context);
        m_vContentView = layoutInflater.inflate(R.layout.popupwindow_select_bingo_size, null);

        initialLayoutComponent(m_vContentView);
        setTextSizeAndLayoutParams(m_vContentView, LViewScaleDef.getInstance(m_context));

        setContentView(m_vContentView);
        setWidth(width);
        setHeight(height);
        setOutsideTouchable(true);
        setTouchable(true);
        //SDK 19 需設定Focus，ListView的item被點擊才能接收到onItemClick事件
        setFocusable(true);
        setBackgroundDrawable(
                new ColorDrawable(ContextCompat.getColor(m_context, R.color.transparent)));
    }

    private void initialLayoutComponent(View view) {
        m_listView = (ListView)view.findViewById(R.id.lv_select_bingo_size);
        m_adapter = new LSelectBingoSizeAdapter(m_context, m_alItems);
        m_listView.setAdapter(m_adapter);
        m_listView.setOnItemClickListener(this);
    }

    private void setTextSizeAndLayoutParams(View view, LViewScaleDef scaleDef) {
        m_listView.getLayoutParams().width = scaleDef.getLayoutWidth(WEIGHT_LISTVIEW);
        m_listView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        m_listView.setDividerHeight(scaleDef.getLayoutHeight(WEIGHT_DIVIDER));
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView,
                            final View view,
                            final int i,
                            final long l) {
        if ( true == m_bDismiss )
            return;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ( null != m_listener )
                    m_listener.onItemClick(adapterView, view, i, l);
            }
        }, DEF_DELAYED_TIME);

        dismiss();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);

        ObjectAnimator oa = ObjectAnimator.ofFloat(
                m_vContentView,
                "translationY",
                -LViewScaleDef.getInstance(m_context).getLayoutHeight(WEIGHT_HIDE_VIEW),
                0);
        oa.setDuration(DEF_ANIM_TIME);
        oa.start();
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                m_bDismiss = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void dismiss() {
        m_bDismiss = true;

        if ( m_bAnimEnd == true ){
            m_bAnimEnd = false;
            super.dismiss();
        }else {
            ObjectAnimator oa = ObjectAnimator.ofFloat(
                    m_vContentView,
                    "translationY",
                    0,
                    -LViewScaleDef.getInstance(m_context).getLayoutHeight(WEIGHT_HIDE_VIEW));
            oa.setDuration(DEF_ANIM_TIME);
            oa.start();
            oa.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    m_bAnimEnd = true;
                    dismiss();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }
}
