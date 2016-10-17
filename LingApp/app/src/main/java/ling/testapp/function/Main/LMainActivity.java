package ling.testapp.function.Main;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import ling.testapp.R;
import ling.testapp.function.Base.LBaseActivity;
import ling.testapp.function.Bingo.LBingoFragment;
import ling.testapp.function.Main.item.LSideMenuItem;
import ling.testapp.function.Setting.LSettingFragment;
import ling.testapp.ui.define.LMenuViewIdDef;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.listener.LNaviBarToMainListener;

/**
 * Created by jlchen on 2016/9/21.
 * 主畫面
 */

public class LMainActivity extends LBaseActivity implements LNaviBarToMainListener {

    DrawerLayout.DrawerListener m_drawerListener = new DrawerLayout.DrawerListener() {

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

            m_fMenuMoveX = (LViewScaleDef
                    .getInstance(m_context)
                    .getLayoutWidth(MENU_WIDTH) * slideOffset);
//            if ( drawerView.getId() == R.id.  ){
//                m_moveFactor *= -1;
//            }

            //menu移動時，main view需跟著移動
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                m_flContent.setTranslationX(m_fMenuMoveX);
            }
            else
            {
                TranslateAnimation anim = new TranslateAnimation(
                        m_fMenuFromX,
                        m_fMenuMoveX,
                        0.0f,
                        0.0f);
                anim.setDuration(0);
                anim.setFillAfter(true);
                m_flContent.startAnimation(anim);

                m_fMenuFromX = m_fMenuMoveX;
            }
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            //super.onDrawerClosed(drawerView);

            if(m_drawerLayout.isDrawerOpen(Gravity.LEFT)) {

                // 解開左邊鎖定的狀態
                m_drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
            } else if(m_drawerLayout.isDrawerOpen(Gravity.RIGHT)) {

                // 解開右邊鎖定的狀態
                m_drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
            } else {

                // 代表兩邊都被關上，以防萬一將兩邊的進行解鎖的動作
                m_drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
                m_drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
            }
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            //super.onDrawerOpened(drawerView);

            if(m_drawerLayout.isDrawerOpen(Gravity.LEFT)) {

                // 若是左側被打開，便將右側給鎖在關閉的狀態
                m_drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            } else {

                // 反之，便是將左側給鎖在關閉的狀態
                m_drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
            }
        }

        @Override
        public void onDrawerStateChanged(int newState) {}
    };

    private LLeftMenuFragment.OnListener m_leftMenuListener
            = new LLeftMenuFragment.OnListener() {
        @Override
        public void OnMenuClick(LSideMenuItem item) {
            selectSideMenu(item);
        }
    };

    private LLeftMenuFragment.OnInterface m_leftMenuInterface       = null;

    private final static double         MENU_WIDTH                  = 720;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private DrawerLayout                m_drawerLayout              = null;
    private FrameLayout                 m_flContent                 = null;
    private LinearLayout                m_llLeftMenu                = null;
    private LinearLayout                m_llRightMenu               = null;
    private LLeftMenuFragment           m_leftMenuFragment          = null;
