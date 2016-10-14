package ling.testapp.function.Main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ling.testapp.R;
import ling.testapp.function.Main.item.LSideMenuGroupItem;
import ling.testapp.function.Main.item.LSideMenuItem;
import ling.testapp.ui.define.LViewScaleDef;

/**
 * Created by jlchen on 2016/9/22.
 * 選單適配器
 */

public class LSideMenuAdapter extends BaseExpandableListAdapter {

    public static class GroupHolder {
        public RelativeLayout rlBg;
        public ImageView ivIcon;
        public TextView tvText;
        public View vDiv;
    }

    public static class ChildHolder {
        public View vBg;
        public RelativeLayout rlBg;
        public ImageView ivIcon;
        public TextView tvText;
        public View vDiv;
    }

    private final static double             GROUP_ITEM_HEIGHT   = 100;
    private final static double             GROUP_TEXT_SIZE     = 48;
    public  final static double             CHILD_ITEM_HEIGHT   = 150;
    public  final static double             CHILD_TEXT_SIZE     = 54;
    public  final static double             TEXT_RIGHT_MARGIN   = 54;
    public  final static double             TEXT_LEFT_MARGIN    = 24;
    public  final static double             IMG_WIDTH           = 96;
    public  final static double             IMG_LEFT_MARGIN     = 54;
    public  final static double             DIV_HEIGHT          = 1;

    private Context m_context       = null;
    private LViewScaleDef                   m_vScaleDef     = null;
    private ArrayList<LSideMenuGroupItem> m_arMenuList    = null;

    public LSideMenuAdapter (Context context, ArrayList<LSideMenuGroupItem> arrayList){
        this.m_context      = context;
        this.m_arMenuList   = arrayList;

        this.m_vScaleDef    = LViewScaleDef.getInstance(context);
    }

