package ling.testapp.function.Bingo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseActivity;
import ling.testapp.function.Bingo.adapter.LBingoAdapter;
import ling.testapp.function.Bingo.item.LBingoItem;
import ling.testapp.ui.define.LUiMessageDef;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.toast.LToast;

/**
 * Created by jlchen on 2016/10/5.
 */

public class LBingoActivity extends LBaseActivity {

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
            return LBingoAdapter.BingoType.INPUT;
        }
    };

    private LBingoAdapter.OnBingoAdapterListener m_adapterListener
            = new LBingoAdapter.OnBingoAdapterListener() {
        @Override
        public void OnItemClick(ArrayList<LBingoItem> arrayList) {

        }

        @Override
        public void OnItemSelect(ArrayList<LBingoItem> arrayList) {

        }

        @Override
        public void updateData(ArrayList<LBingoItem> arrayList) {
            m_alData = arrayList;
        }

        @Override
        public void closeActivity() {
            finish();
        }

        @Override
        public void showToast(String str) {
            LToast toast = LToast.makeText(m_context, str);
            //自定義上間隔
            toast.setGravity(
                    Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                    0,
                    LViewScaleDef.getInstance(m_context).getLayoutHeight(WEIGHT_TOAST_MARGIN));
            toast.show();
        }
    };

    private static final double WEIGHT_TOTAL_WIDTH  = 1020;
    private static final double WEIGHT_PADDING      = 15;
    private static final double WEIGHT_TOAST_MARGIN = 35;

    private LinearLayout m_llRoot        = null;
    private FrameLayout m_flContent     = null;

    private GridView m_gridView      = null;
    private LBingoAdapter           m_adapter       = null;
    private int                     m_iMin          = 0;
    private int                     m_iMax          = 0;
    private int                     m_iCol          = 3;
    private int                     m_iGvWidth      = 0;
    private ArrayList<LBingoItem> m_alData        = new ArrayList<>();


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_bingo;
    }

    @Override
    protected void initialLayoutComponent() {

        //Bundle取得初始化資料
        Bundle bundle = getIntent().getExtras();
        if ( null != bundle ){
            m_alData = (ArrayList)bundle.getSerializable(LUiMessageDef.BUNDLE_TAG_BINGO_DATA);
            m_iCol = bundle.getInt(LUiMessageDef.BUNDLE_TAG_BINGO_COL);
            m_iMin = bundle.getInt(LUiMessageDef.BUNDLE_TAG_BINGO_MIN);
            m_iMax = bundle.getInt(LUiMessageDef.BUNDLE_TAG_BINGO_MAX);
        }

        m_llRoot = (LinearLayout)findViewById(R.id.ll_bingo_root);
        m_llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        View vContent = View.inflate(m_context, R.layout.view_bingo, null);
        m_flContent = (FrameLayout)findViewById(R.id.fl_content);
        m_flContent.addView(vContent);
        m_gridView = (GridView)vContent.findViewById(R.id.gv_bingo);
    }

    @Override
    protected void setTextSizeAndLayoutParams(LViewScaleDef vScaleDef) {
        LinearLayout.LayoutParams params
                = (LinearLayout.LayoutParams)m_flContent.getLayoutParams();
        params.setMargins(vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                vScaleDef.getLayoutWidth(WEIGHT_PADDING * 2),
                vScaleDef.getLayoutWidth(WEIGHT_PADDING), 0);
        m_flContent.setPadding(vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                vScaleDef.getLayoutWidth(WEIGHT_PADDING));

        m_iGvWidth = vScaleDef.getLayoutWidth(WEIGHT_TOTAL_WIDTH);
        m_gridView.getLayoutParams().height = m_iGvWidth;
        m_gridView.getLayoutParams().width = m_iGvWidth;

        m_adapter = new LBingoAdapter(m_context,
                m_adapterParameter,
                m_adapterListener);

        m_gridView.setNumColumns(m_iCol);
        m_gridView.setAdapter(m_adapter);

    }

    @Override
    protected void setOnParameterAndListener() {

    }

    @Override
    protected void registerFragment(FragmentManager fragmentManager) {

    }

    @Override
    public void finish() {

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LUiMessageDef.BUNDLE_TAG_BINGO_DATA, m_alData);
        intent.putExtras(bundle);
        setResult(LUiMessageDef.INTENT_RESULT_CODE_BINGO, intent);

        super.finish();

        overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);
    }
}
