package ling.testapp.function.NBNS;

import android.os.AsyncTask;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import ling.testapp.function.NBNS.item.LNetBSItem;
import ling.testapp.function.NBNS.item.LNetBSResp;

/**
 * Created by jlchen on 2016/10/26.
 */

public class LNetBSModel {

    public interface Callback {
        void onSuccess(LNetBSResp resultData);

        void onError(String strError);

        void showProgress();

        void hideProgress();
    }

    private final static String DEF_URL_TSE
            = "http://iwow.systex.com.tw/webService/ThreeCommOverBS_TSE.ashx";
    private final static String DEF_URL_OTC
            = "http://iwow.systex.com.tw/webService/ThreeCommOverBS_OTC.ashx";
    private final static int    TIME_OUT    = 30000;

    public static void findData(final LNetBSActivity.eType type, final Callback listener) {

        new AsyncTask<LNetBSActivity.eType, Void, LNetBSResp>() {

            @Override
            protected void onPreExecute() {
                // 背景工作處理前所需作的事
                super.onPreExecute();
                listener.showProgress();
            }

            @Override
            protected LNetBSResp doInBackground(LNetBSActivity.eType... params) {
                // 在背景中處理的耗時工作

                String strUrl = DEF_URL_TSE;
                if (type == LNetBSActivity.eType.OTC ){
                    strUrl = DEF_URL_OTC;
                }

                HttpURLConnection   urlConnection   = null;
                XmlPullParser       xmlParser       = Xml.newPullParser();

                LNetBSResp          result          = null;

                try {
                    StringBuilder sBuilder = new StringBuilder();
                    URL url = new URL(strUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(TIME_OUT);
                    urlConnection.setConnectTimeout(TIME_OUT);
                    if(java.net.HttpURLConnection.HTTP_OK == urlConnection.getResponseCode()){
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String strLine = null;
                        while ((strLine = reader.readLine()) != null) {
                            sBuilder.append(strLine);
                        }
                        reader.close();
                    }else {
                        return null;
                    }
                    urlConnection.disconnect();

                    ByteArrayInputStream bArrayInputStream
                            = new ByteArrayInputStream(sBuilder.toString().getBytes());

                    xmlParser.setInput(bArrayInputStream, "UTF-8"); //設置數據編碼
                    int iEventType = xmlParser.getEventType();      //獲取事件類型

                    LNetBSItem item     = null;
                    while (iEventType != XmlPullParser.END_DOCUMENT) {
                        //字串格式化
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");

                        switch (iEventType) {
                            case XmlPullParser.START_DOCUMENT:      //xml準備讀取,相關物件初始化
                                result = new LNetBSResp();
                                result.m_type = type;
                                break;

                            case XmlPullParser.START_TAG:           //開始讀取xml的某個標籤
                                String name = xmlParser.getName();
                                if (name.equalsIgnoreCase("Symbol")) {
                                    item = new LNetBSItem();
                                } else if (null != item) {
                                    if (name.equalsIgnoreCase("qfiiNetAmount")) {
                                        //字串轉數字並且四捨五入到小數第二位
                                        item.m_dQfii = (int)(Math.round(Double.valueOf(xmlParser.nextText())*100.0))/100.0;
                                        //固定顯示到小數點第二位
                                        item.m_strQfii = decimalFormat.format(item.m_dQfii);
                                    } else if (name.equalsIgnoreCase("brkNetAmount")) {
                                        item.m_dBrk = (int)(Math.round(Double.valueOf(xmlParser.nextText())*100.0))/100.0;
                                        item.m_strBrk = decimalFormat.format(item.m_dBrk);
                                    } else if (name.equalsIgnoreCase("itNetAmount")) {
                                        item.m_dIt = (int)(Math.round(Double.valueOf(xmlParser.nextText())*100.0))/100.0;
                                        item.m_strIt = decimalFormat.format(item.m_dIt);
                                    } else if (name.equalsIgnoreCase("date")) {
                                        item.m_strDate = xmlParser.nextText();
                                        item.m_strSimpleDate = new SimpleDateFormat("MM/dd").format(
                                                new SimpleDateFormat("yyyy/MM/dd").parse( item.m_strDate ));
                                    }
                                }
                                break;

                            case XmlPullParser.END_TAG:             //結束讀取xml的某個標籤
                                if (xmlParser.getName().equalsIgnoreCase("Symbol") && item != null) {
                                    item.m_dTotal = item.m_dQfii + item.m_dBrk + item.m_dIt;
                                    item.m_strTotal = decimalFormat.format(item.m_dTotal);

                                    result.m_alData.add(item);
                                    item = null;
                                }
                                break;
                        }
                        iEventType = xmlParser.next();
                    }
                    bArrayInputStream.close();
                } catch (Exception e) {
                    result = new LNetBSResp();
                    result.m_strError = e.toString();

                    e.printStackTrace();
                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }

                return result;
            }

            @Override
            protected void onPostExecute(LNetBSResp result) {
                // 背景工作處理完後需作的事

                if (null != result) {
                    if ( null != result.m_strError ){
                        listener.onError(result.m_strError);
                    }else {
                        listener.onSuccess(result);
                    }
                } else {
                    listener.onError("連線失敗");
                }

                listener.hideProgress();
            }
        }.execute(type);
    }
}
