package ling.testapp.function.Bingo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
 * 賓果遊戲-輸入賓果盤頁面
 *
 * *待完成功能*
 * 1. 單一輸入框清除內容功能
 * 2. 當賓果盤數字驗證錯誤時, 數字顏色要變紅
 * 3. 驗證錯誤則show出訊息(非show dialog/toast)
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
        public void OnItemSelect(View v, ArrayList<LBingoItem> arrayList, int iPosition) {

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
                    LViewScaleDef.getInstance(m_context).getLayoutWidth(WEIGHT_TOAST_MARGIN));
            toast.show();
        }
    };

    private static final double WEIGHT_TOTAL_WIDTH  = 1020;
    private static final double WEIGHT_PADDING      = 15;
    private static final double WEIGHT_TOAST_MARGIN = 60;
    private static final double WEIGHT_TEXT_SIZE    = 36;

    private RelativeLayout          m_rlRoot        = null;
    private TextView                m_tvText        = null;
    private FrameLayout             m_flContent     = null;

    private GridView                m_gridView      = null;
    private LBingoAdapter           m_adapter       = null;
    private int                     m_iMin          = 0;
    private int                     m_iMax          = 0;
    private int                     m_iCol          = 3;
    private int                     m_iGvWidth      = 0;
    private ArrayList<LBingoItem>   m_alData        = new ArrayList<>();


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

        m_rlRoot = (RelativeLayout)findViewById(R.id.rl_bingo_root);
        m_rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        m_tvText = (TextView)findViewById(R.id.tv_detail);
        String str = String.format(
                m_context.getResources().getString(R.string.bingo_detail_text),
                ""+m_iMin,
                ""+m_iMax,
                m_iCol+" x "+m_iCol);
        m_tvText.setText(str);

        View vContent = View.inflate(m_context, R.layout.view_bingo, null);
        m_flContent = (FrameLayout)findViewById(R.id.fl_content);
        m_flContent.addView(vContent);
        m_gridView = (GridView)vContent.findViewById(R.id.gv_bingo);
    }

    @Override
    protected void setTextSizeAndLayoutParams(LViewScaleDef vScaleDef) {
        vScaleDef.setTextSize(WEIGHT_TEXT_SIZE, m_tvText);
        m_tvText.getLayoutParams().height = vScaleDef.getLayoutWidth(WEIGHT_TOAST_MARGIN);

        RelativeLayout.LayoutParams params
                = (RelativeLayout.LayoutParams)m_flContent.getLayoutParams();
        params.setMargins(vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                0,
                vScaleDef.getLayoutWidth(WEIGHT_PADDING),
                0);
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
        cancelEditTextFocus();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LUiMessageDef.BUNDLE_TAG_BINGO_DATA, m_alData);
        intent.putExtras(bundle);
        setResult(LUiMessageDef.INTENT_RESULT_CODE_BINGO, intent);

        super.finish();

        overridePendingTransition(R.anim.anim_alpha_in, R.anim.anim_alpha_out);
    }

    private void cancelEditTextFocus(){
        m_rlRoot.setFocusable(true);
        m_rlRoot.setFocusableInTouchMode(true);
        m_rlRoot.requestFocus();
    }
}
