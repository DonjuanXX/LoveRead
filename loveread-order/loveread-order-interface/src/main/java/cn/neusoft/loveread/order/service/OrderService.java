package cn.neusoft.loveread.order.service;

import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.order.pojo.OrderInfo;

public interface OrderService {
    LoveReadResult createOrder(OrderInfo orderInfo);
}
