package cn.neusoft.loveread.manager.mapper;

import cn.neusoft.loveread.pojo.TbItemDesc;

public interface TbItemDescMapper {
    void insert(TbItemDesc itemDesc);

    void delete(TbItemDesc itemDesc);

    TbItemDesc selectItemDescByPrimaryKey(Long itemId);

    void update(TbItemDesc itemDesc);

}
