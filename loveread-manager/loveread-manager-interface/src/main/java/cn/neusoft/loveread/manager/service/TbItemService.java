package cn.neusoft.loveread.manager.service;

import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.pojo.TbItem;

public interface TbItemService {
    TbItem getItemById(Long itemId);
    EasyUIDataGridResult getItemList(int page,int rows);
}
