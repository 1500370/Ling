package ling.testapp.function.NBNS;

import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseActivity;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.dialog.LInfoDialog;
import ling.testapp.ui.navigation.LNavigationBar;
import ling.testapp.ui.navigation.LTwoItemNavigationBar;

/**
 * Created by jlchen on 2016/10/18.
 * 三大法人買賣超走勢圖
 */

public class LThreeNBNSActivity extends LBaseActivity implements View.OnClickListener{

    private LNavigationBar.OnListener   m_navigationListener
            = new LNavigationBar.OnListener() {
        @Override
        public void OnRightImgClick() {
            LInfoDialog infoDialog = new LInfoDialog(m_context,
                    new LInfoDialog.OnDialogListener() {
                @Override
                public void OnClose() {

                }
            });
            infoDialog.uiSetTitleText(getString(R.string.nbns_info_title));
            infoDialog.uiSetContentText(getString(R.string.nbns_info));
            infoDialog.show();
        }

        @Override
        public void OnSecondRightImgClick() {

        }

        @Override
        public void OnLeftImgClick() {
            finish();
        }
    };

    private LNavigationBar.OnParameter  m_navigationParameter
            = new LNavigationBar.OnParameter() {
        @Override
        public String GetTitle() {
            return m_context.getResources().getString(R.string.title_nbns);
        }

        @Override
        public int GetLeftIconRes() {
            return R.drawable.ic_back_arrow;
        }

        @Override
        public int GetRightIconRes() {
            return R.drawable.ic_information;
        }

        @Override
        public int GetSecondRightIconRes() {
            return 0;
        }
    };

    private LTwoItemNavigationBar.OnListener m_twobarListener
            = new LTwoItemNavigationBar.OnListener() {
        @Override
        public void OnLeftClick() {

        }

        @Override
        public void OnRightClick() {

        }
    };

    private LTwoItemNavigationBar.OnParameter m_twobarParameter
            = new LTwoItemNavigationBar.OnParameter(){

        @Override
        public String GetLeftText() {
            return m_context.getResources().getString(R.string.nbns_tes);
        }

        @Override
        public String GetRightText() {
            return m_context.getResources().getString(R.string.nbns_otc);
        }

        @Override
        public LTwoItemNavigationBar.eItemType GetInitType() {
            return null;
        }
    };

    public  static final double     TWOITEM_WIDTH   = 75;
    public  static final double     TWOITEM_PADDING = 2.5;

    public  static final double     BUTTON_PADDING  = 5;
    public  static final double     BUTTON_WIDTH    = 70;
    public  static final double     IMAGE_WIDTH     = 80;
    public  static final double     TEXTVIEW_WIDTH  = 100;
    public  static final double     TEXT_SIZE       = 42;

    private LNavigationBar          m_navigationBar = null;
    private LTwoItemNavigationBar   m_twoItemBar    = null;

    private View        m_vRootBg                   = null;
    private View        m_vQfiiBg, m_vQfiiImg,
                        m_vBrkBg, m_vBrkImg,
                        m_vItBg, m_vItImg           = null;

    private TextView    m_tvQfii, m_tvBrk, m_tvIt   = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_nbns;
    }

    @Override
    protected void initialLayoutComponent() {

        m_navigationBar = (LNavigationBar)findViewById(R.id.navigation_bar);
        m_twoItemBar    = (LTwoItemNavigationBar)findViewById(R.id.two_bar);

        m_vRootBg       = findViewById(R.id.v_background);

        m_vQfiiBg = findViewById(R.id.v_cb_qfii_bg);
        m_vQfiiBg.setOnClickListener(this);
        m_vQfiiImg = findViewById(R.id.v_cb_qfii);
        m_vQfiiImg.setOnClickListener(this);
        m_vBrkBg = findViewById(R.id.v_cb_brk_bg);
        m_vBrkBg.setOnClickListener(this);
        m_vBrkImg = findViewById(R.id.v_cb_brk);
        m_vBrkImg.setOnClickListener(this);
        m_vItBg = findViewById(R.id.v_cb_it_bg);
        m_vItBg.setOnClickListener(this);
        m_vItImg = findViewById(R.id.v_cb_it);
        m_vItImg.setOnClickListener(this);

        m_tvQfii = (TextView)findViewById(R.id.tv_cb_qfii);
        m_tvBrk = (TextView)findViewById(R.id.tv_cb_brk);
        m_tvIt = (TextView)findViewById(R.id.tv_cb_it);

    }

    @Override
    protected void setTextSizeAndLayoutParams(LViewScaleDef vScaleDef) {
        //抓整個螢幕大小
        DisplayMetrics dm = vScaleDef.getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int iDmW = dm.widthPixels;
        int iDmH = dm.heightPixels;

        if (iDmH < iDmW){
            m_vRootBg.setBackgroundResource(R.drawable.bg_nbns);
        }

        m_navigationBar.getLayoutParams().height
                = vScaleDef.getLayoutHeight(LNavigationBar.NAVIGATION_BAR_HEIGHT);

        RelativeLayout.LayoutParams params
                = (RelativeLayout.LayoutParams)m_twoItemBar.getLayoutParams();
        params.height = vScaleDef.getLayoutMinUnit(TWOITEM_WIDTH);
        params.rightMargin = vScaleDef.getLayoutMinUnit(BUTTON_PADDING);
        m_twoItemBar.setPadding(vScaleDef.getLayoutMinUnit(TWOITEM_PADDING),
                vScaleDef.getLayoutMinUnit(TWOITEM_PADDING),
                vScaleDef.getLayoutMinUnit(TWOITEM_PADDING),
                vScaleDef.getLayoutMinUnit(TWOITEM_PADDING));

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
    protected void setOnParameterAndListener() {

        m_navigationBar.uiSetParameterListener(
                m_navigationParameter, m_navigationListener);

        m_twoItemBar.uiSetParameterListener(m_twobarParameter, m_twobarListener);
    }

    @Override
    protected void registerFragment(FragmentManager fragmentManager) {

    }

    @Override
    protected void onLanguageChangeUpdateUI() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
    }

    @Override
    public void onClick(View v) {

    }
}
