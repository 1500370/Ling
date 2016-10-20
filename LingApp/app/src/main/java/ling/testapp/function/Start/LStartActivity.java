package ling.testapp.function.Start;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import ling.testapp.R;
import ling.testapp.function.Main.LMainActivity;
import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.object.LApplication;

/**
 * Created by jlchen on 2016/9/21.
 * 起始頁
 */

public class LStartActivity extends AppCompatActivity {

    private static final double TEXT_SIZE       = 96;
    private static final double TEXT_PADDING    = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        super.onCreate(savedInstanceState);

        if (appIsduplicateOpen()){
            this.finish();
            return;
        }

        //取得上次儲存的語言設定, 並設定在App取不到, 則預設為繁體中文
        LApplication.getLanguageInfo().initialAppLanguage();

        setContentView(R.layout.activity_start);

        LViewScaleDef   viewScaleDef    = LViewScaleDef.getInstance(LStartActivity.this);
        TextView        tvAppName       = (TextView)findViewById(R.id.tv_title);

        tvAppName.setPadding(viewScaleDef.getLayoutWidth(TEXT_PADDING),
                viewScaleDef.getLayoutHeight(TEXT_PADDING),
                viewScaleDef.getLayoutWidth(TEXT_PADDING),
                viewScaleDef.getLayoutHeight(TEXT_PADDING));
        viewScaleDef.setTextSize(TEXT_SIZE, tvAppName);

        //一秒後跳轉至Main頁面
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent intent = new Intent();
                intent.setClass(LStartActivity.this, LMainActivity.class);
                startActivity(intent);

                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        }, 3000);
    }

    /** 判斷是否重複開啟APP */
    public Boolean appIsduplicateOpen() {

        try {
            //回傳的string判斷是否為null,null就將新開啟的App關閉
            if (getCurrentActivityName(LStartActivity.this) == null) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private String getCurrentActivityName(Context context) throws Exception {

        ActivityManager                         am              = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo>   taskInfo        = am.getRunningTasks(20);
        ActivityManager.RunningTaskInfo         currentTaskInfo = filterApplicationTask(context, taskInfo);
        String                                  activity        = null;

        //取得現有的Activity,若大於1個回傳最上層的ClassName,若小於等於1回傳null
        if (currentTaskInfo.numActivities == 0
                || currentTaskInfo.numActivities == 1) {
            activity = currentTaskInfo.topActivity.getClassName();
        } else {
            activity = null;
        }

        return activity;
    }

    private ActivityManager.RunningTaskInfo filterApplicationTask(Context context,
                                                                  List<ActivityManager.RunningTaskInfo> taskInfo) {

        String                          strApplicationName  = context.getPackageName();
        ActivityManager.RunningTaskInfo currentTaskInfo     = null;

        for (ActivityManager.RunningTaskInfo info : taskInfo) {
            if (info.topActivity.getPackageName().equals(strApplicationName)
                    && info.baseActivity.getPackageName().equals(strApplicationName)) {
                currentTaskInfo = info;
                return currentTaskInfo;
            }
        }

        return null;
    }

}
