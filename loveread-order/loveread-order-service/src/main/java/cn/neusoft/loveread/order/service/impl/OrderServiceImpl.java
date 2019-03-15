package cn.neusoft.loveread.order.service.impl;

import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.manager.mapper.TbOrderItemMapper;
import cn.neusoft.loveread.manager.mapper.TbOrderMapper;
import cn.neusoft.loveread.manager.mapper.TbOrderShippingMapper;
import cn.neusoft.loveread.order.pojo.OrderInfo;
import cn.neusoft.loveread.order.service.OrderService;
import cn.neusoft.loveread.pojo.TbOrderItem;
import cn.neusoft.loveread.pojo.TbOrderShipping;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;
    @Value("${ORDER_ID_BEGIN}")
    private Integer ORDER_ID_BEGIN;
    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;

    @Override
    public LoveReadResult createOrder(OrderInfo orderInfo) {
        //接收表单数据并生成订单
        if (!redisTemplate.hasKey(ORDER_GEN_KEY)) {
            redisTemplate.opsForValue().set(ORDER_GEN_KEY, ORDER_ID_BEGIN);
        }

        String orderId = redisTemplate.opsForValue().increment(ORDER_GEN_KEY, 1L).toString();
        Date date = new Date();
//        String orderId = ORDER_ID_BEGIN+date.toString();
        orderInfo.setOrderId(orderId);
        orderInfo.setPostFee("0");
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        orderInfo.setCreateTime(date);
        orderInfo.setUpdateTime(date);
        // 3、向订单表插入数据。
        orderMapper.insert(orderInfo);
        // 4、向订单明细表插入数据
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem tbOrderItem : orderItems) {
            //生成明细id
            Long orderItemId = redisTemplate.opsForValue().increment(ORDER_ITEM_ID_GEN_KEY, 1L);
            tbOrderItem.setId(orderItemId.toString());
            tbOrderItem.setOrderId(orderId);
            //插入数据
            orderItemMapper.insert(tbOrderItem);
        }
        // 5、向订单物流表插入数据。
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        orderShippingMapper.insert(orderShipping);
        // 6、返回e3Result。
        return LoveReadResult.ok(orderId);
    }
}
