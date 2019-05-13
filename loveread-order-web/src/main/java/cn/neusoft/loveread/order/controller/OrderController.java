package cn.neusoft.loveread.order.controller;

import cn.neusoft.loveread.cart.service.CartService;
import cn.neusoft.loveread.common.pojo.LoveReadResult;
import cn.neusoft.loveread.common.utils.CookieUtils;
import cn.neusoft.loveread.common.utils.JsonUtils;
import cn.neusoft.loveread.order.pojo.OrderInfo;
import cn.neusoft.loveread.order.service.OrderService;
import cn.neusoft.loveread.pojo.TbItem;
import cn.neusoft.loveread.pojo.TbUser;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Reference
    private CartService cartService;
    @Reference
    private OrderService orderService;

    @RequestMapping("/order-cart.html")
    public String showOrderCart(HttpServletRequest request) {
        TbUser user = (TbUser) request.getAttribute("user");
        List<TbItem> cartList = cartService.getCartList(user.getId());
        request.setAttribute("cartList", cartList);
        return "order-cart";
    }

    @PostMapping("/create.html")
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request) {
        // 1、接收表单提交的数据OrderInfo。
        // 2、补全用户信息。
        TbUser user = (TbUser) request.getAttribute("user");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        // 3、调用Service创建订单。
        LoveReadResult result = orderService.createOrder(orderInfo);
        if (result.getStatus() == 200) {
            // 清空购物车
            cartService.clearCartList(user.getId());
        }
        //取订单号
        String orderId = result.getData().toString();
        // a)需要Service返回订单号
        request.setAttribute("orderId", orderId);
        request.setAttribute("payment", orderInfo.getPayment());
        // b)当前日期加三天。
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(3);
        request.setAttribute("date", dateTime.toString("yyyy-MM-dd"));
        // 4、返回逻辑视图展示成功页面
        return "success";
    }

    @RequestMapping("/ownOrder.html")
    public String showOwnOrder(HttpServletRequest request) {
        //从数据库里拿订单
        //先获取用户的ID
        TbUser user = (TbUser) request.getAttribute("user");
        List<OrderInfo> cartList = orderService.getOrderInfoById(user.getId());
        request.setAttribute("cartList", cartList);
//        TbUser user = (TbUser) request.getAttribute("user");
//        List<TbItem> cartList = cartService.getCartList(user.getId());
//        request.setAttribute("cartList", cartList);
        return "ownOrder";
    }


}
