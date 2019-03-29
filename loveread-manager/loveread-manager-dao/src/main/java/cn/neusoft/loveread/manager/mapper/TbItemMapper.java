package cn.neusoft.loveread.manager.mapper;

import cn.neusoft.loveread.pojo.TbItem;

import java.util.List;

public interface TbItemMapper {

    TbItem selectByPrimaryKey(Long id);

    List<TbItem> getItemList();

    void insert(TbItem item);

    void delete(TbItem item);
}