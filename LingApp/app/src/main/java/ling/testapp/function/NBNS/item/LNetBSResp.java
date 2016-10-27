package ling.testapp.function.NBNS.item;

import java.util.ArrayList;

import ling.testapp.function.NBNS.LNetBSActivity;

/**
 * Created by jlchen on 2016/10/26.
 */

public class LNetBSResp {
    public ArrayList<LNetBSItem>    m_alData    = new ArrayList<>();

    public LNetBSActivity.eType     m_type      = LNetBSActivity.eType.TES;

    public String                   m_strError  = null;
}
