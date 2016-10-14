package ling.testapp.function.Main.item;

/**
 */
public class LSideMenuItem {

    public int		iId;
    public int	    iNameResId;
    public int		iDrawableId;
    public Class<?> _class;
    public boolean  bSelect;

    /**
     * @param iId -畫面ID
     * @param iNameResId -顯示名稱的Resourse id
     * @param iDrawableId -icon
     * @param _class 對應的class，有可能使用Fragment或Activity*/
    public LSideMenuItem(int iId,
                         int iNameResId,
                         int iDrawableId,
                         Class<?> _class)
    {
        this.iId			= iId;
        this.iNameResId		= iNameResId;
        this.iDrawableId	= iDrawableId;
        this._class			= _class;
        this.bSelect        = false;
    }
}
