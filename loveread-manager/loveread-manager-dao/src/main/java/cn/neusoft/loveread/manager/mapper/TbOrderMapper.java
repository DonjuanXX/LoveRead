package cn.neusoft.loveread.manager.mapper;


import cn.neusoft.loveread.pojo.TbOrder;

import java.util.List;

public interface TbOrderMapper {

    int insert(TbOrder record);

    List<TbOrder> getIdByfUser(Long id);

    List<TbOrder> getOrderList();
}