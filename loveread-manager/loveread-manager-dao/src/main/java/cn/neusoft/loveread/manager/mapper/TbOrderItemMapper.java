package cn.neusoft.loveread.manager.mapper;


import cn.neusoft.loveread.pojo.TbOrderItem;

import java.util.List;

public interface TbOrderItemMapper {

    int insert(TbOrderItem record);

    List<TbOrderItem> getItemById(String id);
}