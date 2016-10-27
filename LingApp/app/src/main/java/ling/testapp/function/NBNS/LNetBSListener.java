package ling.testapp.function.NBNS;

import ling.testapp.function.NBNS.item.LNetBSResp;

/**
 * Created by jlchen on 2016/10/26.
 */

public interface LNetBSListener {
    void onSuccess(LNetBSResp resultData);

    void onError(String strError);

    void showProgress();

    void hideProgress();
}
