package ling.testapp.ui.listener;

import android.view.LayoutInflater;

import ling.testapp.ui.define.LViewScaleDef;

/**
 * Created by jlchen on 2016/9/21.
 * LBaseView 介面
 */

public interface LBaseViewInterface {

    /**
     * @return 此畫面的 Layout Resource Id
     */
    int getLayoutResourceId();

    /**
     * 元件初始化，靜態取得元件實體、動態建製元件實體…等
     *
     * @param inflater
     */
    void initialLayoutComponent(LayoutInflater inflater);

    /**
     * 設定字型大小及版面大小
     * @param viewScaleDef 請參閱{@link LViewScaleDef}
     */
    void setTextSizeAndLayoutParams(LViewScaleDef viewScaleDef);
}
