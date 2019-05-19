package cn.neusoft.loveread.manager.controller;

import cn.neusoft.loveread.common.pojo.EasyUIDataGridResult;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.order.service.OrderService;
import cn.neusoft.loveread.pojo.TbOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

//    @GetMapping("/{orderId}")
//    @ResponseBody
//    public OrderInfo getOrderById(@PathVariable Long itemId) {
////        return orderService.getOrderById(itemId);
//        return LoveReadResult.ok();
//    }

    @GetMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult getOrderList(Integer page, Integer rows) {
        return orderService.getOrderList(page, rows);
    }

    @RequestMapping("/update")
    @ResponseBody
    public LoveReadResult update(Integer status, TbOrder order) {
        TbOrder tbOrder = orderService.getOrderById(Long.parseLong(order.getOrderId()));
        tbOrder.setStatus(status);
        return orderService.update(tbOrder);
    }
}
