package ling.testapp.ui.define;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ling.testapp.R;
import ling.testapp.function.Bingo.LBingoFragment;
import ling.testapp.function.Main.LHomeFragment;
import ling.testapp.function.Main.item.LSideMenuGroupItem;
import ling.testapp.function.Main.item.LSideMenuItem;
import ling.testapp.function.NBNS.LThreeNBNSActivity;
import ling.testapp.function.Setting.LSettingFragment;

/**
 * Created by jlchen on 2016/9/22.
 */

public class LMenuViewIdDef {

    //首頁
    public static final int MENU_ID_HOME        = 1000;

    //左側選單
    public static final int MENU_ID_TRAINING    = 1100;
    public static final int MENU_ID_BINGO       = 1101;
    public static final int MENU_ID_NBNS        = 1102;

    public static final int MENU_ID_OTHER       = 1200;
    public static final int MENU_ID_SETTING     = 1201;

    //右側選單
    public static final int MENU_ID             = 1201;

    private static final int MENU_TYPE_HOME     = 0;
    private static final int MENU_TYPE_LEFT     = 1;
    private static final int MENU_TYPE_RIGHT    = 2;

    private Map< Integer, ArrayList<LSideMenuGroupItem>> m_mapSideMenuList = new HashMap<>();

    private static LMenuViewIdDef m_Instance = null;

    public static LMenuViewIdDef getInstance(){
        if ( m_Instance == null ){

            m_Instance = new LMenuViewIdDef();
            m_Instance.initialHomeMenu();
            m_Instance.initialLeftMenuList();
            m_Instance.initialRightMenuList();
        }
        return m_Instance;
    }

    private void initialHomeMenu(){

        ArrayList<LSideMenuGroupItem> alGroupList = new ArrayList<>();

        ArrayList<LSideMenuItem> alChildList = new ArrayList<>();
        alChildList.add(new LSideMenuItem(
                MENU_ID_HOME,
                R.string.title_home,
                R.drawable.ic_home,
                LHomeFragment.class));
        alGroupList.add(new LSideMenuGroupItem(
                MENU_ID_HOME,
                R.string.title_home,
                0,
                alChildList));

        m_mapSideMenuList.put(MENU_TYPE_HOME, alGroupList);
    }

    private void initialLeftMenuList(){
        ArrayList<LSideMenuGroupItem> alGroupList = new ArrayList<>();

        ArrayList<LSideMenuItem> alChildTraining = new ArrayList<>();
        alChildTraining.add(new LSideMenuItem(
                MENU_ID_BINGO,
                R.string.title_bingo,
                R.drawable.ic_bingo,
                LBingoFragment.class));
        alChildTraining.add(new LSideMenuItem(
                MENU_ID_NBNS,
                R.string.title_nbns,
                R.drawable.ic_nbns,
                LThreeNBNSActivity.class));
        alGroupList.add(new LSideMenuGroupItem(
                MENU_ID_TRAINING,
                R.string.menu_group_training,
                0,
                alChildTraining));

        ArrayList<LSideMenuItem> alChildOther = new ArrayList<>();
        alChildOther.add(new LSideMenuItem(
                MENU_ID_SETTING,
                R.string.title_setting,
                R.drawable.ic_setting,
                LSettingFragment.class));
        alGroupList.add(new LSideMenuGroupItem(
                MENU_ID_OTHER,
                R.string.menu_group_other,
                0,
                alChildOther));

        m_mapSideMenuList.put(MENU_TYPE_LEFT, alGroupList);
    }

    private void initialRightMenuList(){
        ArrayList<LSideMenuGroupItem> alGroupList = new ArrayList<>();
        ArrayList<LSideMenuItem> alChildList = new ArrayList<>();
//        alChildList.add(new LSideMenuItem(
//                MENU_ID_HOME,
//                R.string.home_page,
//                R.drawable.ic_home,
//                null/*CIHomeFragment.class*/));
//        alGroupList.add(new LSideMenuGroupItem(0, 0, 0, alChildList));
        m_mapSideMenuList.put(MENU_TYPE_RIGHT, alGroupList);
    }

    public ArrayList<LSideMenuGroupItem> getHomeMenuList(){

        if ( null == m_mapSideMenuList ){
            return new ArrayList<>();
        }
        return m_mapSideMenuList.get(MENU_TYPE_HOME);
    }

    public ArrayList<LSideMenuGroupItem> getLeftMenuList(){

        if ( null == m_mapSideMenuList ){
            return new ArrayList<>();
        }
        return m_mapSideMenuList.get(MENU_TYPE_LEFT);
    }

    public ArrayList<LSideMenuGroupItem> getRightMenuList(){

        if ( null == m_mapSideMenuList ){
            return new ArrayList<>();
        }
        return m_mapSideMenuList.get(MENU_TYPE_RIGHT);
    }
}
