package ling.testapp.function.Bingo.item;

import java.io.Serializable;

/**
 * Created by jlchen on 2016/10/5.
 */

public class LBingoItem implements Serializable {

    public String   m_strNum    = "";
    public int      m_iIndex    = 0;
    public boolean  m_bSelect   = false;
    public boolean  m_bFocus    = false;
    public boolean  m_bError    = false;

    public LBingoItem(int iIndex, String strNum){
        this.m_iIndex   = iIndex;
        this.m_strNum   = strNum;
    }
}
