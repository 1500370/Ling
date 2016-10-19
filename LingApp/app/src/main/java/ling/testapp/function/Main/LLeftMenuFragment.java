package ling.testapp.function.Main;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseFragment;
import ling.testapp.function.Main.adapter.LSideMenuAdapter;
import ling.testapp.function.Main.item.LSideMenuGroupItem;
import ling.testapp.function.Main.item.LSideMenuItem;
import ling.testapp.ui.define.LMenuViewIdDef;
import ling.testapp.ui.define.LViewScaleDef;

/**
 * Created by jlchen on 2016/9/21.
 * 左選單
 */

public class LLeftMenuFragment extends LBaseFragment {

    public interface OnListener {

        /**外部點擊選單*/
        void OnMenuClick(LSideMenuItem item);
    }

    public interface OnInterface {

        /**帶入被點擊的View Id, 當View id 相同 Menu 會有被選取的背景色*/
        void OnSelectMenu(int iViewId);
    }

    private OnInterface     m_onInterface   = new OnInterface() {
        @Override
        public void OnSelectMenu(int iViewId) {

            if ( LMenuViewIdDef.MENU_ID_HOME == iViewId ){
                changeHeaderSelectMode(true);
            } else {
                changeHeaderSelectMode(false);
            }
            onCheckMenuIsSelect(iViewId);
        }
    };


