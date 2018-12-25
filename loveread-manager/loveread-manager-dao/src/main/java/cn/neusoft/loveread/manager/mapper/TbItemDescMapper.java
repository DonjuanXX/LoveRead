package cn.neusoft.loveread.manager.mapper;

import cn.neusoft.loveread.pojo.TbItemDesc;

public interface TbItemDescMapper {
    void insert(TbItemDesc itemDesc);

    TbItemDesc selectItemDescByPrimaryKey(Long itemId);

}
