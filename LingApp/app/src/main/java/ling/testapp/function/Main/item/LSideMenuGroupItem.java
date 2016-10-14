package ling.testapp.function.Main.item;

import java.util.ArrayList;

/**
 */
public class LSideMenuGroupItem {

    public int		                iId         = 0;
    public int	                    iNameResId  = 0;
    public int		                iDrawableId = 0;
    public ArrayList<LSideMenuItem> alMenuItem  = new ArrayList<>();

    /**
     * @param iId -畫面ID
     * @param iNameResId -顯示名稱的Resourse id
     * @param iDrawableId -icon*/
    public LSideMenuGroupItem(int iId,
                              int iNameResId,
                              int iDrawableId,
                              ArrayList<LSideMenuItem> alMenuItem)
    {
        this.iId			= iId;
        this.iNameResId		= iNameResId;
        this.iDrawableId	= iDrawableId;
        this.alMenuItem     = alMenuItem;
    }
}
