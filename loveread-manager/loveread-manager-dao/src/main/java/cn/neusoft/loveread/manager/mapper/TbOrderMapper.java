package cn.neusoft.loveread.manager.mapper;


import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.pojo.TbOrder;

import java.util.List;

public interface TbOrderMapper {

    int insert(TbOrder record);

    List<TbOrder> getIdByUser(Long id);

    List<TbOrder> getOrderList();

    void update(TbOrder order);

    TbOrder getOrderById(Long itemId);
}