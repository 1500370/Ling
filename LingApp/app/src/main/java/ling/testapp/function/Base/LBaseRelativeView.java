package ling.testapp.function.Base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import ling.testapp.ui.define.LViewScaleDef;
import ling.testapp.ui.listener.LBaseViewInterface;

/**
 * Created by jlchen on 2016/9/21.
 */

public abstract class LBaseRelativeView extends RelativeLayout
        implements LBaseViewInterface {

    protected Context m_context           = null;
    protected LayoutInflater m_layoutInflater    = null;

    public LBaseRelativeView(Context context) {
        this(context, null);
    }

    public LBaseRelativeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LBaseRelativeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        m_context = context;
    }

    protected final void initial() {

        m_layoutInflater = LayoutInflater.from(m_context);
        m_layoutInflater.inflate( getLayoutResourceId(), this );

        initialLayoutComponent(m_layoutInflater);

        if (isInEditMode()) {//為了讓Xml能正常檢視，所以只讓Eclipse動態讀取到此為止
            return;
        }

        LViewScaleDef viewScaleDef = LViewScaleDef.getInstance(m_context);

        setTextSizeAndLayoutParams( viewScaleDef );
    }
}
