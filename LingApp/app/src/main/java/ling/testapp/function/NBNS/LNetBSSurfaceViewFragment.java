package ling.testapp.function.NBNS;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseFragment;
import ling.testapp.function.NBNS.item.LNetBSItem;
import ling.testapp.function.NBNS.view.LNetBSSurfaceView;
import ling.testapp.ui.define.LViewScaleDef;

/**
 * Created by jlchen on 2016/10/21.
 */

public class LNetBSSurfaceViewFragment extends LBaseFragment implements View.OnClickListener{

    LNetBSSurfaceView.OnListener m_svOnListener = new LNetBSSurfaceView.OnListener() {
        @Override
        public void noData(LNetBSActivity.eType type) {

        }
    };

    LNetBSSurfaceView.OnParameter m_svOnParameter = new LNetBSSurfaceView.OnParameter() {
        @Override
        public ArrayList<LNetBSItem> GetTesData() {
            return null;
        }

        @Override
        public ArrayList<LNetBSItem> GetOtcData() {
            return null;
        }
    };

    private LNetBSSurfaceView.OnInterface m_svOnInterface = null;

    public  static final double     BUTTON_PADDING  = 5;
    public  static final double     BUTTON_WIDTH    = 50;
    public  static final double     IMAGE_WIDTH     = 60;
    public  static final double     TEXTVIEW_WIDTH  = 85;
    public  static final double     TEXT_SIZE       = 36;

    private View        m_vQfiiBg   = null,
                        m_vQfiiImg  = null,
                        m_vBrkBg    = null,
                        m_vBrkImg   = null,
                        m_vItBg     = null,
                        m_vItImg    = null;
    private TextView    m_tvQfii    = null,
                        m_tvBrk     = null,
                        m_tvIt      = null;
    private RelativeLayout m_rlSv   = null;

