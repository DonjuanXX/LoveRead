package cn.neusoft.loveread.manager.mapper;

import cn.neusoft.loveread.pojo.TbOrderShipping;

public interface TbOrderShippingMapper {

    int insert(TbOrderShipping record);

    TbOrderShipping getShippingById(String orderId);

}