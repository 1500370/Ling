package ling.testapp.function.NBNS.item;

import java.text.DecimalFormat;

/**
 * Created by jlchen on 2016/10/21.
 */

public class LNetBSItem {
    public String m_strDate         = null;
    public String m_strSimpleDate   = null;
    public String m_strDay          = null;

    public String m_strQfii         = null;
    public double m_dQfii           = 0.0f;

    public String m_strBrk          = null;
    public double m_dBrk            = 0.0f;

    public String m_strIt           = null;
    public double m_dIt             = 0.0f;

    public String m_strTotal        = null;
    public double m_dTotal          = 0.0f;

    public double getDoubleTotal(){
        return m_dQfii+m_dBrk+m_dIt;
    }

    public String getStringTotal(){
        //字串格式化
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(getDoubleTotal());
    }
}
