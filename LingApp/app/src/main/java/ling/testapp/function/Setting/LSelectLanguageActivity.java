package ling.testapp.function.Setting;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseActivity;
import ling.testapp.function.Setting.item.LLanguageItem;
import ling.testapp.ui.define.LUiMessageDef;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.navigation.LNavigationBar;
import ling.testapp.ui.object.LApplication;

/**
 * Created by jlchen on 2016/10/18.
 */

public class LSelectLanguageActivity extends LBaseActivity {

    private LNavigationBar.OnListener   m_navigationListener
            = new LNavigationBar.OnListener() {
        @Override
        public void OnRightImgClick() {

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
            return m_context.getResources().getString(R.string.setting_language);
        }

        @Override
        public int GetLeftIconRes() {
            return R.drawable.ic_back_arrow;
        }

        @Override
        public int GetRightIconRes() {
            return 0;
        }

        @Override
        public int GetSecondRightIconRes() {
            return 0;
        }
    };

    private static final double         LAYOUT_PADDING_TOP      = 180;
    private static final double         LAYOUT_HEIGHT           = 200;
    private static final double         LAYOUT_MARGIN           = 60;
    private static final double         TEXTVIEW_MARGIN         = 30;
    private static final double         TEXT_SIZE               = 54;
    private static final double         IMAGE_WIDTH             = 96;
    private static final double         LINE_HEIGHT             = 1;

    private LNavigationBar.OnInterface  m_navigationInterface = null;

    private LNavigationBar              m_navigationBar     = null;

    private ListView                    m_listView          = null;
    private LanguageAdpter              m_LanguageAdpter    = null;

    private int                         m_iSelect           = 0;
    private ArrayList<LLanguageItem>    m_arLanguageList    = null;
    private Locale                      m_locale            = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_select_language;
    }

    @Override
    protected void initialLayoutComponent() {

        m_navigationBar = (LNavigationBar)findViewById(R.id.navigation_bar);

        m_listView      = (ListView)findViewById(R.id.lv_language);

        m_arLanguageList = LApplication.getLanguageInfo().getLanguageList();
        m_LanguageAdpter = new LanguageAdpter( this );
        m_listView.setAdapter(m_LanguageAdpter);
        m_listView.setOnItemClickListener(m_onItemClick);
    }

    @Override
    protected void setTextSizeAndLayoutParams(LViewScaleDef vScaleDef) {

        m_navigationBar.getLayoutParams().height
                = vScaleDef.getLayoutHeight(LNavigationBar.NAVIGATION_BAR_HEIGHT);

        LLanguageItem item = LApplication.getLanguageInfo().getLanguage();
        int iSize = m_arLanguageList.size();
        for( int iIdx =0; iIdx < iSize; iIdx++ ){
            if ( m_arLanguageList.get(iIdx).strTag.equals(item.strTag) ){
                m_iSelect = iIdx;
                break;
            }
        }

        m_locale = item.locale;
        m_LanguageAdpter.notifyDataSetChanged();
    }

    @Override
    protected void setOnParameterAndListener() {

        m_navigationInterface = m_navigationBar.uiSetParameterListener(
                m_navigationParameter, m_navigationListener);
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

        LLanguageItem item = LApplication.getLanguageInfo().getLanguage();

        if ( m_locale.equals( item.locale ) ){
            setResult(RESULT_OK);
        } else {
            setResult(LUiMessageDef.INTENT_RESULT_CODE_LANGUAGE);
        }

        super.finish();

        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
    }

    AdapterView.OnItemClickListener m_onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if ( m_iSelect < 0 && m_iSelect > m_arLanguageList.size() ){
                return;
            }

            m_iSelect = position;

            LLanguageItem item = m_arLanguageList.get(m_iSelect);
            LApplication.getLanguageInfo().setLanguage(item.locale);

            m_LanguageAdpter.notifyDataSetChanged();

            //重新設定語系
            m_navigationInterface.changeLanguageText(getString(R.string.setting_language));
        }
    };

    class ItemHolder{

        RelativeLayout  background;
        RelativeLayout  layout;
        TextView        text;
        ImageView       image;
        View            line;
    }

    class LanguageAdpter extends BaseAdapter {

        private Context m_context = null;

        LanguageAdpter( Context context ){
            m_context     = context;
        }

        @Override
        public int getCount() {
            if ( null != m_arLanguageList ){
                return m_arLanguageList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if ( null != m_arLanguageList ){
                return m_arLanguageList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ItemHolder itemHolder = null;

            if ( null == convertView ){
                convertView = LayoutInflater.from(m_context).inflate( R.layout.view_language_item, null);

                itemHolder = new ItemHolder();
                itemHolder.background = (RelativeLayout)convertView.findViewById(R.id.rl_background);
                itemHolder.layout   = (RelativeLayout)convertView.findViewById(R.id.rl_content);
                itemHolder.image    = (ImageView)convertView.findViewById(R.id.image);
                itemHolder.text     = (TextView)convertView.findViewById(R.id.text);
                itemHolder.line     = convertView.findViewById(R.id.v_div);

                LViewScaleDef viewScaleDef = LViewScaleDef.getInstance(m_context);

                viewScaleDef.setTextSize(TEXT_SIZE, itemHolder.text);

                itemHolder.layout.getLayoutParams().height
                        = viewScaleDef.getLayoutHeight(LAYOUT_HEIGHT);

                if ( 0 == position ){
                    itemHolder.background.setPadding(0,
                            viewScaleDef.getLayoutHeight(LAYOUT_PADDING_TOP),
                            0,
                            0);
                } else {
                    itemHolder.background.setPadding(0, 0, 0, 0);
                }

                RelativeLayout.LayoutParams
                        params = (RelativeLayout.LayoutParams)itemHolder.text.getLayoutParams();
                params.leftMargin = viewScaleDef.getLayoutWidth(LAYOUT_MARGIN);
                params.rightMargin = viewScaleDef.getLayoutWidth(TEXTVIEW_MARGIN);

                params = (RelativeLayout.LayoutParams)itemHolder.text.getLayoutParams();
                params.leftMargin = viewScaleDef.getLayoutWidth(LAYOUT_MARGIN);
                params.rightMargin = viewScaleDef.getLayoutWidth(TEXTVIEW_MARGIN);

                params = (RelativeLayout.LayoutParams)itemHolder.image.getLayoutParams();
                params.height = viewScaleDef.getLayoutMinUnit(IMAGE_WIDTH);
                params.width = viewScaleDef.getLayoutMinUnit(IMAGE_WIDTH);
                params.rightMargin = viewScaleDef.getLayoutWidth(LAYOUT_MARGIN);

                params = (RelativeLayout.LayoutParams)itemHolder.line.getLayoutParams();
                params.height = viewScaleDef.getLayoutHeight(LINE_HEIGHT);

                convertView.setBackgroundResource(R.drawable.bg_transparent_press_black20);

                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder)convertView.getTag();
            }

            itemHolder.text.setText(m_arLanguageList.get(position).strDisplayName);

            if ( m_iSelect == position ){
                itemHolder.image.setVisibility(View.VISIBLE);
                itemHolder.background.setSelected(true);
            } else {
                itemHolder.image.setVisibility(View.INVISIBLE);
                itemHolder.background.setSelected(false);
            }

            return convertView;
        }
    }
}
