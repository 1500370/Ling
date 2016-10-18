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
            return R.drawable.ic_left_arrow;
        }

        @Override
        public int GetRightIconRes() {
            return 0;
        }
    };

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
    public void onBackPressed() {

        LLanguageItem item = LApplication.getLanguageInfo().getLanguage();

        if ( m_locale.equals( item.locale ) ){
            LSelectLanguageActivity.this.finish();
        } else {
            setResult(LUiMessageDef.INTENT_RESULT_CODE_LANGUAGE);
            LSelectLanguageActivity.this.finish();
        }
    }

    @Override
    public void finish() {
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
                itemHolder = new ItemHolder();
                convertView = LayoutInflater.from(m_context).inflate( R.layout.view_language_item, null);
                itemHolder.layout   = (RelativeLayout)convertView.findViewById(R.id.rl_content);
                itemHolder.image    = (ImageView)convertView.findViewById(R.id.image);
                itemHolder.text     = (TextView)convertView.findViewById(R.id.text);
                itemHolder.line     = convertView.findViewById(R.id.v_div);

                LViewScaleDef viewScaleDef = LViewScaleDef.getInstance(m_context);

                viewScaleDef.setTextSize(54, itemHolder.text);

                itemHolder.layout.getLayoutParams().height = viewScaleDef.getLayoutHeight(200);

                RelativeLayout.LayoutParams
                        params = (RelativeLayout.LayoutParams)itemHolder.text.getLayoutParams();
                params.leftMargin = viewScaleDef.getLayoutWidth(60);
                params.rightMargin = viewScaleDef.getLayoutWidth(30);

                params = (RelativeLayout.LayoutParams)itemHolder.text.getLayoutParams();
                params.leftMargin = viewScaleDef.getLayoutWidth(60);
                params.rightMargin = viewScaleDef.getLayoutWidth(30);

                params = (RelativeLayout.LayoutParams)itemHolder.image.getLayoutParams();
                params.height = viewScaleDef.getLayoutMinUnit(96);
                params.width = viewScaleDef.getLayoutMinUnit(96);
                params.rightMargin = viewScaleDef.getLayoutWidth(60);

                params = (RelativeLayout.LayoutParams)itemHolder.line.getLayoutParams();
                params.height = viewScaleDef.getLayoutHeight(1);

                convertView.setBackgroundResource(R.drawable.bg_transparent_press_black20);

                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder)convertView.getTag();
            }

            itemHolder.text.setText(m_arLanguageList.get(position).strDisplayName);

            if ( m_iSelect == position ){
                itemHolder.image.setVisibility(View.VISIBLE);
                itemHolder.layout.setSelected(true);
            } else {
                itemHolder.image.setVisibility(View.INVISIBLE);
                itemHolder.layout.setSelected(false);
            }

            return convertView;
        }
    }
}