    View.OnClickListener m_OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ( view.getId() == m_cHeaderHolder.rlBg.getId() ){
                m_menuHomeItem.bSelect = true;
                changeHeaderSelectMode(m_menuHomeItem.bSelect);
                onCheckMenuIsSelect(m_menuHomeItem.iId);
                if (null != m_onListener){
                    m_onListener.OnMenuClick(m_menuHomeItem);
                }
            }
        }
    };

    ExpandableListView.OnGroupClickListener m_OnMenuGroupListener
            = new ExpandableListView.OnGroupClickListener(){

        @Override
        public boolean onGroupClick(ExpandableListView parent,
                                    View v,
                                    int                 groupPosition,
                                    long                id) {
            return true;
        }
    };

    ExpandableListView.OnChildClickListener m_OnMenuChildListener
            = new ExpandableListView.OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent,
                                    View v,
                                    int                 groupPosition,
                                    int                 childPosition,
                                    long                id) {

            LSideMenuItem sideMenuItem = null;
            try {
                sideMenuItem = m_alLeftMenuItem.get(groupPosition).alMenuItem.get(childPosition);
            } catch ( Exception e ) {
                e.printStackTrace();
                return true;
            }

            m_menuHomeItem.bSelect = false;
            changeHeaderSelectMode(m_menuHomeItem.bSelect);

            if ( null != m_onListener && null != sideMenuItem ){
                onCheckMenuIsSelect(sideMenuItem.iId);
                m_onListener.OnMenuClick(sideMenuItem);
            }
            return true;
        }
    };

    private OnListener                      m_onListener        = null;
    private ArrayList<LSideMenuGroupItem> m_alLeftMenuItem    = new ArrayList<>();

    private LSideMenuItem                   m_menuHomeItem      = null;
    private LSideMenuAdapter.ChildHolder    m_cHeaderHolder     = null;
    private LSideMenuAdapter                m_adpater           = null;

    private ExpandableListView m_elvLeftMenu       = null;
    private RelativeLayout m_rlHome            = null;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_left_menu;
    }

    @Override
    protected void initialLayoutComponent(LayoutInflater inflater, View view) {
        m_menuHomeItem = LMenuViewIdDef.getInstance().getHomeMenuList().get(0).alMenuItem.get(0);
        m_menuHomeItem.bSelect = true;

        View vHome = inflater.inflate(R.layout.view_menu_child_item, null);
        m_cHeaderHolder         = new LSideMenuAdapter.ChildHolder();
        m_cHeaderHolder.vBg     = vHome.findViewById(R.id.v_background);
        m_cHeaderHolder.rlBg    = (RelativeLayout)vHome.findViewById(R.id.rl_child_content);
        m_cHeaderHolder.rlBg.setClickable(true);
        m_cHeaderHolder.rlBg.setBackgroundResource(R.drawable.bg_transparent_press_white20);
        m_cHeaderHolder.rlBg.setOnClickListener(m_OnClick);
        m_cHeaderHolder.ivIcon  = (ImageView)vHome.findViewById(R.id.iv_menu);
        m_cHeaderHolder.ivIcon.setImageResource(m_menuHomeItem.iDrawableId);
        m_cHeaderHolder.tvText  = (TextView)vHome.findViewById(R.id.tv_menu);
        m_cHeaderHolder.tvText.setText(m_menuHomeItem.iNameResId);
        m_cHeaderHolder.vDiv    = vHome.findViewById(R.id.v_div);

        m_rlHome                = (RelativeLayout)view.findViewById(R.id.rl_menu_home);
        m_rlHome.addView(vHome);
        m_elvLeftMenu           = (ExpandableListView)view.findViewById(R.id.elv_left_menu);
        m_alLeftMenuItem        = LMenuViewIdDef.getInstance().getLeftMenuList();
        m_adpater               = new LSideMenuAdapter(getActivity(), m_alLeftMenuItem);
        m_elvLeftMenu.setAdapter(m_adpater);
        m_elvLeftMenu.setOnGroupClickListener(m_OnMenuGroupListener);
        m_elvLeftMenu.setOnChildClickListener(m_OnMenuChildListener);

        // 打開所有子選項
        for (int i = 0; i < m_adpater.getGroupCount(); i++) {
            m_elvLeftMenu.expandGroup(i);
        }

        changeHeaderSelectMode(m_menuHomeItem.bSelect);
    }

    @Override
    protected void setTextSizeAndLayoutParams(View view, LViewScaleDef vScaleDef) {
        m_rlHome.getLayoutParams().height
                = vScaleDef.getLayoutHeight(LSideMenuAdapter.CHILD_ITEM_HEIGHT);
        m_cHeaderHolder.rlBg.getLayoutParams().height
                = vScaleDef.getLayoutHeight(LSideMenuAdapter.CHILD_ITEM_HEIGHT);
        m_cHeaderHolder.vBg.getLayoutParams().height
                = vScaleDef.getLayoutHeight(LSideMenuAdapter.CHILD_ITEM_HEIGHT);
        //icon
        RelativeLayout.LayoutParams params
                = (RelativeLayout.LayoutParams)m_cHeaderHolder.ivIcon.getLayoutParams();
        params.height = vScaleDef.getLayoutMinUnit(LSideMenuAdapter.IMG_WIDTH);
        params.width  = vScaleDef.getLayoutMinUnit(LSideMenuAdapter.IMG_WIDTH);
        params.leftMargin = vScaleDef.getLayoutWidth(LSideMenuAdapter.IMG_LEFT_MARGIN);

        //文字
        params = (RelativeLayout.LayoutParams)m_cHeaderHolder.tvText.getLayoutParams();
        params.leftMargin = vScaleDef.getLayoutWidth(LSideMenuAdapter.TEXT_LEFT_MARGIN);
        params.rightMargin = vScaleDef.getLayoutWidth(LSideMenuAdapter.TEXT_RIGHT_MARGIN);
        vScaleDef.setTextSize(LSideMenuAdapter.CHILD_TEXT_SIZE, m_cHeaderHolder.tvText);

        params = (RelativeLayout.LayoutParams)m_cHeaderHolder.vDiv.getLayoutParams();
        params.height = vScaleDef.getLayoutHeight(LSideMenuAdapter.DIV_HEIGHT);
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

    public OnInterface uiSetParameterListener(OnListener onListener) {
        m_onListener = onListener;

        return m_onInterface;
    }

    private void changeHeaderSelectMode( boolean bSelect ){
        m_cHeaderHolder.rlBg.setSelected(bSelect);
        if ( bSelect ){
            //可實現選取時 換圖或文字的動作
            m_cHeaderHolder.vBg.setBackgroundResource(R.color.white_20);
        } else {
            m_cHeaderHolder.vBg.setBackgroundResource(R.color.transparent);
        }
    }

    /**@param iViewID 被點選的View Id*/
    public void onCheckMenuIsSelect( int iViewID ){

        int igSize = m_alLeftMenuItem.size();
        for ( int igIdx = 0; igIdx < igSize; igIdx++ ){
            int iSize = m_alLeftMenuItem.get(igIdx).alMenuItem.size();
            for ( int iIdx = 0; iIdx < iSize; iIdx++ ){
                if ( iViewID == m_alLeftMenuItem.get(igIdx).alMenuItem.get(iIdx).iId ){
                    m_alLeftMenuItem.get(igIdx).alMenuItem.get(iIdx).bSelect = true;
                } else {
                    m_alLeftMenuItem.get(igIdx).alMenuItem.get(iIdx).bSelect = false;
                }
            }
        }

        if ( null != m_adpater ){
            m_adpater.notifyDataSetChanged();
        }
    }
}
