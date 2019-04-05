package cn.neusoft.loveread.pojo;

import java.io.Serializable;

public class TbUserFavoriteAndName extends TbUserFavorite implements Serializable {
    private String itemCatName;

    public String getItemCatName() {
        return itemCatName;
    }

    public void setItemCatName(String itemCatName) {
        this.itemCatName = itemCatName;
    }
    //不知道这个类干什么的？？？
}