    private boolean     m_bQfii     = true,
                        m_bBrk      = true,
                        m_bIt       = true;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_nbns_sv;
    }

    @Override
    protected void initialLayoutComponent(LayoutInflater inflater, View view) {

        m_vQfiiBg   = view.findViewById(R.id.v_cb_qfii_bg);
        m_vQfiiBg.setOnClickListener(this);
        m_vQfiiImg  = view.findViewById(R.id.v_cb_qfii);
        m_vQfiiImg.setOnClickListener(this);
        m_tvQfii    = (TextView)view.findViewById(R.id.tv_cb_qfii);
        m_tvQfii.setOnClickListener(this);

        m_vBrkBg    = view.findViewById(R.id.v_cb_brk_bg);
        m_vBrkBg.setOnClickListener(this);
        m_vBrkImg   = view.findViewById(R.id.v_cb_brk);
        m_vBrkImg.setOnClickListener(this);
        m_tvBrk     = (TextView)view.findViewById(R.id.tv_cb_brk);
        m_tvBrk.setOnClickListener(this);

        m_vItBg     = view.findViewById(R.id.v_cb_it_bg);
        m_vItBg.setOnClickListener(this);
        m_vItImg    = view.findViewById(R.id.v_cb_it);
        m_vItImg.setOnClickListener(this);
        m_tvIt      = (TextView)view.findViewById(R.id.tv_cb_it);
        m_tvIt.setOnClickListener(this);

        m_rlSv      = (RelativeLayout)view.findViewById(R.id.rl_sv);

        LNetBSSurfaceView surfaceView
                = new LNetBSSurfaceView(getActivity(), m_svOnParameter, m_svOnListener);
        m_svOnInterface = surfaceView.getInterface();
        m_rlSv.addView(surfaceView);

    }

    @Override
    protected void setTextSizeAndLayoutParams(View view, LViewScaleDef vScaleDef) {

        m_vQfiiBg.getLayoutParams().width = vScaleDef.getLayoutMinUnit(BUTTON_WIDTH);
        m_vQfiiBg.getLayoutParams().height = vScaleDef.getLayoutMinUnit(BUTTON_WIDTH);
        m_vQfiiBg.setPadding(vScaleDef.getLayoutMinUnit(BUTTON_PADDING),
                vScaleDef.getLayoutMinUnit(BUTTON_PADDING),
                vScaleDef.getLayoutMinUnit(BUTTON_PADDING),
                vScaleDef.getLayoutMinUnit(BUTTON_PADDING));
        m_vQfiiImg.getLayoutParams().width = vScaleDef.getLayoutMinUnit(IMAGE_WIDTH);
        m_vQfiiImg.getLayoutParams().height = vScaleDef.getLayoutMinUnit(IMAGE_WIDTH);

        m_vBrkBg.getLayoutParams().width = vScaleDef.getLayoutMinUnit(BUTTON_WIDTH);
        m_vBrkBg.getLayoutParams().height = vScaleDef.getLayoutMinUnit(BUTTON_WIDTH);
        m_vBrkBg.setPadding(vScaleDef.getLayoutMinUnit(BUTTON_PADDING),
                vScaleDef.getLayoutMinUnit(BUTTON_PADDING),
                vScaleDef.getLayoutMinUnit(BUTTON_PADDING),
                vScaleDef.getLayoutMinUnit(BUTTON_PADDING));
        m_vBrkImg.getLayoutParams().width = vScaleDef.getLayoutMinUnit(IMAGE_WIDTH);
        m_vBrkImg.getLayoutParams().height = vScaleDef.getLayoutMinUnit(IMAGE_WIDTH);

        m_vItBg.getLayoutParams().width = vScaleDef.getLayoutMinUnit(BUTTON_WIDTH);
        m_vItBg.getLayoutParams().height = vScaleDef.getLayoutMinUnit(BUTTON_WIDTH);
        m_vItBg.setPadding(vScaleDef.getLayoutMinUnit(BUTTON_PADDING),
                vScaleDef.getLayoutMinUnit(BUTTON_PADDING),
                vScaleDef.getLayoutMinUnit(BUTTON_PADDING),
                vScaleDef.getLayoutMinUnit(BUTTON_PADDING));
        m_vItImg.getLayoutParams().width = vScaleDef.getLayoutMinUnit(IMAGE_WIDTH);
        m_vItImg.getLayoutParams().height = vScaleDef.getLayoutMinUnit(IMAGE_WIDTH);

        m_tvQfii.getLayoutParams().width = vScaleDef.getLayoutWidth(TEXTVIEW_WIDTH);
        m_tvBrk.getLayoutParams().width = vScaleDef.getLayoutWidth(TEXTVIEW_WIDTH);
        m_tvIt.getLayoutParams().width = vScaleDef.getLayoutWidth(TEXTVIEW_WIDTH);

        vScaleDef.setTextSize(TEXT_SIZE, m_tvQfii);
        vScaleDef.setTextSize(TEXT_SIZE, m_tvBrk);
        vScaleDef.setTextSize(TEXT_SIZE, m_tvIt);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.v_cb_qfii:
            case R.id.v_cb_qfii_bg:
            case R.id.tv_cb_qfii:
                if ( false == m_bBrk && false == m_bIt )
                    return;

                m_bQfii = !m_bQfii;
                if ( true == m_bQfii ){
                    m_vQfiiImg.setVisibility(View.VISIBLE);
                }else {
                    m_vQfiiImg.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.v_cb_brk:
            case R.id.v_cb_brk_bg:
            case R.id.tv_cb_brk:
                if ( false == m_bQfii && false == m_bIt )
                    return;

                m_bBrk = !m_bBrk;
                if ( true == m_bBrk ){
                    m_vBrkImg.setVisibility(View.VISIBLE);
                }else {
                    m_vBrkImg.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.v_cb_it:
            case R.id.v_cb_it_bg:
            case R.id.tv_cb_it:
                if ( false == m_bQfii && false == m_bBrk )
                    return;

                m_bIt = !m_bIt;
                if ( true == m_bIt ){
                    m_vItImg.setVisibility(View.VISIBLE);
                }else {
                    m_vItImg.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}