    @Override
    public int getGroupCount() {
        if ( null == m_arMenuList ){
            return 0;
        } else {
            return  m_arMenuList.size();
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if ( null == m_arMenuList ){
            return 0;
        } else if ( null == m_arMenuList.get(groupPosition) ) {
            return 0;
        } else if ( null == m_arMenuList.get(groupPosition).alMenuItem ){
            return 0;
        }else {
            return m_arMenuList.get(groupPosition).alMenuItem.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if ( null == m_arMenuList || groupPosition >= m_arMenuList.size() ){
            return null;
        } else {
            return m_arMenuList.get(groupPosition);
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if ( null == m_arMenuList
                || groupPosition >= m_arMenuList.size() ){
            return null;
        } else if ( null == m_arMenuList.get(groupPosition) ) {
            return null;
        } else if ( null == m_arMenuList.get(groupPosition).alMenuItem
                || childPosition >= m_arMenuList.get(groupPosition).alMenuItem.size() ){
            return null;
        }else {
            return m_arMenuList.get(groupPosition).alMenuItem.get(childPosition);
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int        groupPosition,
                             boolean    isExpanded,
                             View view,
                             ViewGroup viewGroup) {

        GroupHolder groupHolder = null;
        if ( null == view ){
            groupHolder = new GroupHolder();
            view = LayoutInflater.from(m_context).inflate( R.layout.view_menu_group_item, null);
            groupHolder.rlBg    = (RelativeLayout)view.findViewById(R.id.rl_group_content);
            groupHolder.ivIcon  = (ImageView)view.findViewById(R.id.iv_group_menu);
            groupHolder.tvText  = (TextView)view.findViewById(R.id.tv_group_menu);
            groupHolder.vDiv    = view.findViewById(R.id.v_group_div);

            groupHolder.rlBg.getLayoutParams().height
                    = m_vScaleDef.getLayoutHeight(GROUP_ITEM_HEIGHT);

            //icon
            RelativeLayout.LayoutParams params
                    = (RelativeLayout.LayoutParams)groupHolder.ivIcon.getLayoutParams();
            params.leftMargin = m_vScaleDef.getLayoutWidth(IMG_LEFT_MARGIN);
            //設定icon
//            groupHolder.ivIcon.setImageResource(sideMenuItem.iDrawableId);

            //文字
            params = (RelativeLayout.LayoutParams)groupHolder.tvText.getLayoutParams();
            params.leftMargin = m_vScaleDef.getLayoutWidth(TEXT_LEFT_MARGIN);
            params.rightMargin = m_vScaleDef.getLayoutWidth(TEXT_RIGHT_MARGIN);
            m_vScaleDef.setTextSize(GROUP_TEXT_SIZE, groupHolder.tvText);
            //設定顯示文字
            groupHolder.tvText.setText( m_arMenuList.get(groupPosition).iNameResId );

            params = (RelativeLayout.LayoutParams)groupHolder.vDiv.getLayoutParams();
            params.height = m_vScaleDef.getLayoutHeight(DIV_HEIGHT);

            view.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder)view.getTag();
        }

        return view;
    }

    @Override
    public View getChildView(int        groupPosition,
                             int        childPosition,
                             boolean    isLastChild,
                             View view,
                             ViewGroup viewGroup) {

        LSideMenuItem sideMenuItem = m_arMenuList.get(groupPosition).alMenuItem.get(childPosition);
        ChildHolder childHolder = null;

        if ( null == view ){
            childHolder = new ChildHolder();
            view = LayoutInflater.from(m_context).inflate( R.layout.view_menu_child_item, null);
            childHolder.vBg     = view.findViewById(R.id.v_background);
            childHolder.rlBg    = (RelativeLayout)view.findViewById(R.id.rl_child_content);
            childHolder.ivIcon  = (ImageView)view.findViewById(R.id.iv_menu);
            childHolder.tvText  = (TextView)view.findViewById(R.id.tv_menu);
            childHolder.vDiv    = view.findViewById(R.id.v_div);

            childHolder.rlBg.getLayoutParams().height
                    = m_vScaleDef.getLayoutHeight(CHILD_ITEM_HEIGHT);
            childHolder.vBg.getLayoutParams().height
                    = m_vScaleDef.getLayoutHeight(CHILD_ITEM_HEIGHT);

            //icon
            RelativeLayout.LayoutParams params
                    = (RelativeLayout.LayoutParams)childHolder.ivIcon.getLayoutParams();
            params.height = m_vScaleDef.getLayoutMinUnit(IMG_WIDTH);
            params.width  = m_vScaleDef.getLayoutMinUnit(IMG_WIDTH);
            params.leftMargin = m_vScaleDef.getLayoutWidth(IMG_LEFT_MARGIN);
            //設定icon
            childHolder.ivIcon.setImageResource( sideMenuItem.iDrawableId );

            //文字
            params = (RelativeLayout.LayoutParams)childHolder.tvText.getLayoutParams();
            params.leftMargin = m_vScaleDef.getLayoutWidth(TEXT_LEFT_MARGIN);
            params.rightMargin = m_vScaleDef.getLayoutWidth(TEXT_RIGHT_MARGIN);
            m_vScaleDef.setTextSize(CHILD_TEXT_SIZE, childHolder.tvText);
            //設定顯示文字
            childHolder.tvText.setText( sideMenuItem.iNameResId );

            params = (RelativeLayout.LayoutParams)childHolder.vDiv.getLayoutParams();
            params.height = m_vScaleDef.getLayoutHeight(DIV_HEIGHT);

            view.setTag(childHolder);

        } else {
            childHolder = (ChildHolder)view.getTag();
        }

        if ( sideMenuItem.bSelect ){
            childHolder.rlBg.setSelected(true);
            childHolder.vBg.setBackgroundResource(R.color.white_20);
        } else {
            childHolder.rlBg.setSelected(false);
            childHolder.vBg.setBackgroundResource(R.color.transparent);
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
