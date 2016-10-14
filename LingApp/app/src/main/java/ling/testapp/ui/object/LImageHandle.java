package ling.testapp.ui.object;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;

import ling.testapp.ui.define.LViewScaleDef;

/**
 * Created by jlchen on 2016/10/5.
 * 圖片處理
 */

public class LImageHandle {

    /***
     * 圖片的缩放方法
     * @param bm 原圖
     * @param newWidth 縮放後寬度
     * @param newHeight 縮放後高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bm,
                                   double newWidth,
                                   double newHeight) {
        // 獲取原圖寬高
        float width = bm.getWidth();
        float height = bm.getHeight();

        // 創建操作圖片用的matrix對象
        Matrix matrix = new Matrix();
        // 計算縮放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 進行縮放
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, (int) width,
                (int) height, matrix, true);

        return bitmap;
    }

    //將全螢幕畫面轉換成Bitmap
    public static Bitmap getScreenShot(Activity context) {

        //藉由View來Cache全螢幕畫面後放入Bitmap
        View view = context.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap fullBitmap = view.getDrawingCache();

        //取得系統狀態列高度
        int mStatusBarHeight = LViewScaleDef.getInstance(context).getStatusBarHeight();

        //取得手機螢幕長寬尺寸
        int mPhoneWidth = LViewScaleDef.getInstance(context).getDisplayMetrics().widthPixels;
        int mPhoneHeight = LViewScaleDef.getInstance(context).getDisplayMetrics().heightPixels;

        //將狀態列的部分移除並建立新的Bitmap
        Bitmap bitmap = Bitmap.createBitmap(fullBitmap, 0,
                mStatusBarHeight, mPhoneWidth, mPhoneHeight - mStatusBarHeight);
        //將Cache的畫面清除
        view.destroyDrawingCache();

        return bitmap;
    }

    //模糊處理
    public static Bitmap blurBuilder(Context context, Bitmap image, float radius, float scale) {

        try {
            int width = Math.round(image.getWidth() * scale);
            int height = Math.round(image.getHeight() * scale);

            //先對圖片進行壓縮然後再blur
            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);

            //創建空的Bitmap用於輸出
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            //初始化Renderscript
            RenderScript rs = RenderScript.create(context);

            //Create an Intrinsic Blur Script using the Renderscript
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

            //native層分配内存空間
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

            //設置blur的半徑後進行blur
            theIntrinsic.setRadius(radius);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);

            //拷貝blur後的數據到java緩衝區中
            tmpOut.copyTo(outputBitmap);

            //銷毀Renderscript
            rs.destroy();
            image.recycle();

            return outputBitmap;
        } catch(Exception e) {
            e.printStackTrace();
            //如果發生異常，就返回原圖，不做模糊處理
            return image;
        }
    }

    /**回收圖片在記憶體中佔用的資源*/
    public static void recycleImageViewBitMap(ImageView imageView) {
        if (imageView != null) {
            if ( imageView.getDrawable() instanceof BitmapDrawable){
                BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
                recycleBitmapDrawable(bd);
            }
        }
    }

    public static void recycleBitmapDrawable(BitmapDrawable bd) {
        if (bd != null) {
            Bitmap bitmap = bd.getBitmap();
            recycleBitmap(bitmap);
        }
        bd = null;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
