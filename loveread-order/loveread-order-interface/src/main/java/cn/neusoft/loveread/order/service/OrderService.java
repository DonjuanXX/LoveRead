package cn.neusoft.loveread.order.service;

import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.order.pojo.OrderInfo;

import java.util.List;

public interface OrderService {
    LoveReadResult createOrder(OrderInfo orderInfo);
    List<OrderInfo> getOrderInfoById(Long Id);
    OrderInfo getOrderById(Long itemId);

    EasyUIDataGridResult getOrderList(int page, int rows);
}
