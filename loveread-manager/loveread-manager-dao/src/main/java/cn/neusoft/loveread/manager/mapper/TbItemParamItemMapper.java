package cn.neusoft.loveread.manager.mapper;

import cn.neusoft.loveread.pojo.TbItemParamItem;

public interface TbItemParamItemMapper {
    void insert(TbItemParamItem tbItemParamItem);

    TbItemParamItem selectItemParamByItemId(Long itemId);
}