//    private RightDrawerFragment m_RightDrawerFragment   = null;

    private LSideMenuItem               m_menuHomeItem              = null;
    private LSideMenuItem               m_menuCurrItem              = null;
    /** sidemenu x軸 位置*/
    private float                       m_fMenuMoveX                = 0.0f;
    private float                       m_fMenuFromX                = 0.0f;
    /** 前一次畫面狀態 */
    protected int                       m_iOldViewId                = 0;
    /** 當下畫面的Function Code */
    protected int                       m_iCurrViewId               = 0;
    /** 當下畫面的名稱 */
    protected String                    m_strCurrViewName           = "";
    /** 當下畫面的Tag */
    protected String                    m_strCurrViewTag            = "";
    /** 是否第一次進入首頁 */
    protected boolean                   m_bFirstHome                = true;
    /** 是否要顯示切換fragment動畫 */
    protected boolean                   m_bIsAnim                   = false;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialLayoutComponent() {
        m_menuHomeItem  = LMenuViewIdDef.getInstance().getHomeMenuList().get(0).alMenuItem.get(0);

        m_flContent     = (FrameLayout) findViewById(R.id.fl_content);
        m_drawerLayout  = (DrawerLayout)findViewById(R.id.dl_main);
        m_llLeftMenu    = (LinearLayout)findViewById(R.id.ll_left_menu);

        // 設定 Drawer 的影子
        m_drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);
        m_drawerLayout.setDrawerShadow(R.drawable.drawer_shadow_right, GravityCompat.END);

        //setScrimColor是在設定側邊選單打開時，剩餘空間的遮罩顏色。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            m_drawerLayout.setScrimColor(
                    m_context.getResources().getColor((R.color.transparent),
                    m_context.getTheme()));
        } else {
            m_drawerLayout.setScrimColor(ContextCompat.getColor(m_context, R.color.transparent));
        }
        m_drawerLayout.addDrawerListener(m_drawerListener);
    }

    @Override
    protected void setTextSizeAndLayoutParams(LViewScaleDef vScaleDef) {

        m_llLeftMenu.getLayoutParams().width = vScaleDef.getLayoutWidth(MENU_WIDTH);
//        m_llRightMenu.getLayoutParams().width = vScaleDef.getLayoutWidth(300);
    }

    @Override
    protected void setOnParameterAndListener() {
    }

    @Override
    protected void registerFragment(FragmentManager fragmentManager) {
        m_leftMenuFragment = (LLeftMenuFragment)fragmentManager.findFragmentById(R.id.fragment_left_menu);
        m_leftMenuInterface = m_leftMenuFragment.uiSetParameterListener(m_leftMenuListener);

//        m_RightDrawerFragment = (RightDrawerFragment) fragmentManager.findFragmentById(R.id.drawer_right);
//        m_onRightMenuInterface = m_RightDrawerFragment.SetOnRightDrawerListener(m_onRightMenuListener);

        m_leftMenuInterface.OnSelectMenu(m_menuHomeItem.iId);
        //初始化畫面
        selectSideMenu(m_menuHomeItem);
    }

    @Override
    public void onBackPressed() {

        //當畫面不是HomePage時, 按下Back鍵都會先回到首頁, 在首頁按下Back鍵才會離開App
        if ( m_iCurrViewId == LMenuViewIdDef.MENU_ID_HOME){
            //先把SideMenu收起來在離開App
            if ( m_drawerLayout.isDrawerOpen(Gravity.LEFT) || m_drawerLayout.isDrawerOpen(Gravity.RIGHT) ){
                m_drawerLayout.closeDrawers();
            } else {
                super.onBackPressed();
            }
        } else {
            m_bIsAnim = true;
            selectSideMenu(m_menuHomeItem);
        }
    }

    /**
     * 根據傳入的Function Code 轉換對應的畫面
     *
     * @param sideMenuItem 畫面的相關參數
     */
    public void selectSideMenu(final LSideMenuItem sideMenuItem ) {

        final int       iViewId     = sideMenuItem.iId;
        final String    strViewName = getResources().getString(sideMenuItem.iNameResId);
        final Fragment  fragment    = uiNewFuncFragment(sideMenuItem._class);
        m_menuCurrItem              = sideMenuItem;

        //更新左右選單的背景顏色
        if ( null != m_leftMenuInterface ){
            m_leftMenuInterface.OnSelectMenu(iViewId);
        }
//            if ( null != m_onRightMenuInterface ){
//                m_onRightMenuInterface.onSelectMenu(iViewId);
//            }

        //點選到原本的頁面, 不用換頁
        if ( iViewId == m_iCurrViewId ){

            //關閉menu
            m_drawerLayout.closeDrawers();
            return ;
        }

        switch ( iViewId ) {
            /**首頁*/
            case LMenuViewIdDef.MENU_ID_HOME:
                ((LHomeFragment)fragment).uiSetParameterListener(this);
                break;
            /**賓果遊戲*/
            case LMenuViewIdDef.MENU_ID_BINGO:
                ((LBingoFragment)fragment).uiSetParameterListener(this);
                break;
            /**設定*/
            case LMenuViewIdDef.MENU_ID_SETTING:
                ((LSettingFragment)fragment).uiSetParameterListener(this);
                break;
            default:
                break;
        }

        m_handler.post(new Runnable() {
            @Override
            public void run() {
                changeFragment(iViewId, strViewName, fragment);

                m_menuCurrItem = sideMenuItem;

                //關閉menu
                m_drawerLayout.closeDrawers();
            }
        });
    }

    /** 將Class 轉換成 Fragment */
    public Fragment uiNewFuncFragment(Class<?> _class) {
        try {
            if (null != _class) {
                return (Fragment) _class.newInstance();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根據傳入的Function Code 轉換對應的畫面
     *
     * @param iNewViewCode
     *            畫面的 Function Code
     * @param strNewViewName
     *            下一個畫面的 Function Code
     * @param newFragment
     *            下一個畫面的 Fragment
     */
    private void changeFragment(final int iNewViewCode, final String strNewViewName, final Fragment newFragment ){

        if (null == strNewViewName) {
            return;
        }

        // 開始Fragment的事務Transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 置換舊的Fragment
        m_iOldViewId        = m_iCurrViewId;
        // 換上新的畫面Tag以及Flag
        m_iCurrViewId       = iNewViewCode;
        m_strCurrViewName   = strNewViewName;

        // 設置轉換效果 (第一次近首頁不需要動畫)
        if (true == m_bFirstHome || true == m_bIsAnim) {
            fragmentTransaction.setCustomAnimations(R.anim.anim_right_in, R.anim.anim_left_out);
        }

        m_bFirstHome = false;
        m_bIsAnim = false;

        // 替换容器(container)原来的Fragment
        fragmentTransaction.replace(m_flContent.getId(), newFragment, strNewViewName);

        // 提交事務
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void OnNaviBarLeftImgClick() {

        m_drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void OnNaviBarRightImgClick() {

        m_drawerLayout.openDrawer(Gravity.RIGHT);
    }
}
